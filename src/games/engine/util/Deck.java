/***********************************************************************//**
* @file			Deck.java
* @author		Kurt E. Clothier
* @date			October 12, 2015
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
import java.util.HashMap;
import java.util.Map;


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
			
			// Variables for creating cards
			int dataNdx = 0;
			int quantity = 0;
			int cardNdx = 0;
			int tmpNdx = 0;
			int faceNdx = 0;
			int groupNdx = 0;
			int rank = 0;
			boolean firstPass = false;
			
			do {
				tmpNdx = dataNdx;
				quantity = 1;
				firstPass = true;
				do {
					dataNdx = tmpNdx;
					faceNdx = Integer.parseInt(cardsData[dataNdx]);
					if (ranking != null) {
						rank = ranking.get(faces[faceNdx]);
					}

					// Create a playing card
					if (groupingSpecified) {
						groupNdx = Integer.parseInt(cardsData[dataNdx + 1]);
						cards[cardNdx] = new PlayingCard(faces[faceNdx], rank, groups[groupNdx]);
						dataNdx++;
					} else {
						cards[cardNdx] = new PlayingCard(faces[faceNdx], rank);
					}
					cardNdx++;
					
					// Make this card again, if specified
					if (quantitySpecified) {
						dataNdx++;
						if (firstPass) {
							firstPass = false;
							quantity = Integer.parseInt(cardsData[dataNdx]);
						}
					}
				} while (--quantity > 0);
				dataNdx++;
			} while (cardNdx < size);
		}
	}
	
/*------------------------------------------------
    Accessors and Mutators
 ------------------------------------------------*/
	/**
	 * Returns the name of this <tt>Deck</tt>.
	 * 
	 * @return the name of this deck
	 */
	protected String getName() {
		return name;
	}
	
	/**
	 * Returns the name of this <tt>Deck</tt>.
	 * 
	 * @return the name of this deck
	 */
	protected String getGrouping() {
		return grouping;
	}
	
	/**
	 * Returns the size this <tt>Deck</tt>.
	 * 
	 * @return the size of this deck
	 */
	protected int size() {
		return size;
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
	
	public String toStringVerbose() {
		StringBuilder str = new StringBuilder();
		String NEW_LINE = System.getProperty("line.separator");
		str.append(this.toString());
		for (PlayingCard card : cards) {
			str.append(NEW_LINE);
			str.append(card.toString());
		}
		return str.toString();
	}

/*------------------------------------------------
 	Main Method
 ------------------------------------------------*/
	public static void main(String[] args) {
		
		System.out.println(" --- Deck Test Driver ---\n");
		int capacity = 13;
		Map<String, Integer> val = new HashMap<String, Integer>((int)(capacity/0.75 +1));
		
		// Testing Idiot
		File file = FileIO.getFile(FileIO.Type.DECK, "french_suits");
		for (Integer x = 2; x < 11; x++) {
			val.put(x.toString(), x);
		}
		val.put("jack", 11);
		val.put("queen", 12);
		val.put("king", 13);
		val.put("ace", 14);
		
		Deck deck = new Deck(file, val);
		System.out.println(deck.toStringVerbose());
	}

}
