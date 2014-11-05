import java.io.IOException;


public class ServerRoom extends Thread {
	MessageLine[] connections;
	ServerRoomState state = ServerRoomState.NAME_SETTING_STATE;
	
	/**
	 * Provides references to every possible card. Will always be in order. 
	 * <br><b><i>Do not modify.
	 */
	Card[] baseDeck = new Card[52];
	/**
	 * Keeps track of all the shuffles and splits made to the main deck.
	 * <br><b><i>Modify this for shuffles and such.
	 */
	Card[] mainDeck = new Card[52];
	
	int bidStarter = 0;
	
	public ServerRoom(MessageLine[] connections) {
		this.connections = connections;

		int index = 0;
		for(Suit s: Suit.values()) {
			for(Rank r: Rank.values()) {
				baseDeck[index] = new Card(s,r);
				mainDeck[index]=baseDeck[index];
				index++;
			}
		}
	}
	
	@Override
	public void run() {
		for(int i = 0; i < connections.length; i++) {
			connections[i].sendMessage(Message.fromPairs(
					"name:"+Message.Names.TABLE_FULL.toString()));
		}
		
		Player[] players = new Player[4];
		int numPlayers = 0;
		
		CardDeck[] playerDecks = new CardDeck[4];
		Card[] widow = new Card[4];
		
		int currentBid = 0;
		
		boolean bidPassers[] = new boolean[] {false,false,false,false};
		boolean bidCanFail = true;
		
		boolean quit = false;
		while(!quit) {
			try {
				for(int player = 0; player < connections.length; player++) {
					MessageLine currLine = connections[player];
					if(currLine.isReady()) {
						Message m = currLine.receiveMessage();
						switch(state) {
							case NAME_SETTING_STATE: {
								System.out.println("kfdjvngfdjkbgvfdnvkjfddddjdkjfvb fdkjb");
								switch(m.getName()) {
									case "MY_NAME": {
										if(m.containsKey(Message.Keys.GAME_SETTINGS.toString())) {
											MC.broadcastMessage(connections, Message.fromPairs(
													"name:" + Message.Names.PLAYER_NAME.toString(),
													Message.Keys.PLAYER_NAME.toString() + ":" +m.getValue(Message.Names.PLAYER_NAME.toString()),
													Message.Keys.PLAYER_ID.toString()+":"+player,
													Message.Keys.GAME_SETTINGS.toString()+":"+m.getValue(Message.Keys.GAME_SETTINGS.toString())));
										} else {
											MC.broadcastMessage(connections, Message.fromPairs(
													"name:" + Message.Names.PLAYER_NAME.toString(),
													Message.Keys.PLAYER_NAME.toString() + ":" +m.getValue(Message.Names.PLAYER_NAME.toString()),
													Message.Keys.PLAYER_ID.toString()+":"+player));
										}
										players[player] = new Player(m.getValue(Message.Keys.PLAYER_NAME.toString()));
										numPlayers++;
										
										if(numPlayers==4) {
											MC.broadcastMessage(connections, Message.fromPairs(
													"name:" + Message.Names.GOT_ALL_NAMES.toString()));
											this.state = ServerRoomState.GAME_LOBBY_STATE;
											
											// Ask player 1 for ready
											connections[0].sendMessage(Message.fromPairs("name:"+Message.Names.ARE_YOU_READY.toString()));
										}
									} break;
									//TODO: Add QUITTING message thing
								} 
							} break;
							case GAME_LOBBY_STATE: {
								switch(m.getName()) {
									// A player will tell me that he's ready
									case "I_AM_READY": {
										
										//
										//		Shuffle the persistent main deck
										//
										
										Utilities.overhandArrayShuffle(mainDeck);
										
										// Make new decks for each player.
										
										playerDecks = new CardDeck[4];
										
										// Split the persistent main deck and give each playerdeck 12 cards from it.
										
										for(int i = 0; i < 4; i++) {
											Card[] currDeck = new Card[12];
											System.arraycopy(mainDeck,i*12,currDeck,0,12);
											playerDecks[i] = new CardDeck(currDeck);
										}
										
										// Separate the widow.
										
										widow = new Card[4];
										System.arraycopy(mainDeck, 12*4, widow, 0, 4);
										
										// Send each player their own cards.
										
										for(int i = 0; i < 4; i++) {
											String cards = "";
											for(int j = 0; j < playerDecks[i].getCardCount(); j++) {
												cards+=playerDecks[i].getCard(j).getName();
												if(j!=playerDecks[i].getCardCount()) {
													cards+=",";
												}
											}
											MessageLine l = connections[i];
											l.sendMessage(Message.fromPairs(
													"name:"+Message.Names.GAME_READY.toString(),
													Message.Keys.CARDS.toString()+":"+cards));
										}
										
										// Go to bidding state
										this.state = ServerRoomState.BIDDING_STATE;
										connections[bidStarter].sendMessage(Message.fromPairs(
												"name:"+Message.Names.REQUEST_BID.toString(),
												Message.Keys.CURRENT_BID.toString()+":0"));
										bidStarter ++;
										if(bidStarter==4) {
											bidStarter = 0;
										}
										
										
									} break;
									//TODO: Add QUITTING message thing
								}
							} break;
							
							//=============================================================================================================================================================
							//=============================================================================================================================================================
							//=============================================================================================================================================================
							//=============================================================================================================================================================
							//=============================================================================================================================================================
							//=============================================================================================================================================================
							case BIDDING_STATE: {
								switch(m.getName()) {
									
									case "MY_BID": {
										int bid = m.getInteger(Message.Keys.BID_AMOUNT.toString());
										if(bid == 0) {
											// Tell everyone someone passed
											MC.broadcastMessage(connections, Message.fromPairs(
													"name:"+Message.Names.SOMEONE_PASSED.toString(),
													Message.Keys.PLAYER_ID+":"+player));
											
											// Remember who passed
											bidPassers[player] = true;
											
											// If all passed, tell everybody it failed, and start over. Shuffle maindeck, spread cards, and ask bidstarter to bid again.
											boolean allPass = true;
											for(boolean b : bidPassers) {
												allPass = allPass && b;
											}
											if(allPass) {
												currentBid = 100;
												
												//
												//	Start shufflespread
												//
												
												Utilities.overhandArrayShuffle(mainDeck);
												
												// Make new decks for each player.
												
												playerDecks = new CardDeck[4];
												
												// Split the persistent main deck and give each playerdeck 12 cards from it.
												
												for(int i = 0; i < 4; i++) {
													Card[] currDeck = new Card[12];
													System.arraycopy(mainDeck,i*12,currDeck,0,12);
													playerDecks[i] = new CardDeck(currDeck);
												}
												
												// Separate the widow.
												
												widow = new Card[4];
												System.arraycopy(mainDeck, 12*4, widow, 0, 4);
												
												// Send each player their own cards.
												
												for(int i = 0; i < 4; i++) {
													String cards = "";
													for(int j = 0; j < playerDecks[i].getCardCount(); j++) {
														cards+=playerDecks[i].getCard(j).getName();
														if(j!=playerDecks[i].getCardCount()) {
															cards+=",";
														}
													}
													MessageLine l = connections[i];
													l.sendMessage(Message.fromPairs(
															"name:"+Message.Names.BIDDING_FAIL.toString(),
															Message.Keys.CARDS.toString()+":"+cards));
												}
												
												
												
												//
												//	End shufflespread
												//
												
												connections[bidStarter].sendMessage(Message.fromPairs(
														"name:"+Message.Names.REQUEST_BID.toString(),
														Message.Keys.CURRENT_BID+":0"));
												
												bidPassers = new boolean[] {false,false,false,false};
												
												break;
											}
											
										}
									} break;
									
									
									
									
									
									
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
	}
	
	private void onQuitGame(int i) {
		
	}

	private enum ServerRoomState {
		NAME_SETTING_STATE, GAME_LOBBY_STATE, BIDDING_STATE, GAME_STATE, END_GAME_STATE
	}
}
