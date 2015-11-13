/***********************************************************************//**
* @file			Plugin.java
* @author		Kurt E. Clothier
* @date			November 11, 2015
* @breif		Component data from a game plugin file
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
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/******************************************************************//**
 * The Plugin Class
 * - A representation of the component data contained in a game plugin file
 * - Plugin is quasi-immutable
 * - Also contains various static utilities for handing plugin files
 * Issues
 * - This is likely a "god class," but the implementation makes sense
 * 		^ This could be fixed by splitting off the static methods
 * 		^ All non-static methods are important plugin utilties
 ********************************************************************/
public class Plugin {
	
/*------------------------------------------------
 	Global Constants
 ------------------------------------------------*/
	/**
	 * Possible types of plugins.
	 * Naming convention: TYPE.GAME-VARIANT.txt
	 * 
	 * DECK - a file describing a deck of cards
	 * BOARD - a file describing a game board
	 * RULES - a file detailing the rules of a game
	 */
	public static enum Type { DECK, BOARD, RULES }
	
	/** Directory where plugin files are found. */
	public static final File DIRECTORY = new File(System.getProperty("user.dir").concat("/plugin"));
	
	/** Character set used in plugin files. */
	public static final Charset CHARSET = Charset.forName("UTF-8");
	
	/** Locale to be used. */
	public static final Locale LOCALE = Locale.ENGLISH;
	
	/** Special character(s) denoting comments in plugin files. */
	public static final String COMMENT = "#";
	
	/** Text file extension for plugin files. */
	public static final String EXTENSION = ".txt";
	
	/** Delimiter used for plugin files. */
	public static final String DELIMITER = ".";
	
	/** Number of pieces in a Plugin filename. */
	public static final int LENGTH = 3;
	
/*------------------------------------------------
 	Constants and Attributes
 ------------------------------------------------*/	
	private final static String NEW_LINE = System.getProperty("line.separator");
	private final String name;		// Name of the plugin file
	private final Plugin.Type type;	// Typle of plugin
	private final int size;			// Size of the plugin file
	private List<String> lines;		// Each line of the plugin file
	
/*------------------------------------------------
 	Constructor(s)
 ------------------------------------------------*/
	
	/**
	 * Construct a <tt>Plugin</tt> from the given type and name.
	 * Plugins ignore comments, book-end whitespace, and capitalization.
	 * 
	 * @param type	the type of file to be created
	 * @param rawName	raw name of plugin component (no prefix or extension)
	 * @throws PluginException if the plugin does not exist
	 */
	public Plugin(final Plugin.Type type, final String rawName) throws PluginException {
		this(Plugin.createFile(Plugin.createFilename(type, rawName)));
	}
	
	/**
	 *  Construct a <tt>Plugin</tt> from the given filename.
	 * 
	 * @param pluginFilename full plugin filename: type.name.txt
	 * @throws PluginException if the plugin does not exist
	 */
	public Plugin(final String pluginFilename) throws PluginException {
		this(Plugin.createFile(pluginFilename));
	}

	/**
	 * Construct a <tt>Plugin</tt> from the given <tt>File</tt>.
	 * Plugins ignore comments, book-end whitespace, and capitalization.
	 * 
	 * @param file the file to be converted
	 * @throws PluginException on a file IO error
	 */
	public Plugin(final File file) throws PluginException {
		try {
			lines = Files.readAllLines(file.toPath(), Plugin.CHARSET);
			this.name = file.getName();
			
		} catch (IOException | SecurityException e) {
			throw PluginException.create(PluginException.Type.FILE_READ_ERROR, e, this.name);
		}
		this.type = Plugin.getType(this.name);
		
		//Clean up file, bottom to top
		for (int ndx = lines.size()-1; ndx >=0; --ndx) {
			
			// remove leading/trailing whitespace
			lines.set(ndx, lines.get(ndx).trim());
			
			// remove blank lines
			if (lines.get(ndx).equals("") ||
			// remove null lines
				lines.get(ndx) == null ||
			// remove line comments
				lines.get(ndx).startsWith(Plugin.COMMENT)) {
				lines.remove(ndx);
				
			} else {
				// convert to lower case
				lines.set(ndx, lines.get(ndx).toLowerCase());
				
				// remove end of line comments and trailing whitespace
				if (lines.get(ndx).contains(Plugin.COMMENT)) {
					final String[] stripped = lines.get(ndx).split(Plugin.COMMENT);
					lines.set(ndx, stripped[0].trim());
				}
			}
		}
		this.size = lines.size();
	}
	
