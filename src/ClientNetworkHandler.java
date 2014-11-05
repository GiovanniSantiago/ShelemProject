import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.Socket;
import java.util.Random;

import javax.swing.JOptionPane;

/**
 * Esta clase se encarga de las conecciones entre el cliente y el servidor.
 * Incluye todos los listener utilizados por el UI de Shelem
 * @author Giovanni Santiago
 * 
 */
public class ClientNetworkHandler implements Runnable {
	Socket 			sock;
	MessageLine 	line;
	Player[] 		players 		= new Player[4];
	int 			playerId 		= -1;
	
	
	
	LoginListener 	loginListener 	= new LoginListener();
	
	//==========================================
	//Field para Shelem Game Logic
	//==========================================
	int 		targetScore 	= 0;
	
	Card[] 		myCards 		= new Card[12];
	CardDeck 	myDeck;
	
	/**
	 * Flag set to true when the server asks if player is ready. For use of UI.
	 */
	boolean requestedGameReady = false;
	
	/**
	 * Flag set to true when the server asks for name. For use of UI.
	 */
	boolean isRequestingName = false;
	
	/**
	 * Flag set to true when the server asks for bid. For use of UI.
	 */
	boolean requestedBid = false;
	
	boolean quit = false;
	
	
	ClientGameState state = ClientGameState.TABLE_CREATION_STATE;
	
	public ClientNetworkHandler() {
		

		
	}
	
