/***********************************************************************//**
* @file			CardPileCollecion.java
* @author		Kurt E. Clothier
* @date			November 19, 2015
*
* @breif		A collection of card piles
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

import java.util.Arrays;

import games.Strings;

public final class CardPileCollection {
	
	private final CardPile[] piles;
	
	/**
	 * Constructs a new <tt>CardPileCollection</tt> with the given card piles.
	 * 
	 * @param piles CardPiles to put in this collection
	 */
	public CardPileCollection (final CardPile ... piles) {
		if (piles == null || piles.length == 0) {
			throw new IllegalArgumentException("Piles cannot be null or empty!");
		}
		this.piles = Arrays.copyOf(piles, piles.length);
	}
	
	/**
	 * Returns the number of <tt>CardPiles</tt> in this <tt>CardPileCollecion</tt>.
	 * 
	 * @return the number of <tt>CardPiles</tt> in this <tt>CardPileCollecion</tt>
	 */
	public int getSize() {
		return piles.length;
	}
	
	/**
	 * Returns this <tt>CardPileCollecion</tt> as an array of <tt>CardPiles</tt>. 
	 * 
	 * @return this <tt>CardPileCollecion</tt> as an array of <tt>CardPiles</tt>
	 */
	public CardPile[] toArray() {
		return Arrays.copyOf(piles, piles.length);
	}
	
	/**
	 * Returns all of the <tt>CardPiles</tt> from this <tt>CardPileCollecion</tt>.
	 * 
	 * @return all of the <tt>CardPiles</tt> from this <tt>CardPileCollecion</tt>
	 */
	public CardPile[] get() {
		return toArray();
	}
	
	/**
	 * Returns the common <tt>CardPile</tt> with the given name.
	 * 
	 * @param name name of the pile to return
	 * @return the common card pile with the given name
	 * @throws IllegalArgumentException if name is null or blank
	 */
	public CardPile get(final String name) throws IllegalArgumentException {
		return this.contains(name) ? piles[this.getIndexOf(name)]: null;
	}
	
	/**
	 * Returns the <tt>true</tt> if a <tt>CardPile</tt> with the given name exists.
	 * 
	 * @param name name of the pile to locate
	 * @return true if a common card pile with the given name exists
	 * @throws IllegalArgumentException if name is null or blank
	 */
	public boolean contains(final String name) throws IllegalArgumentException {
		return this.getIndexOf(name) < 0 ? false : true;
	}
	
	/*
	 * Returns the index of the <tt>CardPile</tt> with the given name.
	 * 
	 * @param name name of the pile to locate
	 * @return index of the card pile, or -1 if not found.
	 * @throws IllegalArgumentException if name is null or blank
	 */
	private int getIndexOf(final String name) throws IllegalArgumentException {
		if (name == null || name.equals("")) {
			throw new IllegalArgumentException("Name cannot be null or blank!");
		}
		int index = -1;
		for (int i = 0; i < piles.length; i++) {
			if (piles[i].getParameters().getName().equals(name)) {
				index = i;
				break;
			}
		}
		return index;
	}
	
	/**
	 * Create and return a copy of this <tt>CardPileCollecion</tt>.
	 * 
	 * @return a copy of this card pile collecion
	 */
	public CardPileCollection copy() {
		return new CardPileCollection(piles);
	}
	
	/**
	 * Return information.
	 *
	 * @return string containing information
	 */
	@Override public String toString() {
		final StringBuilder str = new StringBuilder();
		for (CardPile p : piles) {
			str.append(p.toString()).append(Strings.NEW_LINE.toString())
			.append(Strings.NEW_LINE.toString());
		}
		return str.toString();
	}
}