	/**
	 * Construct a <tt>Plugin</tt> containing elements from the specified <tt>List</tt>.
	 * 
	 * @param lines	A list of strings used to create this plugin
	 * @param name The name of this plugin
	 * @throws PluginException if no keywords are found
	 */
	public Plugin(final List<String> lines, final String name) throws PluginException {
		if (lines.isEmpty()) {
			throw PluginException.create(PluginException.Type.MISSING_KEYWORD, name);
		}
		this.lines = new ArrayList<String>(lines);
		this.size = this.lines.size();
		this.name = name;
		this.type = Plugin.getType(this.name);
	}
	
/*------------------------------------------------
	Accessors
 ------------------------------------------------*/
	/**
	 * Returns the name of this <tt>Plugin</tt>.
	 * 
	 * @return the name of this plugin: type.name.txt
	 */
	public final String getName() {
		return name;
	}
	
	/**
	 * Returns the name of this <tt>Plugin</tt>, stripped of prefix and extension.
	 * 
	 * @return the name of this plugin, stripped of prefix and extension
	 * @throws PluginException if the Plugin name is invalid
	 */
	public final String getRawName() throws PluginException {
		return Plugin.getRawName(this.name);
	}
	
	/**
	 * Gets the converted name.
	 *
	 * @return the converted name
	 * @throws PluginException if the Plugin name is invalid
	 */
	public final String getConvertedName() throws PluginException {
		return Plugin.convertName(this.name);
	}
	
	/**
	 * Return the length of this <tt>Plugin</tt>.
	 * 
	 * @return	the length of this plugin
	 */
	public final int getSize() {
		return size;
	}
	
	/**
	 * Returns the type of this <tt>Plugin</tt>.
	 * 
	 * @return the type of this plugin
	 */
	public final Plugin.Type getType() {
		return type;
	}
	
/*------------------------------------------------
	Utility Methods
 ------------------------------------------------*/
	
	/**
	 * Check if this plugin is of the specified type. is the same.
	 * 
	 * @param type plugin type to test against
	 * @throws PluginException if this plugin does not equal that type
	 */
	public void checkType (final Plugin.Type type) throws PluginException {
		// Make sure this is a rules plugin...
		if (this.type != type) {
			throw PluginException.create(PluginException.Type.INVALID_TYPE, name);
		}
	}
	
	/**
	 * Return the index of the first occurrence of the keyword, or -1 if not found.
	 * 
	 * @param keyword	keyword to be located
	 * @return	index 	of the keyword
	 */
	public int getIndexOf(final String keyword) {
		return this.getIndexOf(keyword, 0);
	}
	
	/**
	 * Return the index of the next occurrence of the keyword, or -1 if not found.
	 * 
	 * @param keyword	keyword to be located
	 * @param startNdx	file index to start searching for keyword
	 * @return	index of the keyword, or -1 if not found.
	 */
	public int getIndexOf(final String keyword, final int startNdx) {
		int index = -1;
		for (int ndx = startNdx; ndx < size; ++ndx) {
			if (lines.get(ndx).startsWith(keyword)) {
				index = ndx;
				break;
			}
		}
		return index;
	}
	
	/**
	 * Return the index of the first occurrence of the keyword, or -1 if not found.
	 * 
	 * @param keyword	keyword to be located
	 * @return	index of the keyword, or -1 if not found.
	 * @throws PluginException	if the keyword is not found
	 */
	public int checkIndexOf(final String keyword) throws PluginException {
		final int index = this.getIndexOf(keyword);
		if (index < 0) {
			throw PluginException.create(PluginException.Type.MISSING_KEYWORD, name, keyword);
		}
		return index;
	}
	
