/***********************************************************************//**
* @file			CardPlayer.java
* @author		Kurt E. Clothier
* @date			November 20, 2015
*
* @breif		Player in a card game
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
 * The CardPlayer Class
 * - This class represents a player in a card game.
 * - Each player holds a collection of Card Piles
 ************************************************************************/
public class CardPlayer extends GamePlayer {
	
	//private static final long serialVersionUID = -3701555831455653073L;
	
/*------------------------------------------------
 	Constants and Attributes
 ------------------------------------------------*/
	private CardPileCollection piles;

/*------------------------------------------------
 	Constructor(s)
 ------------------------------------------------*/
	/**
	 * Constructs a new <tt>CardPlayer</tt> with the specified attributes.
	 *
	 * @param player the game player use to create this card player
	 * @param collection a collection of card piles owned by this player
	 */
	public CardPlayer(final GamePlayer player, final CardPileCollection collection) {
		this(player.getName(), collection);
	}
	
	/**
	 * Constructs a new <tt>CardPlayer</tt> with the specified attributes.
	 *
	 * @param name what to call this card player
	 * @param collection a collection of card piles owned by this player
	 */
	public CardPlayer(final String name, final CardPileCollection collection) {
		super(name);
		piles = collection.copy();
	}
	
/*------------------------------------------------
    Accessors and Mutators
 ------------------------------------------------*/
	
	/**
	 * Returns the <tt>CardPileCollecion</tt> held by this player.
	 * 
	 * @return the <tt>CardPileCollecion</tt> held by this player
	 */
	public CardPileCollection getPlayerPiles() {
		return piles;
	}
	
	@Override public String toString() {
		return super.toString() + "\n" + piles.toString();
	}
}
