import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JLabel;
import javax.swing.JOptionPane;


public class CardsMouseListener implements MouseListener{
	static boolean bidProtocol 	= true;
	static boolean isMyTurn		= false;
	final int			MAX_CARDS_AMNT			= 16;							//Maximun of card that a player can have
	final int			CARD_OVERLAP_X_FACTOR 	= 3;							//This is used to declare what factor of the card is going to be show and the next card drawn 
	final int			CARD_OVERLAP_Y_FACTOR 	= 7;
	final int 			CARD_WIDTH 				= 73;
	final int 			CARD_HEGHT 				= 100;
	
	CardDeck 	cards2Discard = new CardDeck();
	
	
	@Override
	public void mouseClicked(MouseEvent e) {
		JLabel label = (JLabel) e.getSource();	
		if(isMyTurn){
			if(bidProtocol){
				
				
				if(label.getY() <= 540){
					label.setLocation(label.getX(), 566 );
					cards2Discard.addCard(new Card(label.getText()));
				}else if(label.getY() <= 570){
					label.setLocation(label.getX(), 550 - CARD_HEGHT/CARD_OVERLAP_Y_FACTOR);
					cards2Discard.removeCard(new Card(label.getText()));
				}
				
			}else{
				Test.mp.board.setPlayedCard(ImageRegistry.getImage(label.getText()));
			}
		}
		
		
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

}
