/***********************************************************************//**
* @file			PlayingCardFactory.java
* @author		Kurt E. Clothier
* @date			November 14, 2015
*
* @breif		Factory for creating PlayingCards and Attributes
*
* @pre			Compiler: Eclipse - Mars Release (4.5.0)
* @pre			Java: JRE 7 or greater
*
* @see			http://www.projectsbykec.com/
*
* @copyright	The MIT License (MIT) - see LICENSE.txt
****************************************************************************/
package games.engine.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/******************************************************************//**
 * The PlayingCardFactory Enum
 * - Combination of Flyweight, Singleton, and Factory patterns
 * - Access statically as PlayingCardFactory.INSTANCE.method()
 *	 or PlayingCardFactory factory = PlayingCardFactory.getInstance()
 ********************************************************************/
public enum PlayingCardFactory {
	
	INSTANCE;
	
	private final List<PlayingCard> cards;
	private final Map<String, PlayingCardFace> faces;
	private final Map<String, PlayingCardGroup> groups;
	
	/*
	 * Constructs this <tt>PlayingCardFactory</tt> when first used.
	 */
	private PlayingCardFactory() {
		cards = new ArrayList<PlayingCard>();
		faces = new ConcurrentHashMap<String, PlayingCardFace>();
		groups = new ConcurrentHashMap<String, PlayingCardGroup>();
	}
	
	/**
	 * Return an instance of this <tt>PlayingCardFactory</tt>.
	 * 
	 * @return an instance of this PlayingCardFactory
	 */
	public static PlayingCardFactory getInstance()
	{
		return INSTANCE;
	}
	
	/**
	 * Creates a new <tt>PlayingCard</tt> (if necessary) and returns a PlayingCard with the given attributes.
	 * 
	 * @param face the face (title, name, etc) of this card
	 * @param group the group of this card (suit, color, etc)
	 * @return a playing card with the given attributes
	 * @throws IllegalArgumentException if both parameters are null
	 */
	public synchronized PlayingCard createPlayingCard(final PlayingCardFace face, final PlayingCardGroup group) {
		PlayingCard card = null;
		for (final PlayingCard p : cards) {
			if 	(p.has(face, group)) {
				card = p;
				break;
			}
		}
		if (card == null) {
				card = new PlayingCard(face, group);
				cards.add(card);
		}
		return card;
	}
	
	/**
	 * Creates a new <tt>PlayingCardFace</tt> attribute (if necessary) and returns a PlayingCardFace attribute.
	 * 
	 * @param face String represented by this attribute
	 * @return a PlayingCardFace attribute with the specified String
	 * @throws IllegalArgumentException if the parameter is null
	 */
	public synchronized PlayingCardFace createFace(final String face) throws IllegalArgumentException {
		if (faces.containsKey(face)) {
			return faces.get(face);
		}
		else {
			final PlayingCardFace f = new PlayingCardFace(face);
			faces.put(face, f);
			return f;
		}
	}
	
	/**
	 * Creates a new <tt>PlayingCardGroup</tt> attribute (if necessary) and returns a PlayingCardGroup attribute.
	 * 
	 * @param group String represented by this attribute
	 * @return a PlayingCardGroup attribute with the specified String
	 * @throws IllegalArgumentException if the parameter is null
	 */
	public synchronized PlayingCardGroup createGroup(final String group) throws IllegalArgumentException {
		if (groups.containsKey(group)) {
			return groups.get(group);
		}
		else {
			final PlayingCardGroup att = new PlayingCardGroup(group);
			groups.put(group, att);
			return att;
		}
	}
}
