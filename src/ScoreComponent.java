import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JPanel;


public class ScoreComponent extends JPanel{
	
	private Dimension dimension = new Dimension(400, 650);
	
	public ScoreComponent(){
		this.setVisible(true);
		this.setBackground(Color.black);
		this.setMinimumSize(dimension);
	}
	
	
	
	
	
}
