package ergasia2;

import java.io.*;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;

public class FileName {

	public static void main(String[] args) throws IOException {

	}

    public static String getFileNames(String path) {
		File folder = new File(path);
		File[] listOfFiles = folder.listFiles();

		String s = "";
    		for (int i = 0; i < listOfFiles.length; i++)
      	    	if (listOfFiles[i].isFile())
        			s += listOfFiles[i].getName()+",";
		return s.substring(0, s.length()-1);
    }
}

