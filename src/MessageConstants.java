
public class MessageConstants {
	public static final String CLIENT_UPDATE_QUITGAME = "I_QUIT";
	public static final String CLIENT_UPDATE_MYNAME = "MY_NAME";
	
	public static final String SERVER_UPDATE_PLAYERQUIT = "PLAYER_QUIT";
	public static final String SERVER_UPDATE_PLAYERNAME = "PLAYER_NAME";
	public static final String SERVER_UPDATE_PLAYERJOIN = "PLAYER_JOINED";
	public static final String SERVER_UPDATE_TABLE_CREATED = "TABLE_CREATED";
	public static final String SERVER_REQUEST_PLAYERNAME = "REQUEST_NAME";
	
	public static final String PARAM_NAME = "name";
	public static final String PARAM_PLAYER_ID = "player_id";
	public static final String PARAM_PLAYER_NAME = "player_name";
	
	public static void broadcastMessage(MessageLine[] lines, Message m) {
		for(MessageLine l : lines) {
			if(l!=null) {
				l.sendMessage(m);
			}
		}
	}
}
