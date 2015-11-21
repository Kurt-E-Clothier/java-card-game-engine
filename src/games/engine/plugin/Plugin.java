/***********************************************************************//**
* @file			Plugin.java
* @author		Kurt E. Clothier
* @date			November 20, 2015
* 
* @breif		Component data from a game plugin file
*
* @pre			Compiler: Eclipse - Mars Release (4.5.0)
* @pre			Java: JRE 7 or greater
*
* @see			http://www.projectsbykec.com/
*
* @copyright	The MIT License (MIT) - see LICENSE.txt
****************************************************************************/

package games.engine.plugin;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import games.Strings;

/******************************************************************//**
 * The Plugin Class
 * - A representation of the component data contained in a game plugin file
 * - Plugin is quasi-immutable
 * - Also contains various static utilities for handing plugin files
 * Issues
 * - This is likely a "god class," but the implementation makes sense
 * 		^ This could be fixed by splitting off the static methods
 * 		^ All non-static methods are important plugin utilities
 ********************************************************************/
public class Plugin implements Serializable {

	private static final long serialVersionUID = 3393999785215891222L;

/*------------------------------------------------
 	Global Constants
 ------------------------------------------------*/
	/** Possible types of plugins; Naming convention: TYPE.NAME.txt */
	public static enum Type { 
		/** Describes a deck of cards */
		DECK, 
		/** Describes a  game board */
		BOARD, 
		/** Describes the rules of a game */
		RULES }
	
	/** Directory where plugin files are found. */
	public static final File DIRECTORY = new File(System.getProperty("user.dir").concat("/plugin"));
	/** Character set used in plugin files. */
	public static final Charset CHARSET = Charset.forName("UTF-8");
	/** Locale to be used. */
	public static final Locale LOCALE = Locale.ENGLISH;
	/** Number of pieces in a Plugin filename. */
	public static final int LENGTH = 3;
	/** Extension used in plugin files */
	public static final String EXTENSION = "txt";
	
/*------------------------------------------------
 	Constants and Attributes
 ------------------------------------------------*/
	private final PluginFilename filename;	// PluginFile used to create this Plugin
	private final List<String> lines;		// Each line of the plugin file
	
/*------------------------------------------------
 	Constructor(s)
 ------------------------------------------------*/
	/**
	 * Construct a <tt>Plugin</tt> from the given type and name.
	 * Plugins ignore comments, book-end whitespace, and capitalization.
	 * 
	 * @param type the type of file to be used
	 * @param rawName raw name of plugin component (no prefix or extension)
	 * @throws PluginException if the plugin does not exist
	 */
	public Plugin(final Plugin.Type type, final String rawName) throws PluginException {
		this(new PluginFilename(type, rawName));
	}
	
	/**
	 *  Construct a <tt>Plugin</tt> from the given filename.
	 *  Plugins ignore comments, book-end whitespace, and capitalization.
	 * 
	 * @param filename full plugin filename: type.name.txt
	 * @throws PluginException if the plugin does not exist
	 */
	public Plugin(final String filename) throws PluginException {
		this(new PluginFilename(filename));
	}
	
	/**
	 *  Construct a <tt>Plugin</tt> from the given <tt>PluginFilename</tt>.
	 *  Plugins ignore comments, book-end whitespace, and capitalization.
	 * 
	 * @param pluginFilename Plugin Filename to use
	 * @throws PluginException if the plugin does not exist
	 */
	public Plugin(final PluginFilename pluginFilename) throws PluginException {
		this(new PluginFile(pluginFilename));
	}
	
	/**
	 *  Construct a <tt>Plugin</tt> from the given <tt>PluginFile</tt>.
	 *  Plugins ignore comments, book-end whitespace, and capitalization.
	 * 
	 * @param pluginFile Plugin File to use
	 * @throws PluginException if the plugin does not exist or is empty
	 */
	public Plugin(final PluginFile pluginFile) throws PluginException {
		this(readPluginFile(pluginFile), pluginFile.getFilename());
	}
	
