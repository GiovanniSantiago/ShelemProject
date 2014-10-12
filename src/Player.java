import java.util.ArrayList;

/**
 * 
 * @author Giovanni
 *
 */
public class Player {
	
	private ArrayList<Card> handCards;
	private String 			name;
	private PlayerPosition 	position;
	
	
	public Player(PlayerPosition position, String name) {
		this.position = position;
		this.name = name;
		this.handCards = new ArrayList<Card>(12);
	}
	
	public boolean hasSuit(Suit s) {
		for(Card c : handCards) {
			if(c.getSuit() == s)
				return true;
		}
		return false;
	}
	
	public int getCardCount() {
		return handCards.size();
	}
	
	public Card getCard(int i) {
		return handCards.get(i);
	}
	
	public void dealCard(Card c) {
		this.handCards.add(c);
	}

	public PlayerPosition getPosition() {
		return position;
	}

	public String getName() {
		return name;
	}
}
