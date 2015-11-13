/***********************************************************************//**
* @file			GameComponentFactory.java
* @author		Kurt E. Clothier
* @date			November 13, 2015
*
* @breif		Utility methods for creating game components
*
* @pre			Compiler: Eclipse - Mars Release (4.5.0)
* @pre			Java: JRE 7 or greater
*
* @see			http://www.projectsbykec.com/
*
* @copyright	The MIT License (MIT) - see LICENSE.txt
****************************************************************************/

package games.engine.util;

import java.util.HashMap;
import java.util.Map;

/******************************************************************//**
 * The GameComponentFactory Class
 * - Static Methods for creating game components
 ********************************************************************/
public final class GameComponentFactory {
	
/*------------------------------------------------
 	Constants and Attributes
 ------------------------------------------------*/

/**
 * Instantiates a new game component factory.
 */
/*------------------------------------------------
 	Constructor(s)
 ------------------------------------------------*/
	private GameComponentFactory() {}
	
/*------------------------------------------------
    Static Methods
 ------------------------------------------------*/
	
	/**
	 * Creates a new GameComponent object.
	 *
	 * @param rulesPlugin the rules plugin
	 * @param players the players
	 * @return the card game engine
	 * @throws PluginException the plugin exception
	 */
	public static CardGameEngine createCardGameEngine(final Plugin rulesPlugin, final CardPlayer[] players) throws PluginException{
		
		// Make sure this is a Rules plugin...
		rulesPlugin.checkType(Plugin.Type.RULES);
		String gameName = rulesPlugin.checkParams("game");
		
		// Create card deck
		Map<String, Integer> cardRankings = GameComponentFactory.createRankings(rulesPlugin);
		Plugin deckPlugin = new Plugin(Plugin.Type.DECK, rulesPlugin.checkParams("deck"));
		Deck deck = GameComponentFactory.createDeck(deckPlugin, cardRankings);
		
		// Create Gameboard
		Plugin boardPlugin = new Plugin(Plugin.Type.BOARD, rulesPlugin.checkParams("board"));
		CardGameboard board = GameComponentFactory.createCardGameboard(boardPlugin);
		
		// Get Plugins for each cardpile
		Plugin[] piles = GameComponentFactory.createPilePlugins(boardPlugin);
		
		// Create card piles for every player and give the piles to the players
		int numPiles = piles.length;
		int numPlayerPiles = boardPlugin.getNumberOf("owner player");
		int pile = 0;
		CardPile[] playerPiles = new CardPile[numPlayerPiles];
		for (CardPlayer p : players) {
			pile = 0;
			for (int i = 0; i < numPiles; i++) {
				if (piles[i].checkParams("owner").equals("player")) {
					playerPiles[pile++] = GameComponentFactory.createPile(piles[i]);
				}
			}
			p.addPiles(playerPiles);
		}
				
		// Create common card piles
		Map<String, CardPile> commonPiles = new HashMap<String, CardPile>();
		//CardPile[] commonPiles = new CardPile[numPiles - numPlayerPiles];
		// make this into a map~!
		for (int i = 0; i < numPiles; i++) {
			if (piles[i].checkParams("owner").equals("common")) {
				CardPile p = GameComponentFactory.createPile(piles[i]);
				commonPiles.put(p.getName(), p);
			}
		}
		
		Dealer dealer = GameComponentFactory.createDealer(rulesPlugin, deck, players, commonPiles);
		
		return new CardGameEngine(gameName, deck, board, players, commonPiles);
	}
	
