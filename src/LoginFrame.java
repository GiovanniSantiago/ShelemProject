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
	int			frameWidth		= 420;
	int			frameHeght		= 442;
	Dimension	frmDimen 		= new Dimension(frameWidth, frameHeght);
	
	
	JButton 	startBtn 		= new JButton("Connect to Server");
	JButton 	exitBtn 		= new JButton("Exit");
	JButton		send			= new JButton("Send");
	JLabel  	srvConLbl		= new JLabel("Connected to Server. Your are player ");
	JLabel		userNamLbl		= new JLabel("Username: ");
	JLabel		statusLbl		= new JLabel("", JLabel.RIGHT);
	JTextField  usrNameTxtFld	= new JTextField();
	
	//===============
	//CONSTRUCTORS
	//==============
	public LoginFrame(){
		
		LoginMainPanel logPnl = new LoginMainPanel();
		
		
		
																
		this.add(logPnl);
		this.setTitle("Shelem Login Lobby");
		this.setSize(frmDimen);
		//this.setResizable(false);
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
			
			send.setVisible(false);
			send.setSize(80, 20);
			send.setFocusable(false);
			send.addActionListener(Test.ch.loginListener);
			send.setFont(new Font("Baskerville Old Face", Font.PLAIN, 14));
			send.setForeground(Color.WHITE);
			send.setBackground(Color.BLACK);
			send.setBounds(frameWidth/2 - send.getWidth()/2, 200, send.getWidth(), send.getHeight());
			
			usrNameTxtFld.setVisible(false);
			usrNameTxtFld.setSize(100, 20);
			usrNameTxtFld.setBounds(frameWidth/2, frameHeght / 2 - 70, usrNameTxtFld.getWidth(), usrNameTxtFld.getHeight());
			
			this.add(send);
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
			userNamLbl.setSize(95, 20);
			userNamLbl.setBounds(frameWidth/2 -  userNamLbl.getWidth(), frameHeght / 2 - 68, userNamLbl.getWidth(), userNamLbl.getHeight());
			userNamLbl.setFont(new Font("Baskerville Old Face", Font.PLAIN, 17));
			
			statusLbl.setVisible(false);
			statusLbl.setForeground(Color.WHITE);
			statusLbl.setBounds(10, (int)frmDimen.getHeight() - 70, (int)frmDimen.getWidth() -45, 20);
			
			
			this.add(statusLbl);
			this.add(srvConLbl);
			this.add(userNamLbl);
		}
	}
	//===========================================================================================================================
	//===========================================================================================================================
	
}
