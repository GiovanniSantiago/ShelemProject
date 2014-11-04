import java.io.IOException;
import java.util.Arrays;


public class ServerRoom extends Thread {
	MessageLine[] connections;
	ServerRoomState state = ServerRoomState.NAME_SETTING_STATE;
	ShelemGame game = new ShelemGame();
	
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
	
	public ServerRoom(MessageLine[] connections) {
		this.connections = connections;

		int index = 0;
		for(Suit s: Suit.values()) {
			for(Rank r: Rank.values()) {
				baseDeck[index] = new Card(s,r);
				index++;
			}
		}
	}
	
	@Override
	public void run() {
		for(int i = 0; i < connections.length; i++) {
			connections[i].sendMessage(Message.fromPairs("name:table_full","id:"+i));
		}
		
		Player[] players = new Player[4];
		int numPlayers = 0;
		
		// ------------------------------------------------------------------------------------------------------
		// ---------------Creates a base deck, shuffles it, and splits it in four, leaving four cards as widow---
		// ------------------------------------------------------------------------------------------------------
		
		CardDeck[] playerDecks = new CardDeck[4];
		Card[] widow = new Card[4];
		
		
		//
		//
		//
		
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
										MC.broadcastMessage(connections, Message.fromPairs(
												"name:" + Message.Names.PLAYER_NAME.toString(),
												Message.Names.PLAYER_NAME.toString() + ":" +m.getValue(Message.Names.PLAYER_NAME.toString()),
												"id:"+player));
										players[player] = new Player(m.getValue("name"));
										numPlayers++;
										
										if(numPlayers==4) {
											MC.broadcastMessage(connections, Message.fromPairs(
													"name:" + Message.Names.GOT_ALL_NAMES.toString()));
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
													"name:game_ready",
													"cards:"+cards));
										}
										
										// Go to bidding state
										this.state = ServerRoomState.BIDDING_STATE;
										
									} break;
									//TODO: Add QUITTING message thing
								}
							} break;
							case BIDDING_STATE: {
								switch(m.getName()) {
									
									
									
									
									
									
									
									
									
									// HAVE TO DO BIDDING STATE THINGS NOW
									// ALSO EXTRACT CARDS FROM CLIENT SIDE MESSAGE i_am_ready
									
									
									
									
									
									
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
