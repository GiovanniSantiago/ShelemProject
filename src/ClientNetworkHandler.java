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
					switch (m.getValue(MC.P_NAME)) {
						case MC.SU_PLAYERQUIT: {
							onPlayerQuit();
							quit = true;
						}
							break;
						case MC.SU_PLAYERNAME: {
							onNameReceived(
									m.getInteger(MC.P_PLAYER_ID),
									m.getValue(MC.P_PLAYER_NAME));
						}
							break;
						case MC.SU_PLAYERJOIN: {
							onPlayerJoin(m
									.getInteger(MC.P_PLAYER_ID));
						}
							break;
						case MC.SR_PLAYERNAME: {
							String name = onNameRequested(m
									.getInteger(MC.P_PLAYER_ID));
							line.sendMessage(new Message(new MessagePair(
									MC.P_NAME,
									MC.CU_MYNAME),
									new MessagePair(
											MC.P_PLAYER_NAME,
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
	
	
	private enum ClientGameState {
		WAITING_LOBBY_PLAYERS, WAITING_PLAYER_NAMES,
	}
}
