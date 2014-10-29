
public class MessagePair {
	String key;
	String value;
	
	/**
	 * Creates a MessagePair with the specified key and value. Used for validating simple messages
	 * <p>Note:
	 * 	NEITHER key NOR value can contain either a colon character or a dollar sign '$'<br>
	 * 	The validating regex is ^[^:$]+$
	 * <p>
	 * 	For example:
	 * 		"Unicorns", "Player 1 score", "GET NAME", and "2,6,43,1" are all valid message values
	 * 		"Player:6", "30$", and "3:40 pm" are not.</p></p>
	 * @param key
	 * @param value
	 */
	public MessagePair(String key, String value) {
		if(key.matches("^[^:$]+$")) {
			if(value.matches("^[^:$]+$")) {
				this.key = key;
				this.value = value;
			} else {
				throw new IllegalArgumentException("Value: [ "+value+" ] is not a valid message string");
			}
		} else {
			throw new IllegalArgumentException("Key: [ "+key+" ] is not a valid message string");
		}
	}
	
	/**
	 * <p>
	 * Creates a MessagePair from the specified encoded string. Used for validating simple messages.
	 * </p>
	 * Same rules as with MessagePair(String,String) apply. Messages must be separated by the colon character, ':'. This character must appear exactly ONCE.
	 * <p>	
	 * Example:  <br>
	 * 		Unicorn4:67,
	 * 		playerName:Guaca,
	 * 		TURN_COUNT:7
	 * 			are all valid message pairs</p><p>
	 * 		PlayerScore:2:6
	 * 		Names:
	 * 			are not
	 * </p>
	 * The output of MessagePair.encode() will ALWAYS be valid.
	 * @param source
	 */
	public MessagePair(String source) {
		if(source.matches("^[^:$]+:[^:$]+$")) {
			String[] s = source.split(":");
			key = s[0];
			value = s[1];
		} else {
			throw new IllegalArgumentException("Source message: ["+source+"] is not a valid encoded message pair");
		}
	}
	
	/**
	 * returns the encoded string representation of this messagePair. Will contain <i>exactly</i> one colon "<b>:</b>" character, and <i>zero</i> dollar signs "<b>$</b>"
	 * <p>Providing this string to MessagePair(String) will correctly recreate this object.</p>
	 * @return
	 */
	public String encode() {
		return key+":"+value;
	}
	
	public String getValue() {
		return value;
	}
	
	public String getKey() {
		return key;
	}
}
