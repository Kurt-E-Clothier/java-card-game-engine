/***********************************************************************//**
* @file			CardGameTB.java
* @author		Kurt E. Clothier
* @date			November 9, 2015
*
* @breif		The public interface for a single card game
*
* @pre			Compiler: Eclipse - Mars Release (4.5.0)
* @pre			Java: JRE 7 or greater
*
* @see			http://www.projectsbykec.com/
*
* @copyright	The MIT License (MIT) - see LICENSE.txt
****************************************************************************/

package games;

import java.io.File;
import java.util.Map;

import games.engine.util.*;

// TODO: Auto-generated Javadoc
/**
 * **********************************************************************
 * The CardGameTB Class
 * - Manually do the work of the CardGame class for testing
 * - Create Rules FileCopy
 * - Create array of CardPlayers
 * - Start Game Builder with Rules and Players
 * **********************************************************************.
 */
public final class CardGameTB {
	
/*------------------------------------------------
 	Constants and Attributes
 ------------------------------------------------*/

/**
 * Instantiates a new card game tb.
 */
/*------------------------------------------------
 	Constructor(s)
 ------------------------------------------------*/
	private CardGameTB() {}
	
/*------------------------------------------------
    Accessors and Mutators
 ------------------------------------------------*/

	
/**
 * The main method.
 *
 * @param args the arguments
 * @throws PluginException the plugin exception
 */
/*------------------------------------------------
    Main Method for Testing
 ------------------------------------------------*/	
	public static void main(String[] args) throws PluginException {
		System.out.println(" --- Card Game Test Bench ---\n");
		
		// Get Rules file
		File rulesFile = Plugin.createFile(Plugin.Type.RULES, "test");
		Plugin rulesPlugin = new Plugin(rulesFile);
		
		// Create card deck
		Map<String, Integer> cardRankings = GameComponentFactory.createRankings(rulesPlugin);
		Plugin deckPlugin = new Plugin(Plugin.Type.DECK, rulesPlugin.checkParams("deck"));
		Deck deck = GameComponentFactory.createDeck(deckPlugin, cardRankings);
		
		/*
		
		// Create Gameboard
		Plugin boardPlugin = new Plugin(Plugin.Type.BOARD, rulesPlugin.checkParams("board"));
		CardGameboard board = GameComponentFactory.createCardGameboard(boardPlugin);
		
		// Get Plugins for each cardpile
		Plugin[] piles = GameComponentFactory.createPilePlugins(boardPlugin);
		
		// Create Card Players
		CardPlayer[] players = new CardPlayer[3];
		players[0] = new CardPlayer("Kurt");
		players[1] = new CardPlayer("Kyle");
		players[2] = new CardPlayer("Lovedeep");
		
		System.out.println(deck.toStringVerbose());
		
		CardPile pile = GameComponentFactory.createPile(piles[0]);
		pile.add(deck.deal());
		pile.add(deck.deal());
		
		pile.add(deck.deal(), deck.deal(), deck.deal());
		
		System.out.println("Top: " + pile.getTop());
		
		PlayingCard[] cards = deck.deal(3);
		
		for (PlayingCard c : cards) {
			System.out.println("Dealt: " + c.toString());
		}
		
		pile.add(cards);
		
		System.out.println("Top: " + pile.getTop());
		
		pile.add(deck.deal(5));
		
		cards = pile.getAll();
		
		System.out.println("\nIn card pile: ");
		for (PlayingCard c : cards) {
			System.out.println(c.toString());
		}
		
		*/
		
		
		// Create card game engine
		//CardGameEngine engine = GameComponentFactory.createCardGameEngine(rulesPlugin, players);
		
		// TODO
		// TEST DEALER FUNCTIONALITY!!!!
		
		
		
		
		
		//CardGameEngineBuilder test = new CardGameEngineBuilder(rules, null);
		//System.out.println(board.getSize());
	}
	
}