	/**
	 * Creates and returns a new <tt>Dealer</tt> for a card game.
	 * 
	 * @param rulesPlugin plugin file used to create this dealer
	 * @param deck deck of cards used for dealing
	 * @param players array of players in this game
	 * @param commonPiles map of common card piles
	 * @return new Dealer for a card game
	 * @throws PluginException for invalid or missing keywords and parameters
	 */
	public static Dealer createDealer(final Plugin rulesPlugin, final Deck deck, final CardPlayer[] players, 
											final Map<String, CardPile> commonPiles) throws PluginException {
		
		// Make sure this is a Rules plugin...
		rulesPlugin.checkType(Plugin.Type.RULES);
		
		// Create deal rules plugin
		int start = rulesPlugin.checkIndexOf("deal") + 1;
		int end = rulesPlugin.checkIndexOf("deal-end");
		Plugin dealPlugin = rulesPlugin.divide(start, end);
		
		// Get Collate parameter
		boolean collate = false;
		String collateStr = dealPlugin.getParams("collate");
		if (collateStr != null && collateStr.equals("yes")) {
			collate = true;
		}
		// Get Direction parameter
		Dealer.Direction direction = null;
		try {
			direction = Dealer.Direction.valueOf(dealPlugin.checkParams("direction").toUpperCase());
		// Exception thrown when a parameter is not a member of the appropriate Enumeration
		} catch (IllegalArgumentException e) {
			String[] message = e.getMessage().split("\\.");
			throw PluginException.create(PluginException.Type.INVALID_PARAMETER, e, dealPlugin.getName(), message[message.length-2], message[message.length-1]);
		}
		
		// Get card pile information
		int playerStart = dealPlugin.getIndexOf("player-piles") + 1;
		int commonStart = dealPlugin.getIndexOf("common-piles") + 1;
		String[] playerNames = null;
		String[] commonNames = null;
		int[] playerTotals = null;
		int[] commonTotals = null;
		int numPiles = 0;
		int index = 0;
		
		// Generate player-piles arrays
		if (playerStart > 0) {
			if (commonStart > 0) {
				numPiles = commonStart - playerStart - 1;
			}
			else {
				numPiles = dealPlugin.getSize() - playerStart;
			}
			playerNames = new String[numPiles];
			playerTotals = new int[numPiles];
		
			index = playerStart;
			for (int i = 0; i < numPiles; i++) {
				String line[] = dealPlugin.getLine(index++).split("\\s");
				
				if (line.length < 2) {
					throw PluginException.create(PluginException.Type.INVALID_PARAMETER, dealPlugin.getName(), "player-pile: " + line[0]);
				}
				
				// Check that this card pile name exists and is not repeated!
				if (players[0].getPile(line[0]) == null) {
					throw PluginException.create(PluginException.Type.INVALID_PARAMETER, dealPlugin.getName(), "player-piles", line[0]);
				}
				for (int j = 0; j < i; j++) {
					if (playerNames[j].equals(line[0])) {
						throw PluginException.create(PluginException.Type.INVALID_PARAMETER, dealPlugin.getName(), "player-piles", line[0]);
					}
				}
				playerNames[i] = line[0];
				
				// Check for "all" then try to get the int value
				if (line[1].equals("all")) {
					playerTotals[i] = deck.getSize();
				}
				else {
					try {
						playerTotals[i] = Integer.parseInt(line[1].replaceAll("\\.\\d*|\\s|[^\\d]", ""));
					} catch (NumberFormatException e) {
						throw PluginException.create(PluginException.Type.INVALID_PARAMETER, e, dealPlugin.getName(), "player-pile: " + line[0], line[1]);
					}
				}
			}
		}
		
		// Generate common-piles arrays
		if (commonStart > 0) {
			numPiles = dealPlugin.getSize() - commonStart;
			commonNames = new String[numPiles];
			commonTotals = new int[numPiles];
			index = commonStart;
			for (int i = 0; i < numPiles; i++) {
				String line[] = dealPlugin.getLine(index++).split("\\s");
				
				if (line.length < 2) {
					throw PluginException.create(PluginException.Type.INVALID_PARAMETER, dealPlugin.getName(), "common-pile: " + line[0]);
				}
				
				// Check that this card pile name exists and is not repeated!
				if (commonPiles.get(line[0]) == null) {
					throw PluginException.create(PluginException.Type.INVALID_PARAMETER, dealPlugin.getName(), "common-piles", line[0]);
				}
				for (int j = 0; j < i; j++) {
					if (commonNames[j].equals(line[0])) {
						throw PluginException.create(PluginException.Type.INVALID_PARAMETER, dealPlugin.getName(), "common-piles", line[0]);
					}
				}
				commonNames[i] = line[0];
				
				// Check for "all" then try to get the int value
				if (line[1].equals("all")) {
					commonTotals[i] = deck.getSize();
				}
				else {
					try {
						commonTotals[i] = Integer.parseInt(line[1].replaceAll("\\.\\d*|\\s|[^\\d]", ""));
					} catch (NumberFormatException e) {
						throw PluginException.create(PluginException.Type.INVALID_PARAMETER, e, dealPlugin.getName(), "common-pile: " + line[0], line[1]);
					}
				}
			}
			
			// See if more than one pile wants all the cards...
			boolean allDealt = false;
			for (int i = 0; i < playerTotals.length; i++) {
				if (allDealt) {
					throw new PluginException("No cards left to deal for player-pile \"" + playerNames[i] + "\" in file: " + dealPlugin.getName(), 
							PluginException.Type.DATA_REPRESENTATION);
				}
				if (playerTotals[i] == deck.getSize()) {
					allDealt = true;
				}
			}
			for (int i = 0; i < commonTotals.length; i++) {
				if (allDealt) {
					throw new PluginException("No cards left to deal for common-pile \"" + commonNames[i] + "\" in file: " + dealPlugin.getName(), 
													PluginException.Type.DATA_REPRESENTATION);
				}
				if (commonTotals[i] == deck.getSize()) {
					allDealt = true;
				}
			}
		}
		return new Dealer(deck, players, commonPiles, direction, collate, playerNames, playerTotals, commonNames, commonTotals);
	}
	
