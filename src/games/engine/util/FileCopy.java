/***********************************************************************//**
* @file			FileCopy.java
* @author		Kurt E. Clothier
* @date			October 12, 2015
*
* @breif		A copy of the data contained in a component file
*
* @pre			Compiler: Eclipse - Mars Release (4.5.0)
* @pre			Java: JRE 7 or greater
*
* @see			http://www.projectsbykec.com/
*
* @copyright	The MIT License (MIT) - see LICENSE.txt
****************************************************************************/

package games.engine.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

/************************************************************************
 * The FileCopy Class
 * - A copy of a file containing game component data
 * - FileCopy is quasi-immutable
 ************************************************************************/
public class FileCopy {
	
/*------------------------------------------------
 	Constants and Attributes
 ------------------------------------------------*/
	private static final String COMMENT = "#";
	private List<String> lines;		// Each line of the file
	private int fileLength;
	
/*------------------------------------------------
 	Constructor(s)
 ------------------------------------------------*/
	
	/**
	 * Construct a <tt>FileCopy</tt> of the given <tt>File</tt>.
	 * The resulting copy is void of comments, bookend whitespace, and capitalization.
	 * 
	 * @param file	the file to be copied
	 */
	protected FileCopy(File file) {
		try {
			lines = Files.readAllLines(file.toPath(), FileIO.CHARSET);
			
			//Clean up file, bottom to top
			for (int ndx = lines.size()-1; ndx >=0; --ndx) {
				
				// remove leading/trailing whitespace
				lines.set(ndx, lines.get(ndx).trim());
				
				// remove blank lines
				if (lines.get(ndx).equals("") ||
				// remove null lines
					lines.get(ndx) == null ||
				// remove line comments
					lines.get(ndx).startsWith(FileCopy.COMMENT)) {
					lines.remove(ndx);
					
				} else {
					// convert to lower case
					lines.set(ndx, lines.get(ndx).toLowerCase());
					
					// remove end of line comments and trailing whitespace
					if (lines.get(ndx).contains(FileCopy.COMMENT)) {
						String[] stripped = lines.get(ndx).split(FileCopy.COMMENT);
						lines.set(ndx, stripped[0].trim());
					}
				}
			}
			fileLength = lines.size();
			
		} catch (IOException e) {
			lines = null;
			fileLength = 0;
		}
	}
	
/*------------------------------------------------
 	Action Methods
 ------------------------------------------------*/
	
	/**
	 * Return the length of this FileCopy.
	 * 
	 * @return	the length of this FileCopy
	 */
	protected int size() {
		return fileLength;
	}
	
	
	/**
	 * Return the index of the first occurrence of the keyword, or -1 if not found.
	 * 
	 * @param keyword	keyword to be searched
	 * @return	index of the keyword, or -1 if not found.
	 */
	protected int indexOf(String keyword) {
		return this.indexOf(keyword, 0);
	}
	
	/**
	 * Return the index of the next occurrence of the keyword, or -1 if not found.
	 * 
	 * @param keyword	keyword to be searched
	 * @param startNdx	file index to start searching for keyword
	 * @return	index of the keyword, or -1 if not found.
	 */
	protected int indexOf(String keyword, int startNdx) {
		for (int ndx = startNdx; ndx < fileLength; ++ndx) {
			if (lines.get(ndx).startsWith(keyword)) {
				return ndx;
			}
		}
		return -1;
	}
	
	/**
	 * Returns the parameter(s) found after the first occurrence of the specified keyword.
	 * Returned string is void of whitespace and comments.
	 * 
	 * @param keyword	keyword to be searched
	 * @return	parameters found after the specified keyword
	 */
	protected String getParams(String keyword) {
		int ndx = indexOf(keyword);
		if (ndx >= 0) {
			String[] words = lines.get(ndx).split("\\s", 2);
			if (words.length > 1) {
				return words[1].replaceAll("\\s", "");
			}
		}
		return null;
	}
	
	/**
	 * Returns the parameter(s) found after the specified keyword.
	 * Returned string is void of whitespace and comments.
	 * 
	 * @param keyword	keyword to be searched
	 * @param startNdx	file index to start searching for keyword
	 * @return	parameters found after the specified keyword
	 */
	protected String getParams(String keyword, int startNdx) {
		int ndx = indexOf(keyword, startNdx);
		if (ndx >= 0) {
			String[] words = lines.get(ndx).split("\\s", 2);
			return words[1].replaceAll("\\s", "");
		}
		return null;
	}
	
	/**
	 * Returns a String representing the specified line of the file.
	 * 
	 * @param ndx	line of the file to return
	 * @return	String representing the specified line of the file
	 */
	protected String getLine(final int ndx) {
		return lines.get(ndx);
	}
	
	/**
	 * Return a string version of this FileCopy.
	 */
	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		String NEW_LINE = System.getProperty("line.separator");
		for (String line : lines) {
			str.append(line);
			str.append(NEW_LINE);
		}
		return str.toString();
	}
	
/*------------------------------------------------
    Static Methods
 ------------------------------------------------*/
	/**
	 * Return an array of strings that were comma separated.
	 * 
	 * @param csv 	string of comma separated values to split
	 * @return	array of strings that were comma separated.
	 */
	protected static String[] splitCSV(String csv) {
		if (csv != null) {
			return csv.split(",");
		}
		return null;
	}
	
	/**
	 * Returns a string, converted for visibility by adding spaces.
	 * 
	 * @param name	name of string to convert
	 * @return	name as string, converted for visibility
	 */
	protected static String convertName(String name) {
		if (name != null) {
			String tmp = name.replaceAll("_", " ");
			return tmp.replaceAll("-", " - ");
		} else {
			return null;
		}
	}
}
