/***********************************************************************//**
* @file			PlayingCardRanking.java
* @author		Kurt E. Clothier
* @date			November 14, 2015
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

import java.io.Serializable;
import java.util.Comparator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class PlayingCardRanking implements Serializable, Comparator<PlayingCard> {
	
	private static final long serialVersionUID = 8219065314816410816L;
	private final Map<String, Integer> faceRanks;
	private final Map<String, Integer> groupRanks;
	
	/**
	 * Constructs a new <tt>PlayingCardRanking</tt> with the specifed mapping.
	 * 
	 * @param faceRanks mapping of PlayingCard faces to numeric values
	 * @param groupRanks mapping of PlayingCard groups to numeric values
	 * @throws IllegalArgumentException if both parameters are null or empty
	 */
	public PlayingCardRanking(final Map<String, Integer> faceRanks, 
							  final Map<String, Integer> groupRanks) throws IllegalArgumentException {
		if (faceRanks == null && groupRanks == null ||
			faceRanks == null && groupRanks.isEmpty() ||
			groupRanks == null && faceRanks.isEmpty()) {
			throw new IllegalArgumentException("Both parameters cannot be null or empty!");
		}
		this.faceRanks = new ConcurrentHashMap<String, Integer>(faceRanks);
		this.groupRanks = new ConcurrentHashMap<String, Integer>(groupRanks);
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
	
	

}
