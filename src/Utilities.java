
public class Utilities {
	
	public static String[] getEnumNames(Object[] o ){
		String[] names = new String[o.length];
		
		for(int i =0; i < names.length; i++){
			names[i] = o[i].toString();
		}
		return names;
		
	}
}
