/***********************************************************************//**
* @file			Dealer.java
* @author		Kurt E. Clothier
* @date			November 13, 2015
*
* @breif		Dealer of a card game
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

import java.util.Map;

/******************************************************************//**
 * The Dealer Class
 * - This class represents the Dealer in a card game.
 **********************************************************************/
public class Dealer {
	
/*------------------------------------------------
 	Keyword Parameter Enumerations
 ------------------------------------------------*/
	/**
	 * Specifies the direction of deal.
	 * CW: Clockwise = to the right
	 * CCW: Counter Clockwise = to the left
	 */
	public static enum Direction { CW, CCW }
	
/*------------------------------------------------
 	Constants and Attributes
 ------------------------------------------------*/
	private final boolean collate;
	private final Deck deck;
	private final int numPlayers;
	private final String[] playerNames;
	private final String[] commonNames;
	private final int[] playerTotals;
	private final int[] commonTotals;
	private final CardPlayer[] players;
	private final Map<String, CardPile> commonPiles;
	private boolean isDoneDealing;			// true when done dealing to all piles
	private boolean isDoneWithPlayerPiles;	// true when done dealing to player piles
	private boolean isDoneWithCommonPiles;	// true when done dealing to common piles
	private int nextPileNdx;				// index to next pile in array to be dealt a card
	private int cardsDealtToPile;			// current number of cards dealt to current pile
	private int nextPlayerNdx;				// index to next player in array to be dealt a card

/*------------------------------------------------
 	Constructor(s)
 ------------------------------------------------*/
	
	/**
	 * Constructs a new <tt>Dealer</tt> with the given attributes.
	 *
	 * @param deck the deck
	 * @param players the players
	 * @param commonPiles the common piles
	 * @param direction the direction
	 * @param collate the collate
	 * @param playerNames the player names
	 * @param playerTotals the player totals
	 * @param commonNames the common names
	 * @param commonTotals the common totals
	 */
	public Dealer(	final Deck deck, final CardPlayer[] players, final Map<String, CardPile> commonPiles, 
					final Dealer.Direction direction, final boolean collate, 
					final String[] playerNames, final int[] playerTotals, 
					final String[] commonNames, final int[] commonTotals) {
		this.deck = deck;
		this.collate = collate;
		this.isDoneDealing = false;
		this.isDoneWithPlayerPiles = false;
		this.isDoneWithCommonPiles = false;
		
		// Get references to card players
		this.numPlayers = players.length;
		this.players = new CardPlayer[numPlayers];
		for (int i = 0; i < numPlayers; i++) {
			switch (direction) {
			case CCW:
				// Put players in reverse order
				this.players[i] = players[numPlayers - 1 - i];
				break;
			case CW:
			default:
				// Put players in forward order
				this.players[i] = players[i];
				break;
			}
		}
		
		// Get references to common piles
		this.commonPiles = commonPiles;

		// Get names of player piles and number of cards to deal
		if(playerNames == null) {
			this.isDoneWithPlayerPiles = true;
			this.playerNames = null;
			this.playerTotals = null;
		}
		else {
			final int numPlayerPiles = playerNames.length;
			this.playerNames = new String[numPlayerPiles];
			this.playerTotals = new int[numPlayerPiles];
			for (int i = 0; i < numPlayerPiles; i++) {
				this.playerNames[i] = playerNames[i];
				this.playerTotals[i] = playerTotals[i];
			}
		}

		// Get names of common piles and number of cards to deal
		if (commonNames == null) {
			this.isDoneWithCommonPiles = true;
			this.commonNames = null;
			this.commonTotals = null;
		}
		else {
			final int numCommonPiles = commonNames.length;
			this.commonNames = new String[numCommonPiles];
			this.commonTotals = new int[numCommonPiles];
			for (int i = 0; i < numCommonPiles; i++) {
				this.commonNames[i] = commonNames[i];
				this.commonTotals[i] = commonTotals[i];
			}
		}
		
		// Determine recipient of first card to be dealt
		nextPileNdx = 0;
		cardsDealtToPile = 0;
		nextPlayerNdx = 0;
		
	}
	
/*------------------------------------------------
    Accessors and Mutators
 ------------------------------------------------*/
	
	/**
 * Deal next.
 *
 * @return true, if successful
 */
public boolean dealNext() {
		final PlayingCard card = deck.deal();
		if (card == null) {
			isDoneDealing = true;
		}
		else {
			if (!this.isDoneWithPlayerPiles) {
				this.dealNextPlayer(card);
			}
			else if (!this.isDoneWithCommonPiles) {
				this.dealNextCommon(card);
			}
			else {
				isDoneDealing = true;
			}
		}
		return isDoneDealing;
	}
	
	/**
	 * Deal next common.
	 *
	 * @param card the card
	 */
	private void dealNextCommon(final PlayingCard card) {
		final CardPile pile = commonPiles.get(commonNames[this.nextPileNdx]);
		pile.add(card);
		
		System.out.print("Dealt ");
		System.out.print(card.toString());
		System.out.print(" to common.");
		System.out.println(pile.getName());
		
		// Deal all cards to each pile
		if (++cardsDealtToPile == commonTotals[this.nextPileNdx]) {
			// Deal to next pile when done dealing to this pile
			cardsDealtToPile = 0;
			if (++nextPileNdx == commonNames.length) {
				// Done dealing to player piles!
				this.isDoneWithCommonPiles = true;
				nextPileNdx = 0;
				cardsDealtToPile = 0;
			}
		}
	}
	
	/**
	 * Deal next player.
	 *
	 * @param card the card
	 */
	private void dealNextPlayer(final PlayingCard card) {
		
		final CardPile pile = players[nextPlayerNdx].getPile(playerNames[this.nextPileNdx]);
		pile.add(card);
		
		System.out.print("Dealt ");
		System.out.print(card.toString());
		System.out.print(" to player.");
		System.out.println(players[nextPlayerNdx].getName());
		System.out.print(".");
		System.out.println(pile.getName());
			
		// Deal 1 card of each pile to each player at a time...
		if (collate) {
			// Deal to next player after every card
			if (++nextPlayerNdx == numPlayers) {
				// increment number of cards dealt to pile when dealt to all players
				nextPlayerNdx = 0;
				if (++cardsDealtToPile == playerTotals[this.nextPileNdx]) {
					// Deal to next pile when done dealing to this pile
					cardsDealtToPile = 0;
					if (++nextPileNdx == playerNames.length) {
						// Done dealing to player piles!
						this.isDoneWithPlayerPiles = true;
						nextPileNdx = 0;
						cardsDealtToPile = 0;
					}
				}
			}
		}
		// Deal all cards of a pile to each player at a time...
		else {
			// increment number of cards dealt to pile after every card
			if (++cardsDealtToPile == playerTotals[this.nextPileNdx]) {
				// Deal to next player when done dealing to this pile
				cardsDealtToPile = 0;
				if (++nextPlayerNdx == numPlayers) {
					// Deal to next pile when done dealing to this player
					nextPlayerNdx = 0;
					if (++nextPileNdx == playerNames.length) {
						// Done dealing to player piles!
						this.isDoneWithPlayerPiles = true;
					
					}
				}
			}
		}
	}
	
	/**
	 * Returns <tt>true</tt> if the dealing is done.
	 * 
	 * @return true if the dealing is done
	 */
	public boolean isDone() {
		return isDoneDealing;
	}

}
