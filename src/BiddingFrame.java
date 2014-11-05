import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class BiddingFrame extends JFrame{
	//-----------------------------------
	//-----------FIELDS------------------
	final int FRAME_WIDTH = 460;
	final int FRAME_HEGTH = 200;
	
	JLabel 			bidingInfo		= new JLabel("<html>For this game you have to Bid the ammount of point that you think are going to collect at the end of the game. "
											+ "The bid is a minnimun of 100 and  maximmun of 165, and in multiplus of 5. Enter your Bid when your time:</html>");
	JLabel			bidStatuslbl	= new JLabel("Waiting for the first bid...");
	JLabel			minBidLbl		= new JLabel("100<=");
	JLabel			maxBidLbl		= new JLabel("=>165");
	JLabel			turnLbl			= new JLabel("Client Handeler lo cambia");
	
	JTextField		bidTxt			= new JTextField();
	BiddingPanel 	bp 				= new BiddingPanel();
	
	
	//-----------------------------------
	//---------CONSTRUCTOR---------------
	public BiddingFrame() {
		
		
		//Frame setup
		this.add(bp);
		this.setVisible(true);
		this.setSize(FRAME_WIDTH, FRAME_HEGTH);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("Shelem Biding Phase");
		
	}
	
	//-----------------------------------
	//-----------METHODS-----------------
	
	

	
	
	
	//=================================================================================================================================================
	//================================================INNER CLASSES=======================================================================INNER CLASSES    
	//=================================================================================================================================================
	class BiddingPanel extends JPanel{
		
		
		//Constructors
		BiddingPanel(){
			setComponents();
			this.setLayout(null);
		}
		
		
		//Methods
		@Override
		public void paintComponents(Graphics g){
			super.paintComponents(g);
			
			Graphics2D g2 = (Graphics2D) g;
		}
		
		private void setComponents(){
			bidingInfo.setFont(new Font("Times New Roman", Font.BOLD, 12));
			bidingInfo.setSize(FRAME_WIDTH + 10, 50);
			bidingInfo.setBounds(3, 3, bidingInfo.getWidth(), bidingInfo.getHeight());
			
			minBidLbl.setSize(50, 10);
			minBidLbl.setBounds(100, 99, minBidLbl.getWidth(), minBidLbl.getHeight());
			
			maxBidLbl.setSize(50, 10);
			maxBidLbl.setBounds(250, 99, maxBidLbl.getWidth(), maxBidLbl.getHeight());
			
			
			bidTxt.setBounds(145, 96, 100, 17);
			bidTxt.setEditable(false);
			
			//turnLbl.setBounds(x, y, width, height)
			
			bidStatuslbl.setSize(FRAME_WIDTH, 40);
			bidStatuslbl.setBounds(5, FRAME_HEGTH - bidStatuslbl.getHeight() - 30, bidStatuslbl.getWidth(), bidStatuslbl.getHeight());
			
			this.add(bidTxt);
			this.add(bidStatuslbl);
			this.add(maxBidLbl);
			this.add(minBidLbl);
			this.add(bidingInfo);
			
		}
		
	}
}
