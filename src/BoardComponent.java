import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import javax.swing.JLabel;
import javax.swing.JPanel;


public class BoardComponent extends JPanel{
	//==========================================================================================
	//FIELD
	//==========================================================================================
	
	final int 			CARD_WIDTH 				= 68;
	final int 			CARD_HEGHT 				= 100;
	final Dimension 	labelsMaximunDimension	= new Dimension(200, 40);
	final int			CARD_OVERLAP_FACTOR 	= 3;							//This is used to declare what factor of the card is going to be show and the next card drawn 	
	final int			MAX_CARDS_AMNT			= 16;							//Maximun of card that a player can have
	final static int	PARTNER_POS				= 2;
	final static int	EAST_OPPONENT_POS		= 1;
	final static int	WEST_OPPONENT_POS		= 3;
	final static int	CURRENT_PLAYER_POS		= 0;
	
	Graphics2D 			g2;
	AffineTransform 	oldForm;												//Original setting for the Graphics tranform
	JLabel[] 			userNames 			= new JLabel[4];;					//Labels for the userNames, Current Player - Player at the right - team player - player at the left
	boolean  			isUserNamesSet 		= false;							//True if the user names where set, false if the user names are nulls
	int[] 				playerCardsAmmount 	= new int[4];						//Amount of card by player
	Point[] 			userNamePoint 		= new Point[4];						//Coordinate for drawing player cards in the center
	Point[] 			deckPoint			= new Point[4];						//Coordinate for drawing the decks, starts coordinates
	BufferedImage		backgroundImg;											//Board image
	
	//For the current player we use a separeta component for easy event handeling 
	
	//=========================================================================================
	//CONSTRUCTORS
	//=========================================================================================
	public BoardComponent(){
		
		//-------------------------
		//Field initialization
		
		for(int i = 0; i < this.userNames.length; i++){							//UserNames labels set ups
			this.userNames[i] = new JLabel("", JLabel.CENTER);
			userNames[i].setFont(new Font("TimesNewRoman", Font.BOLD, 14));
			userNames[i].setForeground(Color.WHITE);
			userNames[i].setSize(labelsMaximunDimension);
			userNames[i].setVisible(true);
		}
		
		for(int i = 0; i < this.userNamePoint.length; i++){						//initialize Points where the labels are going to be inserted
			this.userNamePoint[i] 	= new Point();
			this.deckPoint[i] 		= new Point();
		}
		
		

		
		//-------------------------
		//Panel Set-up
		this.backgroundImg = ImageRegistry.getImage("boardBackground.png");
		this.setLayout(null);
		this.setSize(backgroundImg.getWidth(), backgroundImg.getHeight());
		this.setVisible(true);
	}
	
	//==========================================================================================
	//METHODS
	//==========================================================================================
	/**
	 * -------------------------------------- PAINT COMPONENT -------------------------------------- 
	 * 
	 * @param g
	 */
	@Override
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		this.g2 = (Graphics2D) g;
		oldForm = g2.getTransform();
		
		g2.drawImage(backgroundImg, null, 0, 0);
		
