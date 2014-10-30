public class MC {
	@Deprecated
	public static final String CU_QUITGAME = "I_QUIT";
	@Deprecated
	public static final String CU_MYNAME = "MY_NAME";
	@Deprecated
	public static final String CU_GAMEREADY = "AM_READY";
	
	@Deprecated
	public static final String SU_PLAYERQUIT = "PLAYER_QUIT";
	@Deprecated
	public static final String SU_PLAYERNAME = "PLAYER_NAME";
	@Deprecated
	public static final String SU_PLAYERJOIN = "PLAYER_JOINED";
	@Deprecated
	public static final String SU_TABLE_CREATED = "TABLE_CREATED";
	@Deprecated
	public static final String SU_GAME_START = "GAME_START";
	@Deprecated
	public static final String SR_PLAYERNAME = "REQUEST_NAME";
	
	public static final String P_NAME = "name";
	public static final String P_PLAYER_ID = "player_id";
	public static final String PARAMETER_PLAYER_NAME = "player_name";
	
	
	
	
	public static final String S_TABLE_FULL = "S_TABLE_FULL";
	public static final String S_GOT_ALL_NAMES = "S_GOT_ALL_NAMES";
	public static final String S_GIVE_BID = "S_GIVE_BID";
	public static final String S_BID_FAIL = "S_BID_FAIL";
	public static final String S_BID_WIN = "S_BID_WIN";
	
	public static final String C_MY_NAME = "C_MY_NAME";
	public static final String C_BID = "C_MY_BID";
	public static void broadcastMessage(MessageLine[] lines, Message m) {
		for(MessageLine l : lines) {
			if(l!=null) {
				l.sendMessage(m);
			}
		}
	}
}
