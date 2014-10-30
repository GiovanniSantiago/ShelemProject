import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;

public class ServerLauncher {
	
	public static final int CLIENT_ACCEPT_TIMEOUT = 100;
	
	public static void main(String[] args) {
		ServerSocket servSock;
		System.out.println("SERVER: SERVER LAUNCHER STARTED");
		
		
		//Try to create the server socket
		try {
			servSock = new ServerSocket(7169);
			System.out.println("SERVER: SERVER SOCKET CREATED");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		
		try {
			servSock.setSoTimeout(CLIENT_ACCEPT_TIMEOUT);
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out
				.println("SERVER: STARTING NEW PLAYER LIST, BUILDING UP PLAYERS...");
		MessageLine[] connections = new MessageLine[4];
		int amount = 0;
		
		while (true) {
			try {
				Socket sock = servSock.accept();
				System.out.println(String.format(
						"SERVER: CLIENT ACCEPTED, INDEX %d", amount));
				connections[amount] = new MessageLine(sock);
				MC.broadcastMessage(connections, new Message(
						new MessagePair(MC.P_NAME, MC.SU_PLAYERJOIN), 
						new MessagePair(MC.P_PLAYER_ID, ""	+ amount)));
				connections[amount].sendMessage(Message.fromPairs("name:your_id","player_id:"+amount));
				amount++;
				if (amount == 4) {
					System.out
							.println("SERVER: TABLE COMPLETE, CREATING ROOM...");
					amount = 0;
					new ServerRoom(connections).start();
					System.out
							.println("SERVER: ROOM CREATED AND PLAYERS FLUSHED");
				}
			} catch (SocketTimeoutException e) {
				/*
				 * Check for player messages
				 */
				for (int i = 0; i < amount; i++) {
					try {
						boolean quit = false;
						if (connections[i].isReady()) {
							Message m = connections[i].receiveMessage();
							switch (m.getValue(MC.P_NAME)) {
								case MC.CU_QUITGAME: {
									System.out
											.println("SERVER: A PANSY PLAYER QUIT, BOOHOO, RUINING THE FUN FOR EVERYBODY NOW...");
									quit = true;
									MC.broadcastMessage(
											connections,
											new Message(
													new MessagePair(
															MC.P_NAME,
															MC.SU_PLAYERQUIT),
													new MessagePair(
															MC.P_PLAYER_ID,
															"" + i)));
									amount = 0;
									connections = new MessageLine[4];
									System.out.println("Server: STARTING PLAYERLIST OVER...");
								}
									break;
							}
						}
						if (quit)
							break;
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
}
