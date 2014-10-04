import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.util.Arrays;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;


public class Test {

	public static void main(String[] args) throws IOException {
		JFrame f = new JFrame("imh");
		JLabel lb = new JLabel();
		ImageRegistry.setImageGrid(ImageRegistry.loadImage("classic-playing-cards.jpg"), Utilities.getEnumNames(Rank.values()), Utilities.getEnumNames(Suit.values()), 13, 4);
		BufferedImage img = ImageRegistry.getImage(Rank.EIGHT + "_" + Suit.HEARTS.toString());
		System.out.println(img.getHeight());
		
		lb.setIcon(new ImageIcon(img));
		f.add(lb);
		f.setSize(850, 700);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setVisible(true);
		
	}

}
