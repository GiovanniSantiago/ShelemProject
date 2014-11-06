import java.util.ArrayList;

public class Team {
	
	Player[] 		players 		= new Player[2];
	Card[]			collectedCards 	= new Card[52];
	
	int 			collectedAmount = 0;
	boolean 		isBidWinner 	= false;
	int 			gamePoints 		= 0;
	int 			bid 			= 0;
	
	
	
	public Team(){
		
		
	}
	
	public Team(Player player1, 
			Player player2, 
			boolean bidWinner,
			int bid) {
		players[0] = player1;
		players[1] = player2;
		this.isBidWinner = bidWinner;
		this.bid = bid;
	}
	
	
	//===========================================================================
	//====================METHODS=========================================METHODS     
	//===========================================================================
	/**
	 * --------------------------------------------------------------------------
	 * 								IS PLAYER IN TEAM
	 * --------------------------------------------------------------------------
	 * 	Este metodo verifica si el player dado como parametro es parte del team
	 * @param player
	 * @return
	 */
	public boolean isPlayerInTeam(Player player){
		boolean isInTeam = false;
		for(Player p : this.players){
			if(player.getUserName().equals(p.getUserName())){
				isInTeam = true;
			}
		}
		return isInTeam;		
	}
	
	
	public int getCollectedCardAmount() {
		return collectedAmount;
	}
	
	public String getNames(){
		return players[0].getName() + " " + players[1].getName();
	}
	
	
	public boolean isDeclaringTeam() {
		return isBidWinner;
	}
	
	/**
	 * --------------------------------------------------------------------------
	 * 								GET BID AMMOUT
	 * --------------------------------------------------------------------------
	 * @return
	 */
	public int getBidAmmount(){
		return bid;
	}
	
	//---------------------------------
	//-------	Setters
	//---------------------------------
	/**
	 * --------------------------------------------------------------------------
	 * 								SET IS BID WINNER
	 * --------------------------------------------------------------------------
	 * @param isBidWinner
	 */
	public void setBidStatus(boolean isBidWinner){
		this.isBidWinner = isBidWinner;
	}
	
	/**
	 * --------------------------------------------------------------------------
	 * 								 SET BID AMMOUNT
	 * --------------------------------------------------------------------------
	 * @param bid
	 */
	public void setBidAmmount(int bid){
		this.bid = bid;
	}
	/**
	 * --------------------------------------------------------------------------
	 * 								   ADD PLAYERS
	 * --------------------------------------------------------------------------
	 * 	Este metodo se le tiene que dar un arreglo de 2 Jugadores
	 * @param players
	 */
	public void addPlayers(Player[] players){
		for(int i = 0; i < this.players.length; i++){
			this.players[i] = players[i];
		}
	}
	public void collectHand(Card[] cards) {
		for(int i = 0; i < cards.length; i++) {
			collectedCards[collectedAmount] = cards[i];
			collectedAmount++;
		}
	}
	
	
	

	
	
	
}
