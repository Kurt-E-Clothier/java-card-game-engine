/***********************************************************************//**
* @file			CardPileLayout.java
* @author		Kurt E. Clothier
* @date			November 19, 2015
*
* @breif		Layout for card piles
*
* @pre			Compiler: Eclipse - Mars Release (4.5.0)
* @pre			Java: JRE 7 or greater
*
* @see			http://www.projectsbykec.com/
* @see			PlayingCard
*
* @copyright	The MIT License (MIT) - see LICENSE.txt
****************************************************************************/
package games.engine.util;

import java.io.Serializable;
import java.util.Arrays;

import games.Strings;

public final class CardPileLayout implements Serializable {

	private static final long serialVersionUID = 5177506914312246590L;

	/** Fillers are used to space out card piles. */
	public static enum Filler {
		/** expands as needed, pushes piles as far as possible */
		GLUE, 
		/** single rigid area, half of the width of a playing card */
		SPACE;
		
		/**
		 * Returns <tt>true</tt> if the specified string is a <tt>Filler</tt>.
		 * 
		 * @param string the string to test
		 * @return <tt>true</tt> if the specified string is a <tt>Filler</tt>
		 */
		public static boolean contains(final String string) {
			boolean bool = false;
			for (Filler f : Filler.values()) {
				if (f.toString().equalsIgnoreCase(string)) {
					bool = true;
					break;
				}
			}
			return bool;
		}
	}
	
	private final String[] lines;
	
	/**
	 * Constructs a new <tt>CardPileLayout</tt> with the Strings.
	 * 
	 * @param lines Strings to put in this layout
	 */
	public CardPileLayout (final String[] lines) {
		if (lines == null || lines.length == 0) {
			this.lines = new String[0];
		}
		else {
			this.lines = Arrays.copyOf(lines, lines.length);
		}
	}
	
/*------------------------------------------------
    Accessors
 ------------------------------------------------*/
	/**
	 * Returns the size of this <tt>CardPileLayout</tt>.
	 * 
	 * @return the number of lines in this layout
	 */
	public int getSize() {
		return lines.length;
	}
	
	/**
	 * Returns a String representing the specified line of this layout.
	 *
	 * @param index the index
	 * @return String representing the specified line of this layout
	 * @throws IndexOutOfBoundsException - if the index is out of range (index < 0 || index >= size())
	 */
	public String getLine(final int index) throws IndexOutOfBoundsException {
		return lines[index];
	}
	
	/**
	 * Returns an array of the words on the specified line of this layout.
	 *
	 * @param index the index
	 * @return array of the words on the specified line of this layout
	 * @throws IndexOutOfBoundsException - if the index is out of range (index < 0 || index >= size())
	 */
	public String[] splitLine(final int index) throws IndexOutOfBoundsException {
		return lines[index].split("\\s");
	}
	
/*------------------------------------------------
    Overridden Methods
 ------------------------------------------------*/
	/**
	 * Return information about this <tt>CardPileLayout</tt>.
	 * 
	 * @return information about this CardPileLayout
	 */
	@Override  public String toString() {
		final StringBuilder str = new StringBuilder();
		int i = 0;
		while(i < lines.length) {
			str.append(lines[i]);
			if (++i < lines.length) {
				str.append(Strings.NEW_LINE.toString());
			}
		}
		return str.toString();
	}	
}

