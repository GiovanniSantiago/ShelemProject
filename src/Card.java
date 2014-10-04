import java.awt.image.BufferedImage;
import java.nio.file.FileSystems;


public class Card {
	//---------------------------------------------
	//----------FIELD------------------------------
	//---------------------------------------------
	private Suit 			suit;
	private	Rank 			rank;
	private int				value;
	private BufferedImage 	image;
	
	

	public Card(Suit suit, Rank rank){
		this.suit = suit;
		this.rank = rank;
		this.value = rank.getValue();
		setImg();
	}
	

	//==============================================
	//======METHODS==========METHODS================
	//==============================================
	//---------Public Methods-----------------------
	public Suit getSuit(){
		return this.suit;
	}
	
	public Rank getRank(){
		return this.rank;
	}
	
	public int getValue(){
		return this.value;
	}
	
	public BufferedImage getImg(){
		return this.image;
	}
	
	public String getName(){
		return rank.toString() + "_" + suit.toString();
	}
	
	public void setSuit(Suit suit){
		this.suit = suit;
		setImg();
	}
	
	public void setRank(Rank rank){
		this.rank = rank;
		this.value = rank.getValue();
		setImg();
	}
	
	@Override
	public String toString(){
		return this.rank.toString() + " of " + this.suit.toString();
	}
	
	
	
	
	//----------Private Methods--------------------
	/**
	 * Private method to set the corresponding image to the card. 
	 * This method have dependecy to ImageRegistry Class
	 */
	private void setImg(){
		ImageRegistry.getImage(this.rank.toString() + "_" + this.suit.toString());
	}
}
