import java.io.IOException;

public class ServerRoom extends Thread {
	MessageLine[] connections;
	ServerRoomState state;
	
	public ServerRoom(MessageLine[] connections) {
		this.connections = connections;
		
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
				case GETTING_NAMES: {
					/*
					 * Only if there are not four names already (CLIENTS DO NOT SEND MORE THAN ONE NAME EACH AAAAAAA)
					 */
					if(namesReceived!=4) {
						/*
						 * Poll for messages
						 */
						for(int i = 0; i < connections.length; i++) {
							MessageLine line = connections[i];
							try {
								if(line.isReady()) {
									/*
									 * ALL CLIENT MESSAGES IN NAMEGET PHASE
									 */
									Message m = line.receiveMessage();
									switch(m.getValue(MC.P_NAME)) {
										/*
										 * CLIENT HAS DISCLOSED NAME
										 */
										case MC.CU_MYNAME: {
											namesReceived++;
											MC.broadcastMessage(connections, new Message(
													new MessagePair(MC.P_NAME,MC.SU_PLAYERNAME),
													new MessagePair(MC.P_PLAYER_ID,""+i),
													new MessagePair(MC.P_PLAYER_NAME,m.getValue(MC.P_PLAYER_NAME))));
											//LEFT AT THIS EXACT LINE
										} break;
										/*
										 * CLIENT HAS QUIT NAME
										 * STOPPED HERE
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
				} break;
				
				
				
				
			}
		}
		
	}
	
	private void onQuitGame(int i) {
		
	}

	private enum ServerRoomState {
		GETTING_NAMES, BIDDING,
	}
}
