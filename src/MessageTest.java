
public class MessageTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String[] sources = new String[] {
				"name:GAMESTART$request:name$player_index:0$",
				"game_status:start$initial_value:0$",
				"name:START_DECK$cards:A_H,2_D,K_D$"
		};
		Message[] messages = new Message[sources.length];
		for(int i=0; i < messages.length;i++) {
			messages[i]=new Message(sources[i]);
		}
		
		for(int i = 0; i < messages.length; i++) {
			System.out.println(messages[i].toString());
		}
		
	}

}
