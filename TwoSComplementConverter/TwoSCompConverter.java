/**************************************************************

Description:
    A simple 2's complement converter that converts a 
    decimal to its 2's complment in binary and hexadecimal format,
    and vice versa. It prints the output in different formats 
    for different uses. 

Compilation: $ javac TwoSCompConverter.java

Execution: java TwoSCompConverter yourInput

Input format:

    You can put spaces in your input to make it easier for you.
    For example, "-12345678" is the same as "1234 5678" and 
    "-1 2 3     4 5 678". This works for all kinds of inputs. 

    - Decimal: positive or negative decimal numbers
        example: 10, -256
    
    - Binary: start with the letter b, and the minimum 
              length should be 3. 
              (the char 'b', the sign bit, and the rest bits)
        example: b0001, b 1111 0000 1101

        Note: Auto padding feature is implemented so the length 
              of your input doesn't have to be a multiple of 4.
              For instance, to enter the 2's complement of -9,
              instead of b 1111 0111, you can just enter b10111.

    - Hexadecimal: start with "0x" or "0X" and is not case 
                   sensitive.
        example: 0xf, 0x0ac, 0 X ABCD 1234

@author: Kevin Sun
@since 2019 Jan 11

****************************************************************/

public class TwoSCompConverter {

    private static int nbBits;
    private static int inputLength;
    private static int nbPadding = 0;
    private static final int MAX_NB_BITS = 32;

    public static void main(String[] args) {
        
        // get and process input
        if(args.length > 1) {
           for(int i = 1 ; i < args.length; i++) {
               args[0] += " "; 
               args[0] += args[i];
           }
        }
         
        String binRegex = "[b][01]+";
        String hexRegex = "[0][xX][0-9a-fA-F]+";
        String decimalRegex = "[-]?\\d+";
        
        String input = args[0];

        System.out.println();
        System.out.println("Input: "+ args[0]);
        System.out.println();

        input = input.trim(); 
        input = input.replaceAll("\\s", "");

        inputLength = input.length();
        if(inputLength > MAX_NB_BITS + 1) {
            System.err.println("Input value out of range.");
            System.exit(-1);
        }

        // hexadecimal
        if( input.matches(hexRegex) ) {
          
            inputLength -= 2; // length without 0x

            if(inputLength > MAX_NB_BITS / 4) {
                System.err.println("Input value out of range.");
                System.exit(-1);
            }

            nbBits = 4 * inputLength;

            input = hexaToBin(input);
            int decimalValue = twoSCompToDecimal(input);
            System.out.println("Decimal: "+  decimalValue);

            printBinHex(decimalValue); 
       
        // binary
        }else if( input.matches(binRegex) ) {
            
            if(inputLength < 3) {
                System.err.println("Invalid input. The minimum length of a binary input should be 3.");
                System.exit(-1);
            }

            inputLength--; // length without b

            nbPadding = inputLength % 4 == 0 ? 0 : 4 - inputLength % 4 ;
            nbBits = inputLength + nbPadding;

            input = toLongFormat(input);    

            int decimalValue = twoSCompToDecimal(input);
            System.out.println("Decimal: "+  decimalValue);
                
            printBinHex(decimalValue); 

        // decimal
        }else if(input.matches(decimalRegex)){

            // range test
            int inputValue = 0;
            try {
                inputValue = Integer.parseInt(input);
            }catch(NumberFormatException e) {
                System.err.println("Input value out of range.");
                System.exit(-1);
            }

            int temp = inputValue;

            while ((temp /= 2) != 0) {
                nbBits++;
            }

            nbBits += 1; // +1 for when inputValue / 2 == 0

            if( inputValue > Math.pow(2, nbBits - 1) - 1 || inputValue < -1 * Math.pow(2, nbBits - 1) ) {
                nbBits++;
            }

            nbPadding = nbBits % 4 == 0 ? 0 : 4 - nbBits % 4 ;
            nbBits += nbPadding; 

            printBinHex(inputValue); 
        }else{
            System.err.println("Invalid input");
            System.exit(-1);
        } 

        System.out.println();
    }

    
    /**
     * Convert and print out the result.
     * @param decimal is the value that will be converted
     */
    private static void printBinHex(int decimal) {

            int[] arr = to2SComplement(decimal, nbBits);
            printBin(arr, true);
            printHex(arr);
            System.out.println();
            System.out.println(MAX_NB_BITS + "-bit version:");
            arr = to2SComplement(decimal, MAX_NB_BITS);
            printBin(arr, false);
            printHex(arr);

    }