	/**
	 * ----------------------------------RUN-----------------------------------------------------------RUN--
	 */
	@Override
	public void run() {
		
		try {																			
			sock = new Socket("localhost", 7169);
			line = new MessageLine(sock);
			
			quit = false;
			
		} catch (IOException e) {
				JOptionPane.showMessageDialog(Test.logIn, "Connection unsuccessful. Please try again later.", "Conection Error", JOptionPane.ERROR_MESSAGE);
				quit = true;
		}
		
		
		
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
								case "YOUR_ID": {
									//Si la conecci�n se dio entonces pasa a decirle al usuario su id 
									playerId = m.getInteger(Message.Keys.PLAYER_ID.toString());
									
									if(playerId == 0){				//si es el primer jugador entonces hace setting del juego //
										Test.logIn.srvConLbl.setText("<HTML><p width = \"" + (Test.logIn.getWidth() - 30) + "\" align= \"center\">Congratulations you are player " + (playerId + 1) + "!!!!<br>You have the opportunity to decide the targeted score</p></HTM>");
										Test.logIn.srvConLbl.setSize(Test.logIn.frameWidth, 50);
										Test.logIn.srvConLbl.setLocation(10, Test.logIn.srvConLbl.getY());
										
										Test.logIn.targetScoreLbl.setVisible(true);
										Test.logIn.targetScoreTxtFld.setVisible(true);
										
										Test.logIn.send.setLocation(Test.logIn.send.getX(), Test.logIn.targetScoreLbl.getY() + Test.logIn.targetScoreLbl.getHeight() + 10);
									}else{
										Test.logIn.srvConLbl.setText(Test.logIn.srvConLbl.getText() + (playerId + 1) );
									}
									Test.logIn.exitBtn.setVisible(false);
									Test.logIn.exitBtn.setSelected(false);
	
									Test.logIn.startBtn.setVisible(false);
									Test.logIn.startBtn.setSelected(false);
	
									Test.logIn.srvConLbl.setVisible(true);
									
	
									Test.logIn.userNamLbl.setVisible(true);
									Test.logIn.usrNameTxtFld.setVisible(true);
									// Fix this later, as an extra, so it accepts name anyways, but buffers it. If entered before time, it sends saved name when name requested. If entered after time, send name.
	
									Test.logIn.statusLbl.setVisible(true);
									Test.logIn.statusLbl.setText("Waiting for " + (4 - (m.getInteger(Message.Keys.PLAYER_ID.toString()) + 1)) + " more players...");
	
									
									Test.logIn.send.setVisible(true);
								}break;
								//TODO: Add server notification tick message for when a player joins the table. Happens in ServerLauncher
								case "PLAYER_JOINED": 
									
									Test.logIn.statusLbl.setText("Waiting for " + (4 - (m.getInteger(Message.Keys.PLAYER_ID.toString()) + 1)) + " more players...");
									break;
									
								//Enter here when all player are already logged
								case "TABLE_FULL": {
									//	Table is full. Server state is now in NAME_SETTING_STATE, thus send my name and change myself to NAME_SETTING_STATE
										
									state 				= ClientGameState.NAME_SETTING_STATE;
									isRequestingName	=true;
									
								} break;
								//TODO: Add QUITTING message thing
								
							}
						} break;  //Table creation State out
						case NAME_SETTING_STATE: {
							switch(m.getName()) {
								
								case "PLAYER_NAME": {
									int index = m.getInteger(Message.Keys.PLAYER_ID.toString());
									players[index] = new Player(m.getValue(Message.Names.PLAYER_NAME.toString()));
									System.out.println(index);
									if(m.containsKey(Message.Keys.GAME_SETTINGS.toString())) {
										String settingsSource = m.getValue(Message.Keys.GAME_SETTINGS.toString());
										this.targetScore = Integer.parseInt(settingsSource);
										Test.mp.scoreBoard.setTargetScore(targetScore);
									}
								} break;
								
								//Everybody sent their name. Server state is now in GAME_LOBBY_STATE.		
								case "GOT_ALL_NAMES": {
																	
									state = ClientGameState.GAME_LOBBY_STATE;
									Test.logIn.dispose();
									Test.mainFrame.setVisible(true);
									
									System.out.println(playerId + "   " + ((playerId + 1) % 4) + "   " + (playerId + 2)%4 +   (playerId + 3) % 4);
									String[] names = {players[playerId].getName(), players[(playerId + 1) % 4].getName(), players[(playerId + 2)%4].getName(), players[(playerId + 3) % 4].getName()};
									Test.mp.board.setUserNames(names);
									Test.mp.scoreBoard.setTeamNames(names[0] + " " + names[2], names[1] + " " + names[3]);
								} break;
								//TODO: Add QUITTING message thing
							}
						} break;//NameSetting out
						case GAME_LOBBY_STATE: {
							switch(m.getName()) {
								// I may be asked to say READY.
								// I WILL be told READY.
								//TODO: No server notification tick needed.
								case "ARE_YOU_READY": {
									//TODO: Add UI notification that server asked for ready
									//TODO: USE READYREQUEST FLAG
									requestedGameReady = true;
									
									////TODO:THESE LINES GO IN UI CODE
									//////////////
									////TODO:UI CODE AAAAA
									line.sendMessage(Message.fromPairs("name:"+Message.Names.I_AM_READY.toString()));
								} break;
								case "GAME_READY": {
									
									//	Player in charge said he was ready. I got my cards now. Server state is now in BIDDING_STATE.
									// 	Show initial cards in the board
									
									// DUPE CODE FROM GAME_READY
									
									String 		cardlist 	= m.getValue(Message.Keys.CARDS.toString());		//Gets input from the server
									String[] 	cardNames 	= cardlist.split(",");								//Convierte el input del server a un array de nombres de cartas
																
									for(int i = 0; i < cardNames.length; i++) {								//Inicializa las cartas del current player
										this.myCards[i] = new Card(cardNames[i]);
									}
									
									this.myDeck = new CardDeck(this.myCards);
									Test.mp.board.setDeck(this.myDeck);
									
									int playersCardsAmmounts[] = {12, 12, 12, 12};
									Test.mp.board.setPlayerCardsAmount(playersCardsAmmounts);
									// DUPE END
									
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
								case "SOMEONE_BID": {
									////
									////	Someone submitted a bid
									////
									int player = m.getInteger(Message.Keys.PLAYER_ID.toString());
									int amount = m.getInteger(Message.Keys.BID_AMOUNT.toString());
									
									
									
									
									//		U      U	IIIII
									//		U      U	  I
									//		U      U	  I
									//		U	   U	  I
									//		 UUUUUU 	IIIII
									
									
									
									
								} break;
								case "SOMEONE_PASSED": {
									////
									////	Someone submitted a bid
									////
									int player = m.getInteger(Message.Keys.PLAYER_ID.toString());
									
									
									
									
									//		U      U	IIIII
									//		U      U	  I
									//		U      U	  I
									//		U	   U	  I
									//		 UUUUUU 	IIIII
									
									
									
									
								}
								
								//I will be asked for a bid.
								//I will be told if bidding fails.
								//I will be told if bidding wins.
								case "REQUEST_BID": {
									//TODO: Add UI notification that the server asked for bid
									//TODO: USE BIDREQUEST FLAG
									requestedBid = true;
									
									
									//		U      U	IIIII
									//		U      U	  I
									//		U      U	  I
									//		U	   U	  I
									//		 UUUUUU 	IIIII
									Random r = new Random();
									if(m.getInteger(Message.Keys.CURRENT_BID.toString())==0) {
										////
										////	You are first bidder
										////	This is dummy response
										////
										line.sendMessage(Message.fromPairs(
												"name:"+Message.Names.MY_BID.toString(),
												Message.Keys.BID_AMOUNT.toString()+":"+(r.nextFloat()<.8f?100:0)));
									} else {
										////
										////	This is dummy response
										////
										line.sendMessage(Message.fromPairs(
												"name:"+Message.Names.MY_BID.toString(),
												Message.Keys.BID_AMOUNT.toString()+":"+(r.nextFloat()<.8f?m.getInteger(Message.Keys.CURRENT_BID.toString())+5:0)));
									}
									
								} break;
								case "BIDDING_COMPLETE": {
									//
									//	A bidder has won. Server state is now in GAME_STATE
									//
									
									int bidWinner = m.getInteger(Message.Keys.BID_WINNER.toString());
									if(bidWinner==playerId) {
										state = ClientGameState.WIDOW_STATE;
									} else {
										state = ClientGameState.GAME_STATE;
									}
									//state = ClientGameState.GAME_STATE;
								} break;
								case "BIDDING_FAIL": {
									//
									// Everybody passed. Start bidding over.
									//
									
									String 		cardlist 	= m.getValue(Message.Keys.CARDS.toString());		//Gets input from the server
									String[] 	cardNames 	= cardlist.split(",");								//Convierte el input del server a un array de nombres de cartas
																
									for(int i = 0; i < cardNames.length; i++) {									//cambia las cartas del current player
										this.myCards[i] = new Card(cardNames[i]);
									}
									
									this.myDeck.setDeck(this.myCards);
									Test.mp.board.setDeck(this.myDeck);
									
									int playersCardsAmmounts[] = {12, 12, 12, 12};
									Test.mp.board.setPlayerCardsAmount(playersCardsAmmounts);
									
									
									
									//		U      U	IIIII
									//		U      U	  I
									//		U      U	  I
									//		U	   U	  I
									//		 UUUUUU 	IIIII
									
									//TODO: Add UI notification that bidding failed.
									//TODO: reset bidding everything back to normal, including visual notifiers.
								} break;
								//TODO: Add QUITTING message thing
							}
						} break;
						case WIDOW_STATE: {
							switch(m.getName()) {
								case "YOUR_WIDOW": {
									String widowSource = m.getValue(Message.Keys.CARDS.toString());
									
									///////
									///////
									///////
									///////	TODO: EXTRACT WIDOW FROM THIS AND CAN NOW SEND THE FOUR TRASH CARDS
									///////
									///////
									///////
									
									this.state = ClientGameState.GAME_STATE;
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
								//TODO: THIS IS WHERE I LEFT OFF WEDNESDAY. GAMESTATE (or ENDGAMESTATE) IS NEXT, OR SERVERSIDE EVERYTHING TILL THIS POINT.
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
		return Message.fromPairs("name:" + Message.Names.MY_NAME.toString() ,Message.Names.PLAYER_NAME.toString() + ":"+name);
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
			
			if(o.equals(Test.logIn.exitBtn)){
				System.exit(0);
			}else if(o.equals(Test.logIn.startBtn)){
				new Thread(Test.ch).start();
				
			}else if(o.equals(Test.logIn.send)){
				if(isRequestingName){
					isRequestingName = false;
					
					String name = Test.logIn.usrNameTxtFld.getText();
					name = name.replaceAll("$", "");					//The username can't have $ or : because of message regulation with the server
					name = name.replaceAll(":", "");
					
					if(name.length() == 0){
						JOptionPane.showMessageDialog(Test.logIn, "You have to enter a username. And it can't contain \":\" or \"$\" characters.\nPlease try again.", "Username error", JOptionPane.WARNING_MESSAGE);
					}else{
						if(playerId == 0){
							String targetScore = Test.logIn.targetScoreTxtFld.getText();
							
							if(!Utilities.isInteger(targetScore)){
								JOptionPane.showMessageDialog(Test.logIn, "Score Target must be an integer number.", "Score Target input error", JOptionPane.WARNING_MESSAGE);
							}else{
								int targetScoreInt = Integer.parseInt(targetScore);
								
								if(targetScoreInt < 0){
									        targetScoreInt *= -1;
								}
								targetScore = "" + targetScoreInt;
								line.sendMessage(Message.fromPairs(
										"name:" + Message.Names.MY_NAME,
										Message.Keys.PLAYER_NAME.toString() + ":" +name,
										Message.Keys.GAME_SETTINGS.toString()+":"+targetScore));
								Test.logIn.send.setEnabled(false);
							}
							
							
							
						}else{
							line.sendMessage(Message.fromPairs(
									"name:" + Message.Names.MY_NAME,
									Message.Keys.PLAYER_NAME.toString() + ":" +name));
							Test.logIn.send.setEnabled(false);
						}
					
						
						Test.logIn.statusLbl.setText("Waiting for others player to enter their usernames...");
						Test.logIn.statusLbl.setForeground(Color.RED);
						Test.logIn.statusLbl.setFont(new Font(Test.logIn.statusLbl.getFont().getFontName(), Font.BOLD, 15));
					}
					
				}else{
					JOptionPane.showMessageDialog(Test.logIn, "The server is still waiting for others player to connect.\nPlease wait until all players are connected and then send your username.", "Out message error", JOptionPane.ERROR_MESSAGE);
				}
				
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
		END_GAME_STATE,
		WIDOW_STATE
	}
}