	/**
	 * Returns the number of occurrences of a line starting with the specified words.
	 * 
	 * @param words	the string to be located
	 * @return number of occurrences
	 */
	public int getNumberOf(final String words) {
		int index = 0;
		int num = 0;
		do {
			index = this.getIndexOf(words, index);
			if (index >= 0) {
				num++;
			}
		} while (index++ > 0);
		return num;
	}
	
	/**
	 * Returns the parameter(s) found after the first occurrence of the specified keyword.
	 * 
	 * @param keyword	keyword to be searched
	 * @return	parameters found after the specified keyword
	 */
	public String getParams(final String keyword) {
		return this.getParams(keyword, 0);
	}
	
	/**
	 * Returns the parameter(s) found after the specified keyword.
	 * 
	 * @param keyword	keyword to be searched
	 * @param startNdx	line index to start searching for keyword
	 * @return	parameters found after the specified keyword
	 */
	public String getParams(final String keyword, final int startNdx) {
		final int ndx = this.getIndexOf(keyword, startNdx);
		if (ndx >= 0) {
			final String[] words = lines.get(ndx).split("\\s", 2);
			return words[1];
		}
		return null;
	}
	
	/**
	 * Returns the parameter(s) found after the first occurrence of the specified keyword.
	 * 
	 * @param keyword	keyword to be searched
	 * @return	parameters found after the specified keyword
	 * @throws PluginException	if the keyword or parameters are not found
	 */
	public String checkParams(final String keyword) throws PluginException {
		this.checkIndexOf(keyword);
		final String s = this.getParams(keyword);
		if (s == null) {
			throw PluginException.create(PluginException.Type.INVALID_PARAMETER, name, keyword);
		}
		return s;
	}
	
	/**
	 * Returns an array of the comma separated parameter(s) found after the first occurrence of the specified keyword.
	 * 
	 * @param keyword	keyword to be searched
	 * @return	parameters found after the specified keyword
	 */
	public String[] getCSVParams(final String keyword) {
		return this.getCSVParams(keyword, 0);
	}
	
	/**
	 * Returns an array of the comma separated parameter(s) found after the specified keyword.
	 *
	 * @param keyword keyword to be searched
	 * @param startNdx the start ndx
	 * @return parameters found after the specified keyword
	 */
	public String[] getCSVParams(final String keyword, final int startNdx) {
		final String params = this.getParams(keyword, startNdx);
		return params == null ? null : params.split(",");
	}
	
	/**
	 * Returns an array of the comma separated parameter(s) found after the specified keyword.
	 * 
	 * @param keyword	keyword to be searched
	 * @return	parameters found after the specified keyword
	 * @throws PluginException if the keyword or parameters are not found
	 */
	public String[] checkCSVParams(final String keyword) throws PluginException {
		return this.checkParams(keyword).split(",");
	}
	
	/**
	 * Returns a String representing the specified line of the plugin file.
	 *
	 * @param index the index
	 * @return String representing the specified line of the file
	 * @throws IndexOutOfBoundsException the index out of bounds exception
	 * @throw IndexOutOfBoundsException - if the index is out of range (index < 0 || index >= size())
	 */
	public String getLine(final int index) throws IndexOutOfBoundsException {
		return lines.get(index);
	}
	
