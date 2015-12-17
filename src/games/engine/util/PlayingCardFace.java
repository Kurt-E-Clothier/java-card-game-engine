/***********************************************************************//**
* @file			PlayingCardFace.java
* @author		Kurt E. Clothier
* @date			November 14, 2015
*
* @breif		Immutable PlayingCard face attribute
*
* @pre			Compiler: Eclipse - Mars Release (4.5.0)
* @pre			Java: JRE 7 or greater
*
* @see			http://www.projectsbykec.com/
*
* @copyright	The MIT License (MIT) - see LICENSE.txt
****************************************************************************/
package games.engine.util;

public final class PlayingCardFace extends PlayingCardAttribute<PlayingCardFace> {

	/**
	 * Constructs a new <tt>PlayingCardFace</tt> attribute.
	 * 
	 * @param face String represented by this attribute
	 * @throws IllegalArgumentException if the parameter is null
	 */
	PlayingCardFace(final String face) throws IllegalArgumentException  {
		super(face);
	}
}