	/**
	 * Construct a <tt>Plugin</tt> containing elements from the specified <tt>List</tt>.
	 * Plugins ignore comments, book-end whitespace, and capitalization.
	 * 
	 * @param lines	A list of strings used to create this plugin
	 * @param filename The PluginFilename to use
	 * @throws PluginException if no keywords are found
	 */
	public Plugin(final List<String> lines, final PluginFilename pluginFilename) throws PluginException {
		if (lines.isEmpty()) {
			throw PluginException.create(PluginException.Type.MISSING_KEYWORD, pluginFilename);
		}
		this.lines = new ArrayList<String>(lines);
		this.filename = pluginFilename;
		//Clean up file, bottom to top
		for (int ndx = this.lines.size()-1; ndx >=0; --ndx) {
			// remove leading/trailing whitespace
			this.lines.set(ndx, this.lines.get(ndx).trim());
			// remove blank lines, null lines, and line comments
			if (this.lines.get(ndx).equals(PluginPattern.BLANK.toString()) ||
				this.lines.get(ndx) == null ||
				this.lines.get(ndx).startsWith(PluginPattern.COMMENT.toString())) {
				this.lines.remove(ndx);
			// convert to lower case and remove end of line comments and trailing whitespace
			} else {
				this.lines.set(ndx, this.lines.get(ndx).toLowerCase());
				if (this.lines.get(ndx).contains(PluginPattern.COMMENT.toString())) {
					final String[] stripped = this.lines.get(ndx).split(PluginPattern.COMMENT.toString());
					this.lines.set(ndx, stripped[0].trim());
				}
			}
		}
	}
	
	/*
	 * Create and return a List of Strings found in the PluginFile.
	 */
	private static List<String> readPluginFile(final PluginFile pluginFile) throws PluginException {
		List<String> list;	
		try {
			list = Files.readAllLines(pluginFile.toPath(), Plugin.CHARSET);
			
		} catch (IOException | SecurityException e) {
			throw PluginException.create(PluginException.Type.FILE_READ_ERROR, e, pluginFile.getFilename());
		}
		return list;
	}
	
/*------------------------------------------------
	Utility Methods
 ------------------------------------------------*/
	/**
	 * Return the length of this <tt>Plugin</tt>.
	 * 
	 * @return	the length of this plugin
	 */
	public final int getSize() {
		return lines.size();
	}
	
	/** 
	 * Returns the <tt>PluginFilename</tt> for this <tt?PLugin</tt>.
	 * 
	 * @return the <tt>PluginFilename</tt> for this <tt?PLugin</tt>
	 */
	public final PluginFilename getFilename() {
		return filename;
	}

	/**
	 * Check if the name parameter of this <tt>Plugin</tt> is correct.
	 * Returns the name parameter if so.
	 * 
	 * @return the name parameter if it is correct
	 * @throws PluginException if the name of this Plugin is incorrect
	 */
	public String checkName() throws PluginException {
		final String nameParam = this.checkParamsFor(PluginKeyword.NAME);
		if (!nameParam.equals(filename.getRawName())) {
			throw PluginException.create(PluginException.Type.INVALID_PARAMETER, filename, PluginKeyword.NAME.toString(), nameParam);
		}
		return nameParam;
	}
	
	/**
	 * Check if this plugin is of the specified type. is the same.
	 * 
	 * @param type plugin type to test against
	 * @throws PluginException if this plugin does not equal that type
	 */
	public void checkType (final Plugin.Type type) throws PluginException {
		if (filename.getType() != type) {
			throw PluginException.create(PluginException.Type.INVALID_TYPE, filename);
		}
	}
	
	/**
	 * Returns a String representing the specified line of the plugin file.
	 *
	 * @param index the index
	 * @return String representing the specified line of the file
	 * @throws IndexOutOfBoundsException - if the index is out of range (index < 0 || index >= size())
	 */
	public String getLine(final int index) throws IndexOutOfBoundsException {
		return lines.get(index);
	}
	
	/**
	 * Returns a new <tt>Plugin</tt> from the specified start and ending indices [s,e).
	 *
	 * @param startNdx first line of new file copy
	 * @param endNdx where to stop dividing this file copy
	 * @return new plugin from the specified start and ending indices
	 * @throws PluginException if no keywords are found
	 * @throws IndexOutOfBoundsException if either index is out of range (index < 0 || index >= size())
	 * @throws IllegalArgumentException if the start index is larger than the end index
	 */
	public Plugin divide(final int startNdx, final  int endNdx) throws PluginException, IndexOutOfBoundsException, IllegalArgumentException {
		final List<String> list = new ArrayList<String>(endNdx - startNdx);
		for (int i = startNdx; i < endNdx; i++) {
			list.add(this.getLine(i));
		}
		return new Plugin(list, filename);
	}
	
	/**
	 * Returns a new <tt>Plugin</tt> between the specified start and ending indices (s,e).
	 *
	 * @param star keyword to start the divide
	 * @param end keyword to end the divide
	 * @return new plugin between the specified start and end keyword
	 * @throws PluginException if the keywords are not found
	 */
	public Plugin divide(final PluginKeyword start, final PluginKeyword end) throws PluginException {
		final int startNdx = this.checkIndexOf(start) + 1;
		final int endNdx = this.checkIndexOf(end);
		return this.divide(startNdx, endNdx);
	}
	
