import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;


public class Test {

	public static void main(String[] args) throws IOException {
		
		
		Card[] cards = { new Card(Suit.CLUBS, Rank.NINE), new Card(Suit.HEARTS, Rank.NINE), new Card(Suit.DIAMONDS, Rank.NINE), 
				new Card(Suit.SPADES, Rank.A), new Card(Suit.CLUBS, Rank.TENTH), new Card(Suit.SPADES, Rank.K), };
				
		CardDeck desk = new CardDeck(cards);
		desk.sortDeck();
		desk.printDeck();
		
		
		JFrame f = new JFrame("imh");
		JLabel lb = new JLabel();
		
		ImageRegistry.setImageGrid(ImageRegistry.loadImage("classic-playing-cards.png"),  Utilities.getEnumNames(Suit.values()), Utilities.getEnumNames(Rank.values()), 4, 13);
		BufferedImage img = ImageRegistry.getImage(Rank.EIGHT + "_" + Suit.HEARTS.toString());
		System.out.println(img.getHeight());
		
	
		
		
		lb.setIcon(new ImageIcon(img));
		f.add(lb);
		f.setSize(850, 700);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setVisible(true);
		
	}

}
