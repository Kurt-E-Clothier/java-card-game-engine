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

public enum Strings {
	
	/** New Line operator for printing */
	NEW_LINE(System.getProperty("line.separator"));
	
	private final String string;
	
	private Strings(final String string) {
		this.string = string;
	}
	
	@Override public String toString() {
		return string;
	}
}