	/**
	 * Creates and returns a new <tt>Deck</tt> of <tt>PlayingCards</tt>.
	 * 
	 * @param deck		Plugin used to create this deck
	 * @param ranking	a mapping of the face names to integer values (can be null)
	 * @return			a new deck of playing cards
	 * @throws PluginException 	if the file, keywords, or parameters are invalid
	 */
	public static Deck createDeck(final Plugin deck, final Map<String, Integer> ranking) throws PluginException {
		
		// Make sure this is a Deck plugin...
		deck.checkType(Plugin.Type.DECK);
		
		boolean groupingSpecified;	// True if grouping & groups are non null
		boolean rankingSpecified;	// True if card ranking exists
		boolean quantitySpecified;	// True if specify-quantity is set to "yes"
		PlayingCard[] cards;		// Set of cards for this deck
		String[] groups;			// Deck categories
		String[] faces;				// Faces of the playing cards
		String grouping;			// Deck grouping (categories)
		int size;					// How many cards are in the deck
				
		// Mandatory attributes
		String name = deck.checkParams("deck");
		if (!name.equals(deck.getRawName())) {
			throw PluginException.create(PluginException.Type.INVALID_PARAMETER, deck.getName(), "deck", name);
		}
		faces = deck.checkCSVParams("faces");
				
		// Optional Grouping and (Partially optional) Groups (suits, colors, etc)
		grouping = deck.getParams("grouping");
		String tempString = null;
		if (grouping == null) {
			groupingSpecified = false;
			groups = null;
		}
		else {
			groupingSpecified = true;
			groups = deck.checkCSVParams("groups");
		}
				
		// Optional specify-quantity
		tempString = deck.getParams("specify-quantity");
		if (tempString != null && tempString.equals("yes")) {
			quantitySpecified = true;
		}
		else {
			quantitySpecified = false;
		}

		// Put all of the card data into one string
		int fileNdx = deck.checkIndexOf("cards");
		StringBuilder str = new StringBuilder();
		while(++fileNdx < deck.getSize()) {		// This is length of the deck file, not the size of the deck!
			str.append(deck.getLine(fileNdx));
			// Handle trailing ',' if absent
			if (str.charAt(str.length() - 1) != ',') {
				str.append(',');
			}
		}
		str.setLength(Math.max(str.length() - 1, 0));	// remove final ","
		// This protects against whitespace, negatives, decimal points, trailing decimal places, and non-digits
		String cardDataString = str.toString().replaceAll("\\.\\d*|\\s|[^\\d,\\s],*", "");	// remove all non integers/commas
		String[] cardData = cardDataString.split(",");	// Split Card data into individual values (Possibly: face, group, quantity)
		
		// Get user specified deck size
		tempString = deck.checkParams("size");
		try {
			size = Integer.parseInt(tempString);
		} catch (NumberFormatException e) {
			throw PluginException.create(PluginException.Type.INVALID_PARAMETER, e, name, "size", tempString);
		}
		
		if (size <= 0) {
			throw PluginException.create(PluginException.Type.INVALID_PARAMETER, name, "size", tempString);
		}
		
		// Check for a rankings map
		if (ranking == null) {
			rankingSpecified = false;
		}
		else {
			rankingSpecified = true;
			if (ranking.size() > faces.length) {
				throw PluginException.create(PluginException.Type.MISMATCH, deck.getName(), "Ranking");
			}
		}

		// Create Deck of Cards
		cards = new PlayingCard[size];
		int dataNdx = 0;
		int quantity = 0;
		int cardNdx = 0;
		int faceNdx = 0;
		int groupNdx = 0;
		int rank = 0;
		do {
			try {
				faceNdx = Integer.parseInt(cardData[dataNdx]);
				if (rankingSpecified) {
					if (ranking.containsKey(faces[faceNdx])) {
						rank = ranking.get(faces[faceNdx]);
					}
					else {
						throw PluginException.create(PluginException.Type.MISMATCH, deck.getName(), "Ranking");
					}
				}
	
				// Create a playing card
				if (groupingSpecified) {
					groupNdx = Integer.parseInt(cardData[dataNdx + 1]);
					cards[cardNdx] = new PlayingCard(faces[faceNdx], rank, groups[groupNdx]);
					dataNdx+=2;
				} else {
					cards[cardNdx] = new PlayingCard(faces[faceNdx], rank);
					dataNdx++;
				}
				cardNdx++;
					
				// Make this card again, if specified
				if (quantitySpecified) {
					quantity = Integer.parseInt(cardData[dataNdx]);
					while (--quantity > 0) {
						cards[cardNdx] = cards[cardNdx-1].copy();
						cardNdx++;
					}
					dataNdx++;
				}
			} catch (ArrayIndexOutOfBoundsException e) {
				throw PluginException.create(PluginException.Type.DATA_REPRESENTATION, e, deck.getName(), "cards");
			}
		} while (cardNdx < size);
		if (cardData.length > dataNdx) {
			throw PluginException.create(PluginException.Type.DATA_REPRESENTATION, deck.getName(), "cards");
		}
		
		return new Deck(name, grouping, rankingSpecified, cards);
	}
	
