public enum PlayerPosition {
	
	NORTH, SOUTH, EAST, WEST;
	
	/**
	 * Finds the next position from this one, moving clockwise
	 * @return next position, clockwise
	 */
	public PlayerPosition getNext() {
		switch (this) {
			case WEST: {
				return NORTH;
			}
			case EAST: {
				return SOUTH;
			}
			case SOUTH: {
				return WEST;
			}
			case NORTH: {
				return EAST;
			}
			default: {
				throw new IllegalArgumentException("Somehow a nonexistant enum, or null");
			}
		}
	}
}
