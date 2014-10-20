import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.Border;


public class MainFrame extends JFrame{
	//===============
	//FIELD
	//===============
	private final int		WIDTH			= 1200;
	private final int		HEIGHT			= 870;
	private final double	WIDTH_FACTOR	= 20;
	private final double	HEIGHT_FACTOR	= 30;
	private final boolean 	isFrameVisible 	= true;
	private final Dimension	frameDimension 	= new Dimension(WIDTH, HEIGHT);
	private final Dimension	btnDimension	= new Dimension(0, 20);
	
	private GridBagLayout 		gbl;
	private GridBagConstraints 	gbc;
	
	
	public static BoardComponent	board;
	public static ScoreComponent 	scoreBoard;
	public static JButton			leaveButton;
	public static JButton			endGame;
	public static JPanel			pastPlaysInfo;
	public static JTextArea			txtArea;
	public static JScrollPane		scrollBar;
	

	//===============
	//CONSTRUCTORS
	//===============
	
	public MainFrame(){
		//Field initialization
		gbl	= new GridBagLayout();
		gbc	= new GridBagConstraints();
		
		setComponents();
		setTxtFieldPnl(pastPlaysInfo, txtArea, scrollBar);
		
		//MainFrame set up
		this.setTitle("Shelem");
		this.setLayout(gbl);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(isFrameVisible);
		this.setSize(frameDimension);
		//this.setResizable(false);
		this.setLocationRelativeTo(null);
		
		//Add components
		addComponents();
		
	}
	
	
	//===============
	//METHODS
	//===============	
	/**
	 * Initialize all field variables
	 */
	private void setComponents(){
		board 			= new BoardComponent();
		scoreBoard 		= new ScoreComponent();
		leaveButton 	= new JButton("Leave");
		endGame			= new JButton("End Game");
		pastPlaysInfo	= new JPanel();
		txtArea			= new JTextArea();
		scrollBar		= new JScrollPane();
	
	}
	
	/**
	 * Add all the components to the MainPanel
	 */
	private void addComponents(){
		//Board JPanel set up
		gbc.gridx		= 0;
		gbc.gridy		= 0;
		gbc.gridheight	= 3;
		gbc.gridwidth	= 1;
		gbc.weightx		= WIDTH*(20/WIDTH_FACTOR);
		gbc.weighty		= HEIGHT*(10/HEIGHT_FACTOR);
		gbc.fill		= GridBagConstraints.BOTH;
		gbc.anchor		= GridBagConstraints.NORTH;
		
		
		this.add(board, gbc);
		
		//Score JPanel set up
		gbc.gridx		= 0;
		gbc.gridy		= 3;
		gbc.gridheight	= 0;
		gbc.gridwidth	= 3;
		gbc.weightx		= WIDTH*(7/WIDTH_FACTOR);
		gbc.weighty		= HEIGHT*(5/HEIGHT_FACTOR);
		gbc.anchor		= GridBagConstraints.WEST;
		
		this.add(scoreBoard, gbc);
		
		//Leave button set up
		gbc.gridx		= 1;
		gbc.gridy		= 0;
		gbc.gridheight	= 1;
		gbc.gridwidth	= 1;
		gbc.weightx		= WIDTH*(1.0/WIDTH_FACTOR);
		gbc.weighty		= HEIGHT*(1/HEIGHT_FACTOR);
		gbc.fill		= GridBagConstraints.NONE;
		gbc.anchor		= GridBagConstraints.CENTER;
		gbc.insets		= new Insets(0,0,5,0);
		
		this.add(leaveButton, gbc);
		
		//endGame button set up
		gbc.gridx		= 2;
		gbc.gridy		= 0;
		gbc.gridheight	= 1;
		gbc.gridwidth	= 1;
		gbc.weightx		= WIDTH*(1.0/WIDTH_FACTOR);
		gbc.weighty		= HEIGHT*(1/HEIGHT_FACTOR);
		gbc.fill		= GridBagConstraints.NONE;
		gbc.anchor		= GridBagConstraints.CENTER;
		gbc.insets		= new Insets(0,0,10,0);
		
		this.add(endGame, gbc);
		
		//pastPlaysInfo set up
		gbc.gridx		= 1;
		gbc.gridy		= 2;
		gbc.gridheight	= 1;
		gbc.gridwidth	= 2;
		gbc.weightx		= WIDTH*(2.0/WIDTH_FACTOR);
		gbc.weighty		= HEIGHT*(25/HEIGHT_FACTOR);
		gbc.fill		= GridBagConstraints.BOTH;
		gbc.anchor		= GridBagConstraints.NORTH;
		
		this.add(pastPlaysInfo, gbc);
		
	}
	
	/**
	 * This method set a Jpanel to conain a txt area with a scroll bar
	 * @param txtFieldPnl
	 * @param txtArea
	 * @param scroll
	 */
	private void setTxtFieldPnl(JPanel txtFieldPnl, JTextArea txtArea, JScrollPane scroll){
		Border infoBorder = BorderFactory.createTitledBorder("Past Plays");
		
		
		//scroll = new JScrollPane (txtField, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		scroll.setVisible(true);
		scroll.add(txtArea);
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		txtArea.setEditable(false);
		
		txtFieldPnl.setVisible(true);
		txtFieldPnl.setLayout(new BorderLayout());
		txtFieldPnl.setBorder(infoBorder);
		
		txtFieldPnl.add(scroll, BorderLayout.CENTER);
		
		
		
	}
	
}
