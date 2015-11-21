/***********************************************************************//**
* @file			Deck.java
* @author		Kurt E. Clothier
* @date			November 16, 2015
*
* @breif		Deck of Playing Cards for a card game
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
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import games.Strings;
import games.engine.util.PlayingCard;

/******************************************************************//**
 * The Deck Class
 * - An individual deck of Playing Cards used in a game
 * - Card Decks are quasi-immutable
 * - Card Deck is created from a deck text file
 * - Numeric ranking for each card must be passed in if comparing cards
 * Limitations
 * - can't handle special card abilities (for games like Magic & Yugioh)
 ********************************************************************/
public class Deck implements Serializable {
	
/*------------------------------------------------
 	Constants and Attributes
 ------------------------------------------------*/
	//private static final long serialVersionUID = 4831242385432681020L;
	private final String name;					// Name of the deck
	private final String grouping;				// Deck grouping (categories)
	private final PlayingCard[] cards;			// Set of cards for this deck	
	private int dealNdx;						// Index of next card to be dealt
	
/*------------------------------------------------
 	Constructor(s)
 ------------------------------------------------*/
	/**
	 * Construct a <tt>Deck</tt> of <tt>PlayingCards</tt> with the given attributes.
	 *
	 * @param name name of this deck
	 * @param grouping type of grouping used (or null if none)
	 * @param cards the playing card(s) to put in this deck 
	 */
	public Deck(final String name, final String grouping, final PlayingCard...cards) {
		this.name = name;
		this.grouping = grouping;
		this.cards = Arrays.copyOf(cards, cards.length);
		dealNdx = 0;
	}
	
/*------------------------------------------------
    Accessors
 ------------------------------------------------*/
	/**
	 * Returns the name of this <tt>Deck</tt>.
	 * 
	 * @return the name of this deck
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Returns the grouping used in this <tt>Deck</tt>.
	 * 
	 * @return the grouping used in this deck
	 */
	public String getGrouping() {
		return grouping == null ? "none" : grouping;
	}
	
	/**
	 * Returns the size this <tt>Deck</tt>.
	 * 
	 * @return the size of this deck
	 */
	public int getSize() {
		return cards.length;
	}
	
	/**
	 * Returns <tt>true</tt> if the grouping is specified for this <tt>Deck</tt>.
	 * 
	 * @return true if the grouping is specified for this Deck
	 */
	public boolean hasGrouping() {
		return grouping == null ? false : true;
	}
	
/*------------------------------------------------
    Utilities
 ------------------------------------------------*/
	/**
	 * Returns a Playing Card from the top of this Deck.
	 * Returns null if all cards have been dealt.
	 * 
	 * @return a Playing Card from this Deck
	 */
	public PlayingCard deal() {
		if (dealNdx < cards.length) {
			return cards[dealNdx++];
		}
		return null;
	}
	
	/**
	 * Returns a number of Playing Cards from the top of this deck
	 * Returns an empty array if all cards have been dealt.
	 * If the specified number is larger than the number of remaining cards,
	 * only those remaining will be dealt.
	 *  
	 * @param number how many cards to return
	 * @return a number of cards from this deck
	 */
	public PlayingCard[] deal(final int number) {
		PlayingCard[] tmpCards = null;
		if (cards.length < dealNdx + number) {
			tmpCards = this.dealAll();
		}
		else {
			tmpCards = new PlayingCard[number];
			// Doing this instead of System.arrayCopy in order to increment dealNdx
			for (int i = 0; i < number; i++) {
				tmpCards[i] = cards[dealNdx++];
			}
		}
		return tmpCards;
	}
	
	/**
	 * Returns an array containing all remaining playing cards in this deck.
	 * Returns an empty array if all cards have been dealt.
	 * 
	 * @return an array of all remaining playing cards in this deck
	 */
	public PlayingCard[] dealAll() {
		int arraySize = 0;
		if (dealNdx < cards.length) {
			arraySize = cards.length - dealNdx;
		}
		PlayingCard[] tmpCards = new PlayingCard[arraySize];
		// Doing this instead of System.arrayCopy to account for a size of 0;
		for (int i = 0; i < arraySize; i++) {
			tmpCards[i] = cards[dealNdx++];
		}
		return tmpCards;
	}
	
	/**
	 * Shuffle the cards in this deck which have not been dealt.
	 */
	public void shuffle() {
		int ndx = 0;
		PlayingCard card = null;
		final Random rand = ThreadLocalRandom.current();
		// Durstenfeld updated Fisher-Yates shuffle
		// Modified to only shuffle cards at and above the deal index!
		for (int i = cards.length - 1; i >= dealNdx; i--) {
			ndx = dealNdx + rand.nextInt(i - dealNdx + 1);
			card = cards[ndx];
			cards[ndx] = cards[i];
			cards[i] = card;
		}
	}
	
/*------------------------------------------------
    Overridden Methods
 ------------------------------------------------*/	
	/**
	 * Compares the specified object with this <tt>Deck</tt> for equality.  
	 * Returns <tt>true</tt> if the given object is non-null and is a <tt>Deck</tt>
	 * containing the same <tt>PlayingCards</tt> as this deck.
	 *
	 * @param that object to be compared for equality with this <tt>PlayingCard</tt>
	 * @return        <tt>true</tt> if the specified object is equal to this <tt>PlayingCard</tt>
	 */
	@Override public boolean equals(final Object that) {
		return 	that != null && 
				that.getClass() == this.getClass() &&
				Arrays.equals(this.cards, ((Deck)that).cards);
	}
	
	/**
	 * Returns the hash code associated with this <tt>Deck</tt>.
	 *
	 * @return  the hashCode associated with this deck
	 */
	@Override public int hashCode() {
		return Arrays.hashCode(this.cards);
	}
	
	/**
	 * Return information about the deck.
	 *
	 * @return string containing information about this deck
	 */
	@Override public String toString() {
		final StringBuilder str = new StringBuilder();
		str.append("This ").append(this.name).append(" deck contains ")
			.append(cards.length).append(" cards, grouped by ")
			.append(this.grouping).append('.');
		return str.toString();
	}
	
	/**
	 * Return verbose information about the deck.
	 *
	 * @return the string
	 */
	public String toStringVerbose() {
		final StringBuilder str = new StringBuilder();
		str.append(this.toString()).append(Strings.NEW_LINE.toString());
		if (cards != null) {
			for (final PlayingCard card : cards) {
				str.append(card.toString()).append(Strings.NEW_LINE.toString());
			}
		}
		return str.toString();
	}
}
