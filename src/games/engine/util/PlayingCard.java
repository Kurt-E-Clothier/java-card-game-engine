/***********************************************************************//**
* @file			PlayingCard.java
* @author		Kurt E. Clothier
* @date			November 14, 2015
*
* @breif		Single playing card for card playing games
*
* @pre			Compiler: Eclipse - Mars Release (4.5.0)
* @pre			Java: JRE 7 or greater
*
* @see			http://www.projectsbykec.com/
*
* @copyright	The MIT License (MIT) - see LICENSE.txt
****************************************************************************/

package games.engine.util;

/******************************************************************//**
 * The PlayingCard Class
 * - An individual playing card used in a game
 * - Cards are Immutable
 * Limitations
 * - Cannot handle special attributes or card actions
 *********************************************************************/
public final class PlayingCard implements Comparable<PlayingCard>, Cloneable {		
		
/*------------------------------------------------
 	Constants and Attributes
 ------------------------------------------------*/
	private final PlayingCardFace face;		// The name of the card, Ex: "2" or "King"
	private final PlayingCardGroup group;	// The group of the card, Ex: "Hearts" or "Clubs"
	
/*------------------------------------------------
 	Constructor(s)
 ------------------------------------------------*/
	/**
	 * Construct a new <tt>PlayingCard</tt> with the given attributes.
	 * 
	 * @param face the face (title, name, etc) of this card
	 * @param group the group of this card (suit, color, etc)
	 * @throws IllegalArgumentException if both parameters are null
	 */
	PlayingCard(final PlayingCardFace face, final PlayingCardGroup group) throws IllegalArgumentException {
		if (face == null && group == null ) {
			throw new IllegalArgumentException("Both parameters cannot be null!");
		}
		this.face = face;
		this.group = group;
	}
	
/*------------------------------------------------
    Accessors
 ------------------------------------------------*/
	/**
	 * Returns the face on this <tt>PlayingCard</tt>.
	 * 
	 * @return the face on this card
	 */
	public PlayingCardFace getFace() {
		return face;
	}
	
	/**
	 * Returns the group of this <tt>PlayingCard</tt>.
	 * 
	 * @return the group of this card
	 */
	public PlayingCardGroup getGroup() {
		return group;
	}
	
/*------------------------------------------------
    Utilities
 ------------------------------------------------*/
	/**
	 * Returns <tt>true</tt> if this <tt>PlayingCard</tt> has the attributes of the specified card.   
	 *
	 * @param card to test against this card
	 * @return <tt>true</tt> if this card has the attributes of the specified
	 */
	public boolean has(final PlayingCard that) {
		return 	this.has(that.face, that.group);
	}
	
	/**
	 * Returns <tt>true</tt> if this <tt>PlayingCard</tt> has the specified attributes.  
	 *
	 * @param face face to test against this card
	 * @param group group to test against this card
	 * @return <tt>true</tt> if this card has the specified attributes
	 */
	public boolean has(final PlayingCardFace face, final PlayingCardGroup group) {
		return 	// Compare face attributes with null checks
				(face == null && this.face == null ||
				 face != null && face.equals(this.face)) &&
				// Compare group attribute with null checks
				(group == null && this.group == null ||
				 group != null && group.equals(this.group));
	}
	
/*------------------------------------------------
    Overridden Methods
 ------------------------------------------------*/
	/**
	 * Returns a new <tt>PlayingCard</tt> by cloning this <tt>PlayingCard</tt>.
	 * New card will have identical ID as cloned card!
	 * 
	 * @return new card clone of this card, with same ID
	 */
	@Override public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
	
    /**
     * Compare this <tt>PlayingCard</tt> to that <tt>PlayingCard</tt>, lexicographically.
     * Ex: 3 of Clubs = 3 of clubs < 4 of clubs < 4 of Hearts < Two of Diamonds
     *
     * @param that the that
     * @return N, where N = {-n,0,n if this <,==,> that}
     */
	@Override public int compareTo(final PlayingCard that) {
		int ret = 0;
		if (that == null) {
			ret = 1;
		}
		else if (this.face == null) {
			if (that.face ==  null) {
				ret = this.group.compareTo(that.group);
			}
			else {
				ret = -1;
			}
		}
		else if (this.face.equals(that.face)) {
			if (this.group == null) {
				if (that.group == null) {
					ret = 0;
				}
				else {
					ret = -1;
				}
			}
			else {
				ret = this.group.compareTo(that.group);
			}
		}
		else {
			ret = this.face.compareTo(that.face);
		}
		return ret;
	}
	
	/**
	 * Compares the specified object with this <tt>PlayingCard</tt> for equality.  
	 * Returns <tt>true</tt> if the given object is non-null and is this <tt>PlayingCard</tt>.
	 * The copmareTo() method should be used for value comparisons.
	 *
	 * @param that object to be compared for equality with this <tt>PlayingCard</tt>
	 * @return <tt>true</tt> if the specified object is equal to this <tt>PlayingCard</tt>
	 */
	@Override public boolean equals(final Object that) {
		return 	that != null && 
				that.getClass() == this.getClass() &&
				this.has((PlayingCard)that);
	}
	
	/**
	 * Returns the hash code associated with this <tt>PlayingCard</tt>.
	 *
	 * @return the hashCode associated with this card
	 */
	@Override public int hashCode() {
		int hash = 17;
		hash = hash * 31 + this.face.hashCode();
	    hash = hash * 31 + this.group.hashCode();
		return hash;
	}
	
	/**
	 * Returns string containing information about this <tt>PlayingCard</tt>.
	 *
	 * @return string containing information about this card
	 */
	@Override public String toString() {
		final StringBuilder str = new StringBuilder();
		str.append("Playing Card: ").append(this.face)
		   .append(" - ").append(this.group);
		return str.toString();
	}
}


