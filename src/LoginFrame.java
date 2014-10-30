import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Class for show a Frame where the user log in and wait for anothers players
 * @author Glorimar Castro
 *
 */
public class LoginFrame extends JFrame{
	
	
	//===============
	//FIELD
	//===============
	static JButton startBtn 	= new JButton("Connect to Server");
	static JButton exitBtn 		= new JButton("Exit");
	
	//===============
	//CONSTRUCTORS
	//==============
	public LoginFrame(){
		
		LoginMainPanel logPnl = new LoginMainPanel();
		
		

		
		this.add(logPnl);
		this.setTitle("Shelem Login Lobby");
		this.setSize(new Dimension(420, 442));
		this.setResizable(false);
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
	}
	
	//===============
	//METHODS
	//===============
	/**
	 * -----------------------------------------------------------------------------
	 */

	
	
	
	//===========================================================================================================================
	//===========================================================================================================================
	//===========================================================================================================================
	//===========================================================================================================================
	//===========================================================================================================================
	//===========================================================================================================================
	//===========================================================================================================================
	class LoginMainPanel extends JPanel{
		//Fields
		Font	font		= new Font("Baskerville Old Face", Font.BOLD, 30);
		JLabel 	welcomeLbl 	= new JLabel("Welcome to Shelem Game");
		
		
		//Constructor
		LoginMainPanel(){
			ImageRegistry.loadImage("loginBackground.jpg");
			
			
			btnSetUps();
			
			this.add(startBtn);
			this.add(exitBtn);
			
			//Labels set up
			welcomeLbl.setForeground(Color.WHITE);
			welcomeLbl.setFont(font);
			welcomeLbl.setBounds(new Rectangle(20, 30, 500, 50));
			
			this.setLayout(null);
			this.setVisible(true);
			this.setBackground(Color.BLACK);
			
			
		}
		
		
		//Metodos
		@Override
		public void paintComponent(Graphics g){
			super.paintComponent(g);
			
			Graphics2D g2 = (Graphics2D)g;
			g2.drawImage(ImageRegistry.getImage("loginBackground.jpg"), null, 0, 0);
			
			this.add(welcomeLbl);
		}
		
		/**
		 * 
		 */
		private void btnSetUps(){
			
			startBtn.addActionListener(Test.ch.loginListener);
			startBtn.setBounds(new Rectangle(100, 150, 200, 25));
			startBtn.setFont(new Font("Baskerville Old Face", Font.BOLD, 14));
			startBtn.setForeground(Color.WHITE);
			startBtn.setBackground(Color.BLACK);
			startBtn.setFocusable(false);
			
			exitBtn.addActionListener(Test.ch.loginListener);
			exitBtn.setBounds(new Rectangle(100, 250, 200, 25));
			exitBtn.setFont(new Font("Baskerville Old Face", Font.BOLD, 14));
			exitBtn.setForeground(Color.WHITE);
			exitBtn.setBackground(Color.BLACK);
			exitBtn.setFocusable(false);
			
		}
	}
	//===========================================================================================================================
	//===========================================================================================================================
	
}
