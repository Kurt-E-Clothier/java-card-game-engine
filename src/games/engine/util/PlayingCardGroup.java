/***********************************************************************//**
* @file			PlayingCardGroup.java
* @author		Kurt E. Clothier
* @date			November 14, 2015
*
* @breif		Immutable PlayingCard group attribute
*
* @pre			Compiler: Eclipse - Mars Release (4.5.0)
* @pre			Java: JRE 7 or greater
*
* @see			http://www.projectsbykec.com/
*
* @copyright	The MIT License (MIT) - see LICENSE.txt
****************************************************************************/
package games.engine.util;

public final class PlayingCardGroup extends AbstractAttribute<PlayingCardGroup>{

	private static final long serialVersionUID = 2386774442470183739L;

	/**
	 * Constructs a new <tt>PlayingCardGroup</tt> attribute.
	 * 
	 * @param group String represented by this attribute
	 * @throws IllegalArgumentException if the parameter is null
	 */
	PlayingCardGroup(final String group) throws IllegalArgumentException {
		super(group);
	}
}
