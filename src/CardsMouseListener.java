import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JLabel;
import javax.swing.JOptionPane;


public class CardsMouseListener implements MouseListener{
	static boolean bidProtocol = false;
	final int			MAX_CARDS_AMNT			= 16;							//Maximun of card that a player can have
	final int			CARD_OVERLAP_X_FACTOR 	= 3;							//This is used to declare what factor of the card is going to be show and the next card drawn 
	final int			CARD_OVERLAP_Y_FACTOR 	= 7;
	final int 			CARD_WIDTH 				= 73;
	final int 			CARD_HEGHT 				= 100;
	
	
	@Override
	public void mouseClicked(MouseEvent e) {
		JLabel label = (JLabel) e.getSource();
		System.out.println(label.getText());
		
		
		if(bidProtocol){
			
			if(label.getY() ==  (BoardComponent.deckPoint[0].getY() - CARD_HEGHT/CARD_OVERLAP_Y_FACTOR)){
				label.setLocation(label.getX(), (int) (label.getY() + CARD_HEGHT/CARD_OVERLAP_Y_FACTOR));
			}else{
				label.setLocation(label.getX(), (int) (label.getY() - CARD_HEGHT/CARD_OVERLAP_Y_FACTOR));
			}
			
		}else{
			Test.mf.board.setPartnerPlayedCard(ImageRegistry.getImage(label.getText()));
			Test.mf.board.setPlayedCard(ImageRegistry.getImage(label.getText()));
			Test.mf.board.setWestOpntPlayedCard(ImageRegistry.getImage(label.getText()));
			Test.mf.board.setEastOpntPlayedCard(ImageRegistry.getImage(label.getText()));
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
