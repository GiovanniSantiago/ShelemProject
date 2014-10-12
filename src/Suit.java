
public enum Suit {
	SPADES(0),
	CLUBS(1),
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
