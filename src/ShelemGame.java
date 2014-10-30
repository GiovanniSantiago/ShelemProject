import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class ShelemGame {
	
	public static final int PLAYER_HAND_SIZE = 12;
	
	CardDeck deck = new CardDeck();
	Player[] players = new Player[4];
	Team declaringTeam, defendingTeam;
	Suit currentRule;
	int bid = 100;
	
	public ShelemGame() {
		
	}
	
	public void setPlayers(Player[] players) {
		this.players = players;
	}
	
	public void setPlayerAt(int index, Player p) {
		players[index] = p;
	}
	
	/**
	 * Automatically creates new team objects, setting opposite side players as a team, and 
	 * 
	 */
	public void setTeams(){
		
	}
}
