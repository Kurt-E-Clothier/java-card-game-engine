/***********************************************************************//**
* @file			CardDealer.java
* @author		Kurt E. Clothier
* @date			November 30, 2015
*
* @breif		Dealer for a card game
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

/******************************************************************//**
 * The CardDealer Class
 * - This class represents the CardDealer in a card game.
 **********************************************************************/
public final class CardDealer {

/*------------------------------------------------
 	Keyword Parameter Enumerations
 ------------------------------------------------*/
	/** Specifies the direction of deal. */
	public static enum Direction {
		/** Clockwise = to the right */
		CW, 
		/** Counter Clockwise = to the left */
		CCW }
	/** Specifies the direction of deal. */
	public static enum Deal {
		/** Deal all remaining cards */
		ALL, 
		/** Deal no cards */
		NONE }
	
/*------------------------------------------------
 	Constants and Attributes
 ------------------------------------------------*/
	private final Deck deck;
	private final CardPileCollection commonPiles;
	private final boolean collate;
	private final CardPlayer[] players;
	private final CardDealerSet playerPairs;
	private final CardDealerSet commonPairs;
	private final boolean shouldShuffle;
	
	private boolean isDoneDealing;			// true when done dealing to all piles
	private boolean isDoneWithPlayerPiles;	// true when done dealing to player piles
	private boolean isDoneWithCommonPiles;	// true when done dealing to common piles
	private int nextPileNdx;				// index to next pile in array to be dealt a card
	private int cardsDealtToPile;			// current number of cards dealt to current pile
	private int nextPlayerNdx;				// index to next player in array to be dealt a card
	private int totalDealt;

/*------------------------------------------------
 	Constructor(s)
 ------------------------------------------------*/
	/**
	 * Constructs a new <tt>CardDealer</tt> with the given attributes.
	 * Dealer is best constructed using the <tt>CardDealerFactory</tt>.
	 *
	 * @param deck the deck of cards from which to deal
	 * @param players array of players to be dealt cards
	 * @param commonPiles collection of the common card piles
	 * @param direction the direction of dealing
	 * @param collate if card piles should be completed before dealing to next pile
	 * @param shuffle if the cards should be shuffled before dealing
	 * @param playerPairs set of player pile names and number of cards to deal
	 * @param commonPairs set of common pile names and number of cards to deal
	 */
	public CardDealer(final CardPileCollection commonPiles, final Deck deck, final CardPlayer[] players,
					  final CardDealer.Direction direction, final boolean collate, final boolean shuffle,
					  final CardDealerSet playerPairs, final CardDealerSet commonPairs) {
		this.deck = deck;
		this.commonPiles = commonPiles.copy();
		this.collate = collate;
		this.playerPairs = playerPairs;
		this.commonPairs = commonPairs;
		this.shouldShuffle = shuffle;
		this.isDoneDealing = false;
		this.isDoneWithPlayerPiles = false;
		this.isDoneWithCommonPiles = false;
		this.nextPileNdx = 0;
		this.cardsDealtToPile = 0;
		this.nextPlayerNdx = 0;
		this.totalDealt = 0;
		
		// Get references to card players (do this manually to reorder players if necessary)
		this.players = new CardPlayer[players.length];
		for (int i = 0; i < players.length; i++) {
			switch (direction) {
			case CCW:
				// Put players in reverse order
				this.players[i] = players[players.length - 1 - i];
				break;
			case CW:
			default:
				// Put players in forward order
				this.players[i] = players[i];
				break;
			}
		}
		
		// Shuffle the deck
		if (shuffle) {
			this.deck.shuffle();
			this.deck.shuffle();
		}
	}
	
/*------------------------------------------------
    Utility Methods
 ------------------------------------------------*/
	/**
	 * Reset this <tt>CardDealer</tt> and <tt>Deck</tt> to the default state.
	 */
	public void reset() {
		this.isDoneDealing = false;
		this.isDoneWithPlayerPiles = false;
		this.isDoneWithCommonPiles = false;
		this.nextPileNdx = 0;
		this.cardsDealtToPile = 0;
		this.nextPlayerNdx = 0;
		this.totalDealt = 0;
		deck.reset();
		if (shouldShuffle) {
			this.deck.shuffle();
			this.deck.shuffle();
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
	
	/**
	 * Returns information about this dealer.
	 * 
	 * @return string information about this dealer
	 */
	@Override public String toString() {
		final StringBuilder str = new StringBuilder();
		str.append("Dealt ").append(totalDealt).append(" of ")
		   .append(deck.getSize()).append(" cards.");
		return str.toString();
	}
	
	/**
	 * Deal next card to a pile, and return a string of information.
	 *
	 * @return information about the card dealing
	 */
	public String dealNext() {
		String string = null;
		if (!this.isDoneWithPlayerPiles) {
			string = this.dealNextPlayer();
		}
		else if (!this.isDoneWithCommonPiles) {
			string = this.dealNextCommon();
		}
		else {
			isDoneDealing = true;
			string = "";
		}
		return string;
	}
	
	/**
	 * Deal all remaining cards to the appropriate places.
	 */
	public void dealAll() {
		while(!isDoneDealing) {
			dealNext();
		}
	}
	
	/*
	 * Deal next card to common piles.
	 */
	private String dealNextCommon() {
		String string = null;
		if (cardsDealtToPile < commonPairs.getNumCardsToDeal(this.nextPileNdx)) {
			final PlayingCard card = deck.deal();
			if (card == null) {
				isDoneWithPlayerPiles = true;
				isDoneWithCommonPiles = true;
				isDoneDealing = true;
				return "";
			}
			else {
				final CardPile pile = commonPiles.get(commonPairs.getCardPileName(this.nextPileNdx));
				pile.add(card);
				++totalDealt;
				StringBuilder str = new StringBuilder();
				str.append("Dealt ").append(card.toString())
				   .append(" to common.")
				   .append(pile.getParameters().getName());
				string = str.toString();
			}
		}
		
		// Handle the deal counts
		if (++cardsDealtToPile >= commonPairs.getNumCardsToDeal(this.nextPileNdx)) {
			// Deal to next pile when done dealing to this pile
			cardsDealtToPile = 0;
			if (++nextPileNdx == commonPairs.getSize()) {
				// Done dealing to common piles!
				this.isDoneWithCommonPiles = true;
				nextPileNdx = 0;
				cardsDealtToPile = 0;
			}
		}
		return string == null ? "" : string;
	}
	
	/*
	 * Deal next card to player piles.
	 */
	private String dealNextPlayer() {
		String string = null;
		if (cardsDealtToPile < playerPairs.getNumCardsToDeal(this.nextPileNdx)) {
			final PlayingCard card = deck.deal();
			if (card == null) {
				isDoneWithPlayerPiles = true;
				isDoneWithCommonPiles = true;
				isDoneDealing = true;
				return "";
			}
			else {
				final CardPile pile = players[nextPlayerNdx].getPlayerPiles().get(playerPairs.getCardPileName(this.nextPileNdx));
				pile.add(card);
				++totalDealt;
				StringBuilder str = new StringBuilder();
				str.append("Dealt ").append(card.toString())
				   .append(" to player.")
				   .append(players[nextPlayerNdx].getName())
				   .append('.')
				   .append(pile.getParameters().getName());
				string = str.toString();
			}
		}
		
		// Deal 1 card of each pile to each player at a time...
		if (collate) {
			// Deal to next player after every card
			if (++nextPlayerNdx == players.length) {
				// increment number of cards dealt to pile when dealt to all players
				nextPlayerNdx = 0;
				if (++cardsDealtToPile >= playerPairs.getNumCardsToDeal(this.nextPileNdx)) {
					// Deal to next pile when done dealing to this pile
					cardsDealtToPile = 0;
					if (++nextPileNdx == playerPairs.getSize()) {
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
			if (++cardsDealtToPile >= playerPairs.getNumCardsToDeal(this.nextPileNdx)) {
				// Deal to next player when done dealing to this pile
				cardsDealtToPile = 0;
				if (++nextPlayerNdx == players.length) {
					// Deal to next pile when done dealing to this player
					nextPlayerNdx = 0;
					if (++nextPileNdx == playerPairs.getSize()) {
						// Done dealing to player piles!
						this.isDoneWithPlayerPiles = true;
						nextPileNdx = 0;
						cardsDealtToPile = 0;
					}
				}
			}
		}
		return string == null ? "" : string;
	}
}
