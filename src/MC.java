
public class MC {
	public static final String CU_QUITGAME = "I_QUIT";
	public static final String CU_MYNAME = "MY_NAME";
	
	public static final String SU_PLAYERQUIT = "PLAYER_QUIT";
	public static final String SU_PLAYERNAME = "PLAYER_NAME";
	public static final String SU_PLAYERJOIN = "PLAYER_JOINED";
	public static final String SU_TABLE_CREATED = "TABLE_CREATED";
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
