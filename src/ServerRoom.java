import java.io.IOException;

public class ServerRoom extends Thread {
	MessageLine[] connections;
	ServerRoomState state;
	
	public ServerRoom(MessageLine[] connections) {
		this.connections = connections;
		
	}
	
	@Override
	public void run() {
		MessageConstants.broadcastMessage(connections, new Message(
				new MessagePair(MessageConstants.PARAM_NAME,MessageConstants.SERVER_UPDATE_TABLE_CREATED)));
		int namesReceived = 0;
		while(true) {
			switch(state) {
				case GETTING_NAMES: {
					if(namesReceived!=4) {
						for(int i = 0; i < connections.length; i++) {
							MessageLine line = connections[i];
							if(line.isReady()) {
								Message m = line.receiveMessage();
								switch(m.getValue(MessageConstants.PARAM_NAME)) {
									case MessageConstants.CLIENT_UPDATE_MYNAME: {
										namesReceived++;
										MessageConstants.broadcastMessage(connections, new Message(
												new MessagePair(MessageConstants.PARAM_NAME,MessageConstants.SERVER_UPDATE_PLAYERNAME),
												new MessagePair(MessageConstants.PARAM_PLAYER_ID,""+i),
												new MessagePair(MessageConstants.PARAM_PLAYER_NAME,m.getValue(MessageConstants.PARAM_PLAYER_NAME))));
										//LEFT AT THIS EXACT LINE
									} break;
									case MessageConstants.CLIENT_UPDATE_QUITGAME: {
										MessageConstants.broadcastMessage(connections, new Message( 
												new MessagePair(MessageConstants.PARAM_NAME,MessageConstants.SERVER_UPDATE_PLAYERQUIT),
												new MessagePair(MessageConstants.PARAM_PLAYER_ID,""+i)));
										onQuitGame(i);
										return;
									} break;
								}
							}
						}
					}
				} break;
				
				
				
				
			}
		}
		for(MessageLine l: connections) {
			try {
				if(l.isReady()) {
					Message m = l.receiveMessage();
					
					switch(m.getValue(MessageConstants.PARAM_NAME)) {
						case 
					}
					
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private void onQuitGame(int i) {
		
	}

	private enum ServerRoomState {
		GETTING_NAMES, BIDDING,
	}
}
