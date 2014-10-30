import java.util.ArrayList;

public class Team {
	
	Player[] 		players 		= new Player[2];
	Card[]			collectedCards 	= new Card[52];
	int 			collectedAmount = 0;
	boolean 		bidWinner 		= false;
	int 			gamePoints 		= 0;
	int 			bid 			= 0;
	
	
	public Team(Player player1, 
			Player player2, 
			boolean bidWinner,
			int bid) {
		players[0] = player1;
		players[1] = player2;
		this.bidWinner = bidWinner;
		this.bid = bid;
	}
	
	
	
	public boolean isDeclaringTeam() {
		return bidWinner;
	}
	
	public void collectHand(Card[] cards) {
		for(int i = 0; i < cards.length; i++) {
			collectedCards[collectedAmount] = cards[i];
			collectedAmount++;
		}
	}
	
	
	
	public int getCollectedCardAmount() {
		return collectedAmount;
	}
	
	public String getNames(){
		return players[0].getName() + " " + players[1].getName();
	}
	
	
	
}
