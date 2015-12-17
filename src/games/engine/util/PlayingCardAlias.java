/***********************************************************************//**
* @file			PlayingCardAlias.java
* @author		Kurt E. Clothier
* @date			December 7, 2015
*
* @breif		Alias for a playing card
*
* @pre			Compiler: Eclipse - Mars Release (4.5.0)
* @pre			Java: JRE 7 or greater
*
* @see			http://www.projectsbykec.com/
*
* @copyright	The MIT License (MIT) - see LICENSE.txt
****************************************************************************/

package games.engine.util;

import games.engine.EngineComponent;

public final class PlayingCardAlias implements EngineComponent{
	
/*------------------------------------------------
 	Constants and Attributes
 ------------------------------------------------*/
	private final String alias;
	private final PlayingCard card;
	
/*------------------------------------------------
 	Constructor(s)
 ------------------------------------------------*/
	/**
	 * Construct a new <tt>PlayingCard</tt> with the given attributes.
	 * 
	 * @param face the face (title, name, etc) of this card
	 * @param group the group of this card (suit, color, etc)
	 * @throws IllegalArgumentException if both parameters are null
	 */
	public PlayingCardAlias(final String alias, final PlayingCard card) throws IllegalArgumentException {
		if (alias == null || alias.equals("") || card == null ) {
			throw new IllegalArgumentException("Parameters cannot be null or blank.");
		}
		this.alias = alias;
		this.card = card;
	}
	
/*------------------------------------------------
    Accessors
 ------------------------------------------------*/
	/**
	 * Returns the string alias for this <tt>PlayingCardAlias</tt>.
	 * 
	 * @return the string alias for this <tt>PlayingCardAlias</tt>
	 */
	@Override public String getName() {
		return alias;
	}
	
	/**
	 * Returns the <tt>PlayingCard</tt> for this <tt>PlayingCardAlias</tt>.
	 * 
	 * @return the <tt>PlayingCard</tt> for this <tt>PlayingCardAlias</tt>
	 */
	public PlayingCard getPlayingCard() {
		return card;
	}
}
