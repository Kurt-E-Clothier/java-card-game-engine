/***********************************************************************//**
* @file			PluginKeyword.java
* @author		Kurt E. Clothier
* @date			November 18, 2015
* 
* @breif		Keywords used with Plugins
*
* @pre			Compiler: Eclipse - Mars Release (4.5.0)
* @pre			Java: JRE 7 or greater
*
* @see			http://www.projectsbykec.com/
*
* @copyright	The MIT License (MIT) - see LICENSE.txt
****************************************************************************/
package games.engine.plugin;

public enum PluginKeyword {
	
	/** Name - what is this plugin called */
	NAME,
	
	/** Player-Piles - specifies a player pile */
	PLAYER_PILES,
	
	/** Common-Piles - specifies a common pile */
	COMMON_PILES,
	
/*------------------------------------------------
 	Rules Specific Keywords
 ------------------------------------------------*/
	/** Players - Number of players for this game */
	PLAYERS,
	
	/** Deck - Name of deck to use */
	DECK,
	
	/** Ranking - specify the card ranking */
	RANKING,
	
	/** End-Ranking - end of card ranking */
	END_RANKING,
	
	/** Alias - specify a card name alias */
	ALIAS,
	
	/** Board - Name of board to use */
	BOARD,
	
	/** Direction-of-Play - direction of play among players */
	DIRECTION_OF_PLAY,
	
	/** Deal - start deal info section */
	DEAL,
	
	/** End-Deal - end deal info section */
	END_DEAL,
	
	/** Shuffle - should cards be shuffled before being dealt */
	SHUFFLE,
	
	/** Direction - how cards are dealt */
	DIRECTION,
	
	/** Collate - should the dealing be collated */
	COLLATE,
	
	/** Start-Phase - name of the starting phase for players */
	START_PHASE,
	
	/** Phase - start of a game phase description */
	PHASE,
	
	/** End-Phase - End of a game phase description */
	END_PHASE,
	
	/** Action - Describes a type of action */
	ACTION,
	
	/** End-Action - End of actions */
	END_ACTION,
	
	/** Controlled-Action - Describes a conditional action */
	CONTROLLED_ACTION,
	
	/** End-Controlled-Action - End of controlled actions */
	END_CONTROLLED_ACTION,
	
	/** Condition - Describes a boolean expression */
	CONDITION,
	
	/** End-Condition - End of Conditions */
	END_CONDITION,
	
	/** Repeat - do an action again */
	REPEAT,
	
	/** Do - perform some action */
	DO,
	
	/** Allow - allow some action to be performed */
	ALLOW,
	
	/** End-Allow - end of allowed actions */
	END_ALLOW,
	
/*------------------------------------------------
 	Board Specific Keywords
 ------------------------------------------------*/
	/** Cardpile - Begin cardpile attributes */
	CARDPILE,
	
	/** Owner - who owns this card pile*/
	OWNER,
	
	/** Visibility - who can see a card pile */
	VISIBILITY,
	
	/** Visible - number of face up cards on top of pile */
	VISIBLE,
	
	/** Placement - how cards are placed on the table */
	PLACEMENT,
	
	/** Orientation - how piles are oriented with respect to player */
	ORIENTATION,
	
	/** Tiling - how some piles are layed out */
	TILING,
	
	/** Removal - how cards are removed from pile */
	REMOVAL,
	
	/** Board-Layout - Describes the board layout */
	BOARD_LAYOUT,
	
	/** Shape - shape of playing area */
	SHAPE,
	
	/** Dealer - if space is reserved for a dealer */
	DEALER,
	
	/** Player-Layout - how players are arranged */
	PLAYER_LAYOUT,
	
/*------------------------------------------------
 	Deck Specific Keywords
 ------------------------------------------------*/
	
	/** Size - size of this deck */
	SIZE,
	
	/** Faces - specify the faces of cards in this deck */
	FACES,
	
	/** Grouping - specify how cards are grouped */
	GROUPING,
	
	/** Groups - specify the card groups */
	GROUPS,
	
	/** Specify-Quantity - should quantity be specified */
	SPECIFY_QUANTITY,
	
	/** Cards - list the cards in this deck */
	CARDS;
	
	/*
	 * Construct this plugin keyword.
	 */
	private PluginKeyword() {}
	
	/**
	 * Compare this keyword to the specified string.
	 * Returns <tt>true</tt> if they are the same, ignoring case.
	 * 
	 * @param string the string to compare to this keyword 
	 * @return <tt>true</tt> if they are the same, ignoring case
	 */
	public boolean compareToString(final String string) {
		return string != null && string.equalsIgnoreCase(this.toString());
	}
	
	/**
	 * Returns the parameter(s) to upper case found after the first occurrence of this keyword in the specified <tt>Plugin</tt>.
	 * Designed for use with Enum.valueOf() operations...
	 * 
	 * @param plugin plugin to be searched
	 * @return parameters found after this keyword
	 * @throws PluginException if the keyword or parameters are not found
	 */
	public String checkParams(final Plugin plugin) throws PluginException {
		return plugin.checkParamsFor(this).toUpperCase(Plugin.LOCALE);
	}
	
	/**
	 * Returns the numeric parameter found after the first occurrence of this keyword in the specified <tt>Plugin</tt>.
	 * Parameter must be numeric.
	 * 
	 * @param plugin plugin to be searched
	 * @return numeric parameter found after this keyword
	 * @throws PluginException if the keyword or parameters are not found
	 */
	public int checkNumericParams(final Plugin plugin) throws PluginException {
		return PluginKeyword.checkNumericParams(plugin, this.toString());
	}
	
