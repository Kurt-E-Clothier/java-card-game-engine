/***********************************************************************//**
* @file			PlayingCardFactory.java
* @author		Kurt E. Clothier
* @date			November 14, 2015
*
* @breif		Factory for creating PlayingCards and Attributes
*
* @pre			Compiler: Eclipse - Mars Release (4.5.0)
* @pre			Java: JRE 7 or greater
*
* @see			http://www.projectsbykec.com/
*
* @copyright	The MIT License (MIT) - see LICENSE.txt
****************************************************************************/
package games.engine.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import games.engine.Engine;
import games.engine.plugin.Plugin;
import games.engine.plugin.PluginException;
import games.engine.plugin.PluginKeyword;
import games.engine.plugin.PluginPattern;

/******************************************************************//**
 * The PlayingCardFactory Enum
 * - Combination of Flyweight, Singleton, and Factory patterns
 * - Access statically as PlayingCardFactory.INSTANCE.method()
 *	 or PlayingCardFactory factory = PlayingCardFactory.getInstance()
 ********************************************************************/
public enum PlayingCardFactory {
	
	INSTANCE;
	
	private final List<PlayingCard> cards;
	private final Map<String, PlayingCardFace> faces;
	private final Map<String, PlayingCardGroup> groups;
	
	/*
	 * Constructs this <tt>PlayingCardFactory</tt> when first used.
	 */
	private PlayingCardFactory() {
		cards = new ArrayList<PlayingCard>();
		faces = new ConcurrentHashMap<String, PlayingCardFace>();
		groups = new ConcurrentHashMap<String, PlayingCardGroup>();
	}
	
	/**
	 * Return an instance of this <tt>PlayingCardFactory</tt>.
	 * 
	 * @return an instance of this PlayingCardFactory
	 */
	public static PlayingCardFactory getInstance()
	{
		return INSTANCE;
	}
	
	/**
	 * Create and return a <tt>PlayingCardRanking</tt>.
	 * This ranking must include all know Faces or Groups, if any.
	 * 
	 * @param plugin Rules plugin to be searched
	 * @return a <tt>PlayingCardRanking</tt>
	 * @throws PluginException if no ranking is found, or the ranking is invalid
	 */	
	public PlayingCardRanking createCardRanking(final Plugin plugin) throws PluginException {
		plugin.checkType(Plugin.Type.RULES);
		
		final Map<PlayingCardFace, Integer> faceRanking = new ConcurrentHashMap<PlayingCardFace, Integer>(faces.size());
		final Map<PlayingCardGroup, Integer> groupRanking = new ConcurrentHashMap<PlayingCardGroup, Integer>(groups.size());
		
		for (final PlayingCardRanking.Type type : PlayingCardRanking.Type.values()) {
			final int index = getRankingIndex(plugin, type);
			if (index >= 0) {
				final String line = plugin.getLine(index);
				// Format option 1: ranking <face, group> f,f,f,f ...
				if (line.contains(PluginPattern.COMMA.toString())) {
					String[] parts = line.split(PluginPattern.WHITESPACE.toString(), 3);
					if (parts.length < 3) {
						throw PluginException.create(PluginException.Type.INVALID_PARAMETER, plugin, PluginKeyword.RANKING.toString());
					}
					String rawCSV = parts[2].replaceAll(PluginPattern.WHITESPACE.toString(), PluginPattern.BLANK.toString());
					String[] csv = rawCSV.split(PluginPattern.COMMA.toString());
					for (int i = 0; i < csv.length; i++) {
						putValuesIntoMap(type, faceRanking, groupRanking, csv[i], i, line, plugin);
					}
				}
				// Format Option 2: ranking <face, group> /n face value /n face value /n end-ranking
				else {
					int endNdx = plugin.checkIndexOf(PluginKeyword.END_RANKING, index);
					for (int i = index + 1; i < endNdx; i++) {
						String[] parts = plugin.getLine(i).split(PluginPattern.WHITESPACE.toString());
						if (parts.length != 2) {
							throw PluginException.create(PluginException.Type.INVALID_PARAMETER, plugin, line);
						}
						try {
							putValuesIntoMap(type, faceRanking, groupRanking, parts[0], Integer.valueOf(parts[1]), line, plugin);
						} catch (NumberFormatException e) {
							throw PluginException.create(PluginException.Type.INVALID_PARAMETER, e, plugin, line, parts[1]); 
						}
					}
				}
				validateMaps(type, faceRanking, groupRanking, line, plugin);
			}
		}
		return validateCardRanking(plugin, faceRanking, groupRanking);
	}
	
