
/**
 * Esta clase es para guardar todos los metodos de utilidad que utilizara el programa de Shelem. Estos son metodos que no pertenecen a ninguna clase 
 * en si sino que pueden ser utilizados de manera general.
 * @author Glorimar Castro
 * @author Giovanni 
 *
 */
public class Utilities {
	
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
}
