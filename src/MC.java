
public class MC {
	/**
	 * Sent when a player quits<br><b>
	 * SEND ONLY ONCE
	 */
	public static final String CU_QUITGAME = "I_QUIT";
	/**
	 * Sent when a player decides a username<br>
	 * SEND ONLY ONCE
	 */
	public static final String CU_MYNAME = "MY_NAME";
	/**
	 * Sent when player 1 starts game in gameset<br><b>
	 * SEND ONLY ONCE
	 */
	public static final String CU_GAMEREADY = "AM_READY";
	
	/**
	 * Sent when a player quits<br><b>
	 * SEND ONLY ONCE
	 */
	public static final String SU_PLAYERQUIT = "PLAYER_QUIT";
	/**
	 * Sent when a player has decided a username<br><b>
	 * SEND ONLY ONCE PER PLAYER
	 */
	public static final String SU_PLAYERNAME = "PLAYER_NAME";
	/**
	 * Sent when a player has joined a table <br><b>
	 * SEND ONLY ONCE PER PLAYER CONNECT
	 */
	public static final String SU_PLAYERJOIN = "PLAYER_JOINED";
	public static final String SU_TABLE_CREATED = "TABLE_CREATED";
	public static final String SU_GAME_START = "GAME_START";
	public static final String SR_PLAYERNAME = "REQUEST_NAME";
	
	public static final String P_NAME = "name";
	public static final String P_PLAYER_ID = "player_id";
	public static final String P_PLAYER_NAME = "player_name";
	
	public static void broadcastMessage(MessageLine[] lines, Message m) {
		for(MessageLine l : lines) {
			if(l!=null) {
				l.sendMessage(m);
			}
		}
	}
}
