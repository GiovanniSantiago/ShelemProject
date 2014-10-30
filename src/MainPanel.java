import java.awt.BorderLayout;
import java.awt.Color;
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


public class MainPanel extends JPanel{
	//===============
	//FIELD
	//===============
	public static final int		WIDTH			= 1220;										//width of the frame
	public static final int		HEIGHT			= 873;										//height of the frame
	private final double	WIDTH_FACTOR	= 20;										//WIDTH_FACTOR and HEIGHT_FACTOR are for the gridbagconstraints
	private final double	HEIGHT_FACTOR	= 30;
	private final boolean 	isFrameVisible 	= true;										//Set the frame visibility
	private final Dimension	frameDimension 	= new Dimension(WIDTH, HEIGHT);
	private final Dimension	btnDimension	= new Dimension(0, 20);
	
	private GridBagLayout 		gbl;
	private GridBagConstraints 	gbc;
	
	
	public static MainBoardPanel	board;
	public static ScorePanel 	scoreBoard;
	public static JButton			leaveButton;
	public static JButton			endGame;
	public static JPanel			pastPlaysInfo;
	public static JTextArea			txtArea;
	public static JScrollPane		scrollBar;
	

	//===============
	//CONSTRUCTORS
	//===============
	
	public MainPanel(){
		
		ImageRegistry.loadImage("cardBack.jpg");  				//load the back of the card to the HashMap of images
		ImageRegistry.loadImage("boardBackground.png");
		ImageRegistry.loadImage("scoreBackground.jpg");
		
		//Field initialization
		gbl	= new GridBagLayout();
		gbc	= new GridBagConstraints();
		
		setComponents();
		setTxtFieldPnl(pastPlaysInfo, txtArea, scrollBar);
		
		//MainFrame set up
		this.setLayout(gbl);
		this.setVisible(isFrameVisible);
		this.setSize(frameDimension);
		//this.setResizable(false);
		
		//Add components
		addComponents();
		
	}
	
	
	//===============
	//METHODS
	//===============	
	
	@Override
	public void paintComponent(Graphics g){
		super.paintComponents(g);
		
		Graphics2D g2 = (Graphics2D) g;
		
		g2.drawImage(ImageRegistry.getImage("scoreBackground.jpg"), null, 0, 0);
		
	}
	
	/**
	 * Initialize all field variables
	 */
	private void setComponents(){
		board 			= new MainBoardPanel();
		scoreBoard 		= new ScorePanel();
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
		scroll.setBackground(new Color(0,0,0,0));
		txtArea.setEditable(false);
		txtArea.setBackground(new Color(0,0,0,0));
		
		txtFieldPnl.setVisible(true);
		txtFieldPnl.setLayout(new BorderLayout());
		txtFieldPnl.setBorder(infoBorder);
		txtFieldPnl.setBackground(new Color(0,0,0,0));
		txtFieldPnl.setForeground(Color.WHITE);
		
		txtFieldPnl.add(scroll, BorderLayout.CENTER);
		
		
		
	}
	
}
