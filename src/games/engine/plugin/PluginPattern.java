/***********************************************************************//**
* @file			PluginPattern.java
* @author		Kurt E. Clothier
* @date			November 18, 2015
* 
* @breif		String patterns used with Plugins
*
* @pre			Compiler: Eclipse - Mars Release (4.5.0)
* @pre			Java: JRE 7 or greater
*
* @see			http://www.projectsbykec.com/
*
* @copyright	The MIT License (MIT) - see LICENSE.txt
****************************************************************************/
package games.engine.plugin;

public enum PluginPattern {
	
	/** Empty string */
	BLANK (""),
	
	/** Literal String "Null" */
	NULL ("null"),
	
	/** Single comma */
	COMMA (","),
	
	/** Dash, Hyphen */
	DASH ("-"),
	
	/** Period pattern, with escapes */
	PERIOD ("\\."),
	
	/** Whitespace pattern, with escapes */
	WHITESPACE ("\\s"),
	
	/** Comment character in plugins */
	COMMENT ("#"),
	
	/** Parameter delimiter used in plugin files. */
	DELIMITER (".");
	
	private final String pattern;
	
	/*
	 * Construct this plugin pattern.
	 */
	private PluginPattern(final String pattern) {
		this.pattern = pattern;
	}
	
	/**
	 * Returns the string represented by this plugin pattern.
	 * 
	 * @return string represented by this plugin pattern
	 */
	@Override public String toString() {
		return pattern;
	}

}
