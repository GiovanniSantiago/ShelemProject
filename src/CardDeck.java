import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * Class for simulate a card deck. This class is dependent of the Card class.
 * @author Glorimar Castro
 *
 */

public class CardDeck {

	//===============
	//FIELD
	//===============
	private ArrayList<Card> cardDeck;
	private int				currentPosition = -1;
	
	
	//===============
	//CONSTRUCTORS
	//===============
	public CardDeck(ArrayList<Card> cardDeck){
		this.cardDeck = new ArrayList<Card>();
		this.cardDeck.addAll(cardDeck);
	}
	
	public CardDeck(Card[] cardDeck){
		this.cardDeck = new ArrayList<Card>();
		for(Card card : cardDeck){
			this.cardDeck.add(card);
		}
	}
	//===============
	//METHODS
	//===============
	public void sortDeck(){
		Collections.sort(cardDeck);
	}
	
	public void printDeck(){
		for(Card element: cardDeck){
			System.out.println(element);
			
		}
	}
	
	public void overhandShuffle(){
		sortDeck();
		Utilities.overhandArrayListShuffle(this.cardDeck);
		
	}
	
	public ArrayList<Card> getDeck(){
		return this.cardDeck;
	}
	
	public Card getNextCard(){
		currentPosition++;
		if(currentPosition >= cardDeck.size()){
			currentPosition = 0;
		}
		return cardDeck.get(currentPosition);
	}
	
	
	
}
