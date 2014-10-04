import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.HashMap;

import javax.imageio.ImageIO;


public class ImageRegistry {
	public static HashMap<String, BufferedImage> images = new HashMap<String, BufferedImage>();
	
	
	public static final String IMAGE_PATH = "res/";
	


	public static BufferedImage loadImage(String name) {
		Path fullPath = FileSystems.getDefault().getPath(IMAGE_PATH, name);
		if(images.containsKey(name)) {
			return images.get(name);
		} else {
			BufferedImage b;;
			try {
				b = ImageIO.read(new File(fullPath.toString()));
				images.put(name,b);
				return b;
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("Cannot load image: "+fullPath);
			}
			return null;
		}
	}
	
	/*
	 * 
	 */
	
	public static BufferedImage getImage(String name){
		return images.get(name);
	}
	
	public static void setImageGrid(BufferedImage img, String[] columnName, String[] rowName, int width, int height){
		BufferedImage	buffImg;
		int 			gridImgWidth 	= img.getWidth() / width;
		int				gridImgHeight	= img.getHeight() / height;
		int				suitCount		= 0;
		int				rankCount		= 0;
		for(String s: rowName){
			for(String r: columnName){
				images.put(r + "_" + s, img.getSubimage(gridImgWidth * rankCount, gridImgHeight * suitCount, gridImgWidth - 1, gridImgHeight - 1));
				rankCount++;
			}
			rankCount = 0;
			suitCount++;
		}
		
	}
	

}
