import java.util.ArrayList;
import java.util.Random;


/**
 * Esta clase es para guardar todos los metodos de utilidad que utilizara el programa de Shelem. Estos son metodos que no pertenecen a ninguna clase 
 * en si sino que pueden ser utilizados de manera general.
 * @author Glorimar Castro
 * @author Giovanni 
 *
 */
public class Utilities {
	
	public static void overhandArrayShuffle(Object[] o){
		final int MINIMUN_ITERATION = 4;												//Minimmun cuts to make in the shuffles
		final int MAXIMUN_ITERATION = MINIMUN_ITERATION + 9;							//Maximun cuts to make in the shuffles
		
		Random 	random 		= new Random();
		int	 	firstPos 	= 0; 														//Position to start the cut
		int 	lastPos		= 0;														//Position to end the cut
		int 	iteration 	= MINIMUN_ITERATION + random.nextInt(MAXIMUN_ITERATION);	//Make a minnimun of 4 cuts and a maximmun of 12 cuts
		
		//Make the cuts
		for(int i = 0; i <iteration; i++){
			//initialize needed variables
			if(o.length < 5){
				firstPos 	= 1 + random.nextInt(o.length - 1);
				lastPos		= firstPos + random.nextInt(o.length - firstPos);
			}else{
				firstPos 	= 1 + random.nextInt(o.length - 5);
				lastPos		= firstPos + random.nextInt(o.length - firstPos);
			}
			
			//Save the cards to move at the beggining
			Object[] blockToMove = new Object[lastPos - firstPos + 1];
			for(int k = 0; k < blockToMove.length; k++){
				blockToMove[k] = o[firstPos + k];
			}
			//Save the cards at the beginning where the blockToMove is going to be inserted
			Object[] blockAtBeginning = new Object[firstPos];
			for(int k = 0; k < blockAtBeginning.length; k++){
				blockAtBeginning[k] = o[k];
			}
			
			//Shift the values in the blockToMove a the the begining of the array
			for(int k = 0; k < blockToMove.length; k++){
				o[k] = blockToMove[k];
			}
			
			//Shift the values at the begining to the end of the blockToMove
			for(int k = 0, j = blockToMove.length; k < blockAtBeginning.length; k++, j++){
				o[j] = blockAtBeginning[k];
			}
		}
	}
	
	/**
	 * Simulate a overhand shuffle in the array list passed as a parameter
	 * @param <T>
	 * @param o
	 */
	public static <T> void overhandArrayListShuffle(ArrayList<T> o){
		final int MINIMUN_ITERATION = 4;												//Minimmun cuts to make in the shuffles
		final int MAXIMUN_ITERATION = MINIMUN_ITERATION + 9;							//Maximun cuts to make in the shuffles
		
		Random 	random 		= new Random();
		int	 	firstPos 	= 0; 														//Position to start the cut
		int 	lastPos		= 0;														//Position to end the cut
		int 	iteration 	= MINIMUN_ITERATION + random.nextInt(MAXIMUN_ITERATION);	//Make a minnimun of 4 cuts and a maximmun of 12 cuts
		
		//Make the cuts
		for(int i = 0; i <iteration; i++){
			//initialize needed variables
			firstPos = 1 + random.nextInt(o.size() - 5);
			lastPos	= firstPos + random.nextInt(o.size() - firstPos);
			
			//Save the cards to move at the beggining
			ArrayList<T> blockToMove = new ArrayList<T>();
			for(int k = firstPos; k <= lastPos; k++){
				blockToMove.add(o.get(k));
			}
			
			//Save the cards at the beginning where the blockToMove is going to be inserted
			ArrayList<T> blockAtBeginning = new ArrayList<T>();
			for(int k = 0; k < firstPos; k++){
				blockAtBeginning.add(o.get(k));
			}
			
			//Shift the values in the blockToMove a the the begining of the array
			for(int k = 0; k < blockToMove.size(); k++){
				o.set(k, blockToMove.get(k));
			}
			
			//Shift the values at the begining to the end of the blockToMove
			for(int k = 0, j = blockToMove.size(); k < blockAtBeginning.size(); k++, j++){
				o.set(j, blockAtBeginning.get(k));
			}
		}
	}
	/**
	 * Metodo que devuelve un arreglo de String con los nombres de todos los elementos de un enum. 
	 * @param o
	 * 	Enum al cual se desea obtener arreglo de nombres.
	 * @return
	 */
	public static String[] getEnumNames(Object[] o ){
		String[] names = new String[o.length];
		
		for(int i =0; i < names.length; i++){
			names[i] = o[i].toString();
		}
		return names;
		
	}
	
	public static Object[] toArray(ArrayList o ){
		Object[] array = new String[o.size()];
		
		for(int i =0; i < array.length; i++){
			array[i] = o.get(i);
		}
		return array;
		
	}

	public static boolean isInteger(String s){
		try{
			Integer.parseInt(s);
			return true;
		}catch(NumberFormatException e){
			return false;
		}
		
	}
}
