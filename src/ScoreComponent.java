import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import javax.swing.JLabel;
import javax.swing.JPanel;


public class ScoreComponent extends JPanel{
	//===============
	//FIELD
	//===============
	
	private final int		currentTeamIndentation	= 50;
	private final int		opponentTeamIndentation	= MainPanel.WIDTH/2 + 150;
	private final JLabel	currentTeamScoreLbl		= new JLabel("Score: ");
	private final JLabel	opponentTeamScoreLbl	= new JLabel("Score: ");
	
	
	BufferedImage suitLiveImg;										//image to draw the selected suit as the live
	
	JLabel[] teamLabels 		= new JLabel[2];					//Labels for write the team names
	JLabel[] totalScoresLbl		= new JLabel[2];					//labels for write the total scores 
	JLabel[] divideSlash		= new JLabel[2];					//labels for write /
	JLabel[] maximunScoreLbl	= new JLabel[2];					//Label to print the maximun score
	JLabel[] teamBidsLbl		= new JLabel[2];
	
	Font 	font		= new Font("Arial Black", Font.BOLD, 14);
	
	
	boolean isSuitSet = false;
	boolean isTeamSet = false;
	
	private Dimension dimension = new Dimension(400, 650);

	//===============
	//CONSTRUCTORS
	//==============
	public ScoreComponent(){
		ImageRegistry.loadImage("CLUBS.png");
		ImageRegistry.loadImage("HEARTS.png");
		ImageRegistry.loadImage("DIAMONDS.png");
		ImageRegistry.loadImage("SPADES.png");
		
		teamLabels[0] = new JLabel("[]");
		teamLabels[0].setBounds(new Rectangle(new Point(currentTeamIndentation,3), new Dimension(500, 15)));
		teamLabels[0].setFont(font);
		teamLabels[0].setForeground(Color.WHITE);
		teamLabels[1] = new JLabel("[]");
		teamLabels[1].setBounds(new Rectangle(new Point(opponentTeamIndentation,3), new Dimension(500, 15)));
		teamLabels[1].setFont(font);
		teamLabels[1].setForeground(Color.WHITE);
		
		currentTeamScoreLbl.setBounds(new Rectangle(new Point(currentTeamIndentation + 15, teamLabels[0].getHeight() + 5), new Dimension(60, 15)));
		currentTeamScoreLbl.setForeground(Color.GREEN);
		currentTeamScoreLbl.setFont(font);
		opponentTeamScoreLbl.setBounds(new Rectangle(new Point(opponentTeamIndentation + 15, teamLabels[1].getHeight() + 5), new Dimension(60, 15)));
		opponentTeamScoreLbl.setForeground(Color.RED);
		opponentTeamScoreLbl.setFont(font);
				
		totalScoresLbl[0] = new JLabel("0", JLabel.RIGHT);
		totalScoresLbl[0].setForeground(Color.GREEN);
		totalScoresLbl[0].setBounds(currentTeamScoreLbl.getX() + currentTeamScoreLbl.getWidth(), currentTeamScoreLbl.getY(), 40, 15);
		totalScoresLbl[1] = new JLabel("0", JLabel.RIGHT);
		totalScoresLbl[1].setForeground(Color.RED);
		totalScoresLbl[1].setBounds(opponentTeamScoreLbl.getX() + opponentTeamScoreLbl.getWidth(), opponentTeamScoreLbl.getY(), 40, 15);
		
		teamBidsLbl[0] = new JLabel("Bid: N/A");
		teamBidsLbl[0].setBounds(new Rectangle(new Point(currentTeamIndentation + 15, currentTeamScoreLbl.getY() + currentTeamScoreLbl.getHeight() + 5), new Dimension(160, 15)));
		teamBidsLbl[0].setForeground(Color.ORANGE);
		teamBidsLbl[0].setFont(font);
		teamBidsLbl[1] = new JLabel("Bid: \tN/A");
		teamBidsLbl[1].setBounds(new Rectangle(new Point(opponentTeamIndentation + 15, opponentTeamScoreLbl.getY() + opponentTeamScoreLbl.getHeight() + 5), new Dimension(160, 15)));
		teamBidsLbl[1].setForeground(Color.ORANGE);
		teamBidsLbl[1].setFont(font);
		
		divideSlash[0] = new JLabel("/");
		divideSlash[0].setForeground(Color.GREEN);
		divideSlash[0].setBounds(totalScoresLbl[0].getX() + totalScoresLbl[0].getWidth() + 5, totalScoresLbl[0].getY(), 10, 15);
		divideSlash[1] = new JLabel("/");
		divideSlash[1].setForeground(Color.RED);
		divideSlash[1].setBounds(totalScoresLbl[1].getX() + totalScoresLbl[1].getWidth() + 5, totalScoresLbl[1].getY(), 10, 15);
		
		maximunScoreLbl[0] = new JLabel("0");
		maximunScoreLbl[0].setForeground(Color.GREEN);
		maximunScoreLbl[0].setBounds(divideSlash[0].getX() + divideSlash[0].getWidth(), divideSlash[0].getY(), 40, 15);
		maximunScoreLbl[1] = new JLabel("0");
		maximunScoreLbl[1].setForeground(Color.RED);
		maximunScoreLbl[1].setBounds(divideSlash[1].getX() + divideSlash[1].getWidth(), divideSlash[1].getY(), 40, 15);
		
		this.setLayout(null);
		this.setVisible(true);
		this.setSize(new Dimension(500, 500));
		this.setMinimumSize(dimension);
	}
	

	
	
