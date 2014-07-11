package com.googlecode.jcobs.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * General purpose methods for working with files.
 * 
 * @author Samuel Y. Deschamps [samueldeschamps at gmail dot com]
 * @since July/2014
 */
public class FileUtils {

	/**
	 * Reads a text file into a list of strings.
	 * 
	 * @param file
	 *            the file to be read
	 * @return a list containing one string for each line in the text file
	 * @throws IOException
	 *             if the file doesn't exists or an I/O error occur
	 */
	public static List<String> fileToString(File file) throws IOException {
		List<String> result = new ArrayList<>();
		FileReader reader = new FileReader(file);
		BufferedReader bufReader = new BufferedReader(reader);
		try {
			String line;
			while ((line = bufReader.readLine()) != null) {
				result.add(line);
			}
		} finally {
			bufReader.close();
			reader.close();
		}
		return result;
	}

}
