import java.io.IOException;
import java.util.Arrays;


public class ServerRoom extends Thread {
	MessageLine[] connections;
	ServerRoomState state = ServerRoomState.NAME_SETTING_STATE;
	ShelemGame game = new ShelemGame();
	
	public ServerRoom(MessageLine[] connections) {
		this.connections = connections;
	}
	
	@Override
	public void run() {
		for(int i = 0; i < connections.length; i++) {
			connections[i].sendMessage(Message.fromPairs("name:table_full","id:"+i));
		}
		
		Player[] players = new Player[4];
		int numPlayers = 0;
		
		Card[] baseDeck = new Card[52];
		{
			int index = 0;
			for(Suit s: Suit.values()) {
				for(Rank r: Rank.values()) {
					baseDeck[index] = new Card(s,r);
					index++;
				}
			}
		}
		Utilities.overhandArrayShuffle(baseDeck);
		
		CardDeck[] playerDecks = new CardDeck[4];
		//playerDecks[0] = new CardDeck(Utilities.subArray(baseDeck, 0, end));
		
		boolean quit = false;
		while(!quit) {
			try {
				for(int player = 0; player < connections.length; player++) {
					MessageLine currLine = connections[player];
					if(currLine.isReady()) {
						Message m = currLine.receiveMessage();
						switch(state) {
							case NAME_SETTING_STATE: {
								switch(m.getName()) {
									case "my_name": {
										MC.broadcastMessage(connections, Message.fromPairs(
												"name:player_name",
												"player_name:"+m.getValue("player_name"),
												"id:"+player));
										players[player] = new Player(m.getValue("name"));
										numPlayers++;
										
										if(numPlayers==4) {
											MC.broadcastMessage(connections, Message.fromPairs(
													"name:got_all_names"));
											this.state = ServerRoomState.GAME_LOBBY_STATE;
											game.setPlayers(players);
											
											// Ask player 1 for ready
											connections[0].sendMessage(Message.fromPairs("name:is_game_ready"));
										}
									} break;
									//TODO: Add QUITTING message thing
								} 
							} break;
							case GAME_LOBBY_STATE: {
								switch(m.getName()) {
									// A player will tell me that he's ready
									case "i_am_ready": {
										// DEAL ALL CARDS, SPLIT IN FOUR, AND SEND
										// DEAL ALL CARDS, SPLIT IN FOUR, AND SEND
										// DEAL ALL CARDS, SPLIT IN FOUR, AND SEND
										// DEAL ALL CARDS, SPLIT IN FOUR, AND SEND
										// DEAL ALL CARDS, SPLIT IN FOUR, AND SEND
										// DEAL ALL CARDS, SPLIT IN FOUR, AND SEND
										// DEAL ALL CARDS, SPLIT IN FOUR, AND SEND
										// DEAL ALL CARDS, SPLIT IN FOUR, AND SEND
										// DEAL ALL CARDS, SPLIT IN FOUR, AND SEND
										// DEAL ALL CARDS, SPLIT IN FOUR, AND SEND// DEAL ALL CARDS, SPLIT IN FOUR, AND SEND
										// DEAL ALL CARDS, SPLIT IN FOUR, AND SEND
										// DEAL ALL CARDS, SPLIT IN FOUR, AND SEND
										
										
									} break;
									//TODO: Add QUITTING message thing
								}
							} break;
							case BIDDING_STATE: {
								switch(m.getName()) {
									//TODO: Add QUITTING message thing
								}
							} break;
							case GAME_STATE: {
								switch(m.getName()) {
									//TODO: Add QUITTING message thing
								}
							} break;
							case END_GAME_STATE: {
								
							} break;
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
