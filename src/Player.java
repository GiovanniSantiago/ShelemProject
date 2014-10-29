import java.util.ArrayList;

/**
 * 
 * @author Giovanni
 *
 */
public class Player {
	
	//===============
	//FIELD
	//===============
	private CardDeck		handDeck;
	private String 			name;
	private PlayerPosition 	position;
	private Player			teamPlayer; //TODO
	

	
	
	//===============
	//CONSTRUCTORS
	//==============
	public Player(PlayerPosition position, String name) {
		this.position 	= position;
		this.name 		= name;
		this.handDeck 	= new CardDeck();
	}
	
	

	
	
	//===============
	//METHODS
	//===============
	public boolean hasSuit(Suit s) {
		for(Card c : handDeck.getDeck()) {
			if(c.getSuit() == s)
				return true;
		}
		return false;
	}
	
	public int getAmntOfCard() {
		return handDeck.getAmntOfCards();
	}
	
	public Card getCard(int i) {
		return handDeck.getCard(i);
	}
	
	public void dealCard(Card c) {
		this.handDeck.addCard(c);
	}

	public PlayerPosition getPosition() {
		return position;
	}

	public String getName() {
		return name;
	}
}
