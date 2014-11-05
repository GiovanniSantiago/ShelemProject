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
	
	Card[] teamDeck1 = new Card[52];
	Card[] teamDeck2 = new Card[52];
	int teamDeck1amount = 0;
	int teamDeck2amount = 0;
	
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
		
		boolean bidCanFail = true;
		int bidPassCount = 0;
		boolean[] havePassedBid = new boolean[] {false,false,false,false};
		
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
										
										
										//Reset failbid countthings
										bidCanFail = true;
										bidPassCount = 0;
										havePassedBid = new boolean[] { false,false,false,false};
										
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
										
										
										/*
										 * 
										 * 
										 * Did player pass?
										 * 	yes-Remember who passed
										 * 		Tell everybody someone passed
										 * 		Is there initial passStreak?
										 * 		yes-Increase passstreak counter
										 * 			Did everyone pass?
										 * 			yes-Shuffle and split all cards again
										 * 				Tell everyone bidding failed. 
										 * 				Reset passstreak parameters
										 * 			 no-Do nothing special
										 * 		 no-Is there one left?
										 * 			yes-Declare one left the winner
										 * 				Tell everybody there was winner
										 * 				Move to widowstate (tell winner and give bid)
										 * 			 no-Do nothing special.
										 * 	 no-Get new bid.
										 * 		Tell everybody new bid
										 * 		Is this the last person?
										 * 		yes-Declare this person the winner
										 * 			Tell everybody there was winner
										 * 			Move to widowstate (tell winner and give bid)
										 * 		 no-Do nothing special
										 * 		
										 * 
										 * Find next correct person
										 * 
										 * 
										 * Ask the correct next person
										 * 
										 */
										
										//Did player pass?
										if(bid==0) { // Yes player passed
											
											//Remember who passed
											havePassedBid[player] = true;
											
											//Tell everybody someone passed
											MC.broadcastMessage(connections, Message.fromPairs(
													"name:"+Message.Names.SOMEONE_PASSED.toString(),
													Message.Keys.PLAYER_ID.toString()+":"+player));
											
											// Is there initial passStreak?
											if(bidCanFail) { // Yes initial passStreak
												
												// Increase passStreak counter
												bidPassCount++;
												
												// Did everyone pass?
												if(bidPassCount == 4) { // Yes everyone passed
													
													// !Shuffle and split cards again
													
													
													Utilities.overhandArrayShuffle(mainDeck);
													
													// !Make new decks for each player.
													
													playerDecks = new CardDeck[4];
													
													// !Split the persistent main deck and give each playerdeck 12 cards from it.
													
													for(int i = 0; i < 4; i++) {
														Card[] currDeck = new Card[12];
														System.arraycopy(mainDeck,i*12,currDeck,0,12);
														playerDecks[i] = new CardDeck(currDeck);
													}
													
													// !Separate the widow.
													
													widow = new Card[4];
													System.arraycopy(mainDeck, 12*4, widow, 0, 4);
													
													
													
													// Tell everyone bidding failed
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
													
													// Reset passStreak parameters
													havePassedBid = new boolean[] { false, false, false, false};
													bidCanFail = true;
													bidPassCount = 0;
													
													
												} else { // No everyone did not pass
													
													// Do nothing special
												}
												
												
											} else { // No initial passStreak
												
												// Is there one left?
												int peopleLeft = 0;
												for(int i = 0; i < 4; i++) {
													if(!havePassedBid[i]) {
														peopleLeft++;
													}
												}
												// Is there one left?
												if(peopleLeft == 1) { // Yes only one left
													
													// Declare one left the winner
													int lonePlayer = 0;
													{
														for(int i = 0; i < 4; i++) {
															if(!havePassedBid[i]) {
																lonePlayer = i;
																break;
															}
														}
														
													}
													
													// Tell everyone there was a winner
													MC.broadcastMessage(connections, Message.fromPairs(
															"name:"+Message.Names.BIDDING_COMPLETE.toString(),
															Message.Keys.BID_AMOUNT.toString()+":"+currentBid,
															Message.Keys.BID_WINNER.toString()+":"+lonePlayer));
													
													// Move to widowstate
													state = ServerRoomState.WIDOW_STATE;
													String cardlist = "";
													for(int i = 0; i < widow.length;i++) {
														cardlist+=widow[i].getName();
														if(i!=widow.length-1) {
															cardlist+=",";
														}
													}
													connections[lonePlayer].sendMessage(Message.fromPairs(
															"name:"+Message.Names.YOUR_WIDOW,
															Message.Keys.CARDS+":"+cardlist));
													
													
													
													
													
												} else { // Not only one left
													// Do nothing special
													
												}
												
											}
										} else { // No player did not pass
											
											// Get new bid
											currentBid = bid;
											
											// Tell everyone new bid
											MC.broadcastMessage(connections, Message.fromPairs(
													"name:"+Message.Names.SOMEONE_BID.toString(),
													Message.Keys.BID_AMOUNT.toString()+":"+currentBid,
													Message.Keys.PLAYER_ID.toString()+":"+player));
											
											// Is this the last person?
											int peopleLeft = 0;
											for(int i = 0; i < 4; i++) {
												if(!havePassedBid[i]) {
													peopleLeft++;
												}
											}
											
											// Is this the last person?
											if(peopleLeft == 1) { // Yes this is the last person
												
												//Declare this person the winner
												
												//Tell everybody this is the winner
												MC.broadcastMessage(connections, Message.fromPairs(
														"name:"+Message.Names.BIDDING_COMPLETE,
														Message.Keys.BID_AMOUNT+":"+currentBid,
														Message.Keys.BID_WINNER+":"+player));
												
												// Move to widowstate
												state = ServerRoomState.WIDOW_STATE;
												String cardlist = "";
												for(int i = 0; i < widow.length;i++) {
													cardlist+=widow[i].getName();
													if(i!=widow.length-1) {
														cardlist+=",";
													}
												}
												connections[player].sendMessage(Message.fromPairs(
														"name:"+Message.Names.YOUR_WIDOW,
														Message.Keys.CARDS+":"+cardlist));
											} else { // No this is not the last person
												
												// Do nothing special
											}
										}
									}
									//TODO: Add QUITTING message thing
								}
							} break;
							case WIDOW_STATE: {
								switch(m.getName()) {
									case "MY_TRASH": {
										Card[] trashCards = new Card[4];
										String cardSource = m.getValue(Message.Keys.CARDS.toString());
										String[] sourceList = cardSource.split(",");
										for(int i = 0; i < trashCards.length;i++) {
											trashCards[i] = new Card(sourceList[i]);
										}
										
										
										if(player%2==0) {
											System.arraycopy(trashCards, 0, teamDeck1, teamDeck1amount, trashCards.length);
											teamDeck1amount+=trashCards.length;
										}
										if(player%2==1) {
											System.arraycopy(trashCards, 0, teamDeck2, teamDeck2amount, trashCards.length);
											teamDeck2amount+=trashCards.length;
										}
										
										this.state = ServerRoomState.GAME_STATE;
									} break;
									//TODO: Add QUITTING message thing
								}
							}
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
		NAME_SETTING_STATE, GAME_LOBBY_STATE, BIDDING_STATE, GAME_STATE, END_GAME_STATE, WIDOW_STATE
	}
}
