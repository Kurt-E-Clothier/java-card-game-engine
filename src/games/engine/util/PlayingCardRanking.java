/***********************************************************************//**
* @file			PlayingCardRanking.java
* @author		Kurt E. Clothier
* @date			December 7, 2015
*
* @breif		Ranking for playing cards
*
* @pre			Compiler: Eclipse - Mars Release (4.5.0)
* @pre			Java: JRE 7 or greater
*
* @see			http://www.projectsbykec.com/
*
* @copyright	The MIT License (MIT) - see LICENSE.txt
****************************************************************************/
package games.engine.util;

import java.util.Comparator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import games.Strings;

public final class PlayingCardRanking implements Comparator<PlayingCard> {
	
	/** Type of card ranking */
	public static enum Type {
		/** Ranking by Face */
		FACE,
		/** Ranking by Group */
		GROUP;
	}
	
	private final Map<PlayingCardFace, Integer> faceRanks;
	private final Map<PlayingCardGroup, Integer> groupRanks;
	
	/**
	 * Constructs a new <tt>PlayingCardRanking</tt> with the specified mapping.
	 * 
	 * @param faceRanks mapping of PlayingCard faces to numeric values
	 * @param groupRanks mapping of PlayingCard groups to numeric values
	 */
	public PlayingCardRanking(final Map<PlayingCardFace, Integer> faceRanks, final Map<PlayingCardGroup, Integer> groupRanks) {
		this.faceRanks = faceRanks == null ? null : new ConcurrentHashMap<PlayingCardFace, Integer>(faceRanks);
		this.groupRanks = groupRanks == null ? null : new ConcurrentHashMap<PlayingCardGroup, Integer>(groupRanks);
	}
	
	/**
	 * Returns the numeric value of this <tt>PlayingCard</tt>.
	 * Face ranking is checked before group ranking.
	 * A null parameter or no ranking for the card returns -1.
	 * 
	 * @param card PlayingCard to get value of
	 * @return numeric value of this playing card, -1 if no is ranking found
	 */
	public int valueOf(final PlayingCard card) {
		int rank = -1;
		if (card != null) {
			if (faceRanks != null && card.getFace() != null && 
				faceRanks.containsKey(card.getFace())) {
				rank = faceRanks.get(card.getFace());
			}
			else if (groupRanks != null && card.getGroup() != null && 
					 groupRanks.containsKey(card.getGroup())) {
				rank = groupRanks.get(card.getGroup());
			}
		}
		return rank;
	}

	/**
     * Compare <tt>PlayingCard</tt> A to B using this ranking.
     * null < non-null.
     *
     * @param A Playing Card to test
     * @param A Playing Card to test against
     * @return N, where N = {-n,0,n if A <,==,> B}
     */
	@Override public int compare(final PlayingCard A, final PlayingCard B) {
		return valueOf(A) - valueOf(B);
	}
	
	/**
	 * Returns this <tt>PlayingCardRanking</tt> as a string.
	 *
	 * @return this <tt>PlayingCardRanking</tt> as a string
 	 */
	@Override public String toString() {
		final StringBuilder str = new StringBuilder();
		if (!(faceRanks == null || faceRanks.isEmpty())) {
			str.append("Face Ranks:").append(Strings.NEW_LINE);
			for (final PlayingCardFace key : faceRanks.keySet()) {
				str.append(key).append(" = ").append(faceRanks.get(key)).append(Strings.NEW_LINE);
			}
		}
		if (!(groupRanks == null || groupRanks.isEmpty())) {
			str.append("Group Ranks:").append(Strings.NEW_LINE);
			for (final PlayingCardGroup key : groupRanks.keySet()) {
				str.append(key).append(" = ").append(groupRanks.get(key)).append(Strings.NEW_LINE);
			}
		}
		return str.toString();
	}
}
