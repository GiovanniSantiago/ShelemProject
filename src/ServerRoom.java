import java.io.IOException;

public class ServerRoom extends Thread {
	MessageLine[] connections;
	ServerRoomState state = ServerRoomState.NAME_SETTING_STATE;
	
	public ServerRoom(MessageLine[] connections) {
		this.connections = connections;
	}
	
	@Override
	public void run() {
		for(int i = 0; i < connections.length; i++) {
			connections[i].sendMessage(Message.fromPairs("name:table_full","id:"+i));
		}
		
		
		boolean quit = false;
		
		while(!quit) {
			try {
				for(int player = 0; player < connections.length; player++) {
					MessageLine currLine = connections[player];
					if(currLine.isReady()) {
						Message m = currLine.receiveMessage();
						switch(state) {
							//THIS IS WHERE I LEFT OFF WEDNESDAY. THIS IS NEXT, OR CLIENT GAMESTATE (and ENDGAMESTATE).
							//THIS IS WHERE I LEFT OFF WEDNESDAY. THIS IS NEXT, OR CLIENT GAMESTATE (and ENDGAMESTATE).
							//THIS IS WHERE I LEFT OFF WEDNESDAY. THIS IS NEXT, OR CLIENT GAMESTATE (and ENDGAMESTATE).
							//THIS IS WHERE I LEFT OFF WEDNESDAY. THIS IS NEXT, OR CLIENT GAMESTATE (and ENDGAMESTATE).
							//THIS IS WHERE I LEFT OFF WEDNESDAY. THIS IS NEXT, OR CLIENT GAMESTATE (and ENDGAMESTATE).
							//THIS IS WHERE I LEFT OFF WEDNESDAY. THIS IS NEXT, OR CLIENT GAMESTATE (and ENDGAMESTATE).
							//THIS IS WHERE I LEFT OFF WEDNESDAY. THIS IS NEXT, OR CLIENT GAMESTATE (and ENDGAMESTATE).
							//THIS IS WHERE I LEFT OFF WEDNESDAY. THIS IS NEXT, OR CLIENT GAMESTATE (and ENDGAMESTATE).
							//THIS IS WHERE I LEFT OFF WEDNESDAY. THIS IS NEXT, OR CLIENT GAMESTATE (and ENDGAMESTATE).
							
							
							//TODO: Here go all of the state checks. Good luck.
						}
					}
				}
			} catch (IOException e) {
				// Something borked with reading a thing. A socket probably broke or something, I dunno.
			}
		}
		
		
		/*if(true)return;
		MC.broadcastMessage(connections, new Message(
				new MessagePair(MC.P_NAME,MC.SU_TABLE_CREATED)));
		int namesReceived = 0;
		while(true) {
			switch(state) {
				/*
				 * If getting the names form the users
				 
				case WAITING_PLAYER_NAMES: {
					/*
					 * Only if there are not four names already (CLIENTS DO NOT SEND MORE THAN ONE NAME EACH)
					 
					if(namesReceived!=4) {
						/*
						 * Poll for messages
						 
						for(int i = 0; i < connections.length; i++) {
							MessageLine line = connections[i];
							try {
								if(line.isReady()) {
									Message m = line.receiveMessage();
									switch(m.getValue(MC.P_NAME)) {
										/*
										 * CLIENT HAS DISCLOSED NAME
										 
										case MC.CU_MYNAME: {
											
											//Increase name counter and tell everybody.
											
											namesReceived++;
											
											System.out.println("Player "+i+" submitted name: "+m.getValue(MC.PARAMETER_PLAYER_NAME));
											
											MC.broadcastMessage(connections, new Message(
													new MessagePair(MC.P_NAME,MC.SU_PLAYERNAME),
													new MessagePair(MC.P_PLAYER_ID,""+i),
													new MessagePair(MC.PARAMETER_PLAYER_NAME,m.getValue(MC.PARAMETER_PLAYER_NAME))));
										} break;
										/*
										 * CLIENT HAS QUIT GAME
										 
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
					 
				} break;
				case WAITING_GAMESTART_READY: {
					/*
					 * Check of player1 said ready
					 
					try {
						for(int i = 0; i < connections.length;i++) {
							if(connections[i].isReady()) {
								Message m = connections[i].receiveMessage();
								if(m.getValue(MC.P_NAME).equals(MC.CU_QUITGAME)) {
									MC.broadcastMessage(connections, new Message( 
											new MessagePair(MC.P_NAME,MC.SU_PLAYERQUIT),
											new MessagePair(MC.P_PLAYER_ID,""+i)));
									onQuitGame(i);
									return;
								}
								if(m.getValue(MC.P_NAME).equals(MC.CU_GAMEREADY)) {
									
									
									/*
									 * Go to waiting bid state
									 
									state = ServerRoomState.WAITING_PLAYER_BIDS;
									
									// STOPPED HERE
									
									MC.broadcastMessage(connections,new Message(
											new MessagePair(MC.P_NAME,MC.SU_GAME_START)));
								}
							}
						}
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} break;
				
				
				
			}
		}*/
		
	}
	
	private void onQuitGame(int i) {
		
	}

	private enum ServerRoomState {
		NAME_SETTING_STATE, GAME_LOBBY_STATE, BIDDING_STATE, GAME_STATE, END_GAME_STATE
	}
}
