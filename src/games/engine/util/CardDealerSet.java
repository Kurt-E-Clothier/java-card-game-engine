/***********************************************************************//**
* @file			CardDealerSet.java
* @author		Kurt E. Clothier
* @date			November 19, 2015
*
* @breif		Set of Card Dealer Pairs
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

import games.Strings;

public final class CardDealerSet implements Serializable {

	private static final long serialVersionUID = 5816080138394559889L;
	private final CardDealerPair[] pairs;
	private int index;
	
	/**
	 * Construct an empty <tt>CardDealerSet</tt>.
	 * 
	 * @param capacity how many pairs this set can hold.
	 */
	public CardDealerSet(final int capacity) {
		pairs = new CardDealerPair[capacity];
	}
	
	/**
	 * Returns the current number of pairs in this set.
	 * 
	 * @return the current number of pairs in this set
	 */
	public int getSize() {
		return pairs.length;
	}
	
	/**
	 * Add a pair to this set.
	 * Returns <tt>false</tt> if the set is full or this pair already exists.
	 * 
	 * @param pair CardDealerPair to be added
	 * @return false if the set is full or this pair already exists
	 */
	public boolean add(final CardDealerPair pair) {
		boolean bool = false;
		if (index < pairs.length && !contains(pair.getCardPileName())) {
			pairs[index++] = pair;
			bool = true;
		}
		return bool;
	}
	
	/**
	 * Returns the number of cards to be dealt to the specified card pile.
	 * 
	 * @param cardPileName name of the card pile in question
	 * @return  the number of cards to be dealt to the specified card pile
	 * @throws IllegalArgumentException if the string is null or is not in this set
	 */
	public int get(final String cardPileName) throws IllegalArgumentException {
		if (cardPileName == null || !contains(cardPileName)) {
			throw new IllegalArgumentException("String not found in set!");
		}
		int i = -1;
		for (CardDealerPair p : pairs) {
			if (p.getCardPileName().equals(cardPileName)) {
				i = p.getNumCardsToDeal();
				break;
			}
		}
		return i;
	}
	
	/**
	 * Returns the card pile name at the specified index.
	 * 
	 * @param index the index of the desired element
	 * @return the card pile name at the specified index
	 * @throws IndexOutOfBoundsException if the index is out of range (index < 0 || index >= size())
	 */
	public String getCardPileName(final int index) throws IndexOutOfBoundsException {
		return pairs[index].getCardPileName();
	}
	
	/**
	 * Returns the number of cards to deal at the specified index.
	 * 
	 * @param index the index of the desired element
	 * @return the number of cards to deal at the specified index
	 * @throws IndexOutOfBoundsException if the index is out of range (index < 0 || index >= size())
	 */
	public int getNumCardsToDeal(final int index) throws IndexOutOfBoundsException {
		return pairs[index].getNumCardsToDeal();
	}
	
	/**
	 * Returns <tt>true</tt> if the specified card pile name exists in this set.
	 * 
	 * @param cardPileName the card pile name to be located
	 * @return  <tt>true</tt> if the specified card pile name exists in this set
	 */
	public boolean contains(final String cardPileName) {
		boolean bool = false;
		for (int i = 0; i < index; i++) {
			if (cardPileName != null && pairs[i].getCardPileName().equals(cardPileName)) {
				bool = true;
				break;
			}
		}
		return bool;
	}
	
	/**
	 * Returns string representations of the pairs in this set.
	 * 
	 * @return string representations of the pairs in this set
	 */
	@Override public String toString() {
		final StringBuilder str = new StringBuilder();
		for (CardDealerPair p : pairs) {
			str.append(p.toString()).append(Strings.NEW_LINE.toString());
		}
		return str.toString();
	}
}
