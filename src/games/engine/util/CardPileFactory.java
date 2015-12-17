/***********************************************************************//**
* @file			CardPileFactory.java
* @author		Kurt E. Clothier
* @date			November 19, 2015
*
* @breif		Factory for creating Playing Card Piles and related
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
import games.engine.plugin.PluginKeywordCode;

/******************************************************************//**
 * The CardPileFactory Enum
 * - Access statically as CardPileFactory.INSTANCE.method()
 *	 or CardPileFactory factory = CardPileFactory.getInstance()
 ********************************************************************/
public enum CardPileFactory {
	
	INSTANCE;
	
	/*
	 * Constructs this <tt>CardPileFactory</tt> when first used.
	 */
	private CardPileFactory() {
	}
	
	/**
	 * Return an instance of this <tt>CardPileFactory</tt>.
	 * 
	 * @return an instance of this CardPileFactory
	 */
	public static CardPileFactory getInstance()
	{
		return INSTANCE;
	}
	
	/**
	 * Creates and returns a new <tt>CardPile</tt>.
	 * 
	 * @param plugin Board Plugin used to create this card pile
	 * @return a new card pile
	 * @throws PluginException for invalid file, keywords, or parameters
	 */
	public synchronized CardPile createCardPile(final Plugin plugin) throws PluginException {
		
		String name = plugin.checkParamsFor(PluginKeyword.NAME);
		
		// Get the parameters found after the plugin keywords - returned as an enum member
		final CardPileParameter.Owner owner = PluginKeyword.OWNER.checkBoundedParams(plugin, CardPileParameter.Owner.class);
		final CardPileParameter.Visibility visibility = PluginKeyword.VISIBILITY.checkBoundedParams(plugin, CardPileParameter.Visibility.class);
		final CardPileParameter.Placement placement = PluginKeyword.PLACEMENT.checkBoundedParams(plugin, CardPileParameter.Placement.class);
		final CardPileParameter.Orientation orientation = PluginKeyword.ORIENTATION.checkBoundedParams(plugin, CardPileParameter.Orientation.class);
		final CardPileParameter.Removal removal = PluginKeyword.REMOVAL.checkBoundedParams(plugin, CardPileParameter.Removal.class);
		CardPileParameter.Tiling tiling = null;
		switch (placement) {
		case SPREAD:
		case SPACED:
			tiling = PluginKeyword.TILING.checkBoundedParams(plugin, CardPileParameter.Tiling.class);
			break;
		default:
			tiling = null;
			break;
		}
		// A bit of extra care for the "Visible" Keyword (can be a number or word)
		int numVisible = 0;
		CardPileParameter.Visible visible = PluginKeyword.VISIBLE.getBoundedParams(plugin, CardPileParameter.Visible.values());
		if (visible == null) {
			visible = CardPileParameter.Visible.NUMBER;
			numVisible = PluginKeyword.VISIBLE.checkPositiveNumericParams(plugin);
		}
		return new CardPile(new CardPileParameter(name, owner, visibility, visible, numVisible, placement, orientation, tiling, removal));
	}
	
	/**
	 * Create and return an array of <tt>Plugins</tt> for creating <tt>CardPiles</tt>.
	 * 
	 * @param plugin Board Plugin used to create this these CardPile plugins
	 * @return array of card pile plugins
	 * @throws PluginException for invalid file, keywords, or parameters
	 */
	public synchronized Plugin[] createPlugins(final Plugin plugin) throws PluginException {
		
		plugin.checkType(Plugin.Type.BOARD);
		
		int numPiles = plugin.getNumberOf(PluginKeyword.CARDPILE);
				
		// Break Board into individual card piles
		Plugin[] piles = new Plugin[numPiles];
		int finish = plugin.checkIndexOf(PluginKeyword.CARDPILE);
		int start = 0;
				
		// Create Plugin for each CardPile
		for (int i = 0; i < numPiles; i++) {
			start = finish + 1;
			finish = plugin.getIndexOf(PluginKeyword.CARDPILE, start);
			if (finish < 0) {
				finish = plugin.getIndexOf(PluginKeyword.BOARD_LAYOUT);
			}
			piles[i] = plugin.divide(start, finish);
		}
		return piles;
	}
	
