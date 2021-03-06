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
	
	Card[][] teamDecks = new Card[52][2];
	int[] teamDeckAmount = new int[] {0,0};
	int[] teamRoundScore = new int[] {0, 0};
	int[] teamTotalScore = new int[] {0,0};
	int currentTurn = 0;
	
	int gameSetGoal = 0;
	int biddingTeam = -1;
	
	//--------------------------------
	//---CURRENT HAND Variables
	//--------------------------------
	
	Card[] currentHand;
	int currentHandCount = 0;
	Suit gameSuit = null;
	//------------------------
	//---BID Variables
	//------------------------
	int 		bidStarter = 0;
	int			isFirstBid = 1;
	int 		currentBid = 0;
	int			playerIdInTurnToBid	= 0;
	
	/**
	 * ================================================================================
	 * 						CONSTRUCTOR
	 * ================================================================================
	 * @param connections
	 */
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
	
	/**
	 * ================================================================================
	 * 								RUN
	 * ================================================================================
	 */
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
		
		
		//Bid Field
		int currentBid = 0;
		boolean 	bidCanFail = true;
		int 		bidPassCount = 0;
		boolean[] 	havePassedBid = new boolean[] {false,false,false,false};
		
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
									case "MY_NAME": {
										if(m.containsKey(Message.Keys.GAME_SETTINGS.toString())) {
											MC.broadcastMessage(connections, Message.fromPairs(
													"name:" + Message.Names.PLAYER_NAME.toString(),
													Message.Keys.PLAYER_NAME.toString() + ":" +m.getValue(Message.Names.PLAYER_NAME.toString()),
													Message.Keys.PLAYER_ID.toString()+":"+player,
													Message.Keys.GAME_SETTINGS.toString()+":"+m.getValue(Message.Keys.GAME_SETTINGS.toString())));
											
											gameSetGoal = m.getInteger(Message.Keys.GAME_SETTINGS.toString());
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
													Message.Keys.CARDS.toString()+":"+cards,
													Message.Keys.PLAYER_ID+":"+playerIdInTurnToBid));
										}
										
										// Go to bidding state
										this.state = ServerRoomState.BIDDING_STATE;
										
										
										//Reset failbid countthings
										havePassedBid = new boolean[] { false, false, false, false};
										bidCanFail = true;
										bidPassCount = 0;
										currentBid = 100;
										
									
										
										connections[bidStarter].sendMessage(Message.fromPairs(
												"name:"+Message.Names.REQUEST_BID.toString(),
												Message.Keys.CURRENT_BID.toString()+":0",
												Message.Keys.PLAYER_ID+":"+playerIdInTurnToBid,
												Message.Keys.IS_FIRST_BID+":"+isFirstBid));
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
										
										
										//TODO: REVISE CONCEPTUAL CORRECTNESS OF THIS
										
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
													Message.Keys.PLAYER_ID.toString()+":"+player,
													Message.Keys.IS_FIRST_BID+":"+isFirstBid));
											
											// Is there initial passStreak?
											if(bidCanFail) { // Yes initial passStreak
												
												// Increase passStreak counter
												bidPassCount++;
												
												// Did everyone pass?
												if(bidPassCount == 4) { // Yes everyone passed
													
													// !Shuffle and split cards again
													
													isFirstBid = 1;
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
																Message.Keys.CARDS.toString()+":"+cards,
																Message.Keys.PLAYER_ID+":"+playerIdInTurnToBid));
													}
													
													// Reset passStreak parameters
													havePassedBid = new boolean[] { false, false, false, false};
													bidCanFail = true;
													bidPassCount = 0;
													currentBid = 100;
													
													
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
													
													// Remember bidding team
													biddingTeam=lonePlayer;
													
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
													
													
													break;
													
												} else { // Not only one left
													// Do nothing special
													
												}
												
											}
										} else { // No player did not pass
											isFirstBid = 0;
											// REMEMBER THAT BID CANNOT FAIL NOW
											bidCanFail = false;
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
												playerIdInTurnToBid++;
												// Remember bidding team
												biddingTeam=player;
												
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
												
												break;
											} else { // No this is not the last person
												
												// Do nothing special
											}
										}
										
										// Find the next correct person.
										//GCN Giova te corregi este pedazo de codigo no debe de comenzar en uno y el nextPerson no era i
										int nextPerson = 0;
										for(int i = 1; i < 4; i++) {
											if(!havePassedBid[(player+i)%4]) {
												nextPerson = (player+i)%4;
												break;
											}
										}
										
										//Ask the next correct person
										connections[nextPerson].sendMessage(Message.fromPairs(
												"name:"+Message.Names.REQUEST_BID,
												Message.Keys.CURRENT_BID+":"+currentBid,
												Message.Keys.IS_FIRST_BID+":"+isFirstBid));
									}
									//TODO: Add QUITTING message thing
								}
							} break;
							case WIDOW_STATE: {
								switch(m.getName()) {
									case "MY_TRASH": {
										
										
										//TODO: VERIFY THAT THIS IS CORRECT, CONCEPTUALLY
										Card[] trashCards = new Card[4];
										String cardSource = m.getValue(Message.Keys.CARDS.toString());
										String[] sourceList = cardSource.split(",");
										for(int i = 0; i < trashCards.length;i++) {
											trashCards[i] = new Card(sourceList[i]);
										}
										
										teamDecks[0] = new Card[52];
										teamDecks[1] = new Card[52];
										teamDeckAmount[0]=0;
										teamDeckAmount[1]=0;

										teamRoundScore[0] = 0;
										teamRoundScore[1]=0;
										
										int playerTeam = player%2;
										
										System.arraycopy(trashCards,0,teamDecks[playerTeam],teamDeckAmount[playerTeam], trashCards.length);
										teamDeckAmount[playerTeam]+=trashCards.length;
										
										teamRoundScore[playerTeam]=calculateScore(trashCards);
					
										
										MC.broadcastMessage(connections, Message.fromPairs(
												"name:"+Message.Names.GAME_SUIT,
												Message.Keys.SUIT+":"+m.getValue(Message.Keys.SUIT.toString()),

														Message.Keys.PLAYER_TURN_ID+":"+player));

										currentHand = new Card[4];
										currentHandCount = 0;
										gameSuit = Suit.valueOf(m.getValue(Message.Keys.SUIT.toString()));
										this.state = ServerRoomState.GAME_STATE;
									} break;
									//TODO: Add QUITTING message thing
								}
							}
							case GAME_STATE: {
								switch(m.getName()) {
									case "MY_CARD": {
										//	REQUEST NEXT CARD
										
										
										Card card = new Card(m.getValue(Message.Keys.PLAYED_CARD.toString()));
										
										currentHand[player] = card;
										currentHandCount++;
										
										// Tell everyone someone played a card
										MC.broadcastMessage(connections, Message.fromPairs(
												"name:"+Message.Names.CARD_PLAYED,
												Message.Keys.PLAYED_CARD+":"+card.getName(),
												Message.Keys.PLAYER_ID+":"+player,
												Message.Keys.PLAYER_TURN_ID+":"+((player+1)%4)));
										
										//if this turn is LAST, compute hand winner
										if(currentHandCount == 4) { // Hand complete
											
											
											
											int handWinner = calculateWinner(currentHand,(player+1)%4,gameSuit);
											int handScore = calculateScore(currentHand) + 5;
											int handWinningTeam = handWinner%2;
											
											// Pass hand to a team deck
											System.arraycopy(currentHand,0,teamDecks[handWinningTeam],teamDeckAmount[handWinningTeam],4);
											teamDeckAmount[handWinningTeam] +=4;
											
											//Tell everybody who won hand, and how many points.
											MC.broadcastMessage(connections, Message.fromPairs(
													"name:"+Message.Names.HAND_WON,
													Message.Keys.PLAYER_ID+":"+handWinner,
													Message.Keys.HAND_SCORE+":"+handScore
													));
											
											// Increase team round score
											teamRoundScore[handWinningTeam]+=handScore;
											
											//Reset hand counters !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
											currentTurn++;
											currentHandCount = 0;
											//Reset hand!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
											currentHand = new Card[4];
											
											//Is round end?
											if(currentTurn==12) { // Round ended
												int[] teamScoreDeltas = new int[]{0,0};
												// Did declaring win?
												boolean declaringWon = teamRoundScore[biddingTeam]>=currentBid;
												
												
												// Remember which team won
												int roundWinningTeam = declaringWon?biddingTeam:((biddingTeam+1)%2);
												
												
												//Did declaring win?
												if(declaringWon) { // Declaring won
													teamScoreDeltas[biddingTeam] = teamRoundScore[biddingTeam];
													teamScoreDeltas[(biddingTeam+1)%2] = 0;
													
													if(teamRoundScore[biddingTeam]==165) {
														teamScoreDeltas[biddingTeam]+= 100+(currentBid==165?100:0);
													}
												} else {
													teamScoreDeltas[biddingTeam] = -currentBid;
													teamScoreDeltas[(biddingTeam+1)%2] = teamRoundScore[(biddingTeam+1)%2];
												}
												
												//Update total scores.
												teamTotalScore[0]+=teamScoreDeltas[0];
												teamTotalScore[1]+=teamScoreDeltas[1];
												
												// If last round
												//		Tell everyone winning team
												//		Tell everyone round is over (and no next one)
												//		Go to end state
												//		else
												// 		Rebuild main deck
												//		Tell everyone winning team
												//		Tell everyone round is over (and next one comes)
												//		Go to lobby state
												if(teamTotalScore[0] >= gameSetGoal || teamTotalScore[1] >= gameSetGoal) { // Last round
													
													
													int setWinningTeam = (teamTotalScore[0] >= gameSetGoal?0:1);
													
													MC.broadcastMessage(connections, Message.fromPairs(
															"name:"+Message.Names.SET_OVER,
															Message.Keys.TEAM_ID+":"+roundWinningTeam,
															Message.Keys.GAME_SET_WIN_ID+":"+setWinningTeam,
															Message.Keys.SCORE_DELTAS+":"+teamScoreDeltas[0]+","+teamScoreDeltas[1]));
													
													// Go to end state
													this.state = ServerRoomState.END_GAME_STATE;
												} else { // Not last round
													System.arraycopy(teamDecks[0],
															0, mainDeck, 0,
															teamDeckAmount[0]);
													System.arraycopy(teamDecks[1],
															0, mainDeck,
															teamDeckAmount[0],
															teamDeckAmount[1]);
													MC.broadcastMessage(connections, Message.fromPairs(
															"name:"+Message.Names.SET_OVER,
															Message.Keys.TEAM_ID+":"+roundWinningTeam,
															Message.Keys.SCORE_DELTAS+":"+teamScoreDeltas[0]+","+teamScoreDeltas[1]));
													
													// Go to lobby state
													this.state = ServerRoomState.GAME_LOBBY_STATE;
												}
												
											} else { // Round not end
												connections[handWinner].sendMessage(Message.fromPairs(
														"name:"+Message.Names.GIVE_CARD));
											}
										} else { // Hand not complete
											//Tell next person
											connections[(player+1)%4].sendMessage(Message.fromPairs("name:"+Message.Names.GIVE_CARD));
										}
										
									} break;
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
	
	private int calculateScore(Card[] hand) {
		int score = 0;
		for(Card c : hand) {
			score+=c.getValue();
		}
		return score;
	}

	private int calculateWinner(Card[] hand, int firstCard, Suit trump) {
		// if hand contains trump suit, find highest.
		// else find highest handsuit
		int highestValue = -1;
		int winningSlot = -1;
		{
			boolean hasTrump = false;
			
			for(int i = 0; i < hand.length; i++) {
				Card card = hand[i];
				if(hand[i].getSuit()==trump) {
					hasTrump=true;
					if(highestValue<card.getRank().getTrumpValue()) {
						highestValue = card.getRank().getTrumpValue();
						winningSlot = i;
					}
				}
			}
			if(hasTrump) {
				return winningSlot;
			}
		}
		Suit winSuit = hand[firstCard].getSuit();
		for(int i = 0; i < hand.length; i++) {
			Card card = hand[i];
			if(card.getSuit()==winSuit) {
				if(highestValue<card.getRank().getTrumpValue()) {
					highestValue=card.getRank().getTrumpValue();
					winningSlot = i;
				}
			}
		}
		
		return winningSlot;
	}

	private void onQuitGame(int i) {
		
	}

	private enum ServerRoomState {
		NAME_SETTING_STATE, GAME_LOBBY_STATE, BIDDING_STATE, GAME_STATE, END_GAME_STATE, WIDOW_STATE
	}
}
