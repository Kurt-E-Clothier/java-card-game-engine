/***********************************************************************//**
* @file			CardDealerPair.java
* @author		Kurt E. Clothier
* @date			November 19, 2015
*
* @breif		Card Pile Name and Number of Cards to deal
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

public final class CardDealerPair implements Serializable {

	private static final long serialVersionUID = 8358906197541963919L;
	private final String cardPileName;
	private final int numCardsToDeal;
	
	/**
	 * Constructs a <tt>CardDealerPair</tt> using the specified data pair.
	 * 
	 * @param cardPileName name of a card pile
	 * @param numCardsToDeal number of cards to deal to that card pile
	 * @throws IllegalArgumentException if the string is null or int < 0
	 */
	public CardDealerPair(final String cardPileName, final int numCardsToDeal) throws IllegalArgumentException {
		if (cardPileName == null || cardPileName.equals("") || numCardsToDeal < 0 ) {
			throw new IllegalArgumentException("Parameters cannot be null, empty, or negative!");
		}
		this.cardPileName = cardPileName;
		this.numCardsToDeal = numCardsToDeal;
	}
	
	/**
	 * Returns the card pile name associated with this pair.
	 * 
	 * @return the card pile name associated with this pair
	 */
	public String getCardPileName() {
		return cardPileName;
	}
	
	/**
	 * Returns the number of cards to deal associated with this pair.
	 * 
	 * @return the number of cards to deal associated with this pair.
	 */
	public int getNumCardsToDeal() {
		return numCardsToDeal;
	}
	
	/**
	 * Returns a string version of this pair.
	 * 
	 * @return a string version of this pair
	 */
	@Override public String toString() {
		return cardPileName + " " + numCardsToDeal;
	}
}
