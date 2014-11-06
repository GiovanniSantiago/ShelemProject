import java.util.ArrayList;

/**
 * 
 * @author Giovanni
 * @author Glorimar Castro
 *
 */
public class Player {
	
	//===============
	//FIELD
	//===============
	private CardDeck		handDeck;
	private String 			userName;
	private Team 			team;
	private	int				playerID;
	private int				playerTablePosition;
	

	
	
	//===============
	//CONSTRUCTORS
	//==============
	public Player(String name) {
		this.userName 		= name;
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
	
	/**
	 * ---------------------------------------------------------------
	 * 						SET PLAYER ID
	 * ---------------------------------------------------------------
	 * @param id
	 */
	public void setPlayerID(int id){
		this.playerID = id;
	}
	
	/**
	 * ---------------------------------------------------------------
	 * 						SET PLAYER POSITION
	 * ---------------------------------------------------------------
	 * @param pos
	 */
	public void setPlayerPosition(int pos){
		this.playerTablePosition = pos;
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

	/**
	 * ---------------------------------------------------------------
	 * 						GET PLAYER ID
	 * ---------------------------------------------------------------
	 * @return
	 */
	public int getPlayerId(){
		return playerID;
	}
	
	/**
	 * ---------------------------------------------------------------
	 * 						GET PLAYER POSITION
	 * ---------------------------------------------------------------
	 * @return
	 */
	public int getPlayerPosition(){
		return this.playerTablePosition;
	}
	public String getUserName(){
		return userName;
	}
	public String getName() {
		return userName;
	}
}
