/***********************************************************************//**
* @file			CardGame.java
* @author		Kurt E. Clothier
* @date			October 13, 2015
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

package games.engine.util;

import javax.swing.*;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

/************************************************************************
 * The CardGame Class
 * - This class serves as the public interface for a card game
 * - All necessary objects are created and managed by the CardGame
 * - A driver class is required to actually play through a game
 ************************************************************************/
public final class CardGame {
	
	
/*------------------------------------------------
 	Constants and Attributes
 ------------------------------------------------*/
	private final FileCopy rules;	// a local copy of the rules file
	private final Deck deck;		// the card deck to be used
	
/*------------------------------------------------
 	Constructor(s)
 ------------------------------------------------*/
	/*
	 * Construct a <tt>CardGame</tt> using the rules file.
	 * 
	 * @param file 	Rules file used to construct this game
	 */
	public CardGame(File rulesFile) {
		rules = new FileCopy(rulesFile);

		// Read Rules to create Deck of Playing Cards
		File deckFile = FileIO.getFile(FileIO.Type.DECK, rules.getParams("deck"));
		String[] ranking = FileCopy.splitCSV(rules.getParams("ranking"));
		Map<String, Integer>cardRankings = new HashMap<String, Integer>((int)(ranking.length/0.75 +1));
		for (int i = 0; i < ranking.length; i++) {
			cardRankings.put(ranking[i], i);
		}
		deck = new Deck(deckFile, cardRankings);
		
		// Read Rules to create playing area
		// TODO
	}
	
/*------------------------------------------------
    Accessors and Mutators
 ------------------------------------------------*/
	
	/**
	 * Returns the <tt>Deck</tt> used in 
	 * this game.
	 * @return the deck used in this game
	 */
	public Deck getDeck() {
		return deck;
	}

/*------------------------------------------------
 	Main Method for Testing
 ------------------------------------------------*/
	public static void main(String[] args) {
		System.out.println("--- CardGame Test Bench ---");

		JFrame board = new GameGUI();
		board.setVisible(true);

		File file = FileIO.getFile(FileIO.Type.RULES, "idiot");
		CardGame game = new CardGame(file);
		Deck deck = game.getDeck();
		deck.shuffle();

		System.out.println(deck.toStringVerbose());
		System.out.println(deck.deal().toString());
		System.out.println(deck.deal().toString());
		System.out.println(deck.deal().toString());
		System.out.println(deck.deal().toString());
	}
}

