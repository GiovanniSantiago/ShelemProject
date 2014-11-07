/**
 * 
 * @author Glorimar Castro
 *
 */
public enum Rank {
	
	A(10, 13),
	TWO(0, 1),
	THREE(0, 2),
	FOUR(0, 3),
	FIVE(5, 4),
	SIX(0, 5),
	SEVEN(0, 6),
	EIGHT(0, 7),
	NINE(0, 8),
	TENTH(10, 9),
	J(0, 10),
	Q(0, 11),
	K(0, 12);
	
	
	private int value;
	private int trumpValue;
	

	/**
	 * 
	 * @param value
	 * 		The value is the points that this card give to a player
	 * @param position
	 * 		This is for sorting 
	 */
	private Rank(int value, int trumpValue){
		this.value = value;
		this.trumpValue = trumpValue;
	}
	
	public int getValue(){
		return this.value;
	}
	
	public int getTrumpValue() {
		return this.trumpValue;
	}
	
}