	/**
	 * Create a new <tt>CardPileCollecion</tt> for the specified owner.
	 * 
	 * @param owner the owner of the piles to return
	 * @param piles array of plugins to use in creating these card piles
	 * @return array of card piles for the specified owner
	 * @throws PluginException for invalid file, keywords, or parameters
	 */
	public synchronized CardPileCollection createCardPileCollection(final CardPileParameter.Owner owner, final Plugin...pilePlugins) throws PluginException {
		// Create search string
		final StringBuilder str = new StringBuilder();
		final String searchString = str.append(PluginKeyword.OWNER.toString())
									   .append(' ').append(owner.toString().toLowerCase()).toString();
		
		// Get the number of piles owned by the specified owner
		int numOwnedPiles = 0;
		for (Plugin p : pilePlugins) {
			if (p.getIndexOf(searchString, 0) >= 0) {
				numOwnedPiles++;
			}
		}
		// Create card piles for the specified owner
		int pileNdx = 0;
		final CardPile[] cardPiles = new CardPile[numOwnedPiles];
		for (Plugin p : pilePlugins) {
			if (p.getIndexOf(searchString, 0) >= 0) {
				cardPiles[pileNdx++] = this.createCardPile(p);
			}
		}
		return new CardPileCollection(cardPiles);
	}
	
	/**
	 * Create a new <tt>CardPileLayout</tt> for the specified owner.
	 * 
	 * @param owner the owner of the piles in the layout to return
	 * @param plugin Board Plugin containing layout
	 * @return layout for the card piles of the specified owner
	 * @throws PluginException for invalid file, keywords, or parameters
	 */
	public synchronized CardPileLayout createCardPileLayout(final CardPileParameter.Owner owner, final Plugin plugin) throws PluginException {
		// Make sure this is a board plugin...
		plugin.checkType(Plugin.Type.BOARD);
		
		int start = 0;
		int end = 0;
		final int commonIndex = plugin.getIndexOf(PluginKeyword.COMMON_PILES);
		final int playerIndex = plugin.getIndexOf(PluginKeyword.PLAYER_PILES);
		final PluginKeywordCode code = plugin.compare(PluginKeyword.COMMON_PILES, PluginKeyword.PLAYER_PILES);

		switch (owner) {
		case COMMON:
			start = commonIndex + 1;
			switch (code) {
			
			case A_EXISTS:
			case A_GREATER:
				end = plugin.getSize();
				break;
			case B_GREATER:
				end = playerIndex;
				break;
			case B_EXISTS:
			case NEITHER_EXISTS:
			default:
				throw PluginException.create(PluginException.Type.MISSING_KEYWORD, plugin, PluginKeyword.COMMON_PILES.toString());
			}
			break;
			
		case PLAYER:
			start = playerIndex + 1;
			switch (code) {
			
			case B_EXISTS:
			case B_GREATER:
				end = plugin.getSize();
				break;
			case A_GREATER:
				end = playerIndex;
				break;
			case A_EXISTS:
			case NEITHER_EXISTS:
			default:
				throw PluginException.create(PluginException.Type.MISSING_KEYWORD, plugin, PluginKeyword.PLAYER_PILES.toString());
			}
			break;
			
		default:
			throw new IllegalArgumentException("Switch case does not include all types of owners!");
		}
		if (start == end) {
			throw PluginException.create(PluginException.Type.MISSING_PARAMETER, plugin, 
					PluginKeyword.COMMON_PILES.toString(), PluginKeyword.PLAYER_PILES.toString());
		}
		// Copy the layout text
		final String[] lines = new String[end - start];
		for (int i = 0; i < lines.length; i++) {
			lines[i] = plugin.getLine(start++);
		}
		return new CardPileLayout(lines);
	}
}