	/**
	 * Creates and returns a new <tt>CardPile</tt>.
	 * 
	 * @param pile	Plugin used to create this card pile
	 * @return a new card pile
	 * @throws PluginException for invalid file, keywords, or parameters
	 */
	public static CardPile createPile(final Plugin pile) throws PluginException {
		
		String name = pile.checkParams("name");
		
		CardPile.Owner owner = null;
		CardPile.Visibility visibility = null;
		CardPile.Visible visible = CardPile.Visible.NUMBER;
		CardPile.Placement placement = null;
		CardPile.Orientation orientation = null;
		CardPile.Tiling tiling = null;
		CardPile.Removal removal = null;
		int numVisible = 0;
		
		// Try to get attributes from Plugin file - match to CardPile enumeration values
		try {
			owner = CardPile.Owner.valueOf(pile.checkParams("owner").toUpperCase());
			visibility = CardPile.Visibility.valueOf(pile.checkParams("visibility").toUpperCase());
			placement = CardPile.Placement.valueOf(pile.checkParams("placement").toUpperCase());
			orientation = CardPile.Orientation.valueOf(pile.checkParams("orientation").toUpperCase());
			removal = CardPile.Removal.valueOf(pile.checkParams("removal").toUpperCase());
			
			// Tiling only matters for certain card placements
			switch (placement) {
			case SPREAD:
			case SPACED:
				tiling = CardPile.Tiling.valueOf(pile.checkParams("tiling").toUpperCase());
				break;
			default:
				tiling = null;
				break;
			}
			
			// A bit of extra care for the "Visible" Keyword (can be a number or word)
			String visibleParam = pile.checkParams("visible").toUpperCase();
			for (CardPile.Visible val : CardPile.Visible.values()) {
				if (val.name().equals(visibleParam)) {
					visible = CardPile.Visible.valueOf(visibleParam);
					break;
				}
			}
			numVisible = 0;
			if (visible == CardPile.Visible.NUMBER) {
				try {
					numVisible = Math.abs(Integer.valueOf(visibleParam));
				} catch (NumberFormatException e) {
					// This exception will be caught below (with the others) and transformed into a PluginException
					throw new IllegalArgumentException("No enum constant games.engine.util.CardPile.Visible." + visibleParam, e);
				}
			}
			
		// Exception thrown when a parameter is not a member of the appropriate Enumeration
		} catch (IllegalArgumentException e) {
			String[] message = e.getMessage().split("\\.");
			throw PluginException.create(PluginException.Type.INVALID_PARAMETER, e, pile.getName(), message[message.length-2], message[message.length-1]);
		}
		
		return new CardPile(name, owner, visibility, visible, numVisible, placement, orientation, tiling, removal);
	}
	
