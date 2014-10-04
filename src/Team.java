import java.util.ArrayList;

public class Team {
	Player[] players = new Player[2];
	ArrayList<Card> collectedCards = new ArrayList<Card>();
	boolean bidWinner = false;
	int gamePoints = 0;
	String name;
	int bid = 0;
	
	public Team(String name, 
			Player player1, 
			Player player2, 
			boolean bidWinner,
			int bid) {
		players[0] = player1;
		players[1] = player2;
		this.name = name;
		this.bid = bid;
	}
	
	public boolean isDeclaringTeam() {
		return bidWinner;
		
		
	}
	
	
	
}