	/**
	 * Returns the numeric parameter found after the first occurrence of this keyword in the specified <tt>Plugin</tt>.
	 * Parameter must be numeric.
	 * 
	 * @param plugin plugin to be searched
	 * @param string string to be located
	 * @return numeric parameter found after this keyword
	 * @throws PluginException if the keyword or parameters are not found
	 */
	public static int checkNumericParams(final Plugin plugin, final String string) throws PluginException {
		int number = 0;
		final String param = plugin.getParamsFor(string, 0);
		try {
			number = Integer.valueOf(param);
		} catch (NumberFormatException e) {
			throw PluginException.create(PluginException.Type.INVALID_PARAMETER, e, plugin,  string, param); 
		}
		return number;
	}
	
	/**
	 * Returns the numeric parameter found after the first occurrence of this keyword in the specified <tt>Plugin</tt>.
	 * Parameter must be numeric and greater than zero.
	 * 
	 * @param plugin plugin to be searched
	 * @return numeric parameter found after this keyword
	 * @throws PluginException if the keyword or parameters are not found, or if the number is <= 0
	 */
	public int checkPositiveNumericParams(final Plugin plugin) throws PluginException {
		return PluginKeyword.checkPositiveNumericParams(plugin, this.toString());
	}
	
	/**
	 * Returns the numeric parameter found after the first occurrence of this keyword in the specified <tt>Plugin</tt>.
	 * Parameter must be numeric and greater than zero.
	 * 
	 * @param plugin plugin to be searched
	 * @param string string to be loated
	 * @return numeric parameter found after this keyword
	 * @throws PluginException if the keyword or parameters are not found, or if the number is < 0
	 */
	public static int checkPositiveNumericParams(final Plugin plugin, final String string) throws PluginException {
		final int number = PluginKeyword.checkNumericParams(plugin, string);
		if (number < 0) {
			throw PluginException.create(PluginException.Type.INVALID_PARAMETER, plugin, string, Integer.toString(number));
		}
		return number;
	}
	
	/**
	 * Returns <tt>true</tt> if "yes" is found after the first occurrence of this keyword in the specified <tt>Plugin</tt>.
	 * Returns <tt>false</tt> if this keyword is not found.
	 * 
	 * @param plugin plugin to be searched
	 * @return <tt>true</tt> if "yes," "y", "true", or "t" is found after this keyword
	 */
	public boolean getBooleanParams(final Plugin plugin) {
		final String param = plugin.getParamsFor(this);
		return this.exists(plugin) && 
				(param.equalsIgnoreCase("yes") || param.equalsIgnoreCase("y") ||
				 param.equalsIgnoreCase("true") || param.equalsIgnoreCase("t"));
	}
	
	/**
	 * Returns <tt>true</tt> if this keyword is found with parameters in the specified <tt>Plugin</tt>.
	 * 
	 * @param plugin plugin to be searched
	 * @return <tt>true</tt> if this keyword is found with parameters in the specified <tt>Plugin</tt>
	 */
	public boolean exists(final Plugin plugin) {
		final String param = plugin.getParamsFor(this);
		return !(param == null || param.equals(PluginPattern.BLANK.toString()) ||
								  param.equals(PluginPattern.NULL.toString()));
	}
	
	/**
	 * Returns the enum member found after the first occurrence of the keyword in the specified <tt>Plugin</tt>.
	 * 
	 * @param plugin plugin to be searched
	 * @param enumType parent class of the enumMember to return
	 * @return enum member found after this keyword
	 * @throws PluginException if the keyword or parameters are invalid or not found
	 */
	public <T extends Enum<T>> T checkBoundedParams(final Plugin plugin, final Class<T> enumType) throws PluginException {
		T enumMember = null;
		try {
			enumMember = Enum.valueOf(enumType, this.checkParams(plugin));
		} catch (IllegalArgumentException e) {
			String[] message = e.getMessage().split("\\.");
			throw PluginException.create(PluginException.Type.INVALID_PARAMETER, e, plugin, message[message.length-2], message[message.length-1]);
		}
		return enumMember;
	}
	
	/**
	 * Returns the enum member found after this keyword, or null if no valid parameter is found.
	 * 
	 * @param plugin plugin to be searched
	 * @param values enum type values; pass enum.values() for test enum
	 * @return enum member found after this keyword, or null
	 */
	public <T extends Enum<T>> T getBoundedParams(final Plugin plugin, final T[] values) {
		return PluginKeyword.getBoundedParams(plugin, values, this.toString());
	}
	
	/**
	 * Returns the enum member found after this keyword, or null if no valid parameter is found.
	 * 
	 * @param plugin plugin to be searched
	 * @param values enum type values; pass enum.values() for test enum
	 * @param string string to be located (used as a keyword)
	 * @return enum member found after this keyword, or null
	 */
	public static <T extends Enum<T>> T getBoundedParams(final Plugin plugin, final T[] values, final String string) {
		T enumMember = null;
		for (T e : values) {
			if (e.toString().equalsIgnoreCase(plugin.getParamsFor(string, 0))) {
				enumMember = e;
				break;
			}
		}
		return enumMember;
	}
	
	/**
	 * Returns the string represented by this plugin keyword.
	 * 
	 * @return string represented by this plugin keyword
	 */
	@Override public String toString() {
		return super.toString().toLowerCase(Plugin.LOCALE).replace('_', '-');
	}

}