	/* Validate rankingMaps and return the final card ranking object */
	private PlayingCardRanking validateCardRanking(final Plugin plugin, final Map<PlayingCardFace, Integer> faceRanking, 
												 final Map<PlayingCardGroup, Integer> groupRanking) throws PluginException {
		plugin.checkType(Plugin.Type.RULES);
		if (isEmpty(faceRanking) && isEmpty(groupRanking)) {
			throw PluginException.create(PluginException.Type.MISSING_KEYWORD, plugin, PluginKeyword.RANKING.toString());
		}
		if	(plugin.getNumberOf(PluginKeyword.RANKING) == 2 &&
			(isEmpty(faceRanking) || isEmpty(groupRanking))) {
			throw PluginException.create(PluginException.Type.INVALID_PARAMETER, plugin, PluginKeyword.RANKING.toString());
		}
		return new PlayingCardRanking(faceRanking, groupRanking);
	}
	
	/* Returns the line containing a rank map of the specified type, if any */
	private int getRankingIndex(final Plugin plugin, final PlayingCardRanking.Type type) {
		final StringBuilder str = new StringBuilder();
		str.append(PluginKeyword.RANKING.toString()).append(' ').append(type.toString().toLowerCase(Engine.LOCALE));
		return plugin.getIndexOf(str.toString(), 0);
	}
	
	/* Returns true if the specified map is empty */
	private static <K, V> boolean isEmpty(final Map<K, V> map) {
		return map == null || map.isEmpty();
	}
	
	/* Ensure the rankings contain every possible face or group, if any */
	private void validateMaps(final PlayingCardRanking.Type type, final Map<PlayingCardFace, Integer> faceRanking, 
							  final Map<PlayingCardGroup, Integer> groupRanking, final String line, final Plugin plugin) throws PluginException {
		switch (type) {
		case GROUP:
			if (!isEmpty(groupRanking)) {
				for (final String key : groups.keySet()) {
					if (!groupRanking.containsKey(groups.get(key))) {
						throw PluginException.create(PluginException.Type.MISSING_PARAMETER, plugin, line, key);
					}
				}
			}
			break;
		case FACE:
		default:
			if (!isEmpty(faceRanking)) {
				for (final String key : faces.keySet()) {
					if (!faceRanking.containsKey(faces.get(key))) {
						throw PluginException.create(PluginException.Type.MISSING_PARAMETER, plugin, line, key);
					}
				}
			}
			break;
		}
	}
	
	/* Validate strings and put into appropriate ranking maps */
	private void putValuesIntoMap(final PlayingCardRanking.Type type, final Map<PlayingCardFace, Integer> faceRanking, final Map<PlayingCardGroup, Integer> groupRanking,
								final String key, final int value, final String line, final Plugin plugin) throws PluginException {
		if (type == PlayingCardRanking.Type.GROUP && groups.containsKey(key)) {
			groupRanking.put(groups.get(key), value);
		}
		else if (type == PlayingCardRanking.Type.FACE && faces.containsKey(key)) {
			faceRanking.put(faces.get(key), value);
		}
		else {
			throw PluginException.create(PluginException.Type.INVALID_PARAMETER, plugin, line, key);
		}
	}
	
	/**
	 * Creates a new <tt>PlayingCard</tt> (if necessary) and returns a PlayingCard with the given attributes.
	 * 
	 * @param face the face (title, name, etc) of this card
	 * @param group the group of this card (suit, color, etc)
	 * @return a playing card with the given attributes
	 * @throws IllegalArgumentException if both parameters are null
	 */
	public PlayingCard createPlayingCard(final PlayingCardFace face, final PlayingCardGroup group) throws IllegalArgumentException {
		PlayingCard card = null;
		for (final PlayingCard p : cards) {
			if 	(p.has(face, group)) {
				card = p;
				break;
			}
		}
		if (card == null) {
				card = new PlayingCard(face, group);
				cards.add(card);
		}
		return card;
	}
	
	/**
	 * Creates a new <tt>PlayingCardFace</tt> attribute (if necessary) and returns a PlayingCardFace attribute.
	 * 
	 * @param face String represented by this attribute
	 * @return a PlayingCardFace attribute with the specified String
	 * @throws IllegalArgumentException if the parameter is null
	 */
	public PlayingCardFace createFace(final String face) throws IllegalArgumentException {
		if (face != null && faces.containsKey(face)) {
			return faces.get(face);
		}
		else {
			final PlayingCardFace f = new PlayingCardFace(face);
			faces.put(face, f);
			return f;
		}
	}
	
	/**
	 * Creates a new <tt>PlayingCardGroup</tt> attribute (if necessary) and returns a PlayingCardGroup attribute.
	 * 
	 * @param group String represented by this attribute
	 * @return a PlayingCardGroup attribute with the specified String
	 * @throws IllegalArgumentException if the parameter is null
	 */
	public PlayingCardGroup createGroup(final String group) throws IllegalArgumentException {
		if (group != null && groups.containsKey(group)) {
			return groups.get(group);
		}
		else {
			final PlayingCardGroup att = new PlayingCardGroup(group);
			groups.put(group, att);
			return att;
		}
	}
}
