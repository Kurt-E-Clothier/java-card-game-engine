/***********************************************************************//**
* @file			CardGameEngine.java
* @author		Kurt E. Clothier
* @date			November 13, 2015
*
* @breif		An Engine to run a game of cards
*
* @pre			Compiler: Eclipse - Mars Release (4.5.0)
* @pre			Java: JRE 7 or greater
*
* @see			http://www.projectsbykec.com/
*
* @copyright	The MIT License (MIT) - see LICENSE.txt
****************************************************************************/

package games.engine.util;

import java.util.Map;

/******************************************************************//**
 * The CardGameEngine Class
 * - This class builds all of the necessary components for a card game
 * - All necessary objects are created and managed by the CardGame
 * - A driver class is required to actually play through a game
 * TODO
 ********************************************************************/
public final class CardGameEngine {
	
/*------------------------------------------------
 	Constants and Attributes
 ------------------------------------------------*/
	private final String name;
	private final Deck deck;
	private final CardGameboard board;
	private final int numPlayers;
	private final CardPlayer[] players;
	private final Map<String, CardPile> commonPiles;
	
/*------------------------------------------------
 	Constructor(s)
 ------------------------------------------------*/
	/**
 * Construct a <tt>CardGameEngine</tt> using the rules file.
 *
 * @param name the name
 * @param deck the deck
 * @param board the board
 * @param players the players
 * @param commonPiles the common piles
 */
	public CardGameEngine(final String name, final Deck deck, final CardGameboard board, final CardPlayer[] players, final Map<String, CardPile> commonPiles) {
		this.name = name;
		this.deck = deck;
		this.board = board;
		
		// Copy players
		numPlayers = players.length;
		this.players = new CardPlayer[numPlayers];
		System.arraycopy(players, 0, this.players, 0, this.players.length);
		
		// Copy common piles
		this.commonPiles = commonPiles;
	}
	
/*------------------------------------------------
    Accessors and Mutators
 ------------------------------------------------*/

	
/**
 * The main method.
 *
 * @param args the arguments
 */
/*------------------------------------------------
    Main Method for Testing
 ------------------------------------------------*/	
	public static void main(String[] args) {
		System.out.println(" --- Card Game Engine Test Bench ---\n");
	}
		

}