		if(isUserNamesSet){
			drawOpponentCards();
			drawPartneCards();
		}
		
				
	}
	
	//TODO
	/**
	 * -------------------------------------- SET USER NAME --------------------------------------
	 * 
	 * The order for the names in the Array are Current Player - Player at the right - team player - player at the left
	 * This method initialize deckPoint, userNamePoint and the userNameLabels
	 * @param names
	 */
	public void setUserNames(String[] names){
		//save the names on the label array
		for(int i = 0; i < userNames.length; i++){
			userNames[i].setText(names[i]);
		}
		
		//Set point location for labels
		this.userNamePoint[0].setLocation(this.getWidth()/2.0 - userNames[0].getWidth()/2.0 , this.getHeight() - userNames[0].getHeight() );
		this.userNamePoint[1].setLocation(this.getWidth() 	- userNames[1].getWidth() 		, this.getHeight()*(1.0/4));
		this.userNamePoint[2].setLocation(this.getWidth()/2 - userNames[2].getWidth()/2 	, userNames[2].getHeight()/2);
		this.userNamePoint[3].setLocation(0													, this.getHeight()*(1.0/4));
		
		//Set user name labels bounds and add the labels to the panel
		for(int i = 0; i < this.userNames.length; i++){
			this.userNames[i].setBounds(new Rectangle(userNamePoint[i], labelsMaximunDimension));
			this.add(userNames[i]);
		}
	
		//Set deck points
		int powSign = 1;
		for(int i = 0; i < this.deckPoint.length; i+=2){
			double x = userNamePoint[i].getX() + userNames[i].getWidth()/2 -((MAX_CARDS_AMNT*CARD_WIDTH/3 + CARD_WIDTH*2.0/3))/2;
			double y = userNamePoint[i].getY() + Math.pow(-1, powSign)*CARD_HEGHT/2;
			this.deckPoint[i].setLocation(x, y);
			this.deckPoint[i + 1].setLocation(userNamePoint[i + 1].getX() + userNames[i + 1].getWidth()/2 + CARD_HEGHT/2, userNames[i+ 1].getHeight()+ userNames[i+ 1].getY());
			powSign++;
		}
		
		isUserNamesSet = true;
		repaint();
		
	}
	
	/**
	 * -------------------------------------- SET PLAYER CARDS AMOUNT -------------------------------------- 
	 * 
	 * 	This method set the amount of card for the specified player in the position given
	 * @param playerPosition
	 * @param playerCardsAmount
	 * @return
	 */
	public boolean setPlayerCardsAmount(int playerPosition, int playerCardsAmount){
		if(playerPosition < 4 && playerPosition> -1 && playerCardsAmount > -1 && playerCardsAmount <= this.MAX_CARDS_AMNT){
			this.playerCardsAmmount[playerPosition] = playerCardsAmount;
			repaint();
			return true;
		}else{
			return false;
		}

	}
	
	/**
	 * -------------------------------------- SET PLAYER CARDS AMOUNT -------------------------------------- 
	 * 
	 * 	Return false if the array given is greater than four or if one of the integer values in the 
	 * array where less than 0 or greater than 16
	 * @param playerCardsAmount
	 * @return
	 */
	public boolean setPlayerCardsAmount(int[] playerCardsAmount){
		if(playerCardsAmount.length == this.playerCardsAmmount.length){
			//Verify that all inputs are good
			for(int i = 0; i < this.playerCardsAmmount.length; i++){
				if (playerCardsAmount[i] < 0 || playerCardsAmount[i] > this.MAX_CARDS_AMNT) {
					return false;																				//return false if one of the input is out of bound
				}
			}
			for(int i = 0; i < this.playerCardsAmmount.length; i++){
				this.playerCardsAmmount[i] = playerCardsAmount[i];
			}
			
			repaint();
			return true;
		}else
			return false;
	}
	
	/**
	 *--------------------------------------  DRAW OPPONENT CARDS -------------------------------------- 
	 *
	 *	This method need that the user names and player cards ammount arrays have been already initialize. 
	 *If this two array are initialize the draw the cards deck for the opponents. What is draw is the back 
	 *of the card since the player can't see the opponent cards, for that reason what is needed is the 
	 *amount of cards that the opponent player have. 
	 * 
	 * @param playerPosition
	 * @param ammountOfCards
	 */
	private void drawOpponentCards(){
		for(int playerPosition = 1; playerPosition < 4; playerPosition+=2){					//Loop for drawing the cards for the two opponents
			int x =(int)deckPoint[playerPosition].getX();
			int y =(int)deckPoint[playerPosition].getY();
	        g2.rotate(Math.toRadians(90), x, y);					//especifica que se rote alrededor de su propio eje
	        
			for(int i = 0; i < playerCardsAmmount[playerPosition]; i++){
				g2.drawImage(ImageRegistry.getImage("cardBack.jpg"), null, x, y);
				x += CARD_WIDTH/CARD_OVERLAP_FACTOR;
			}
			
			g2.setTransform(oldForm);
		}	
	}
	
	/**
	 * --------------------------------------  DRAW PARTNER CARDS --------------------------------------
	 * 
	 */
	private void drawPartneCards(){
		int x =(int)deckPoint[PARTNER_POS].getX();
		int y =(int)deckPoint[PARTNER_POS].getY();
                
		for(int i = 0; i < playerCardsAmmount[PARTNER_POS]; i++){
			g2.drawImage(ImageRegistry.getImage("cardBack.jpg"), null, x, y);
			x += CARD_WIDTH/CARD_OVERLAP_FACTOR;
		}
		
		g2.setTransform(oldForm);
	}
	
	
}