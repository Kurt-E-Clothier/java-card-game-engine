/***********************************************************************//**
* @file			Deck.java
* @author		Kurt E. Clothier
* @date			October 9, 2015
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

/************************************************************************
 * The CardPile Class
 * - An individual pile of Playing Cards used in a game
 ************************************************************************/
public class CardPile {
	
/*------------------------------------------------
 	Constants and Attributes
 ------------------------------------------------*/
	
	/**
	 * Specifies who owns this card pile.
	 * PLAYER: a single player of the game
	 * COMMON: all players of the game
	 */
	public enum Owner { PLAYER, COMMON }
	
	/**
	 * Specifies who can view this card pile.
	 * ALL: anyone
	 * OWNER: only the owner 
	 * OTHER: anyone but the owner
	 * NONE: no one
	 */
	public enum Visibility { ALL, OWNER, OTHER, NONE }
	
	/**
	 * Specifies how the pile should be arranged.
	 * STACKED: all cards in a single stack
	 * SPREAD: cards slightly spread out
	 * SPACED: cards do not touch one another
	 * MESSY: cards are in a messy pile
	 */
	public enum Arrangement { STACKED, SPREAD, SPACED, MESSY }
	
	/**
	 * Specifies how the cards are oriented with respect to the owner.
	 * HORIZONTAL: cards are placed from left to right
	 * VERTICAL: cards are placed from bottom to top
	 */
	public static enum Orientation { HORIZONTAL, VERTICAL }
	
	private final String name = "";
/*	private final int capacity;
	private final Owner owner;
	private final Visibility visibility;
	private final Arrangement arrangement;
	private final Orientation orientation;
*/
	private int remaining;
	private int size;
	
	private PlayingCard top;
	private PlayingCard[] cards;
	
	
/*------------------------------------------------
 	Constructor(s)
 ------------------------------------------------*/
	/**
	 * Construct a blank <tt>PlayingCard</tt>.
	 */
	protected void Deck() {
		//TODO - Read deck.<game>.txt file to create cards
	}
	
/*------------------------------------------------
    Accessors and Mutators
 ------------------------------------------------*/
	/**
	 * Returns the name of this <tt>Deck</tt>.
	 * 
	 * @return the name of this deck
	 */
	protected String getName() {
		return name;
	}
	
	/**
	 * Returns the size this <tt>Deck</tt>.
	 * 
	 * @return the size of this deck
	 */
	protected int size() {
		return size;
	}
	
	/**
	 * Returns the capacity this <tt>Deck</tt>.
	 * 
	 * @return the size of this deck
	 */
	protected int remaining() {
		return remaining;
	}

}

