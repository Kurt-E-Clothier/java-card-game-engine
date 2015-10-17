/***********************************************************************//**
* @file			FileIO.java
* @author		Kurt E. Clothier
* @date			October 12, 2015
*
* @breif		Static methods for handling File I/O
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
import java.io.FilenameFilter;
import java.nio.charset.Charset;

/************************************************************************
 * The FileIO Class
 * - Static Methods for handling File I/O
 ************************************************************************/
public final class FileIO {
	
	/**
	 * Possible types of files.
	 * Naming convention: TYPE.GAME-VARIANT.txt
	 * 
	 * DECK - a file describing a deck of cards
	 * BOARD - a file describing a game board
	 * RULES - a file detailing the rules of a game
	 */
	public static enum Type { DECK, BOARD, RULES } 
	
/*------------------------------------------------
 	Constants and Attributes
 ------------------------------------------------*/
	public static final Charset CHARSET = Charset.forName("UTF-8");
	public static final File PLUGIN_DIR = new File(System.getProperty("user.dir").concat("/plugin"));

/*------------------------------------------------
 	Constructor(s)
 ------------------------------------------------*/
	private FileIO() {}
	
/*------------------------------------------------
    Static Methods
 ------------------------------------------------*/

	/**
	 * Return a <tt>File</tt> using the given type and name if it exists.
	 * 
	 * @param type	the type of file to be created
	 * @param name	what is described by this file
	 * @return	file using the given type and game if it exists.
	 */
	public static File getFile(FileIO.Type type, String name) {
		return FileIO.getFile(FileIO.getFilenmae(type, name));
	}
	
	/**
	 * Return a <tt>File</tt> using the given file name if it exists.
	 * 
	 * @param filename	name of file to return
	 * @return	file using the given file name if it exists.
	 */
	public static File getFile(String filename) {
		File file = new File(FileIO.PLUGIN_DIR, filename);
		if (file.exists())
			return file;
		else
			return null;
	}
	
	/**
	 * Returns a valid filename (without full path) from the given type and name
	 * 
	 * @param type	the type of file to be created
	 * @param name	what is described by this file
	 * @return	valid filename (without full path) from the given type and name
	 */
	public static String getFilenmae(FileIO.Type type, String name) {
		StringBuilder str = new StringBuilder();
		str.append(type.toString().toLowerCase());
		str.append(".");
		str.append(name);
		str.append(".txt");
		return str.toString();
	}
	
	/**
	 * Returns an array of abstract pathnames denoting the files of the specified type found in the "plugin" directory.
	 * 
	 * @param type	the <code>FileIO.Type</code> of file to include
	 * @return	array of abstract pathnames
	 */
	public static File[] listFiles(FileIO.Type type) {
		return FileIO.PLUGIN_DIR.listFiles(new SpecificFilenameFilter(type));
	}
	
	/**
	 * Returns an array of strings denoting the files of the specified type found in the "plugin" directory.
	 * 
	 * @param type	the <code>FileIO.Type</code> of file to include
	 * @return	array of abstract pathnames
	 */
	public static String[] list(FileIO.Type type) {
		return FileIO.PLUGIN_DIR.list(new SpecificFilenameFilter(type));
	}
	
	/* **********************************************************************
	 * Nested Private class: SpecificFilenameFilter
	 * - Used to create FilenameFilters specific to a file type
	 ************************************************************************/
	private static final class SpecificFilenameFilter implements FilenameFilter {
		
		private final String prefix;
		
		private SpecificFilenameFilter(FileIO.Type type) {
			prefix = type.toString().toLowerCase();
		}

		@Override
		public boolean accept(File dir, String name) {
			String nameLC = name.toLowerCase();
			return (nameLC.startsWith(prefix) &&
					nameLC.endsWith(".txt"));
		}
	}
}
