import java.io.IOException;
import java.net.Socket;

public class ClientNetworkHandler implements Runnable {
	Socket sock;
	MessageLine line;
	
	public ClientNetworkHandler() {
		try {
			sock = new Socket("localhost", 7169);
			line = new MessageLine(sock);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@Override
	public void run() {
		boolean quit = false;
		
		while (!quit) {
			try {
				if (line.isReady()) {
					Message m = line.receiveMessage();
					switch (m.getValue(MessageConstants.PARAM_NAME)) {
						case MessageConstants.SERVER_UPDATE_PLAYERQUIT: {
							onPlayerQuit();
							quit = true;
						}
							break;
						case MessageConstants.SERVER_UPDATE_PLAYERNAME: {
							onNameReceived(
									m.getInteger(MessageConstants.PARAM_PLAYER_ID),
									m.getValue(MessageConstants.PARAM_PLAYER_NAME));
						}
							break;
						case MessageConstants.SERVER_UPDATE_PLAYERJOIN: {
							onPlayerJoin(m
									.getInteger(MessageConstants.PARAM_PLAYER_ID));
						}
							break;
						case MessageConstants.SERVER_REQUEST_PLAYERNAME: {
							String name = onNameRequested(m
									.getInteger(MessageConstants.PARAM_PLAYER_ID));
							line.sendMessage(new Message(new MessagePair(
									MessageConstants.PARAM_NAME,
									MessageConstants.CLIENT_UPDATE_MYNAME),
									new MessagePair(
											MessageConstants.PARAM_PLAYER_NAME,
											name)));
						}
							break;
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		System.out.println("NETWORK HANDLING THREAD DONE");
	}
	
	private String onNameRequested(int index) {
		System.out.println("THEY ASKED FOR MY NAME. I AM APOLLOPOPS!");
		return "Apollopops";
	}
	
	private void onPlayerQuit() {
		System.out
				.println("A PLAYER QUIT. THE LOBBY IS BORKED NOW. RAGEQUITTING...");
	}
	
	private void onNameReceived(int index, String name) {
		System.out.println(String.format("THIS OTHER GUY, %d, IS CALLED %s",
				index, name));
	}
	
	private void onPlayerJoin(int index) {
		System.out.println(String.format("SOMEONE IS INTRUDING. HE'S #%d",
				index));
	}
	
}
