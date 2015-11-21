/***********************************************************************//**
* @file			CardGameBoardFactory.java
* @author		Kurt E. Clothier
* @date			November 19, 2015
*
* @breif		Factory for creating card game boards
*
* @pre			Compiler: Eclipse - Mars Release (4.5.0)
* @pre			Java: JRE 7 or greater
*
* @see			http://www.projectsbykec.com/
*
* @copyright	The MIT License (MIT) - see LICENSE.txt
****************************************************************************/
package games.engine.util;

import games.engine.plugin.Plugin;
import games.engine.plugin.PluginException;
import games.engine.plugin.PluginKeyword;

/******************************************************************//**
 * The CardGameBoardFactory Enum
 * - Access statically as CardGameBoardFactory.INSTANCE.method()
 *	 or CardGameBoardFactory factory = CardGameBoardFactory.getInstance()
 *********************************************************************/
public enum CardGameBoardFactory {
	
	INSTANCE;
	
	/*
	 * Constructs this <tt>CardGameBoardFactory</tt> when first used.
	 */
	private CardGameBoardFactory() {
	}
	
	/**
	 * Return an instance of this <tt>CardGameBoardFactory</tt>.
	 * 
	 * @return an instance of this CardGameBoardFactory
	 */
	public static CardGameBoardFactory getInstance()
	{
		return INSTANCE;
	}
	
	/**
	 * Create and return a <tt>CardGameBoard</tt> using the specified <tt>Plugin</tt>.
	 * If a rules plugin is passed, a deck plugin will be created 
	 * by parsing the rules for the "board" keyword.
	 * 
	 * @param rules Rules Plugin used to create this card gameboard
	 * @return new CardGameBoard
	 * @throws PluginException for invalid file, keywords, or parameters
	 */
	public synchronized CardGameBoard createCardGameBoard(final Plugin rulesPlugin) throws PluginException {
		// Create Plugins
		rulesPlugin.checkType(Plugin.Type.RULES);
		final Plugin boardPlugin = new Plugin(Plugin.Type.BOARD, rulesPlugin.checkParamsFor(PluginKeyword.BOARD));
		final Deck deck = DeckFactory.INSTANCE.createDeck(rulesPlugin);
		boardPlugin.checkName();
		// Mandatory attributes
		final CardPileFactory factory = CardPileFactory.getInstance();
		final String name = boardPlugin.getFilename().getConvertedName();
		final Plugin[] pilePlugins = factory.createPlugins(boardPlugin);
		final CardPileCollection commonPiles = factory.createCardPileCollection(CardPileParameter.Owner.COMMON, pilePlugins);
		final CardGameBoardLayout layout = this.createCardGameBoardLayout(boardPlugin);
		return new CardGameBoard(name, deck, commonPiles, layout);
	}
	
	/**
	 * Create and return a <tt>CardGameBoardLayout using the specifed <tt>Plugin</tt>.
	 * 
	 * @param board Plugin used to create this card game board layout
	 * @return card game board layout
	 * @throws PluginException for invalid file, keywords, or parameters
	 */
	public synchronized CardGameBoardLayout createCardGameBoardLayout(final Plugin board) throws PluginException {
		board.checkType(Plugin.Type.BOARD);
		
		// Mandatory Parameters
		final CardGameBoardLayout.Shape shape = 
				PluginKeyword.SHAPE.checkBoundedParams(board, CardGameBoardLayout.Shape.class);
		final CardGameBoardLayout.Player_Layout pLayout = 
				PluginKeyword.PLAYER_LAYOUT.checkBoundedParams(board, CardGameBoardLayout.Player_Layout.class);
		
		// Optional dealer
		boolean dealerPresent = PluginKeyword.DEALER.getBooleanParams(board);
		
		// Check for pile layouts
		final int commonIndex = board.getIndexOf(PluginKeyword.COMMON_PILES);
		final int playerIndex = board.getIndexOf(PluginKeyword.PLAYER_PILES);
		// Have to have at least one or the other
		if (commonIndex < 0 && playerIndex < 0) {
			throw PluginException.create(PluginException.Type.MISSING_KEYWORD, board, 
					PluginKeyword.COMMON_PILES + "\" and \"" + PluginKeyword.PLAYER_PILES);
		}
		final CardPileLayout commonLayout = commonIndex < 0 ? new CardPileLayout(null) :
														CardPileFactory.INSTANCE.createCardPileLayout(CardPileParameter.Owner.COMMON, board);
		final CardPileLayout playerLayout = playerIndex < 0 ? new CardPileLayout(null) :
														CardPileFactory.INSTANCE.createCardPileLayout(CardPileParameter.Owner.PLAYER, board);
		return new CardGameBoardLayout(shape, pLayout, dealerPresent, commonLayout, playerLayout);
	}
}

