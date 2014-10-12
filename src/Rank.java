/**
 * 
 * @author Glorimar Castro
 *
 */
public enum Rank {
	
	A(10, 1),
	TWO(0, 2),
	THREE(0, 3),
	FOUR(0, 4),
	FIVE(5, 5),
	SIX(0, 6),
	SEVEN(0, 7),
	EIGHT(0, 8),
	NINE(0, 9),
	TENTH(10, 10),
	J(0, 11),
	Q(0, 12),
	K(0, 13);
	
	
	private int value;
	private int position;
	

	/**
	 * 
	 * @param value
	 * 		The value is the points that this card give to a player
	 * @param position
	 * 		This is for sorting 
	 */
	private Rank(int value, int position){
		this.value = value;
		this.position = position;
	}
	
	public int getValue(){
		return this.value;
	}
	
	public int getPosition(){
		return this.position;
	}
	
	
}