	/**
	 * Create and return a <tt>CardGameboard</tt>.
	 * 
	 * @param board Plugin used to create the card gameboard
	 * @return new CardGameboard
	 * @throws PluginException for invalid file, keywords, or parameters
	 */
	public static CardGameboard createCardGameboard(final Plugin board) throws PluginException {
		
		// Make sure this is a board plugin...
		board.checkType(Plugin.Type.BOARD);
		
		// Mandatory attributes
		String name = board.checkParams("game");
		if (!name.equals(board.getRawName())) {
			throw PluginException.create(PluginException.Type.INVALID_PARAMETER, board.getName(), "game", name);
		}
		
		return new CardGameboard(name);
	}
	
	/**
	 * Create and return a card rankings map from the rules <tt>Plugin</tt>.
	 * 
	 * @param rules plugin to use to create this ranking map
	 * @return card rankings map
	 * @throws PluginException for invalid file, keywords, or parameters
	 */
	public static Map<String, Integer> createRankings(final Plugin rules) throws PluginException {
		
		// Make sure this is a rules plugin...
		rules.checkType(Plugin.Type.RULES);
		
		// Get Deck file and Rankings from Rules text file
		String[] ranking = rules.checkCSVParams("ranking");
				
		// Create rankings map and deck of cards
		/* TODO
		 * This is obviously flawed - it doesn't allow for equally ranked cards!"
		 * Might have to try a more complicated system...
		 */
		
		Map<String, Integer>cardRankings = new HashMap<String, Integer>();
		for (int i = 0; i < ranking.length; i++) {
			cardRankings.put(ranking[i], i);
		}
		return cardRankings;
	}
	
	/**
	 * Create and return an array of <tt>Plugins</tt> for creating <tt>CardPiles</tt>.
	 * 
	 * @param board Plugin to be split into card pile plugins
	 * @return array of card pile plugins
	 * @throws PluginException for invalid file, keywords, or parameters
	 */
	public static Plugin[] createPilePlugins(final Plugin board) throws PluginException {
		
		// Make sure this is a board plugin...
		board.checkType(Plugin.Type.BOARD);
		
		int numPiles = board.getNumberOf("cardpile");
				
		// Break Board into individual card piles
		Plugin[] piles = new Plugin[numPiles];
		int finish = board.checkIndexOf("cardpile");
		int start = 0;
				
		// Create Plugin for each CardPile
		for (int i = 0; i < numPiles; i++) {
			start = finish + 1;
			finish = board.getIndexOf("cardpile", start);	// find next keyword
			if (finish < 0) {
				finish = board.getIndexOf("board-layout");
			}
			piles[i] = board.divide(start, finish);
		}
		return piles;
	}
}
