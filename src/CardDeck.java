import java.util.ArrayList;
import java.util.Arrays;
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
	private Card[] 			cardDeck;
	private int				currentPosition;
	private int 			actualLenght;
	
	
	//===============
	//CONSTRUCTORS
	//===============
	public CardDeck(Card[] cardDeck){
		currentPosition = -1;
		this.cardDeck = new Card[16];
		actualLenght = 0;
		for(Card card : cardDeck){
			this.cardDeck[actualLenght] = card;
			actualLenght++;
		}
	}
	
	public CardDeck(){
		currentPosition	= -1;
		actualLenght	= 0;
		this.cardDeck = new Card[16];
		
	}
	
	/**
	 * Sort in ascending order the cards. Spades < Club < Heart < Diamond
	 */
	public void sortDeck(){
		Card[] temp = new Card[actualLenght];
		for(int i = 0; i < actualLenght; i++){
			temp[i] = cardDeck[i];
		}
		Arrays.sort(temp);
		for(int i = 0; i < actualLenght; i++){
			cardDeck[i] = temp[i];
		}
		
	}
	
	/**
	 * -------------------------------------------------------------------------------------------------------------
	 * 											SET CARD DECK
	 * -------------------------------------------------------------------------------------------------------------
	 * Allow the user to set the deck with a new set of cards
	 * @param cardDeck
	 */
	public void setDeck(Card[] cardDeck){
		currentPosition = -1;
		actualLenght = 0;
		for(Card card : cardDeck){
			this.cardDeck[actualLenght] = card;
			actualLenght++;
		}
		if(actualLenght < 16 && actualLenght > 12){
			this.cardDeck[13] = null;
			this.cardDeck[14] = null;
			this.cardDeck[15] = null;
			this.cardDeck[16] = null;
		}
	}
	
	public void printDeck(){
		for(int i = 0; i < actualLenght; i++){
			System.out.println(cardDeck[i]);
			
		}
	}
	
	/**
	 * Shuffle the arrayof card with the overhand thecnique
	 */
	public void overhandShuffle(){
		sortDeck();
		Card[] temp = new Card[actualLenght];
		for(int i = 0; i < actualLenght; i++){
			temp[i] = cardDeck[i];
		}
		
		Utilities.overhandArrayShuffle(temp);
		for(int i = 0; i < actualLenght; i++){
			cardDeck[i] = temp[i];
		}
		
	}
	
	public boolean addCard(Card card){
		if(actualLenght < cardDeck.length){
			cardDeck[actualLenght] = card;
			actualLenght++;
			return true;
		}else
			return false;
	}
	
	public boolean removeCard(Card card){
		boolean found = false;
		int position = -1;
		for(int i = 0; i < actualLenght; i++){
			if(cardDeck[i].equals(card)){
				position	= i;
				found 		= true;
				break;
			}
		}
		if(position > -1){
			for(int i = position; i < actualLenght - 1; i++){
				cardDeck[i] = cardDeck[i+1];
			}
			cardDeck[actualLenght-1] = null;
			
			if(currentPosition >= position){
				currentPosition--;
			}
			actualLenght--;
		}
		
		return found;
	}
	
	public Card getCard(int position){
		if(position >= actualLenght){
			return null;
		}else
			return cardDeck[position];
		
	}
	
	public int getAmntOfCards(){
		return actualLenght;
	}
	
	public Card[] getDeck(){
		return cardDeck;
	}
	
	public int getSize(){
		return actualLenght;
	}
	
	public int getCardCount() {
		int r = 0;
		for(int i = 0; i < cardDeck.length; i++) {
			if(cardDeck[i]!=null) {
				r++;
			} else {
				break;
			}
		}
		return r;
	}
	
	/**
	 * return the next card and move the pointer to the next card
	 * @return
	 */
	
	//TODO return an exception
	public Card getNextCard(){
		if(currentPosition < actualLenght){
			currentPosition++;
			return cardDeck[currentPosition]; 	
		}else
			return null;
		
	}
	
	/*public boolean hasNextCard(){
		if(currentPosition < actualLenght -1){
			return true;
		}else
			return false;
	}*/
	
	public boolean setCurrentPosition(int position){
		if(position > -1 && position < actualLenght){
			this.currentPosition = position;
			return true;
		}else
			return false;
		
	}
	
	/**
	 * Return the previous card but leave the pointer where it is
	 * @return
	 */
	public Card getPreviousCard(){
		if(currentPosition > 0){
			return cardDeck[currentPosition - 1];
		}else
			return null;
		
	}
	
}
