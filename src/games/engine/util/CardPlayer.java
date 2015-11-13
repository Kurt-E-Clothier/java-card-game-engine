/***********************************************************************//**
* @file			CardPlayer.java
* @author		Kurt E. Clothier
* @date			November 11, 2015
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

import java.util.HashMap;
import java.util.Map;

/************************************************************************
 * The CardPlayer Class
 * - This class represents a player in a card game.
 * - Each player should be given a collection of Card Piles
 ************************************************************************/
public class CardPlayer extends AbstractPlayer{
	
/** The piles. */
/*------------------------------------------------
 	Constants and Attributes
 ------------------------------------------------*/
	private Map<String, CardPile> piles;	// Card Piles owned by this player

/*------------------------------------------------
 	Constructor(s)
 ------------------------------------------------*/
	
	/**
 * Constructs a new <tt>CardPlayer</tt> with the given name.
 *
 * @param name 		what to call this card player
 */
	public CardPlayer(String name) {
		super(name);
		piles = null;
	}
	
/*------------------------------------------------
    Accessors and Mutators
 ------------------------------------------------*/
	
	/**
	 * Returns the <tt>CardPile</tt> with the specified name.
	 * 
	 * @param name the <tt>CardPile</tt> to return
	 * @return the <tt>CardPile</tt> with the specified name
	 */
	public CardPile getPile(String name) {
		return piles.get(name);
	}
	
	/**
	 * Gives the created <tt>CardPiles</tt> to this player.
	 * 
	 * @param piles array of CardPiles to give to this player
	 */
	public void addPiles (CardPile[] piles) {
		if (this.piles == null) {
			this.piles = new HashMap<String, CardPile>((int)(piles.length/0.75 +1));
		}
		for (CardPile p : piles) {
			this.piles.put(p.getName(), p);
		}
	}

}