	/**
	 * Returns a <tt>PluginKeywordCode</tt> by comparing the the specified keywords.
	 * NEITHER_EXISTS - neither keyword exists in this plugin
	 * A_EXISTS - A exists, B does not
	 * B_EXISTS - B exists, A does not
	 * A_GREATER - Both exist, A has a greater index
	 * B_GREATER - Both exist, B has a greater index
	 * EQUAL - Both exist, A and B are the same keyword
	 *  
	 * @param A keyword A
	 * @param B keyword B
	 * @return a <tt>PluginKeywordCode</tt> by comparing the the specified keywords
	 */
	public PluginKeywordCode compare(final PluginKeyword A, final PluginKeyword B) {
		PluginKeywordCode code = null;
		final int indexA = this.getIndexOf(A);
		final int indexB = this.getIndexOf(B);
		if (indexA < 0) {
			if (indexB < 0 ) {
				code = PluginKeywordCode.NEITHER_EXISTS;
			}
			else {
				code = PluginKeywordCode.B_EXISTS;
			}
		}
		else {
			if (indexB < 0 ) {
				code = PluginKeywordCode.A_EXISTS;
			}
			else if (indexA > indexB) {
				code = PluginKeywordCode.A_GREATER;
			}
			else if (indexB > indexA) {
				code = PluginKeywordCode.B_GREATER;
			}
			else {
				code = PluginKeywordCode.EQUAL;
			}
		}
		return code;
	}
	
/*------------------------------------------------
	Finding Occurrence of a String or Keyword
 ------------------------------------------------*/
	/**
	 * Returns <tt>true</tt> if the specified keyword exists in this <tt>Plugin</tt>.
	 * 
	 * @param keyword the keyword to be located
	 * @return <tt>true</tt> if the specified keyword exists in this <tt>Plugin</tt>
	 */
	public boolean contains (final PluginKeyword keyword) {
		return (this.getIndexOf(keyword) >= 0);
	}
	
	/**
	 * Returns <tt>true</tt> if the specified string exists in this <tt>Plugin</tt>.
	 * 
	 * @param string the string to be located
	 * @return <tt>true</tt> if the specified string exists in this <tt>Plugin</tt>
	 */
	public boolean conatins (final String string) {
		return (this.getIndexOf(string, 0) >= 0);
	}
	
	/**
	 * Returns the index of the first occurrence of the keyword, or -1 if not found.
	 * Throws an exception is the keyword is not found.
	 * 
	 * @param keyword	keyword to be located
	 * @return	index of the keyword, or -1 if not found.
	 * @throws PluginException	if the keyword is not found
	 */
	public int checkIndexOf(final PluginKeyword keyword) throws PluginException {
		final int index = this.getIndexOf(keyword);
		if (index < 0) {
			throw PluginException.create(PluginException.Type.MISSING_KEYWORD, filename, keyword.toString());
		}
		return index;
	}
	
	/**
	 * Returns the index of the first occurrence of the keyword, or -1 if not found.
	 * 
	 * @param keyword	keyword to be located
	 * @return	index 	of the keyword
	 */
	public int getIndexOf(final PluginKeyword keyword) {
		return this.getIndexOf(keyword, 0);
	}
	
	/**
	 * Returns the index of the next occurrence of the keyword, or -1 if not found.
	 * 
	 * @param keyword	keyword to be located
	 * @param startNdx	file index to start searching for keyword
	 * @return	index of the keyword, or -1 if not found.
	 */
	public int getIndexOf(final PluginKeyword keyword, final int startNdx) {
		return this.getIndexOf(keyword.toString(), startNdx);
	}
	
	/**
	 * Returns the index of the next occurrence of the string, or -1 if not found.
	 * 
	 * @param string string to be located
	 * @param startNdx file index to start searching for keyword
	 * @return index of the keyword, or -1 if not found.
	 */
	public int getIndexOf(final String string, final int startNdx) {
		int index = -1;
		for (int ndx = startNdx; ndx < lines.size(); ++ndx) {
			if (lines.get(ndx).startsWith(string)) {
				index = ndx;
				break;
			}
		}
		return index;
	}
	
	/**
	 * Returns the number of occurrences of a line starting with the specified keyword.
	 * 
	 * @param keyword the keyword to be located
	 * @return number of occurrences
	 */
	public int getNumberOf(final PluginKeyword keyword) {
		return this.getNumberOf(keyword.toString());
	}
	