	//===============
	//METHODS
	//===============
	@Override
	public void paintComponent(Graphics g){
		super.paintComponents(g);
		
		Graphics2D g2 = (Graphics2D) g;
		
		if(isSuitSet){
			g2.drawImage(suitLiveImg, null, MainPanel.WIDTH/2 - suitLiveImg.getWidth()/2, 3);
		}
		
		
		this.add(teamLabels[0]);
		this.add(teamLabels[1]);
		
		this.add(currentTeamScoreLbl);
		this.add(opponentTeamScoreLbl);
		
		this.add(totalScoresLbl[0]);
		this.add(totalScoresLbl[1]);
		
		this.add(divideSlash[0]);
		this.add(divideSlash[1]);
		
		this.add(maximunScoreLbl[0]);
		this.add(maximunScoreLbl[1]);
		
		this.add(teamBidsLbl[0]);
		this.add(teamBidsLbl[1]);
	}
	
	
	/**
	 * ---------------------------------------------------- SET SUIT ----------------------------------------------------
	 * 
	 * 	This method is for set or change the selected suit as the main suit
	 * @param suit
	 */
	public void setSuit(String suit){
		switch(suit){
		case "CLUBS":
			suitLiveImg = ImageRegistry.getImage("CLUBS.png");
			isSuitSet = true;
			break;
		case "SPADES":
			suitLiveImg = ImageRegistry.getImage("SPADES.png");
			isSuitSet = true;
			break;
		case "HEARTS":
			suitLiveImg = ImageRegistry.getImage("HEARTS.png");
			isSuitSet = true;
			break;
		case "DIAMONDS":
			suitLiveImg = ImageRegistry.getImage("DIAMONDS.png");
			isSuitSet = true;
			break;
		default:
			break;
		}
		
		repaint();
	}
	
	public void setTeamNames(String nameTeam1, String nameTeam2){
		teamLabels[0].setText("[Team1: " + nameTeam1 + "]");
		teamLabels[1].setText("[Team2: " + nameTeam2 + "]");
		repaint();
		
	}
	
	public void setTotalScores(int team1Score, int team2Score){
		totalScoresLbl[0].setText("" + team1Score);
		totalScoresLbl[1].setText("" + team2Score);
		repaint();
		
	}
	
	public void setMaximunScore(int maximunScore){
		maximunScoreLbl[0].setText("" + maximunScore);
		maximunScoreLbl[1].setText("" + maximunScore);
		repaint();
	}
	
	public void setBid(int team1Bid, int team2Bid){
		teamBidsLbl[0].setText("Bid: \t" + team1Bid);
		teamBidsLbl[1].setText("Bid: \t" + team2Bid);
		repaint();
	}
	
}
