/***********************************************************************//**
* @file			DeckFactory.java
* @author		Kurt E. Clothier
* @date			November 19, 2015
*
* @breif		Factory for creating Deck of Playing Cards
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
import games.engine.util.Deck;

/******************************************************************//**
 * The DeckFactory Enum
 * - Access statically as DeckFactory.INSTANCE.method()
 *	 or DeckFactory factory = DeckFactory.getInstance()
 ********************************************************************/
public enum DeckFactory {
	
	INSTANCE;
	
	/*
	 * Constructs this <tt>DeckFactory</tt> when first used.
	 */
	private DeckFactory() {
	}
	
	/**
	 * Return an instance of this <tt>DeckFactory</tt>.
	 * 
	 * @return an instance of this DeckFactory
	 */
	public static DeckFactory getInstance()
	{
		return INSTANCE;
	}
	
	/**
	 * Creates and returns a new <tt>Deck</tt> using the specified <tt>Plugin</tt>.
	 * If a rules plugin is passed, a deck plugin will be created 
	 * by parsing the rules for the "deck" keyword.
	 * 
	 * @param plugin Plugin used to create this deck
	 * @return a new deck of playing cards
	 * @throws PluginException if the file, keywords, or parameters are invalid
	 */
	public synchronized Deck createDeck(final Plugin plugin) throws PluginException {
		
		// Get correct Plugin file
		Plugin deck = null;
		switch (plugin.getFilename().getType()) {
		case RULES:
			deck = new Plugin(Plugin.Type.DECK, plugin.checkParamsFor(PluginKeyword.DECK));
			break;
		case DECK:
			deck = plugin;
			break;
		default:
			throw PluginException.create(PluginException.Type.INVALID_TYPE, plugin);
		}
		
		// Mandatory attributes
		final String name = deck.checkName();
		final String[] faces = deck.checkCSVParamsFor(PluginKeyword.FACES);
				
		// Optional Grouping and (Partially optional) Groups (suits, colors, etc)
		final boolean groupingSpecified = PluginKeyword.GROUPING.exists(deck);
		final String grouping = groupingSpecified ? deck.checkParamsFor(PluginKeyword.GROUPING) : null;
		final String[] groups = groupingSpecified ? deck.checkCSVParamsFor(PluginKeyword.GROUPS) : null;
				
		// Optional specify-quantity
		final boolean quantitySpecified = PluginKeyword.SPECIFY_QUANTITY.getBooleanParams(deck);

		// Put all of the card data into one string
		int fileNdx = deck.checkIndexOf(PluginKeyword.CARDS);
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
		final int size = PluginKeyword.SIZE.checkPositiveNumericParams(deck);

		// Create Deck of Cards
		final PlayingCard[] cards = new PlayingCard[size];
		final PlayingCardFactory factory = PlayingCardFactory.getInstance();
		PlayingCardFace face = null;
		PlayingCardGroup group = null;
		int dataNdx = 0;
		int quantity = 0;
		int cardNdx = 0;
		int faceNdx = 0;
		int groupNdx = 0;
		do {
			try {
				faceNdx = Integer.parseInt(cardData[dataNdx]);
				face = factory.createFace(faces[faceNdx]);
				
				// Create a playing card
				if (groupingSpecified) {
					groupNdx = Integer.parseInt(cardData[dataNdx + 1]);
					group = factory.createGroup(groups[groupNdx]);
					cards[cardNdx] = factory.createPlayingCard(face, group);
					dataNdx+=2;
				} else {
					cards[cardNdx] = factory.createPlayingCard(face, null);
					dataNdx++;
				}
				cardNdx++;
					
				// Make this card again, if specified
				if (quantitySpecified) {
					quantity = Integer.parseInt(cardData[dataNdx]);
					while (--quantity > 0) {
						cards[cardNdx] = cards[cardNdx-1];
						cardNdx++;
					}
					dataNdx++;
				}
			} catch (ArrayIndexOutOfBoundsException e) {
				throw PluginException.create(PluginException.Type.DATA_REPRESENTATION, e, deck, "cards");
			}
		} while (cardNdx < size);
		if (cardData.length > dataNdx) {
			throw PluginException.create(PluginException.Type.DATA_REPRESENTATION, deck, "cards");
		}
		return new Deck(name, grouping, cards);
	}
}
