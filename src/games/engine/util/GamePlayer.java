/***********************************************************************//**
* @file			GamePlayer.java
* @author		Kurt E. Clothier
* @date			December 7, 2015
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

import games.Strings;
import games.engine.Phase;

/************************************************************************
 * The GamePlayer Class
 * - This class represents a player in a game.
 ************************************************************************/
public class GamePlayer {	
	
	
/*------------------------------------------------
 	Constants and Attributes
 ------------------------------------------------*/
	private final String name;
	private Phase phase;
	private Phase startingPhase;
	private boolean hasWon;
	private boolean hasLost;
	
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
		this.phase = null;
		this.startingPhase = null;
		this.hasLost = false;
		this.hasWon = true;
	}
	
/*------------------------------------------------
    Accessors and Mutators
 ------------------------------------------------*/
	/**
	 * Reset this <tt>GamePlayer</tt> to the default state.
	 */
	public void reset() {
		this.phase = startingPhase;
		this.hasLost = false;
		this.hasWon = false;
	}
	
	/**
	 * Returns the name of this <tt>GamePlayer</tt>.
	 * 
	 * @return the name of this <tt>GamePlayer</tt>
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Returns the <tt>Phase</tt> this <tt>GamePlayer</tt> is in.
	 * 
	 * @return the <tt>Phase</tt> this <tt>GamePlayer</tt> is in
	 */
	public Phase getPhase() {
		return phase;
	}
	
	/**
	 * Sets the <tt>Phase</tt> this <tt>GamePlayer</tt> is in.
	 * 
	 * @param phase the <tt>Phase</tt> this <tt>GamePlayer</tt> should be in
	 */
	public void setPhase(final Phase phase) {
		this.phase = phase;
	}
	
	/**
	 * Returns the starting <tt>Phase</tt> this <tt>GamePlayer</tt> is in.
	 * 
	 * @return the <tt>Phase</tt> this <tt>GamePlayer</tt> is in
	 */
	public Phase getStartingPhase() {
		return startingPhase;
	}
	
	/**
	 * Sets the starting <tt>Phase</tt> this <tt>GamePlayer</tt> is in.
	 * 
	 * @param phase the <tt>Phase</tt> this <tt>GamePlayer</tt> should be in
	 */
	public void setStartingPhase(final Phase phase) {
		this.startingPhase = phase;
	}
	
	/**
	 * Returns <tt>true</tt> if this <tt>GamePlayer</tt> has won.
	 * 
	 * @return <tt>true</tt> if this <tt>GamePlayer</tt> has won
	 */
	public boolean hasWon() {
		return hasWon;
	}
	
	/**
	 * Returns <tt>true</tt> if this <tt>GamePlayer</tt> has lost.
	 * 
	 * @return <tt>true</tt> if this <tt>GamePlayer</tt> has lost
	 */
	public boolean hasLost() {
		return hasLost;
	}
	
	/**
	 * Set this <tt>GamePlayer</tt> as the winner.
	 */
	public void wins() {
		hasWon = true;
	}
	
	/**
	 * Set this <tt>GamePlayer</tt> as a loser.
	 * Note, this can also be used if a player is removed from the game, but the game continues.
	 */
	public void loses() {
		hasLost = true;
	}
	
/*------------------------------------------------
	Overridden Methods
 ------------------------------------------------*/
	/**
	 * Returns information about this <tt>GamePlayer</tt>.
	 *
	 * @return information about this <tt>GamePlayer</tt>
 	 */
	@Override public String toString() {
		final StringBuilder str = new StringBuilder();
		str.append("Player: ").append(name).append(Strings.NEW_LINE.toString());
		if (phase != null) {
			str.append("In game phase: ").append(phase.getName()).append(Strings.NEW_LINE.toString());
		}
		return str.toString();
	}
}
