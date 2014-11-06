import java.util.HashMap;

/**
 * Represents a complete message, sendable through a socket with the use of MessageLines. Encodes itself as a String, which is then sent, and then decoded into another Message object, identical to the first.
 * 
 * @author Giovanni Santiago
 *
 */
public class Message {
	String source;
	HashMap<String, String> map;

	/**
	 * Creates a Message object form an encoded String. String MUST be a sequence of string pairs, each followed by a <b>$</b> sign.
	 * <p>Each string pair contains <i>exactly one</i> colon character <b>:</b>, and <i>at least one</i> non-colon character before and after the colon.</p>
	 * <p>I.E. each message is a list of pairs terminated by dollar signs, and each pair consists of two <b>non-empty</b> string separated by a colon character</p>
	 * <p>Examples:</p>
	 * <p>name:value$other_data:datastuff$ is valid</p>
	 * @param source
	 */
	public Message(String source) {
		this.source = source;
		map = generateMap(source);
		
	}
	
	/**
	 * Creates a Message from a list of MessagePairs. No special validation done in this method as sources are already validated in MessagePair constructors.
	 * @param pairs
	 */
	public Message(MessagePair... pairs) {
		source = "";
		HashMap<String, String> result = new HashMap<String, String>();
		for(MessagePair p : pairs) {
			result.put(p.getKey(),p.getValue());
			source += p.getKey()+":"+p.getValue()+"$";
		}
		map = result;
		
	}
	//					KEY:VALUE$KEY:VALUE$
	
	/**
	 * Generates a HashMap<String,String> after validating the source String. 
	 * @param source
	 * @return
	 */
	private HashMap<String, String> generateMap(String source) {
		if(source.matches("([^:$]+:[^:$]+\\$)+")) {
			HashMap<String, String> result = new HashMap<String, String>();
			String[] pieces = source.split("\\$");
			for(String s : pieces) {
				String[] pair = s.split(":");
				result.put(pair[0],pair[1]);
			}
			return result;
		} else {
			throw new IllegalArgumentException("Encoded message: ["+source + "] is not a valid message string.");
		}
		
		
	}
	
	/**
	 * Accesses the value mapped to the provided key.
	 * @param key
	 * @return
	 */
	public String getValue(String key) {
		if(map.containsKey(key)) {
			return map.get(key);
		} else {
			throw new IllegalArgumentException("Message does not contain a pair with key: ["+key+"]");
		}
	}
	
	/**
	 * Accesses the value mapped to the provided key as an integer
	 * @param key
	 * @return
	 */
	public int getInteger(String key) {
		return Integer.parseInt(getValue(key));
	}
	
	public boolean containsKey(String key) {
		return map.containsKey(key);
	}

	/**
	 * Returns the entire encoded string for this message. Passing this String to Message(String) will create a Message object identical to this one, in terms of key:value pairs.
	 * @return
	 */
	public String getCompleteString() {
		return source;
	}
	
	/**
	 * Creates a Message from a list of pairs in string form
	 * 
	 * "KEY:VALUE","KEY:VALUE","KEY:VALUE"
	 * @param pairs
	 * @return
	 */
	public static Message fromPairs(String...pairs) {
		MessagePair[] mPairs = new MessagePair[pairs.length];
		for(int i = 0; i < pairs.length; i++) {
			mPairs[i] = new MessagePair(pairs[i]);
		}
		return new Message(mPairs);
	}
	
	/**
	 * Gets the value mapped to the "name" key.
	 * @return
	 */
	public String getName() {
		return getValue("name");
	}

	
	public String toString() {
		String result="";
		for(String s : map.keySet()) {
			result+="[ "+s+" | "+map.get(s)+" ] ";
		}
		return result;
	}
	
	
	
	public enum Names {
		PLAYER_JOINED,
		/**
		 * Server notification when all 4 players have joined the table.
		 * PLAYER_ID:<this player id>
		 */
		TABLE_FULL, 
		/**
		 * Server notification when all 4 players have provided names.
		 */
		GOT_ALL_NAMES, 
		/**
		 * Server request for this client's current bid.
		 */
		REQUEST_BID, 
		/**
		 * Server notification when the bidding is successful
		 */
		BIDDING_COMPLETE, 
		/**
		 * Server notification when everybody passed
		 */
		BIDDING_FAIL, 
		/**
		 * Server request for confirmation for starting a new game in the gamelobby.
		 */
		ARE_YOU_READY, 
		/**
		 * Client response to ARE_YOU_READY. Sent when player is ready to start new game
		 */
		I_AM_READY, 
		/**
		 * Server notification for when the controlling player starts next round
		 */
		GAME_READY, 
		/**
		 * Client response to REQUEST_BID. Sent when player has chosen a new bid.
		 * BID_AMOUNT is set to 0 if pass.
		 */
		MY_BID, 
		MY_NAME, 
		PLAYER_NAME, 
		YOUR_ID,
		
		GAME_SUIT,
		GIVE_CARD,
		CARD_PLAYED,
		HAND_WON,
		/**
		 * When a round is over, but a new one will happen
		 */
		GAME_OVER,
		/**
		 * When a round is over, but the game set is done.
		 */
		SET_OVER,
		
		MY_CARD,
		
		SOMEONE_BID,
		SOMEONE_PASSED,
		
		I_QUIT,
		PLAYER_QUIT,
		
		YOUR_WIDOW,
		MY_TRASH,
		
		
	}
	
	public enum Keys {
		PLAYER_ID, PLAYER_NAME, CURRENT_BID, BID_WINNER, BID_AMOUNT, CARDS, GAME_SETTINGS, SUIT
	}
}
