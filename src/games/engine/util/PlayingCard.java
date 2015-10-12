/***********************************************************************//**
* @file			PlayingCard.java
* @author		Kurt E. Clothier
* @date			October 12, 2015
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

import java.io.Serializable;

/************************************************************************
 * The PlayingCard Class
 * - An individual card used in a game
 * - Cards are quasi-immutable
 ************************************************************************/
public class PlayingCard implements Comparable<PlayingCard>, Serializable{

/*------------------------------------------------
 	Constants and Attributes
 ------------------------------------------------*/
	private static final long serialVersionUID = 280649966375777899L;
	private final String face;	// The name of the card, Ex: "2" or "King"
	private final int value;	// The value of the card, Ex: "2" = 2, "Jack" = 10
	private final String group;	// The group of the card, Ex: "Hearts" or "Clubs"
	
/*------------------------------------------------
 	Constructor(s)
 ------------------------------------------------*/
	/**
	 * Construct a blank <tt>PlayingCard</tt>.
	 */
	protected PlayingCard() {
		this("", 0, "");
	}
	
	/** 
	 * Construct a new <tt>PlayingCard</tt> with the given attributes.
	 * 
	 * @param face	the face (title, name, etc) of this card
	 */
	protected PlayingCard(String face) {
		this.face = face;
		this.value = 0;
		this.group = null;
	}
	
	/** 
	 * Construct a new <tt>PlayingCard</tt> with the given attributes.
	 * 
	 * @param face	the face (title, name, etc) of this card
	 * @param value the value of this card
	 */
	protected PlayingCard(String face, final int value) {
		this.face = face;
		this.value = value;
		this.group = null;
	}
	
	/** 
	 * Construct a new <tt>PlayingCard</tt> with the given attributes.
	 * 
	 * @param face	the face (title, name, etc) of this card
	 * @param value the value of this card
	 * @param group the group of this card (suit, color, etc)
	 */
	protected PlayingCard(String face, final int value, String group) {
		this.face = face;
		this.value = value;
		this.group = group;
	}
	
	/**
	 * Construct a new <tt>PlayingCard</tt> by copying the given <tt>PlayingCard</tt>.
	 * 
	 * @param PlayingCard	<tt>PlayingCard</tt> to be copied
	 */
	protected PlayingCard(final PlayingCard PlayingCard) {
		this.face = PlayingCard.getFace();
		this.value = PlayingCard.getValue();
		this.group = PlayingCard.getGroup();
	}
	
/*------------------------------------------------
    Accessors and Mutators
 ------------------------------------------------*/
	/**
	 * Returns the face on this <tt>PlayingCard</tt>.
	 * 
	 * @return the face on this card
	 */
	protected String getFace() {
		return face;
	}

	/**
	 * Returns the numerical value of this <tt>PlayingCard</tt>.
	 * 
	 * @return the numerical value of this card
	 */
	protected int getValue() {
		return value;
	}
	
	/**
	 * Returns the numerical value of this <tt>PlayingCard</tt>.
	 * 
	 * @return the numerical value of this card
	 */
	protected int getRank() {
		return value;
	}
	
	/**
	 * Returns the group of this <tt>PlayingCard</tt>.
	 * 
	 * @return the group of this card
	 */
	protected String getGroup() {
		return group;
	}

/*------------------------------------------------
    Overridden Methods
 ------------------------------------------------*/
    /**
     * Compare this <tt>PlayingCard</tt> to that <tt>PlayingCard</tt> using their values.
     *
     * @param   that  <tt>PlayingCard</tt> object to compare to 'this'
     * @return        -1,0,1 if this is <,==,> that
     * @throws        NullPointerException if <tt>that</tt> is null.   
     */
	@Override public int compareTo(PlayingCard that) {
        if(this.value == that.getValue())		return 0;
        else if(this.value < that.getValue())	return -1;
        else									return 1;
	}
	
	/**
	 * Compares the specified object with this <tt>PlayingCard</tt> for equality.  
	 * Returns <tt>true</tt> if the given object is non-null, also a <tt>PlayingCard</tt>, 
	 * and has the same attributes.
	 *
	 * @param other   object to be compared for equality with this <tt>PlayingCard</tt>
	 * @return        <tt>true</tt> if the specified object is equal to this <tt>PlayingCard</tt>
	 */
	@Override public boolean equals(Object other) {
		if(other == null)
			return false;
		if(this.getClass() != other.getClass()) 
			return false;
		if(!this.getFace().equals(((PlayingCard)other).getFace()))
			return false;
		if(this.value != ((PlayingCard)other).getValue())
			return false;
		if(!this.getGroup().equals(((PlayingCard)other).getGroup()))
			return false;
		return true;
	}
	
	/**
	 * Returns the hash code associated with this <tt>PlayingCard</tt>.
	 *
	 * @return  the hashCode associated with this card
	 */
	@Override public int hashCode() {
		int hash = 17;
		hash = hash * 31 + this.face.hashCode();
	    hash = hash * 31 + this.value;
	    hash = hash * 31 + this.group.hashCode();
		return hash;
	}
	
	/**
	 * Returns string containing information about this <tt>PlayingCard</tt>.
	 *
	 * @return  string containing information about this card
	 */
	@Override public String toString() {
		StringBuilder str = new StringBuilder();
		str.append("Playing Card: ");
		str.append(this.face);
		if (this.group != null) {
			str.append(" (");
			str.append(this.group);
			str.append(")");
		}
		str.append(" = ");
		str.append(this.value);
		return str.toString();
	}
}

