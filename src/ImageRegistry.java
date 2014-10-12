import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.HashMap;

import javax.imageio.ImageIO;

/**
 * This class is for generate and save all the image needed by the Shelem Progam. 
 * @author Glorimar Castro
 *
 */
public class ImageRegistry {
	
	public static HashMap<String, BufferedImage> images = new HashMap<String, BufferedImage>();
	public static final String IMAGE_PATH = "res\\";
	

	/**
	 * Este metodo se encarga de buscar la imagen solicitada y añadirla al HashMap de imagenes. Si existe no la añade y solo la devuelve al usuario, 
	 * si no existe crea la imagen y la añade al HashMap, devolviendo la imagen como reusltado. 
	 * @param name
	 * 		Nombre del archivo de la imagen
	 * @return
	 */
	@SuppressWarnings("finally")
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
			}finally{
				return images.get(name);
			}
			
		}
	}
	

	/**
	 * 
	 * @param name
	 * 	Nombre del archivo de la imagen.
	 * @return
	 */
	public static BufferedImage getImage(String name){
		return images.get(name);
	}
	
	/**
	 * Este metodo se encarga de tomar una imagen y generar sub imagenes a partir de la imagen original. Las sub imagenes se generaran a 
	 * partir de dividir la imagen en una matriz con las columnas y filas especificadas por el usuario. El nombre de cada imagen generada sera el equivalente 
	 * a los nombres ingresados por el usuario en el orden en que se generan las imagenes con el orden de los nombres en el arreglo de nombres especificados.
	 * @param img
	 * 	Imagen a cortar
	 * @param rowName
	 * 	Nombres para las filas generadas
	 * @param columnName
	 * 	Nombres para las columnas generada
	 * @param rows
	 * 	Cantidad de filas a cortar la imagen
	 * @param colums
	 * 	Cantidad de columnas a cortar la imagen
	 */
	public static void setImageGrid(BufferedImage img, String[] rowName, String[] columnName, int rows, int colums){
		BufferedImage	buffImg;
		int 			gridImgWidth 	= img.getWidth() / colums;
		int				gridImgHeight	= img.getHeight() / rows;
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