    /**
     * Add paddings to the binary input so its length is
     * a multiple of 4.
     * @param input is the string of the binary input
     * @return A padded string
     */
    private static String toLongFormat(String input) {
        String longFormat = "";

        int paddingValue = input.charAt(1) - '0';

        for(int i = 0; i < nbPadding; i++) {
            longFormat += paddingValue;    
        }

        longFormat += input.substring(1);

        return longFormat;

    }


    /**
     * Convert 2's complement to decimal
     * @param  binComp is a String that contains the n-bit 2's complement binary 
     * @return the decimal number
     */
    private static int twoSCompToDecimal(String binComp) {

        byte[] arr = binComp.getBytes();

        // change to actual 0s and 1s from the ascii code for '0' and '1'
        for (int i = 0; i < arr.length; i++) {
            arr[i] -= '0';
        }

        // check if the number is negative
        boolean negative;

        if (negative = arr[0] == 1) {

            int j = arr.length - 1; 
            // minus 1, from 2's complement to 1's complement
            while (--arr[j] == -1) {
                arr[j] = 1;
                j--;
            }

            // invert bits
            for (int i = 0; i < arr.length; i++) {
                arr[i] = (byte) (1 - arr[i]);
            }          
        }
        
        int absDecimal = 0;
        // the absolute value of the decimal
        for (int i = 0; i < arr.length; i++) {
            absDecimal += arr[i] * (int)Math.pow(2, arr.length - 1 - i);
        }
   
        // check the sign and return 
        return negative ? -absDecimal : absDecimal;
    }


    /**
     * Convert the hexa string to a binary string
     * @param hexaComp the string that represents a 2'scomplement in hexadecimal format  
     * @return  the string that represents a 2'scomplement in binary format  
     */
    private static String hexaToBin(String hexaComp) {
        String binComp = "";
        // skip 0x in the string
        for (int i = 2; i < hexaComp.length(); i++) {
            char c = hexaComp.charAt(i);
            String temp = Integer.toBinaryString(getInt(c));
            // fill with 0 to make it 4-bit long for each char
            int leng = temp.length();
            for (int j = leng; j < 4; j++) {
                temp = 0 + temp;
            }
            binComp += temp;
        }
        
        return binComp;
    }

    /**
     * Get the value of a number from its char representation
     * @param c is the representation char
     * @return the actual value 
     */
    private static int getInt(char c) {
       if(c >= '0' && c <= '9') {
           return c - '0';
       }else if(c >= 'A' && c <= 'F'){
           return 10 + c - 'A'; 
       }else if(c >= 'a' && c <= 'f'){
           return 10 + c - 'a'; 
       }
       // if c is not from 0 to f
       return -1;
    }


    /**
     * Convert a decimal integer to its 2's complement
     * @param i an integer
     * @return an int array that contains the 2's complement representation of i
     */
    private static int[] to2SComplement(int i, int nbBits) {
        int[] arr = new int[nbBits]; 

        if (i == 0) {
            return arr;
        } else {

            int j = arr.length - 1;
            int q = Math.abs(i);

            // decimal to binary
            while (q != 0) {
                arr[j] = q % 2;
                q /= 2;
                j--;
            }

            if (i > 0) {
                return arr;
            } else {
                // invert the bits

                for (int k = arr.length - 1; k >= 0; k--) {
                    arr[k] = 1 - arr[k];
                }
                // plus 1 to get the 2's complement
                j = arr.length - 1;
                while (++arr[j] == 2) {
                    arr[j] = 0;
                    j--;
                }
                return arr;
            }
        }
    }

    /**
     * Print out the 2's complement in binary format
     * @param arr is the array that contains the 2's complement
     * @param printSpace determines whether the print a space between every 4 bits or not
     */
    private static void printBin(int[] arr, boolean printSpace) {
        System.out.print("Binary : ");
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i]);
            if (printSpace && ( (i + 1) % 4 == 0) && (i != arr.length - 1) ) {
                System.out.print(" ");
            }
        }
        System.out.println();
    }

    /**
     * Print out the 2's complement in hexadecimal format
     * @param arr is the array that contains the 2's complement
     */
    private static void printHex(int[] arr) {
        int value = 0;

        System.out.print("Hexadecimal : 0X");
        for (int i = 0; i < arr.length; i += 4) {
            value = 8 * arr[i] + 4 * arr[i + 1] + 2 * arr[i + 2] + arr[i + 3];
            if (value > 9) {
                switch (value) {
                case 10:
                    System.out.print("A");
                    break;
                case 11:
                    System.out.print("B");
                    break;
                case 12:
                    System.out.print("C");
                    break;
                case 13:
                    System.out.print("D");
                    break;
                case 14:
                    System.out.print("E");
                    break;
                case 15:
                    System.out.print("F");
                    break;
                }
            } else {
                System.out.print(value);
            }
        }
        System.out.println();
    }

}
