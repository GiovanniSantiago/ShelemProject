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
	private Team 			team;
	

	
	
	//===============
	//CONSTRUCTORS
	//==============
	public Player(String name) {
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
	
	public void setTeam(Team t) {
		this.team = t;
	}
	
	public Team getTeam() {
		return this.team;
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

	public String getName() {
		return name;
	}
}
