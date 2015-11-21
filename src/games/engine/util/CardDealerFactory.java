/***********************************************************************//**
* @file			CardDealerFactory.java
* @author		Kurt E. Clothier
* @date			November 18, 2015
*
* @breif		Factory for creating a card game dealer
*
* @pre			Compiler: Eclipse - Mars Release (4.5.0)
* @pre			Java: JRE 7 or greater
*
* @see			http://www.projectsbykec.com/
*
* @copyright	The MIT License (MIT) - see LICENSE.txt
****************************************************************************/
package games.engine.util;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import games.engine.plugin.Plugin;
import games.engine.plugin.PluginException;
import games.engine.plugin.PluginKeyword;
import games.engine.plugin.PluginKeywordCode;
import games.engine.plugin.PluginPattern;

/******************************************************************//**
 * The CardDealerFactory Enum
 * - Access statically as CardDealerFactory.INSTANCE.method()
 *	 or CardDealerFactory factory = CardDealerFactory.getInstance()
 *********************************************************************/
public enum CardDealerFactory {
	
	INSTANCE;
	
	/*
	 * Constructs this <tt>CardDealerFactory</tt> when first used.
	 */
	private CardDealerFactory() {
	}
	
	/**
	 * Return an instance of this <tt>CardDealerFactory</tt>.
	 * 
	 * @return an instance of this CardDealerFactory
	 */
	public static CardDealerFactory getInstance()
	{
		return INSTANCE;
	}
	
	/**
	 * Creates and returns a new <tt>CardDealer</tt> for a card game.
	 * 
	 * @param rulesPlugin plugin file used to create this dealer
	 * @param board CardGameBoard to deal on
	 * @param players array of players in this game
	 * @return new CardDealer for a card game
	 * @throws PluginException for invalid or missing keywords and parameters
	 */
	public synchronized CardDealer createCardDealer(final Plugin rulesPlugin, final CardPileCollection commonPileCollecion, 
													final Deck deck, final CardPlayer[] players) throws PluginException {
		rulesPlugin.checkType(Plugin.Type.RULES);
		final Plugin plugin = rulesPlugin.divide(PluginKeyword.DEAL, PluginKeyword.DEAL_END);
		
		// Get Parameters
		final boolean collate = PluginKeyword.COLLATE.getBooleanParams(plugin);
		final boolean shuffle = PluginKeyword.SHUFFLE.getBooleanParams(plugin);
		final CardDealer.Direction direction = PluginKeyword.DIRECTION.checkBoundedParams(plugin, CardDealer.Direction.class);
		//final CardPileCollection commonPileCollection = board.getCommonPiles();
		
		// Determine number of piles being dealt
		int numPlayerPiles = 0;
		int numCommonPiles = 0;
		final int commonIndex = plugin.getIndexOf(PluginKeyword.COMMON_PILES);
		final int playerIndex = plugin.getIndexOf(PluginKeyword.PLAYER_PILES);
		final PluginKeywordCode code = plugin.compare(PluginKeyword.PLAYER_PILES, PluginKeyword.COMMON_PILES);
		switch (code) {
		case A_GREATER:
			numCommonPiles = playerIndex - (commonIndex + 1);
			// continue
		case A_EXISTS:
			numPlayerPiles = plugin.getSize() - (playerIndex + 1);
			break;
		case B_GREATER:
			numPlayerPiles = commonIndex - (playerIndex + 1);
			// Continue
		case B_EXISTS:
			numCommonPiles = plugin.getSize() - (commonIndex + 1);
			break;
		case NEITHER_EXISTS:
		default:
			throw PluginException.create(PluginException.Type.MISSING_KEYWORD, rulesPlugin, 
					 PluginKeyword.COMMON_PILES.toString(), PluginKeyword.PLAYER_PILES.toString());
		}
		
		// Generate player pile map
		final CardDealerSet playerPairs = new CardDealerSet(numPlayerPiles);
		if (numPlayerPiles > 0) {
			for (int i = playerIndex + 1; i < numPlayerPiles + playerIndex + 1; i++) {
				String[] line = plugin.getLine(i).split(PluginPattern.WHITESPACE.toString());
				
				// Make sure there is just a pile name and number of cards, the card name is new, and it is a valid card pile
				if (line.length != 2 || 
					playerPairs.contains(line[0]) ||
					!players[0].getPlayerPiles().contains(line[0])) {
					throw PluginException.create(PluginException.Type.INVALID_PARAMETER, plugin, PluginKeyword.PLAYER_PILES.toString(), line[0]);
				}
				else {
					// Calculate the number of cards to deal
					int numToDeal = 0;
					final CardDealer.Deal deal = PluginKeyword.getBoundedParams(plugin, CardDealer.Deal.values(), line[0]);
					if (deal == null) {
						numToDeal = PluginKeyword.checkPositiveNumericParams(plugin, line[0]);
					}
					else {
						switch (deal) {
						case ALL:
							numToDeal = deck.getSize();
							break;
						case NONE:
						default:
							numToDeal = 0;
							break;
						}
					}
					playerPairs.add(new CardDealerPair(line[0], numToDeal));
				}
				
			}
		}
		
		// Generate common pile map
		final CardDealerSet commonPairs = new CardDealerSet(numCommonPiles);
		if (numCommonPiles > 0) {
			for (int i = commonIndex + 1; i < numCommonPiles + commonIndex + 1; i++) {
				String[] line = plugin.getLine(i).split(PluginPattern.WHITESPACE.toString());
				
				// Make sure there is just a pile name and number of cards, the card name is new, and it is a valid card pile
				if (line.length != 2 || 
					commonPairs.contains(line[0]) ||
					!commonPileCollecion.contains(line[0])) {
					throw PluginException.create(PluginException.Type.INVALID_PARAMETER, plugin, PluginKeyword.COMMON_PILES.toString(), line[0]);
				}
				else {
					// Calculate the number of cards to deal
					int numToDeal = 0;
					final CardDealer.Deal deal = PluginKeyword.getBoundedParams(plugin, CardDealer.Deal.values(), line[0]);
					if (deal == null) {
						numToDeal = PluginKeyword.checkPositiveNumericParams(plugin, line[0]);
					}
					else {
						switch (deal) {
						case ALL:
							numToDeal = deck.getSize();
							break;
						case NONE:
						default:
							numToDeal = 0;
							break;
						}
					}
					commonPairs.add(new CardDealerPair(line[0], numToDeal));
				}
			}
		}
		
		// See if more than one pile wants all the cards...
		boolean allDealt = false;
		
		for (int i = 0 ; i < playerPairs.getSize(); i++) {
			if (playerPairs.getNumCardsToDeal(i) == deck.getSize()) {
				if (allDealt) {
					throw new PluginException("No cards left to deal for player-pile \"" + playerPairs.getCardPileName(i) + "\" in file: " + plugin.getFilename().getName(),
							PluginException.Type.DATA_REPRESENTATION);
				}
				else {
					allDealt = true;
				}
			}
		}
		for (int i = 0 ; i < commonPairs.getSize(); i++) {
			if (commonPairs.getNumCardsToDeal(i) == deck.getSize()) {
				if (allDealt) {
					throw new PluginException("No cards left to deal for common-pile \"" + commonPairs.getCardPileName(i) + "\" in file: " + plugin.getFilename().getName(),
							PluginException.Type.DATA_REPRESENTATION);
				}
				else {
					allDealt = true;
				}
			}
		}
		return new CardDealer(commonPileCollecion, deck, players, direction, collate, shuffle, playerPairs, commonPairs);
	}
}

