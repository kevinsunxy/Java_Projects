import java.io.File;

/**************************************************************

Print out all the subdirectories and subfiles of a directory in the tree format.

-Description: 
    Java version of the tree program for when the tree is not installed 
    and you don't have the admin access to install it (i.e. mcgill cs servers, so
    I wrote my own tree).

    Support the -a, -d and -f switch and print out the number of subdirectories 
    and subfiles.
             
    After compilation, to use this program outside of the directory which 
    contains the .class file, you need to add that directory to the CLASSPATH.
    
-Compilation: $ javac MyTree.java

-Execution: $ java MyTree [-switches] [path]

-Note: 
    If you use this program to print out the directories/files that you 
    don't have access to, it will probably give you a null pointer exception.

-CLASSPATH:    
    For the mimi server, enter 
        $ echo $CLASSPATH 
    to see your current CLASSPATH.
   
    If your CLASSPATH is empty like mine, to add a directory to CLASSPATH for 
    the current session, enter
        $ export CLASSPATH=the path of the directory
    For example, I put my MyTree.class on the desktop, so 
        $ export CLASSPATH=~/Desktop

    To change the CLASSPATH permenantly, ask google or wait to learn that later in 206.   
    (I tried and failed so I'll wait)

-Switches: 
    -a : show hidden files
    -d : only show directories
    -f : show full path name

    No switches means print all the visible files and directories.

-Path: 
    If you don't enter anything, the program will print the things in your 
    current location.

@author Kevin Sun
@since 2019 Jan 15

**************************************************************/

public class MyTree {

    private File dir;
    private File[] subfiles;

    private String fileName;
    private int nbDirs;
    private int nbFiles;
    private String offsetString = "";
    
    private boolean all = false;
    private boolean dirOnly = false;
    private boolean fullPath = false;
    
    private String boxDrawing;
    private String nextSymbol;

    private static final String TAB = "   ";
    private static final String VERTICAL_LINE = "\u2502  ";
    private static final String BOX_DRAWING_T = "\u251c\u2500\u2500 ";
    private static final String BOX_DRAWING_L = "\u2514\u2500\u2500 ";

    /**
     * Constructor 
     */
    public MyTree(String[] input) {
        processInput(input);
        subfiles = dir.listFiles();
        System.out.println( dir.getPath() );
    }
    
    /**
     * A method that gathers information such as the switches and the path from the input.
     * @param args is a String array 
     */
    private void processInput(String[] args) {
        switch(args.length) {
            case 0:
                dir = getDir(".");
                break;
            case 1:
                if(args[0].charAt(0) == '-') {
                    setSwitches(args[0]);
                    dir = getDir(".");
                }else {
                    dir = getDir(args[0]);
                }
                break;
            case 2:
                // you can enter invalid switches, idc. :p
                if(args[0].charAt(0) == '-') {
                    setSwitches(args[0]);
                }
                dir = getDir(args[1]);
                break;
            default:
                System.err.println("Invalid Input. Format: $ java MyTree [-switches] [path]");
                System.exit(-1);
        
        }
    }
    
    /**
     * Set switches
     * @param s is the args[0] from the arg list if it starts with -
     */
    private void setSwitches(String s) {
        all = s.contains("a");
        dirOnly = s.contains("d");
        fullPath = s.contains("f");
    }

    /**
     * Print the file tree and other information
     */
    public void printTree() {

        printTree(subfiles, offsetString);
        System.out.println();
        
        System.out.println(nbDirs + " directories");
        if(!dirOnly) System.out.println(nbFiles + " files");
    }

    /**
     * Print the file tree recursively
     * @param subfiles is the array of all the files/directories that are in the current dirctory
     * @param String offsetString is the string that contains the symbols such as vertical lines and tabs
     */
    private void printTree(File[] subfiles, String offsetString) {

        int lastIndex = lastIndex(subfiles);
        
        for (int i = 0; i < subfiles.length; i++) {

            fileName = fullPath ? subfiles[i].getAbsolutePath(): subfiles[i].getName();
            
            if(subfiles[i].isHidden() && !all) {
                continue;
            }

            if(dirOnly && subfiles[i].isFile()) {
                continue;
            }
            
            // print the vertical lines and tabs    
            System.out.print(offsetString);
        
            // the last file/dir of the array
            if (i == subfiles.length - 1) {

                System.out.println(BOX_DRAWING_L + fileName); 

                if (!subfiles[i].isFile()) {
                    nbDirs++;
                    printTree(subfiles[i].listFiles(), offsetString + TAB);
                }else {
                    nbFiles++;
                }
                    
            } else {
                boxDrawing = BOX_DRAWING_T;
                      
                /* The file is not the last file in the array. 
                * But if it is the last file that will be printed 
                * out on the screen depends on the switches entered,
                * so use L instead of T
                */ 

                if( i == lastIndex) {
                    boxDrawing = BOX_DRAWING_L;
                }
                
                System.out.println(boxDrawing + fileName); 

                if (!subfiles[i].isFile()) {
                
                    nbDirs++;
                    nextSymbol = VERTICAL_LINE;

                    if(dirOnly && i == lastIndex) {
                        nextSymbol = TAB;
                    }

                   printTree(subfiles[i].listFiles(), offsetString + nextSymbol);
                }else {
                    nbFiles++;
                }
                    
            }
        }
    }


    /**
     * Find and return the index of the last file that will be printed
     * @param subfiles is the array of all the files/directories that are in the current dirctory
     * @return the index of the last file that will be printed 
     */
    private int lastIndex(File[] subfiles) {
        // some part of this method is for the situation where some 
        // hidden directories/files are printed after the visible ones,
        // I didn't know this order is possible, until I run the code on mimi

        int i = -1;
        // the last file that will be printed out is last visible file of the array
        if(!all && !dirOnly) {
            for(i = subfiles.length - 1; i > 0; i--) {
                if( !subfiles[i].isHidden() ) break;
            }
        }

        // the last directory that will be printed out is last visible directory of the array
        if(!all && dirOnly) {
            for(i = subfiles.length - 1; i > 0; i--) {
                if( !subfiles[i].isHidden() && subfiles[i].isDirectory()) break;
            }
        }

        // the last directory that will be printed out is just the last directory of the array
        if(all && dirOnly) {
            for(i = subfiles.length - 1; i > 0; i--) {
                if( subfiles[i].isDirectory() ) break;
            }
        }

        return i;
    }

    /**
     * Get the directory from the input string 
     * @param dir is the string that contains the path of the directory
     * @return a File object of the directory
     */
    private File getDir(String dir) {
        File file = null;
        file = new File(dir);

        if (!file.exists()) {
            System.err.println("The directory doesn't exist.");
            System.exit(-1);
        } else if (file.isFile()) {
            System.err.println("The input is not a directory.");
            System.exit(-1);
        } else {
            return file;
        }
        return file;
    }

    /**
     * main method 
     */
    public static void main(String[] args) {
        
        MyTree tree = new MyTree(args);
        tree.printTree();
    }
}
