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
import javax.swing.JTextField;

/**
 * Class for show a Frame where the user log in and wait for anothers players
 * @author Glorimar Castro
 *
 */
public class LoginFrame extends JFrame{
	
	
	//===============
	//FIELD
	//===============
	Dimension	frmDimen 		= new Dimension(420, 442);
	
	JButton 	startBtn 		= new JButton("Connect to Server");
	JButton 	exitBtn 		= new JButton("Exit");
	JLabel  	srvConLbl		= new JLabel("Connected to Server. Your are player ");
	JLabel		userNamLbl		= new JLabel("Username: ");
	JTextField  usrNameTxtFld	= new JTextField();
	
	//===============
	//CONSTRUCTORS
	//==============
	public LoginFrame(){
		
		LoginMainPanel logPnl = new LoginMainPanel();
		
		

		
		this.add(logPnl);
		this.setTitle("Shelem Login Lobby");
		this.setSize(frmDimen);
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
			labelsSetups();
			
			
			
			
			//Labels set up
			welcomeLbl.setForeground(Color.WHITE);
			welcomeLbl.setFont(font);
			welcomeLbl.setBounds(new Rectangle(20, 30, 500, 50));
			
			this.setLayout(null);
			this.setVisible(true);
			this.setBackground(Color.BLACK);
			this.setSize(frmDimen);
			
			
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
			
			usrNameTxtFld.setVisible(false);
			usrNameTxtFld.setSize(100, 20);
			usrNameTxtFld.setBounds(187, 125, 100, 20);
			
			this.add(usrNameTxtFld);
			this.add(startBtn);
			this.add(exitBtn);
		}
		
		private void labelsSetups(){
			srvConLbl.setVisible(false);
			srvConLbl.setBounds(70, welcomeLbl.getY() + welcomeLbl.getHeight() + srvConLbl.getHeight() + 75 , 400, 20);
			srvConLbl.setForeground(Color.WHITE);
			srvConLbl.setFont(new Font("Baskerville Old Face", Font.PLAIN, 17));
			
			userNamLbl.setVisible(false);
			userNamLbl.setForeground(Color.WHITE);
			userNamLbl.setBounds(100, 120, 100, 40);
			userNamLbl.setFont(new Font("Baskerville Old Face", Font.PLAIN, 17));
			
			this.add(srvConLbl);
			this.add(userNamLbl);
		}
	}
	//===========================================================================================================================
	//===========================================================================================================================
	
}
