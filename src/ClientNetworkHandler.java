import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.Socket;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JLabel;
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
	Team			myTeam			= new Team();
	Team			oponentTeam		= new Team();
	int				playersPos[]	= new int[4];
	int 			playerId 		= -1;
	
	
	BidListener		bidListener 	= new BidListener();
	LoginListener 	loginListener 	= new LoginListener();
	
	JLabel			infoLbl			= new JLabel();
	
	//==========================================
	//Field para Shelem Game Logic
	//==========================================
	final int	MAX_BID_POSSIBLE	= 165;
	final int	BID_MULTIPLE		= 5;
	
	
	int 		targetScore 	= 0;
	int			actualBid		= 100;
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
									players[index].setPlayerID(index);
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
									
									//Set Players Position Array
									playersPos[playerId] = 0;
									playersPos[(playerId + 1) % 4] = 1;
									playersPos[(playerId + 2) % 4] = 2;
									playersPos[(playerId + 3) % 4] = 3;
									
									for(int i = 0; i < players.length; i++){
										players[i].setPlayerPosition(playersPos[(playerId + i)%4]);
									}
									
									//Set teams
									myTeam.addPlayers(new Player[] {players[playerId], players[(playerId + 2)%4]});
									oponentTeam.addPlayers(new Player[] {players[(playerId + 1) % 4], players[(playerId + 3)%4]});
									
									
									//UI update
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
									
									myDeck = new CardDeck();
									
									for(String s : cardNames){
										myDeck.addCard(new Card(s));
									}
									Test.mp.board.setDeck(myDeck);
									
									int playersCardsAmmounts[] = {12, 12, 12, 12};
									Test.mp.board.setPlayerCardsAmount(playersCardsAmmounts);
									
									//Muestra es panel para el Bid y hace el set del mismo
									String userNames[] = {players[playerId].getName(), players[(playerId + 1)%4].getName(), players[(playerId + 2)%4].getName(), players[(playerId + 3)%4].getName()};
									Test.mp.bidPanel = new BiddingPanel(userNames);
									
									//GCN ===============================CORREGIRR
									//Esto no puede ser asi tiene que ser mediante un key de parte del servidor
									if(playerId == 0){
										Test.mp.bidPanel.eneableButtons();
									}
									
									Test.mp.bidPanel.setBounds(Test.mp.board.getWidth()/2 - Test.mp.bidPanel.getWidth()/2, Test.mp.board.getHeight()/2 - Test.mp.bidPanel.getHeight()/2 + 20, Test.mp.bidPanel.getWidth(), Test.mp.bidPanel.getHeight());
									Test.mp.board.add(Test.mp.bidPanel);
									
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
								case "SOMEONE_BID": {						//Action taken when someone else make a bid
									int player = m.getInteger(Message.Keys.PLAYER_ID.toString());		//Busca quien hizo el Bid
									actualBid = m.getInteger(Message.Keys.BID_AMOUNT.toString());		//Hace Set en el programa del Bid actual
									
									
									Test.mp.bidPanel.setPlayerBid(playersPos[player], actualBid);		//Update al UI para notificar el bid del jugador	
									Test.mp.bidPanel.bidStatuslbl.setText(players[player].getName() + " bid. Waiting for next player...");		//Update al mensaje de estatus.
								} break;
								
								case "SOMEONE_PASSED": {			////	Someone submitted a bid
									
									int player = m.getInteger(Message.Keys.PLAYER_ID.toString());
									Test.mp.bidPanel.setPlayerBid(playersPos[player], -1);
									Test.mp.bidPanel.bidStatuslbl.setText(players[player].getName() + " pass. Waiting for next player...");
									
								}break; //-_- -_- Te estoy mirando mal Giovanni estube 36min dandole cabeza de pq esto esta dando problema y te comistes un BREAK!!!! JAJAJA
										//loll y pa colmo se fue en loop infinito
								//I will be asked for a bid.
								//I will be told if bidding fails.
								//I will be told if bidding wins.
								case "REQUEST_BID": {
									
									Test.mp.bidPanel.sumBtn.setEnabled(true);
									Test.mp.bidPanel.senBidBtn.setEnabled(true);
									Test.mp.bidPanel.passBtn.setEnabled(true);
									Test.mp.bidPanel.bidStatuslbl.setText("It is your Turn");
									Test.mp.bidPanel.bidStatuslbl.setForeground(Color.YELLOW);
									
								} break;
								
								case "BIDDING_COMPLETE": {   	//	A bidder has won. Server state is now in GAME_STATE
				
									int bidWinnerID = m.getInteger(Message.Keys.BID_WINNER.toString());		//busca quien gano
									
									//set winning team
									if(myTeam.isPlayerInTeam(players[bidWinnerID])){
										myTeam.setBidStatus(true);
										myTeam.setBidAmmount(actualBid);
										Test.mp.scoreBoard.teamBidsLbl[0].setText("" + actualBid);
									}else{
										oponentTeam.setBidStatus(true);
										Test.mp.scoreBoard.teamBidsLbl[1].setText("" + actualBid);
									}
									
									if(bidWinnerID==playerId) {
										state = ClientGameState.WIDOW_STATE;
									} else {
										
										state = ClientGameState.GAME_STATE;
									}
									
									//UI Update
									Test.mp.bidPanel.setVisible(false);
									//JOptionPane.showMessageDialog(Test.mainFrame, "Player " + bidWinnerID + " gano");
									
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
									Test.mp.board.setDeck(myDeck);
									
									
									
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
									String 		widowSource 	= m.getValue(Message.Keys.CARDS.toString());
									String[] 	widow 			= widowSource.split(",");
									
									for(String s : widow){
										myDeck.addCard(new Card(s));
									}
									
									//UI Update
									myDeck.sortDeck();
									Test.mp.board.setDeck(myDeck);
									Test.mp.board.mouseLst.isMyTurn = true;
									
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
								isRequestingName = false;
							}
							
							
							
						}else{
							line.sendMessage(Message.fromPairs(
									"name:" + Message.Names.MY_NAME,
									Message.Keys.PLAYER_NAME.toString() + ":" +name));
							Test.logIn.send.setEnabled(false);
							isRequestingName = false;
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
	
	
	/**
	 * -------------------------------------------------------------------------------------------------------------------------
	 * ----------------------------------------BID LISTENER----------------------------------------------------------------------
	 * -------------------------------------------------------------------------------------------------------------------------
	 * 	Esta clase se encarga de manejar las acciones ha tomar cuando un usuario presiona uno de los botones
	 * del panel de Bid. Este panel solo presenta tres botones para las tres acciones a tomar: amuntar Bid, pasar mandar 
	 * Bid.
	 * 		Amuntar Bid: 	No puede ser menos de 100 ni pasar 165
	 * 		Pass:			En esta opcion el cliene decide que no apostara mas y se hace disable de los demas botones
	 * 		Send:			Se envia la cantidad de apostar al servidor, ya en el boton aumentar se toma medidas de seguridad
	 * 						para que no se pase de 165.
	 * @author Glorimar Castro
	 *
	 */
	class BidListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			JButton btn = (JButton) e.getSource();
			
			if(btn == Test.mp.bidPanel.sumBtn){							//Aumentar Bid
				if(actualBid < MAX_BID_POSSIBLE - BID_MULTIPLE){
					actualBid+= BID_MULTIPLE;
					Test.mp.bidPanel.bidTxt.setText("" + actualBid);
				}else{
					JOptionPane.showMessageDialog(Test.mp, "Maximmun bid value reached.", "Max Bid Value", JOptionPane.WARNING_MESSAGE);
				}
			}else if(btn == Test.mp.bidPanel.passBtn){					//Pass button
				Test.mp.bidPanel.senBidBtn.setEnabled(false);
				Test.mp.bidPanel.senBidBtn.setVisible(false);
				Test.mp.bidPanel.passBtn.setEnabled(false);
				Test.mp.bidPanel.passBtn.setVisible(false);
				Test.mp.bidPanel.sumBtn.setEnabled(false);
				Test.mp.bidPanel.sumBtn.setVisible(false);
				System.out.println("dfgbfgbhfgHN");
				line.sendMessage(Message.fromPairs(
						"name:"+Message.Names.MY_BID.toString(),
						Message.Keys.BID_AMOUNT.toString()+":"+0));
				Test.mp.bidPanel.bidStatuslbl.setForeground(Color.WHITE);

			}else{														//Send Button
				Test.mp.bidPanel.senBidBtn.setEnabled(false);
				Test.mp.bidPanel.passBtn.setEnabled(false);
				Test.mp.bidPanel.sumBtn.setEnabled(false);
				line.sendMessage(Message.fromPairs(
						"name:"+Message.Names.MY_BID.toString(),
						Message.Keys.BID_AMOUNT.toString()+":"+actualBid));
				Test.mp.bidPanel.bidStatuslbl.setForeground(Color.WHITE);
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
