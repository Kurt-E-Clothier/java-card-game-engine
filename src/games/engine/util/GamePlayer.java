/***********************************************************************//**
* @file			GamePlayer.java
* @author		Kurt E. Clothier
* @date			November 20, 2015
*
* @breif		Player in a game
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

/************************************************************************
 * The GamePlayer Class
 * - This class represents a player in a game.
 ************************************************************************/
public class GamePlayer implements Serializable {	
	
	private static final long serialVersionUID = -4248346240701007222L;
	
/*------------------------------------------------
 	Constants and Attributes
 ------------------------------------------------*/
	private final String name;
	private boolean isMyTurn;
	
/*------------------------------------------------
 	Constructor(s)
 ------------------------------------------------*/
	/**
	 * Construct a <tt>GamePlayer</tt>.
	 *
	 * @param name the name of this player
	 */
	public GamePlayer(final String name) {
		this.name = name;
		this.isMyTurn = false;
	}
	
/*------------------------------------------------
    Accessors and Mutators
 ------------------------------------------------*/
	/**
	 * Returns the name of this player.
	 * 
	 * @return the name of this player
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Returns <tt>True</tt> if it is this players turn.
	 * 
	 * @return true if it is this players turn
	 */
	public boolean isPlayersTurn() {
		return isMyTurn;
	}
	
	/**
	 * Specify if it is this player's turn.
	 * 
	 * @param bool <tt>True</tt> if it is this player's turn
	 */
	protected void setTurnAs(final boolean bool) {
		this.isMyTurn = bool;
	}
	
	@Override public String toString() {
		return name;
	}

}
