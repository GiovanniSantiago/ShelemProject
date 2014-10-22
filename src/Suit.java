
public enum Suit {
	CLUBS(0),
	SPADES(1),
	HEARTS(2),
	DIAMONDS(3);
	
	int position;
	
	private Suit(int position){
		this.position = position;
	}
	
	public int getPosition(){
		return this.position;
	}
	
	
	
}
