import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JComponent;
import javax.swing.JPanel;


public class DeckComponent extends JPanel{
	//====================
	//FIELD
	//====================
	CardDeck deck;
	
	//====================
	//CONSTRUCTORS
	//====================
	public DeckComponent(CardDeck deck){
		this.deck = deck;
		this.setVisible(true);
		this.setBackground(Color.BLACK);
	
		
	}
	
	//====================
	//METHODS
	//====================
	public void paintComponent(Graphics g){
		Graphics2D g2 = (Graphics2D)g;
		
		
	}
}
