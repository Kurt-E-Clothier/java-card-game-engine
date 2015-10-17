/***********************************************************************//**
* @file			Deck.java
* @author		Kurt E. Clothier
* @date			October 15, 2015
*
* @breif		Deck of Playing Cards for a card game
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

import java.io.File;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/************************************************************************
 * The Deck Class
 * - An individual deck of Playing Cards used in a game
 * - Card Decks are quasi-immutable
 * - Card Deck is created from a deck text file
 * - Numeric ranking for each card must be passed in
 ************************************************************************/
public class Deck {
	
/*------------------------------------------------
 	Constants and Attributes
 ------------------------------------------------*/
	private final FileCopy deckFile;			// A copy of the deck file
	private final String name;					// Name of the deck
	private final int size;						// How many cards are in the deck
	private final String grouping;				// Deck grouping (categories)
	private final String[] groups;				// Deck categories
	private final String[] faces;				// Faces of the playing cards
	private final boolean quantitySpecified;	// True if specify-quantity is set to "yes"
	private final boolean groupingSpecified;	// True if grouping & groups are non null
	private final PlayingCard[] cards;			// Set of cards for this deck
	private Random rand;						// Random number for shuffling
	private int dealNdx;

/*------------------------------------------------
 	Constructor(s)
 ------------------------------------------------*/
	/**
	 * Construct a <tt>Deck</tt> of <tt>PlayingCards</tt>.
	 * Use only for card games with no card ranking.
	 * 
	 * @param file 		File used to construct this deck
	 */
	protected Deck(final File file) {
		this(file, null);
	}
	
	/**
	 * Construct a <tt>Deck</tt> of <tt>PlayingCards</tt>.
	 * 
	 * @param file 		File used to construct this deck
	 * @param ranking	a mapping of the face names to integer values
	 */
	protected Deck(final File file, Map<String, Integer> ranking) {
		boolean tmp = false;
		deckFile = new FileCopy(file);
		name = FileCopy.convertName(deckFile.getParams("deck"));
		size = Integer.parseInt(deckFile.getParams("size"));
		faces = FileCopy.splitCSV(deckFile.getParams("faces"));
		grouping = FileCopy.convertName(deckFile.getParams("grouping"));
		groups = FileCopy.splitCSV(deckFile.getParams("groups"));
		if (deckFile.getParams("specify-quantity").equals("yes"))
			tmp = true;
		quantitySpecified = tmp;
		tmp = true;
		if ((grouping == null) || (groups == null)) {
			tmp = false;
		}
		groupingSpecified = tmp;
		
		// Generate the playing cards from deck file
		if (size == 0) {
			cards = null;
		} else {
			cards = new PlayingCard[size];		
			int fileNdx = deckFile.indexOf("cards");
			if (fileNdx >= 0) {
				
				// Put all of the card data into one string
				StringBuilder str = new StringBuilder();
				while(++fileNdx < deckFile.size()) {
					str.append(deckFile.getLine(fileNdx));
					// Handle trailing ',' if present in deck file
					if (str.charAt(str.length() - 1) != ',')
						str.append(",");
				}
				str.setLength(Math.max(str.length() - 1, 0));	// remove final ","
				String cardData = str.toString().replaceAll("\\s", "");
				
				// Split CSV 
				String[] cardsData = FileCopy.splitCSV(cardData);
				
				// Create Deck of Cards
				int dataNdx = 0;
				int quantity = 0;
				int cardNdx = 0;
				int faceNdx = 0;
				int groupNdx = 0;
				int rank = 0;
				do {
					faceNdx = Integer.parseInt(cardsData[dataNdx]);
					if (ranking != null) {
						rank = ranking.get(faces[faceNdx]);
					}

					// Create a playing card
					if (groupingSpecified) {
						groupNdx = Integer.parseInt(cardsData[dataNdx + 1]);
						cards[cardNdx] = new PlayingCard(faces[faceNdx], rank, groups[groupNdx]);
						dataNdx+=2;
					} else {
						cards[cardNdx] = new PlayingCard(faces[faceNdx], rank);
						dataNdx++;
					}
					cardNdx++;
						
					// Make this card again, if specified
					if (quantitySpecified) {
						quantity = Integer.parseInt(cardsData[dataNdx]);
						while (--quantity > 0) {
							cards[cardNdx] = new PlayingCard(cards[cardNdx-1]);
							cardNdx++;
						}
						dataNdx++;
					}
				} while (cardNdx < size);
			}
		}
		dealNdx = 0;
	}
	
/*------------------------------------------------
    Accessors and Mutators
 ------------------------------------------------*/
	/**
	 * Returns the name of this <tt>Deck</tt>.
	 * 
	 * @return the name of this deck
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Returns the name of this <tt>Deck</tt>.
	 * 
	 * @return the name of this deck
	 */
	public String getGrouping() {
		return grouping;
	}
	
	/**
	 * Returns the size this <tt>Deck</tt>.
	 * 
	 * @return the size of this deck
	 */
	public int size() {
		return size;
	}
	
/*------------------------------------------------
    Utilties
 ------------------------------------------------*/
	/**
	 * Returns a Playing Card from this Deck.
	 * 
	 * @return a Playing Card from this Deck
	 */
	public PlayingCard deal() {
		if (dealNdx < size) {
			return cards[dealNdx++];
		}
		return null;
	}
	
	/**
	 * Returns a number of Playing Cards from this deck
	 *  
	 * @param number	how many cards to return
	 * @return	a number of cards from this deck
	 */
	public PlayingCard[] deal(final int number) {
		PlayingCard[] tmpCards = null;
		if (dealNdx < size) {
			tmpCards = new PlayingCard[number];
			for (int i = 0; i < number; i++) {
				tmpCards[i] = cards[dealNdx];
				dealNdx--;
			}
		}
		return tmpCards;
	}
	
	/**
	 * Returns an array of all remaining playing cards in this deck.
	 * 
	 * @return an array of all remaining playing cards in this deck
	 */
	public PlayingCard[] dealAll() {
		PlayingCard[] tmpCards = null;
		if (dealNdx < size) {
			tmpCards = new PlayingCard[size - dealNdx];
			for (int i = size - dealNdx - 1; i >= 0; i--) {
				tmpCards[i] = cards[dealNdx];
				dealNdx--;
			}
		}
		return tmpCards;
	}
	
	/**
	 * Shuffle this deck of cards.
	 */
	public void shuffle() {
		int ndx = 0;
		PlayingCard card = null;
		rand = ThreadLocalRandom.current();
		// Durstenfeld updated Fisher-Yates shuffle
		for (int i = cards.length - 1; i > 0; i--) {
			ndx = rand.nextInt(i + 1);
			card = cards[ndx];
			cards[ndx] = cards[i];
			cards[i] = card;
		}
	}

	/**
	 * Return information about the deck.
	 */
	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		str.append("This ");
		str.append(this.name);
		str.append(" deck contains ");
		str.append(this.size);
		str.append(" cards, grouped by ");
		str.append(this.grouping);
		str.append(".");
		return str.toString();
	}
	
	/**
	 * Return verbose information about the deck.
	 */
	public String toStringVerbose() {
		StringBuilder str = new StringBuilder();
		String NEW_LINE = System.getProperty("line.separator");
		str.append(this.toString());
		if (cards != null) {
			for (PlayingCard card : cards) {
				str.append(NEW_LINE);
				str.append(card.toString());
			}
		}
		return str.toString();
	}
}
