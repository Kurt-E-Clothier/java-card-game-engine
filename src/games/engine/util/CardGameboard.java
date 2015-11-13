/***********************************************************************//**
* @file			CardGameboard.java
* @author		Kurt E. Clothier
* @date			November 11, 2015
*
* @breif		Gameboard used in a card game
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

// TODO: Auto-generated Javadoc
/**
 * **********************************************************************
 * The CardGameboard Class
 * - A gameboard for a card game
 * **********************************************************************.
 */
public class CardGameboard {
	
/** The name. */
/*------------------------------------------------
 	Constants and Attributes
 ------------------------------------------------*/
	private final String name;				// Name of the board

/*------------------------------------------------
 	Constructor(s)
 ------------------------------------------------*/
	/**
 * Construct a <tt>CardGameboard</tt> with the given attributes;.
 *
 * @param name the name
 */
	public CardGameboard(final String name) {
		this.name = name;
	}
	
/*------------------------------------------------
    Accessors
 ------------------------------------------------*/
	/**
	 * Returns the name of this <tt>CardGameboard</tt>.
	 * 
	 * @return the name of this card gameboard
	 */
	public String getName() {
		return name;
	}
	
/*------------------------------------------------
    Overridden Methods
 ------------------------------------------------*/	
	
}
