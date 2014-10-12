public class Message {
	String source;
	Integer[] params;

	public Message(String source) {
		this.source = source;
		params = generateParameters(source);
	}

	/**
	 * Creates a message with the provided name and values. For a name-only
	 * message, leave values as null
	 * 
	 * @param name
	 *            The name of the message
	 * @param values
	 *            The parameters of the message. null for no parameters
	 */
	public Message(String name, Integer... values) {
		if (values == null || values.length == 0) {
			this.source = name;
			params = new Integer[0];
			return;
		}
		params = new Integer[values.length];
		System.arraycopy(values, 0, params, 0, values.length);
		this.source = generateSource(name, params);
	}

	private Integer[] generateParameters(String source) {
		if(source.contains(":")) {
			String[] contents = source.substring(source.indexOf(":") + 1)
					.split(",");
			Integer[] numbers = new Integer[contents.length];

			for (int i = 0; i < contents.length; i++) {
				numbers[i] = Integer.valueOf(contents[i]);
			}
			return numbers;
		}
		
		return new Integer[0];
	}

	private String generateSource(String name, Integer[] params) {
		if(params.length == 0) {
			return name;
		}
		String result = name + ":";
		for (int i = 0; i < params.length - 1; i++) {
			result += params[i] + ",";
		}
		result += params[params.length - 1];
		return result;
	}

	public String getName() {
		if(source.contains(":")) {
			return source.substring(0, source.indexOf(":"));
		} else {
			return source;
		}
		
	}

	/**
	 * Gets the index-th parameter of the message. Assumes index is within
	 * range.
	 * 
	 * @param index
	 * @return
	 */
	public int getParameter(int index) {
		return params[index];
	}

	public int getParameterCount() {
		return params.length;
	}

	public String getCompleteString() {
		return source;
	}

	public String toString() {
		return source;
	}
}
