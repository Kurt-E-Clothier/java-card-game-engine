/***********************************************************************//**
* @file			CardGameBoard.java
* @author		Kurt E. Clothier
* @date			November 19, 2015
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

import games.Strings;

/******************************************************************//**
 * The CardGameBoard Class
 * - A gameboard for a card game
 *********************************************************************/
public class CardGameBoard {

/*------------------------------------------------
 	Constants and Attributes
 ------------------------------------------------*/
	private final String name;
	private final Deck deck;
	private final CardPileCollection commonPiles;
	private final CardGameBoardLayout layout;

/*------------------------------------------------
 	Constructor(s)
 ------------------------------------------------*/
	/**
 * Construct a <tt>CardGameboard</tt> with the given attributes;
 *
 * @param name the name
 */
	public CardGameBoard(final String name, final Deck deck, 
						 final CardPileCollection commonPiles,
						 final CardGameBoardLayout layout) {
		this.name = name;
		this.deck = deck;
		this.commonPiles = commonPiles.copy();
		this.layout = layout;
	}
	
/*------------------------------------------------
    Accessors
 ------------------------------------------------*/
	/**
	 * Returns the name of this <tt>CardGameBoard</tt>.
	 * 
	 * @return the name of this card game board
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Returns the deck for this <tt>CardGameBoard</tt>.
	 * 
	 * @return the deck for this card game board
	 */
	public Deck getDeck() {
		return deck;
	}
	
	/**
	 * Returns the common owned <tt>CardPileCollecion</tt> for this board.
	 * 
	 * @return the common owned <tt>CardPileCollecion</tt> for this board
	 */
	public CardPileCollection getCommonPiles() {
		return commonPiles;
	}
	
	/**
	 * Returns the <tt>CardGameBoardLayout</tt> for this board.
	 * 
	 * @return the <tt>CardGameBoardLayout</tt> for this board
	 */
	public CardGameBoardLayout getLayout() {
		return layout;
	}
	
/*------------------------------------------------
    Overridden Methods
 ------------------------------------------------*/
	/**
	 * Return information.
	 *
	 * @return string containing information
	 */
	@Override public String toString() {
		final StringBuilder str = new StringBuilder();
		str.append(name).append(Strings.NEW_LINE.toString())
		   .append(" - Deck -").append(Strings.NEW_LINE.toString())
		   .append(deck.toString()).append(Strings.NEW_LINE.toString())
		   .append(" - Common Piles -").append(Strings.NEW_LINE.toString())
		   .append(commonPiles.toString())
		   .append(" - Layout -").append(Strings.NEW_LINE.toString())
		   .append(layout.toString());
		return str.toString();
	}
}
