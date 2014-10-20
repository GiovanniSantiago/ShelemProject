import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JComponent;
import javax.swing.JPanel;


public class DeckComponent extends JComponent{
	//====================
	//FIELD
	//====================
	CardDeck 		deck;
	Graphics2D 		g2;
	boolean 		show;			//if true draw the cards if false draw the ammount of cadrs in the deck but the back of the card
	int				amntCards = 0;
	static int		cardWidth;			//When we try to draw the opponent cards this variables are going to be used and have to be initializy
	static int		cardHeight;
	
	
	
	//====================
	//CONSTRUCTORS
	//====================
	public DeckComponent(int amntCards){
		this.setVisible(true);
		this.show 	= true;
		cardWidth 	= 0;
		cardHeight 	= 0;
		this.amntCards = amntCards;
		show = false;															//it is drawing the cards for the opponents player
	}
	public DeckComponent(CardDeck deck){
		this.deck 	= deck;
		deck.sortDeck();
		this.setVisible(true);
		cardWidth 	= deck.getCard(0).getImg().getWidth();
		cardHeight 	= deck.getCard(0).getImg().getHeight();
		show = true;  															//it is drawing the cards for the actual player
		
	}
	public DeckComponent(){
		this.setVisible(true);
	}
	
	//====================
	//METHODS
	//====================
	public void paintComponent(Graphics g){
		this.g2 = (Graphics2D)g;
		
		if(show){
			drawCurrentPlayerDeck();
		}else{
			drawOpponentPlayerDeck();
		}
		
		
	}
	/**
	 * Private method to draw the cards in the player deck
	 */
	private void drawCurrentPlayerDeck(){
		int x = 0;
		int y = 0;
		for(Card card : this.deck.getDeck()){
			g2.drawImage(card.getImg(), null, x, y);
			x += cardWidth/3;
		}
	}
	
	
	/**
	 * Private method to draw the deck pf an opponent player
	 */
	private void drawOpponentPlayerDeck(){
		int x = 0;
		int y = 0;
		g2.translate(x, (amntCards*cardWidth)/3 + cardWidth*(2.0/3)); 
        g2.rotate(-Math.PI/2);
		for(int i = 0; i < amntCards; i++){
			g2.drawImage(ImageRegistry.getImage("cardBack.jpg"), null, x, y);
			x += cardWidth/3;
		}
		
	}
	
	public void setCardDeck(CardDeck cd){
		this.deck 	= cd;
		deck.sortDeck();
		this.setVisible(true);
		cardWidth 	= deck.getCard(0).getImg().getWidth();
		cardHeight 	= deck.getCard(0).getImg().getHeight();
		show = true;  															//it is drawing the cards for the actual player
		repaint();
		
	}
	
	public void setAmntOfCards(int amntOfCards){
		this.amntCards = amntOfCards;
		repaint();
	}
	
	
}
