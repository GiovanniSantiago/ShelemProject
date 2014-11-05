import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class BiddingPanel extends JPanel{
	//-----------------------------------
	//-----------FIELDS------------------
	final int FRAME_WIDTH = 540;
	final int FRAME_HEGTH = 300;
	
	JLabel 			bidingInfo		= new JLabel("<html><p align = \"center\" width = \"" + FRAME_WIDTH + "\">For this game you have to Bid the ammount of points that you think you are going to collect at the end of the game. "
											+ "The bid is a minnimun of 100 and  maximmun of 165, and it's in multiplus of 5. Enter your Bid when your time:</p></html>");
	JLabel			bidStatuslbl	= new JLabel("Waiting for the first bid...", JLabel.RIGHT);
	JLabel			minBidLbl		= new JLabel("100<=");
	JLabel			maxBidLbl		= new JLabel("<=165");
	JLabel			turnLbl			= new JLabel("Client Handeler lo cambia");
	JLabel			playersBids[]	= new JLabel[4];
	
	JButton			sumBtn			= new JButton("+5");
	JButton			senBidBtn		= new JButton("send");
	JButton			passBtn			= new JButton("pass");
	
	JTextField		bidTxt			= new JTextField();
	BiddingPanel 	bp;
	
		String[] userNames = new String[4];
		
		//Constructors
		BiddingPanel(String[] userNames){
			//ImageRegistry.loadImage("back.jpg");
			for(int i = 0; i < userNames.length; i++){
				this.userNames[i] = userNames[i];
			}
			setComponents();
			//this.setBackground(new Color(0,0,0,0));
			this.setSize(FRAME_WIDTH + 15, FRAME_HEGTH + 10);
			this.setLayout(null);
		}
		
		
		//Methods
		@Override
		public void paintComponent(Graphics g){
			super.paintComponents(g);
			
			Graphics2D g2 = (Graphics2D) g;
			
			//g2.drawImage(ImageRegistry.getImage("back.jpg"), null, 0, 0);
			
		}
		
		
		
		private void setComponents(){
			bidingInfo.setFont(new Font("Times New Roman", Font.PLAIN, 20));
			bidingInfo.setSize(FRAME_WIDTH , 120);
			bidingInfo.setForeground(new Color(117,117,117));
			bidingInfo.setBounds(3, 3, bidingInfo.getWidth(), bidingInfo.getHeight());
			
			for(int i = 0; i < playersBids.length; i++){
				playersBids[i] = new JLabel(userNames[i] + " bid: 0", JLabel.CENTER);
				playersBids[i].setSize(120, 20);
				playersBids[i].setForeground(new Color(118, 0, 0));
				playersBids[i].setFont(new Font("Times New Roman", Font.PLAIN, 18));
			}
			
			playersBids[2].setBounds(FRAME_WIDTH/2 - playersBids[2].getWidth()/2, bidingInfo.getHeight(), playersBids[0].getWidth(), playersBids[0].getHeight());
			playersBids[0].setBounds(FRAME_WIDTH/2 - playersBids[0].getWidth()/2, playersBids[2].getY() + 50, playersBids[0].getWidth(), playersBids[0].getHeight());
			playersBids[3].setBounds(20, playersBids[2].getY()   + (playersBids[0].getY() - playersBids[2].getY())/2, playersBids[0].getWidth(), playersBids[0].getHeight());
			playersBids[1].setBounds(FRAME_WIDTH -150, playersBids[3].getY(), playersBids[0].getWidth(), playersBids[0].getHeight());
			
			
			bidTxt.setSize(100, 25);
			bidTxt.setText("100");
			bidTxt.setBounds(FRAME_WIDTH/2 - bidTxt.getWidth()/2, playersBids[0].getY() + playersBids[0].getHeight() + 15, bidTxt.getWidth(), bidTxt.getHeight());
			bidTxt.setEditable(false);
			
			minBidLbl.setSize(50, 13);
			minBidLbl.setForeground(new Color(118, 0, 0));
			minBidLbl.setFont(new Font("Times New Roman", Font.BOLD, 14));
			minBidLbl.setBounds(bidTxt.getX() - minBidLbl.getWidth(), bidTxt.getY() + minBidLbl.getHeight()/2 + 2, minBidLbl.getWidth(), minBidLbl.getHeight());
			
			maxBidLbl.setSize(50, 10);
			maxBidLbl.setForeground(new Color(118, 0, 0));
			maxBidLbl.setFont(new Font("Times New Roman", Font.PLAIN, 14));
			maxBidLbl.setBounds(bidTxt.getX() + bidTxt.getWidth() + 10, minBidLbl.getY(), maxBidLbl.getWidth(), maxBidLbl.getHeight());
			
			
			senBidBtn.setSize(75, 20);
			senBidBtn.addActionListener(Test.ch.bidListener);
			senBidBtn.setEnabled(false);
			senBidBtn.setForeground(Color.WHITE);
			senBidBtn.setBackground(Color.DARK_GRAY);
			senBidBtn.setBounds(FRAME_WIDTH/2 - senBidBtn.getWidth()/2- 5, bidTxt.getY() + bidTxt.getHeight() + 10, senBidBtn.getWidth(), senBidBtn.getHeight());
			
			sumBtn.setSize(50, 20);
			sumBtn.addActionListener(Test.ch.bidListener);
			sumBtn.setEnabled(false);
			sumBtn.setForeground(Color.WHITE);
			sumBtn.setBackground(Color.DARK_GRAY);
			sumBtn.setBounds(senBidBtn.getX() - sumBtn.getWidth() - 5, bidTxt.getY() + bidTxt.getHeight() + 10, sumBtn.getWidth(), sumBtn.getHeight());
			
			passBtn.setSize(75, 20);
			passBtn.addActionListener(Test.ch.bidListener);
			passBtn.setEnabled(false);
			passBtn.setForeground(Color.WHITE);
			passBtn.setBackground(Color.DARK_GRAY);
			passBtn.setBounds(senBidBtn.getX() + senBidBtn.getWidth() + 5, senBidBtn.getY(), passBtn.getWidth(), passBtn.getHeight());
			//turnLbl.setBounds(x, y, width, height)
			
			bidStatuslbl.setSize(FRAME_WIDTH - 15, 40);
			bidStatuslbl.setFont(new Font("Times New Roman", Font.ITALIC, 13));
			bidStatuslbl.setForeground(Color.WHITE);
			bidStatuslbl.setBounds(5, FRAME_HEGTH - bidStatuslbl.getHeight() + 7, bidStatuslbl.getWidth(), bidStatuslbl.getHeight());
			
			for(JLabel jl : playersBids){
				this.add(jl);
			}
			
			this.add(passBtn);
			this.add(senBidBtn);
			this.add(sumBtn);
			this.add(bidTxt);
			this.add(bidStatuslbl);
			this.add(maxBidLbl);
			
			this.add(minBidLbl);
			this.add(bidingInfo);
			
		}
		
		public void eneableButtons(){
			this.passBtn.setEnabled(true);
			this.senBidBtn.setEnabled(true);
			this.sumBtn.setEnabled(true);
		}
		
		/**
		 * ------------------------------------------------------------------------------------------
		 * 									SET PLAYER BID
		 * ------------------------------------------------------------------------------------------
		 *      Este metodo se encarga de hacer un update al label de un jugador e indicar cuanto aposto.
		 * Si el jugador paso entonces se recibe como ammount -1 y esto se encarga de poner en el UI que dicho usuario
		 * paso.En el caso de que un jugador haya pasado no se hace update al bid text.
		 * @param playerPos
		 * @param ammount
		 */
		public void setPlayerBid(int playerPos, int ammount){
			if(ammount == -1){
				playersBids[playerPos].setText(userNames[playerPos] + " pass");
			}else{
				playersBids[playerPos].setText(userNames[playerPos] + " bid: " + ammount);
				bidTxt.setText("" + ammount);
			}
			
			repaint();
		}
		
		
		
		
		
		
	
}
