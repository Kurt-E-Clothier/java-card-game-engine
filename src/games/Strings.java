/***********************************************************************//**
* @file			Strings.java
* @author		Kurt E. Clothier
* @date			November 19, 2015
*
* @breif		String constants for public use
*
* @pre			Compiler: Eclipse - Mars Release (4.5.0)
* @pre			Java: JRE 7 or greater
*
* @see			http://www.projectsbykec.com/
* @see			PlayingCard
*
* @copyright	The MIT License (MIT) - see LICENSE.txt
****************************************************************************/
package games;

import java.nio.charset.Charset;
import java.util.Locale;

public enum Strings {
	
	/** New Line operator for printing */
	NEW_LINE(System.getProperty("line.separator"));
	
	/** Character set to be used. */
	public static final Charset CHARSET = Charset.forName("UTF-8");
	/** Locale to be used. */
	public static final Locale LOCALE = Locale.ENGLISH;
	
	/** Commonly used RegEx and search patterns */
	public static enum Pattern {
		/** Empty string */
		BLANK (""),
		/** Literal String "null" */
		NULL ("null"),
		/** Single comma */
		COMMA (","),
		/** Period pattern, with escapes */
		PERIOD ("\\."),
		/** Whitespace pattern, with escapes */
		WHITESPACE ("\\s");
		
		private final String pattern;
		
		/* Construct this plugin pattern. */
		private Pattern(final String pattern) {
			this.pattern = pattern;
		}
		
		/** Returns the string represented by this pattern. */
		@Override public String toString() {
			return pattern;
		}
	}
	
	private final String string;
	
	private Strings(final String string) {
		this.string = string;
	}
	
	/**
	 * Returns the specified string converted to spaced CamelCase words.
	 * 
	 * @param string the string to be converted
	 * @return the specified string converted to spaced CamelCase words.
	 */
	public static String toCamelCase(final String string) {
		if (string.isEmpty()) {
			return "";
		}
		final String[] words = string.toLowerCase(Strings.LOCALE)
									 .split(Strings.Pattern.WHITESPACE.toString());
		final StringBuilder str = new StringBuilder();
		for (final String word : words) {
			str.append(Strings.capitalizeFirstLetter(word)).append(' ');
		}
		str.setLength(str.length() - 1);
		return str.toString();
	}
	
	/**
	 * Returns the specified string with only the first letter capitalized, all others lower case.
	 * 
	 * @param string the string to be converted
	 * @return the specified string with only the first letter capitalized, all others lower case
	 */
	public static String capitalizeFirstLetter(final String string) {
		if (string.isEmpty()) {
			return "";
		}
		final String s = string.toLowerCase(Strings.LOCALE);
		final StringBuilder str = new StringBuilder();
		str.append(Character.toUpperCase(s.charAt(0)))
		   .append(s.substring(1));
		return str.toString();
	}
	
	@Override public String toString() {
		return string;
	}
}
