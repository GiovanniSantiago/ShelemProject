import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.Socket;
import java.util.Random;


public class ClientNetworkHandler implements Runnable {
	Socket 			sock;
	MessageLine 	line;
	Player[] 		players 		= new Player[4];
	int 			playerId 		= -1;
	
	LoginListener 	loginListener 	= new LoginListener();
	
	
	/**
	 * Flag set to true when the server asks if player is ready. For use of UI.
	 */
	boolean requestedGameReady = false;
	
	/**
	 * Flag set to true when the server asks for name. For use of UI.
	 */
	boolean requestedName = false;
	
	/**
	 * Flag set to true when the server asks for bid. For use of UI.
	 */
	boolean requestedBid = false;
	
	
	ClientGameState state = ClientGameState.TABLE_CREATION_STATE;
	
	public ClientNetworkHandler() {
		

		
	}
	
	@Override
	public void run() {
		
		try {
			sock = new Socket("localhost", 7169);
			line = new MessageLine(sock);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		boolean quit = false;
		state = ClientGameState.TABLE_CREATION_STATE;
		
		/*
		 * First get all state changing messages (yet to add quit messages)
		 */
		
		//Loop semi infinito para verificar cuando el servidor envio un mensaje
		while(!quit) {
			try {
				if(line.isReady()) {															//busca si ya hay un mensaje para leer
					Message m = line.receiveMessage();
					
					switch(state) {
						case TABLE_CREATION_STATE: {
							switch(m.getName()) {
								//TODO: Add server notification tick message for when a player joins the table. Happens in ServerLauncher
							
								case "table_full": {
									//
									//	Table is full. Server state is now in NAME_SETTING_STATE, thus send my name and change myself to NAME_SETTING_STATE
									//
									
									
									////TODO: Add UI notification that server asked for name (?) (if name is not provided before any networking already)
									////TODO: USE NAMEREQUEST FLAG
									////
									
									requestedName=true;
									
									/////////////////
									//TODO:These three lines will go in UI code
									/////////////////
									//TODO:UI CODE AAAAA NAME CAN'T HAVE ':' OR '$' AAAAAAAA
									/////////////////
									String name = "Apollopops" + new Random().nextInt(100);
									line.sendMessage(Message.fromPairs("name:my_name","player_name:"+name));
									playerId = m.getInteger("id");
									state = ClientGameState.NAME_SETTING_STATE;
									/////^^^those three
								} break;
								//TODO: Add QUITTING message thing
							}
						} break;
						case NAME_SETTING_STATE: {
							switch(m.getName()) {
								case "player_name": {
									players[m.getInteger("id")] = new Player(m.getValue("player_name"));
								} break;
								case "got_all_names": {
									//
									//	Everybody sent their name. Server state is now in GAME_LOBBY_STATE.
									//
									
									state = ClientGameState.GAME_LOBBY_STATE;
								} break;
								//TODO: Add QUITTING message thing
							}
						} break;
						case GAME_LOBBY_STATE: {
							switch(m.getName()) {
								// I may be asked to say READY.
								// I WILL be told READY.
								//TODO: No server notification tick needed.
								case "is_game_ready": {
									//TODO: Add UI notification that server asked for ready
									//TODO: USE READYREQUEST FLAG
									requestedGameReady = true;
									
									////TODO:THESE LINES GO IN UI CODE
									//////////////
									////TODO:UI CODE AAAAA
									line.sendMessage(Message.fromPairs("name:i_am_ready"));
								} break;
								case "game_ready": {
									//
									//	Player in charge said he was ready. Server state is now in BIDDING_STATE.
									//
									
									String cardlist = m.getValue("cards");
									//TODO: Add card receiving mechanism here. cardlist will have the format SUIT_RANK,SUIT_RANK,SUIT_RANK...SUIT_RANK,SUIT_RANK, where SUIT can be H,D,S,C, and RANK can be A,2,3,4,5,6,7,8,9,J,Q,K
									state = ClientGameState.BIDDING_STATE;
								}
								
								//TODO: Add QUITTING message thing
							}
						} break;
						case BIDDING_STATE: {
							switch(m.getName()) {
								//TODO: Add server notification tick message for when a player passes or sets a bid:
								//			I will be told if a player sets a bid.
								//			I will be told if a player passes a bid.
								
								//I will be asked for a bid.
								//I will be told if bidding fails.
								//I will be told if bidding wins.
								case "give_bid": {
									//TODO: Add UI notification that the server asked for bid
									//TODO: USE BIDREQUEST FLAG
									requestedBid = true;
									
									////TODO:THESE LINES GO IN UI CODE
									//////////////
									////TODO:UI CODE AAAAA
									Random r = new Random();
									line.sendMessage(Message.fromPairs("name:my_bid","bid:"+(r.nextFloat()<.8f?m.getInteger("current_bid")+5:0)));
								} break;
								case "bidding_win": {
									//
									//	A bidder has won. Server state is now in GAME_STATE
									//
									state = ClientGameState.GAME_STATE;
								} break;
								case "bidding_fail": {
									//
									// Everybody passed. Start bidding over.
									//
									//TODO: Add UI notification that bidding failed.
									//TODO: reset bidding everything back to normal, including visual notifiers.
								} break;
								//TODO: Add QUITTING message thing
							}
						} break;
						case GAME_STATE: {
							switch(m.getName()) {
								//THIS IS WHERE I LEFT OFF WEDNESDAY. GAMESTATE (or ENDGAMESTATE) IS NEXT, OR SERVERSIDE EVERYTHING TILL THIS POINT.
								//THIS IS WHERE I LEFT OFF WEDNESDAY. GAMESTATE (or ENDGAMESTATE) IS NEXT, OR SERVERSIDE EVERYTHING TILL THIS POINT.
								//THIS IS WHERE I LEFT OFF WEDNESDAY. GAMESTATE (or ENDGAMESTATE) IS NEXT, OR SERVERSIDE EVERYTHING TILL THIS POINT.
								//THIS IS WHERE I LEFT OFF WEDNESDAY. GAMESTATE (or ENDGAMESTATE) IS NEXT, OR SERVERSIDE EVERYTHING TILL THIS POINT.
								//THIS IS WHERE I LEFT OFF WEDNESDAY. GAMESTATE (or ENDGAMESTATE) IS NEXT, OR SERVERSIDE EVERYTHING TILL THIS POINT.
								//THIS IS WHERE I LEFT OFF WEDNESDAY. GAMESTATE (or ENDGAMESTATE) IS NEXT, OR SERVERSIDE EVERYTHING TILL THIS POINT.
								//THIS IS WHERE I LEFT OFF WEDNESDAY. GAMESTATE (or ENDGAMESTATE) IS NEXT, OR SERVERSIDE EVERYTHING TILL THIS POINT.
								//THIS IS WHERE I LEFT OFF WEDNESDAY. GAMESTATE (or ENDGAMESTATE) IS NEXT, OR SERVERSIDE EVERYTHING TILL THIS POINT.
								//THIS IS WHERE I LEFT OFF WEDNESDAY. GAMESTATE (or ENDGAMESTATE) IS NEXT, OR SERVERSIDE EVERYTHING TILL THIS POINT.
								//THIS IS WHERE I LEFT OFF WEDNESDAY. GAMESTATE (or ENDGAMESTATE) IS NEXT, OR SERVERSIDE EVERYTHING TILL THIS POINT.
								
								
								
								//TODO: Add server notification tick message for EVERYTHING:
								//			A player played a card
								//			A hand was won
								//I will be asked for my play. I will provide a valid card every time.
								//I will be told if the game is over, if there will be a next game, who won. If so, go to GAME_LOBBY_STATE
								//I will be told if the game is over, if the gameset is over. If so, go to END_GAME_STATE
								
								//cases here
								
								//TODO: Add QUITTING message thing
							}
						} break;
						case END_GAME_STATE: {
							
						} break;
					}
					
					
				}
			} catch (IOException e) {
				
			}
		}
		
		
		
		
		/*
		 * End all statechanging messages
		 */
		
		
		
		
		
		/*
		 * DISABLING BOTTOM CODE FOR ATTEMPTS AT LESS NONSENSICAL AND DISPARATE REPLACEMENT
		 */
		/*while (!quit) {
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
									m.getValue(MC.PARAMETER_PLAYER_NAME));
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
											MC.PARAMETER_PLAYER_NAME,
											name)));
						}
							break;
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}*/
		
		System.out.println("NETWORK HANDLING THREAD DONE");
	}
	


	
	
	@Deprecated
	private String onNameRequested(int index) {
		System.out.println("THEY ASKED FOR MY NAME. I AM APOLLOPOPS!");
		return "Apollopops";
	}
	
	@Deprecated
	private void onPlayerQuit() {
		System.out
				.println("A PLAYER QUIT. THE LOBBY IS BORKED NOW. RAGEQUITTING...");
	}
	
	@Deprecated
	private void onNameReceived(int index, String name) {
		System.out.println(String.format("THIS OTHER GUY, %d, IS CALLED %s",
				index, name));
	}
	
	@Deprecated
	private void onPlayerJoin(int index) {
		System.out.println(String.format("SOMEONE IS INTRUDING. HE'S #%d",
				index));
	}
	
	
	/*
	 * Message quickCreation methods
	 */
	
	private Message createSetNameMessage(String name) {
		return Message.fromPairs("name:my_name","player_name:"+name);
		/*
		return new Message(new MessagePair("name",MC.C_MY_NAME),
				new MessagePair(MC.PARAMETER_PLAYER_NAME,name));*/
	}
	
	////////////////////////////////
	////
	////		THIS IS WHERE THE MOUSELISTENER CLASS IS DECLARED AND DONE
	////
	////////////////////////////////
	
	
	/**
	 * -------------------------------------------------------------------------------------------------------------------------
	 * ----------------------------------------LOGIN LISTENER--------------------------------------------------------------------
	 * -------------------------------------------------------------------------------------------------------------------------
	 * @author Glorimar Castro
	 *
	 */
	class LoginListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent arg0) {
			Object o = arg0.getSource();
			
