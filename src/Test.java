import java.awt.BorderLayout;
import java.awt.Insets;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class Test {

	public static void main(String[] args) throws IOException {
		
		Path fullPath = FileSystems.getDefault().getPath("res\\", "classic-playing-cards.png");
		BufferedImage b = ImageIO.read(new File(fullPath.toString()));
		ImageRegistry.setImageGrid(b, Utilities.getEnumNames(Suit.values()), Utilities.getEnumNames(Rank.values()), 4, 13);
		ImageRegistry.loadImage("cardBack.jpg");  				//load the back of the card to the HashMap of images
		ImageRegistry.loadImage("boardBackground.png");
		MainFrame mf = new MainFrame();
		
		String[] names = {"Galarfbcfbvwen", "Hola", "Rafael", "perro"};
		mf.board.setUserNames(names);
		
		int[] cards = {16, 16, 16, 16};
		mf.board.setPlayerCardsAmount(cards);
		
	/*	JFrame f = new JFrame("Test");
		
		JPanel p = new JPanel();
		JButton[] labels = new JButton[3];
		p.setLayout(null);
		
		int x = 0;
		int y = 0;
		for(JButton h : labels){
			h = new JButton("hdsgdeghdrthrdthj");
			h.setBounds(x, y, 50, 50);
			x += 25;
			p.add(h);
		}
		
		f.add(p);
		f.setVisible(true);
		f.setSize(700, 700);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);*/
		
		
		/*Path fullPath = FileSystems.getDefault().getPath("res\\", "classic-playing-cards.png");
		BufferedImage b = ImageIO.read(new File(fullPath.toString()));
		ImageRegistry.setImageGrid(b, Utilities.getEnumNames(Suit.values()), Utilities.getEnumNames(Rank.values()), 4, 13);
		ImageRegistry.loadImage("cardBack.jpg");  				//load the back of the card to the HashMap of images
		ImageRegistry.loadImage("boardBackground.png");
		Card[] cards = { new Card(Suit.CLUBS, Rank.NINE), new Card(Suit.HEARTS, Rank.NINE), new Card(Suit.DIAMONDS, Rank.NINE), 
				new Card(Suit.SPADES, Rank.A), new Card(Suit.CLUBS, Rank.TENTH), new Card(Suit.SPADES, Rank.K), };
				
		CardDeck deck = new CardDeck(cards);
		deck.sortDeck();
		System.out.println(ImageRegistry.getImage(Rank.FIVE + "_" + Suit.CLUBS).getWidth());
		MainFrame mf = new MainFrame();
		
		MainFrame.board.setCurrentPlayerDeck(deck);*/
		
		/*BoardComponent bc = new BoardComponent();
		bc.setCurrentPlayerDeck(deck);
		
		JFrame f = new JFrame();
		
		DeckComponent dc = new DeckComponent();
		dc.setCardDeck(deck);
		DeckComponent dc2 = new DeckComponent();
		dc2.setAmntOfCards(5);
		f.setLayout(new BorderLayout());
		f.add(bc, BorderLayout.CENTER);
		f.setVisible(true);
		f.setSize(700, 300);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		*/
		
		
		
	}

}