	/**
	 * Returns the number of occurrences of a line starting with the specified string.
	 * 
	 * @param string the string to be located
	 * @return number of occurrences
	 */
	public int getNumberOf(final String string) {
		int index = 0;
		int num = 0;
		do {
			index = this.getIndexOf(string, index);
			if (index >= 0) {
				num++;
			}
		} while (index++ > 0);
		return num;
	}
	
/*------------------------------------------------
	Getting the Parameters after a String or Keyword
 ------------------------------------------------*/
	/**
	 * Returns the parameter(s) found after the first occurrence of the specified keyword.
	 * Throws an exception if the keyword is not found or is missing parameters.
	 * 
	 * @param keyword	keyword to be searched
	 * @return	parameters found after the specified keyword
	 * @throws PluginException	if the keyword or parameters are not found
	 */
	public String checkParamsFor(final PluginKeyword keyword) throws PluginException {
		this.checkIndexOf(keyword);
		final String s = this.getParamsFor(keyword);
		if (s == null || s.equals(PluginPattern.BLANK.toString()) ||
						 s.equals(PluginPattern.NULL.toString())) {
			throw PluginException.create(PluginException.Type.MISSING_PARAMETER, filename, keyword.toString());
		}
		return s;
	}
	
	/**
	 * Returns the parameter(s) found after the first occurrence of the specified keyword.
	 * 
	 * @param keyword	keyword to be searched
	 * @return	parameters found after the specified keyword
	 */
	public String getParamsFor(final PluginKeyword keyword) {
		return this.getParamsFor(keyword, 0);
	}
	
	/**
	 * Returns the parameter(s) found after the specified keyword.
	 * 
	 * @param keyword	keyword to be searched
	 * @param startNdx	line index to start searching for keyword
	 * @return	parameters found after the specified keyword
	 */
	public String getParamsFor(final PluginKeyword keyword, final int startNdx) {
		return this.getParamsFor(keyword.toString(), startNdx);
	}
	
	/**
	 * Returns the parameter(s) found after the specified keyword.
	 * Returns "" if keyword is not found.
	 * 
	 * @param string string to be located
	 * @param startNdx	line index to start searching for keyword
	 * @return	parameters found after the specified keyword
	 */
	public String getParamsFor(final String string, final int startNdx) {
		final int ndx = this.getIndexOf(string, startNdx);
		if (ndx >= 0) {
			final String[] words = lines.get(ndx).split(PluginPattern.WHITESPACE.toString(), 2);
			if (words.length > 1) {
				return words[1];
			}
		}
		return PluginPattern.BLANK.toString();
	}
	
/*------------------------------------------------
	Get and Split parameters after a string or keyword
 ------------------------------------------------*/
	/**
	 * Returns an array of the comma separated parameter(s) found after the first occurrence of the specified keyword.
	 * 
	 * @param keyword	keyword to be searched
	 * @return	parameters found after the specified keyword
	 */
	public String[] getCSVParamsFor(final PluginKeyword keyword) {
		return this.getCSVParamsFor(keyword, 0);
	}
	
	/**
	 * Returns an array of the comma separated parameter(s) found after the specified keyword.
	 *
	 * @param keyword keyword to be searched
	 * @param startNdx the start ndx
	 * @return parameters found after the specified keyword
	 */
	public String[] getCSVParamsFor(final PluginKeyword keyword, final int startNdx) {
		final String params = this.getParamsFor(keyword, startNdx);
		return params.equals(PluginPattern.BLANK.toString()) ? new String[0] : params.split(PluginPattern.COMMA.toString());
	}
	
	/**
	 * Returns an array of the comma separated parameter(s) found after the specified keyword.
	 * 
	 * @param keyword	keyword to be searched
	 * @return	parameters found after the specified keyword
	 * @throws PluginException if the keyword or parameters are not found
	 */
	public String[] checkCSVParamsFor(final PluginKeyword keyword) throws PluginException {
		return this.checkParamsFor(keyword).split(PluginPattern.COMMA.toString());
	}
	
/*------------------------------------------------
	Overridden Methods
 ------------------------------------------------*/
	/**
	 * Return all lines of this plugin as a string.
	 *
	 * 	@return the string
 	 */
	@Override public String toString() {
		final StringBuilder str = new StringBuilder();
		str.append("File: ").append(filename.toString()).append(Strings.NEW_LINE.toString());
		for (final String line : lines) {
			str.append(line).append(Strings.NEW_LINE.toString());
		}
		return str.toString();
	}
}