	/**
	 * Returns a new <tt>Plugin</tt> from the specified start and ending indices [s,e).
	 * Returns <tt>null</tt> if the start index is invalid.
	 * Use minimum of endNdx and size of this file copy for end index.
	 *
	 * @param startNdx first line of new file copy
	 * @param endNdx where to stop dividing this file copy
	 * @return new file copy from the specified start and ending indices
	 * @throws PluginException if no keywords are found
	 * @throws IndexOutOfBoundsException the index out of bounds exception
	 * @throws IllegalArgumentException the illegal argument exception
	 * @throw IndexOutOfBoundsException if either index is out of range (index < 0 || index >= size())
	 * @throw IllegalArgumentException if the start index is larger than the end index
	 */
	public Plugin divide(final int startNdx,final  int endNdx) throws PluginException, IndexOutOfBoundsException, IllegalArgumentException {
		final List<String> list = new ArrayList<String>(endNdx - startNdx);
		for (int i = startNdx; i < endNdx; i++) {
			list.add(this.getLine(i));
		}
		return new Plugin(list, this.name);
	}
	
/*------------------------------------------------
	Overridden Methods
 ------------------------------------------------*/
	/**
 * Return all lines of this plugin as a string.
 *
 * @return the string
 */
	@Override public String toString() {
		final StringBuilder str = new StringBuilder();
		for (final String line : lines) {
			str.append(line).append(NEW_LINE);
		}
		return str.toString();
	}
	
/*------------------------------------------------
	Static Utility Methods
 ------------------------------------------------*/
	
	/**
	 * Return a <tt>File</tt> using the given type and name if it exists.
	 * 
	 * @param type	the type of file to be created
	 * @param rawName	raw name of plugin component (no prefix or extension)
	 * @return	file using the given type and game if it exists.
	 * @throws PluginException if the plugin does not exist
	 */
	public static final File createFile(final Plugin.Type type, final String rawName) throws PluginException {
		return Plugin.createFile(Plugin.createFilename(type, rawName));
	}
	
	/**
	 * Return a <tt>File</tt> using the given filename if it exists.
	 * 
	 * @param pluginFilename full plugin filename: type.name.txt
	 * @return	file using the given filename
	 * @throws PluginException if the plugin does not exist
	 */
	public static final File createFile(final String pluginFilename) throws PluginException {
		final File file = new File(Plugin.DIRECTORY, pluginFilename);
		if (!file.exists()) {
			throw PluginException.create(PluginException.Type.DOES_NOT_EXIST);
		}
		return file;
	}
	
	/**
	 * Returns a valid filename (without full path) from the given type and name.
	 *
	 * @param type the type of plugin filename to be created
	 * @param rawName raw name of plugin component (no prefix or extension)
	 * @return plugin filename (without full path)
	 */
	public static String createFilename(final Plugin.Type type, final String rawName) {
		final StringBuilder str = new StringBuilder();
		str.append(type.toString().toLowerCase(Plugin.LOCALE))
		   .append('.').append(rawName).append(Plugin.EXTENSION);
		return str.toString();
	}
	
	/**
	 * Returns the type of a plugin file.
	 * 
	 * @param pluginFilename full plugin filename: type.name.txt
	 * @return type of a plugin file
	 * @throws PluginException if the plugin is an incompatible type
	 */
	public static final Plugin.Type getType(final String pluginFilename) throws PluginException {
		Plugin.Type t = null;
		try {
			t = Plugin.Type.valueOf(Plugin.getTypePrefix(pluginFilename).toUpperCase());
		} catch (IllegalArgumentException e) {
			throw PluginException.create(PluginException.Type.INVALID_TYPE, e, pluginFilename);
		}
		return t;
	}
	
	/**
	 * Returns the type of a plugin file.
	 * 
	 * @param pluginFile full plugin filename: type.name.txt
	 * @return type of a plugin file
	 * @throws PluginException if the plugin is an incompatible type
	 */
	public static final Plugin.Type getType(final File pluginFile) throws PluginException {
		return Plugin.getType(pluginFile.getName());
	}
	
	/**
	 * Returns the name of a plugin file as a string, without the type prefix or extension.
	 * 
	 * @param pluginFilename full plugin filename: type.name.txt
	 * @return name of a plugin file, without the type prefix or extension.
	 * @throws PluginException if the plugin is named incorrectly
	 */
	public static final String getRawName(final String pluginFilename) throws PluginException {
		return Plugin.split(pluginFilename)[1];
	}
	
