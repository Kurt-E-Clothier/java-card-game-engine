/***********************************************************************//**
* @file			EngineFactory.java
* @author		Kurt E. Clothier
* @date			December 7, 2015
*
* @breif		Factory for creating game engine components
*
* @pre			Compiler: Eclipse - Mars Release (4.5.0)
* @pre			Java: JRE 7 or greater
*
* @see			http://www.projectsbykec.com/
*
* @copyright	The MIT License (MIT) - see LICENSE.txt
****************************************************************************/
package games.engine;

import games.engine.plugin.Plugin;
import games.engine.plugin.PluginException;
import games.engine.plugin.PluginKeyword;
import games.engine.util.CardDealer;
import games.engine.util.CardDealerFactory;
import games.engine.util.CardGameBoard;
import games.engine.util.CardGameBoardFactory;
import games.engine.util.CardPileCollection;
import games.engine.util.CardPileFactory;
import games.engine.util.CardPileParameter;
import games.engine.util.CardPlayer;
import games.engine.util.Deck;
import games.engine.util.DeckFactory;
import games.engine.util.GamePlayer;
import games.engine.util.PlayingCardAlias;
import games.engine.util.PlayingCardFactory;
import games.engine.util.PlayingCardRanking;

/******************************************************************//**
 * The EngineFactory Enum
 * - Access statically as EngineFactory.INSTANCE.method()
 *	 or EngineFactory factory = EngineFactory.getInstance()
 *
 * These methods are extremely UGLY, and will be cleaned up if time allows...
 *********************************************************************/
public enum EngineFactory {
	
	/** An instance of this component factory */
	INSTANCE;
	
	/* Constructs this <tt>EngineFactory</tt> when first used. */
	private EngineFactory() {}
	
	/** Return an instance of this <tt>EngineFactory</tt>. */
	public static EngineFactory getInstance()
	{
		return INSTANCE;
	}
	
	public CardGameEngine createCardGameEngine(final Plugin plugin, final GamePlayer...gamePlayers) throws PluginException {
		plugin.checkType(Plugin.Type.RULES);
		
		// Create Engine Components
		EngineComponentFactory comFactory = EngineComponentFactory.getInstance();
		EngineComponentSet<Action> actions = comFactory.createActions(plugin);
		EngineComponentSet<Condition> conditions = comFactory.createConditions(plugin);
		EngineComponentSet<ControlledAction> cActions = comFactory.createControlledActions(plugin, conditions, actions);
		EngineComponentSet<Phase> phases = comFactory.createPhases(plugin, actions, conditions, cActions);
		EngineComponentSet<PlayingCardAlias> aliases = comFactory.createAliases(plugin);
		EngineComponentCollecion components = new EngineComponentCollecion(conditions, actions, cActions, phases, aliases);
		
		// Create CardPile collections
		final Plugin boardPlugin = new Plugin(Plugin.Type.BOARD, plugin.checkParamsFor(PluginKeyword.BOARD));
		final CardPileFactory pileFactory = CardPileFactory.getInstance();
		final Plugin[] pilePlugins = pileFactory.createPlugins(boardPlugin);
		
		// Create Card Players using the passed game players...
		final CardPlayer[] players = new CardPlayer[gamePlayers.length];
		final String startPhase = plugin.checkParamsFor(PluginKeyword.START_PHASE);
		if (!phases.contains(startPhase)) {
			throw PluginException.create(PluginException.Type.INVALID_PARAMETER, plugin, PluginKeyword.START_PHASE.toString(), startPhase);
		}
		for (int i = 0; i < players.length; i++) {
			// Read rules to get direction of play, order players in array accordingly
			switch (PluginKeyword.DIRECTION_OF_PLAY.checkBoundedParams(plugin, Engine.DirectionOfPlay.class)) {
			case CCW:
				// Put players in reverse order
				players[i] = new CardPlayer(gamePlayers[players.length - 1 - i], pileFactory.createCardPileCollection(CardPileParameter.Owner.PLAYER, pilePlugins));
				break;
			case CW:
			default:
				// Put players in forward order
				players[i] = new CardPlayer(gamePlayers[i], pileFactory.createCardPileCollection(CardPileParameter.Owner.PLAYER, pilePlugins));
				
				break;
			}
			players[i].setStartingPhase(phases.get(startPhase));
		}
		
		// Create Deck, Board, and Dealer
		final Deck deck = DeckFactory.INSTANCE.createDeck(plugin);
		final CardGameBoard board = CardGameBoardFactory.INSTANCE.createCardGameBoard(boardPlugin, deck);
		final CardPileCollection commonPiles = board.getCommonPiles();
		final CardDealer dealer = CardDealerFactory.INSTANCE.createCardDealer(plugin, commonPiles, deck, players);
		final PlayingCardRanking ranking = PlayingCardFactory.INSTANCE.createCardRanking(plugin);

		return new CardGameEngine(plugin.getFilename().getConvertedName(), components, ranking, board, dealer, players);
	}
	
}