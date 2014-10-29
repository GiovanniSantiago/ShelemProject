import java.io.IOException;

public class ServerRoom extends Thread {
	MessageLine[] connections;
	ServerRoomState state;
	
	public ServerRoom(MessageLine[] connections) {
		this.connections = connections;
		state = ServerRoomState.WAITING_PLAYER_NAMES;
	}
	
	@Override
	public void run() {
		MC.broadcastMessage(connections, new Message(
				new MessagePair(MC.P_NAME,MC.SU_TABLE_CREATED)));
		int namesReceived = 0;
		while(true) {
			switch(state) {
				/*
				 * If getting the names form the users
				 */
				case WAITING_PLAYER_NAMES: {
					/*
					 * Only if there are not four names already (CLIENTS DO NOT SEND MORE THAN ONE NAME EACH)
					 */
					if(namesReceived!=4) {
						/*
						 * Poll for messages
						 */
						for(int i = 0; i < connections.length; i++) {
							MessageLine line = connections[i];
							try {
								if(line.isReady()) {
									Message m = line.receiveMessage();
									switch(m.getValue(MC.P_NAME)) {
										/*
										 * CLIENT HAS DISCLOSED NAME
										 */
										case MC.CU_MYNAME: {
											
											//Increase name counter and tell everybody.
											
											namesReceived++;
											
											System.out.println("Player "+i+" submitted name: "+m.getValue(MC.P_PLAYER_NAME));
											
											MC.broadcastMessage(connections, new Message(
													new MessagePair(MC.P_NAME,MC.SU_PLAYERNAME),
													new MessagePair(MC.P_PLAYER_ID,""+i),
													new MessagePair(MC.P_PLAYER_NAME,m.getValue(MC.P_PLAYER_NAME))));
										} break;
										/*
										 * CLIENT HAS QUIT GAME
										 */
										case MC.CU_QUITGAME: {
											MC.broadcastMessage(connections, new Message( 
													new MessagePair(MC.P_NAME,MC.SU_PLAYERQUIT),
													new MessagePair(MC.P_PLAYER_ID,""+i)));
											onQuitGame(i);
											return;
										} 
									}
								} 
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
					else {
						state = ServerRoomState.WAITING_GAMESTART_READY;
					}
					/*
					 * End getting names state
					 */
				} break;
				case WAITING_GAMESTART_READY: {
					/*
					 * Check of player1 said ready
					 */
					try {
						if(connections[0].isReady()) {
							Message m = connections[0].receiveMessage();
							switch(m.getValue(MC.P_NAME)) {
								case MC.CU_GAMEREADY: {
									state = ServerRoomState.WAITING_PLAYER_BIDS;
									MC.broadcastMessage(connections,new Message(
											new MessagePair(MC.P_NAME,MC.)));
								} break;
							}
						}
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} break;
				
				
				
			}
		}
		
	}
	
	private void onQuitGame(int i) {
		
	}

	private enum ServerRoomState {
		WAITING_PLAYER_NAMES, WAITING_GAMESTART_READY, WAITING_PLAYER_BIDS,
	}
}