	/**
	 * Returns the type of a plugin file as a string.
	 * 
	 * @param pluginFilename full plugin filename: type.name.txt
	 * @return the plugin prefix - a string representing the plugin type
	 * @throws PluginException if the plugin is named incorrectly
	 */
	public static final String getTypePrefix(final String pluginFilename) throws PluginException {
		return Plugin.split(pluginFilename)[0];
	}
	
	/**
	 * Returns the name of a plugin file, stripped of its prefix and extension.
	 * 
	 * @param pluginFilename full plugin filename: type.name.txt
	 * @return the name of a plugin file, stripped of its prefix and extension.
	 * @throws PluginException if the plugin is named incorrectly
	 */
	public static String[] split(final String pluginFilename) throws PluginException {
		final String str[] = pluginFilename.split("\\.");
		if (str.length != Plugin.LENGTH) {
			throw PluginException.create(PluginException.Type.INVALID_NAME, pluginFilename);
		}
		return str;
	}
	
	/**
	 * Returns <tt>true</tt> if the specified filename does not contain a prefix or extension.
	 * 
	 * @param filename	the filename to test
	 * @return true if filename does not contain a prefix or extension
	 */
	public static boolean isRawName(final String filename) {
		return !filename.contains(Plugin.DELIMITER);
	}
	
	/**
	 * Returns the specified filename with capitalization and spaces.
	 * 
	 * @param filename name of the file to convert (full or raw)
	 * @return a more visually appealing version of the filename
	 * @throws PluginException if the plugin is named incorrectly
	 */
	public static String convertName(final String filename) throws PluginException {
		String name = Plugin.isRawName(filename) ? filename : Plugin.getRawName(filename); 
		name = name.toLowerCase(Plugin.LOCALE).replaceAll("_", " ").replaceAll("-", " - ");
		final String[] words = filename.split("\\s");
		final StringBuilder str = new StringBuilder();
		for (final String word : words) {
			str.append(Character.toUpperCase(word.charAt(0)))
			   .append(word.substring(1)).append(' ');
		}
		str.setLength(str.length() - 1);
		return str.toString();
	}
	
	/**
	 * Returns an array of abstract pathnames denoting the files of the specified type found in the "plugin" directory.
	 * 
	 * @param type	the <code>FileIO.Type</code> of file to include
	 * @return	array of abstract pathnames
	 */
	public static File[] listFiles(final Plugin.Type type) {
		return Plugin.DIRECTORY.listFiles(new PluginFilenameFilter(type));
	}
	
	/**
	 * Returns an array of strings denoting the files of the specified type found in the "plugin" directory.
	 * 
	 * @param type	the <code>FileIO.Type</code> of file to include
	 * @return	array of abstract pathnames
	 */
	public static String[] listFilenames(final Plugin.Type type) {
		return Plugin.DIRECTORY.list(new PluginFilenameFilter(type));
	}
	
/**
 * The Class PluginFilenameFilter.
 */
/* **********************************************************************
 * Nested Private class: SpecificFilenameFilter
 * - Used to create FilenameFilters specific to a file type
 ************************************************************************/
	private static final class PluginFilenameFilter implements FilenameFilter {
		
		private final String prefix;	// The type of plugin file
		
		/**
		 * Constructs a new <tt>PluginFilenameFilter</tt>.
		 * 
		 * @param type the type of plugin being filtered
		 */
		private PluginFilenameFilter(final Plugin.Type type) {
			prefix = type.toString().toLowerCase(Plugin.LOCALE);
		}

		/**
		 * Returns <tt>true</tt> if the specified parameters are accepted.
		 * 
		 * @param dir the directory in which the file was found
		 * @param name the name of the file
		 * @returns <tt>true</tt> if and only if the name should be included in the file list
		 * @see java.io.FilenameFilter#accept(java.io.File, java.lang.String)
		 * 
		 */
		@Override public boolean accept(final File dir, final String name) {
			final String nameLC = name.toLowerCase(Plugin.LOCALE);
			return 	nameLC.startsWith(prefix) &&
					nameLC.endsWith(Plugin.EXTENSION) &&
					!(	nameLC.contains("template") || 
						nameLC.contains("test"));
		}
	}
}