			if(o.equals(LoginFrame.exitBtn)){
				System.exit(0);
			}else if(o.equals(LoginFrame.startBtn)){
				
				LoginFrame.exitBtn.setVisible(false);
				LoginFrame.exitBtn.setSelected(false);
				
				LoginFrame.startBtn.setVisible(false);
				LoginFrame.startBtn.setSelected(false);
				
				new Thread(Test.ch).start();
				
			}
			
		}
		
	}
	private enum ClientGameState {
		/**
		 * State in which client waits for enough clients to connect to the server
		 * Clients only listen in this state
		 */
		TABLE_CREATION_STATE, 
		/**
		 * State in which each client tells the server their name, and they each wait till the server tells everybody each name
		 * Clients only send one message
		 */
		NAME_SETTING_STATE, 
		/**
		 * State in which a player will be asked if they're ready to start game<br>
		 * Only one client will send a message
		 * All clients will receive notification
		 */
		GAME_LOBBY_STATE, 
		/**
		 * State in which all bidding happens, from beginning, until a player wins a bid
		 * Clients will only respond to specific requests from the server
		 */
		BIDDING_STATE,
		/**
		 * State in which all card-related actions happen
		 */
		GAME_STATE,
		/**
		 * State after game is over. Communication with server may or may not have ceased when entering this state
		 */
		END_GAME_STATE
	}
}
