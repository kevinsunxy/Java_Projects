package converter;

import javax.swing.JOptionPane;

/**
 * @author Xinyao Sun
 * @since 2017.05.27
 * Description: Positional notation converter
 *
 */
public class Converter {
	
	/**Convert decimal to a new base between 2 and 9
	 * @param decimalNumber is the decimal number
	 * @param newBase is the new numeral base
	 * @return the number in the new base
	 */
	public static String decimalToBase2To9 (String decimalNumber , String newBase) {
		String inverse = "";
		
		if( (newBase+"").matches("[2-9]") ) {
			
			int base = Integer.parseInt(newBase);
			int temp = Integer.parseInt(decimalNumber);
			
			while(temp != 0) {
				inverse += temp % base;
				temp /= base;
			}
			
		}else{
			System.err.println("Invalid base");
		}
		
		
		return inverseString(inverse);
	}
	
	/**Convert decimal to hexadecimal
	 * @param decimalNumber is the decimal number
	 * @return the hexadecimal number
	 */
	public static String decimalToHexadecimal(String decimalNumber) {
		String inverseHexa = "";
		int temp = Integer.parseInt(decimalNumber);
		
		while(temp != 0) {
			
			int mod = temp % 16 ;
			
			if(mod<10) {
				inverseHexa += mod;
			}else{
				
				inverseHexa += "ABCDEF".charAt( mod % 10 );
				
			}
			
			temp /= 16;
		}
		return inverseString(inverseHexa);
	}
	
	/**Convert a number of a base between 2 and 9 to decimal
	 * @param number is the number of a base between 2 and 9
	 * @param base is the base of the number
	 * @return the decimal number
	 */
	public static String base2To9ToDecimal(String number,String base) {
		int decimal = 0;
		
		if( (base+"").matches("[2-9]") ) {
			
			int validBase = Integer.parseInt(base);
			
			if(number.matches("[0-"+(validBase-1)+"]+")) {
				for(int i=0;i<number.length();i++) {
					int temp = Integer.parseInt(number.charAt(i)+"");
					decimal += temp*Math.pow(validBase, number.length()-i-1);
				}
				
			}else{
				System.err.println("Invalid number");
			}
		}else{
			System.err.println("Invalid base");
		}
		
		
		return decimal+"";
	}
	
	/**Convert hexadecimal to decimal
	 * @param hexa is the hexadecimal number
	 * @return the decimal number
	 */
	public static String hexadecimalToDecimal(String hexa) {
		
		int decimal = 0; 
		
		if(hexa.matches("[0-9A-F]+")) {
			for(int i=0;i<hexa.length();i++) {
				
				String s = hexa.charAt(i)+"";
				int temp;
				if(s.matches("\\d")) {
					temp = Integer.parseInt(s);					
				}else{
					temp = 10 + "ABCDEF".indexOf(s);
				}
				decimal += temp*Math.pow(16, hexa.length()-i-1);
				
			}
		}else{
			System.err.println("Invalid number");
		}
		
		return decimal+"";
	}
	
	/**Convert a number of base between 2 and 9 to hexadecimal
	 * @param numberBase2To9 is the number
	 * @param base is the current base
	 * @return hexadecimal number
	 */
	public static String base2To9ToHexadecimal(String numberBase2To9, String base) {		
		return decimalToHexadecimal(base2To9ToDecimal(numberBase2To9,base));
	}
	
	
	/**Convert a hexadecimal number to a base between 2 and 9
	 * @param hexadecimal is the hexadecimal number
	 * @param newBase is the new base
	 * @return the number of the new base
	 */
	public static String hexadecimalToBase2To9(String hexadecimal, String newBase) {
		return decimalToBase2To9(hexadecimalToDecimal(hexadecimal),newBase);
	}
	
	/**Inverse the string
	 * @param inverse the original string
	 * @return the inverse string of the original string
	 */
	private static String inverseString(String inverse) {
		String s = "";
		
		for(int i=inverse.length()-1;i>=0;i--) {
			s += inverse.charAt(i);
		}
		
		return s;
	}

}
