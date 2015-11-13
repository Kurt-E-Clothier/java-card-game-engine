/***********************************************************************//**
* @file			PlayingCard.java
* @author		Kurt E. Clothier
* @date			November 12, 2015
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
import java.util.concurrent.atomic.AtomicInteger;

/******************************************************************//**
 * The PlayingCard Class
 * - An individual playing card used in a game
 * - Each card has a unique ID number
 * - Cards are quasi-immutable
 * Limitations
 * - Cannot handle special attributes or card actions
 ********************************************************************/
public class PlayingCard implements Comparable<PlayingCard>, Serializable, Cloneable{	
	
/*------------------------------------------------
 	Constants and Attributes
 ------------------------------------------------*/
	private static final long serialVersionUID = -4180762009957474007L;
	private static final AtomicInteger UNIQUE_ID = new AtomicInteger(0);	// Thread safe!
	private final int id;		// A unique ID for each Playing Card
	private final String face;	// The name of the card, Ex: "2" or "King"
	private final int value;	// The value of the card, Ex: "2" = 2, "Jack" = 10
	private final String group;	// The group of the card, Ex: "Hearts" or "Clubs"
	
/*------------------------------------------------
 	Constructor(s)
 ------------------------------------------------*/
	/** 
	 * Construct a new <tt>PlayingCard</tt> with the given attributes.
	 * 
	 * @param face	the face (title, name, etc) of this card
	 */
	public PlayingCard(final String face) {
		this(face, 0, null);
	}
	
	/** 
	 * Construct a new <tt>PlayingCard</tt> with the given attributes.
	 * 
	 * @param face	the face (title, name, etc) of this card
	 * @param value the value of this card
	 */
	public PlayingCard(final String face, final int value) {
		this(face, value, null);
	}
	
	/** 
	 * Construct a new <tt>PlayingCard</tt> with the given attributes.
	 * 
	 * @param face	the face (title, name, etc) of this card
	 * @param value the value of this card
	 * @param group the group of this card (suit, color, etc)
	 */
	public PlayingCard(final String face, final int value, final String group) {
		this(face, value, group, UNIQUE_ID.getAndIncrement());
	}
	
	/**
	 * Instantiates a new playing card.
	 *
	 * @param face the face
	 * @param value the value
	 * @param group the group
	 * @param id the id
	 */
	/* 
	 * Construct a new <tt>PlayingCard</tt> with the given attributes.
	 * 
	 * @param face	the face (title, name, etc) of this card
	 * @param value the value of this card
	 * @param group the group of this card (suit, color, etc)
	 */
	protected PlayingCard(final String face, final int value, final String group, final int id) {
		this.face = face;
		this.value = value;
		this.group = group;
		this.id = id;
	}
	
	/**
	 * Instantiates a new playing card.
	 *
	 * @param card the card
	 */
	/*
	 * Construct a new <tt>PlayingCard</tt> by cloning the given <tt>PlayingCard</tt>.
	 * 
	 * @param PlayingCard	PlayingCard to be copied
	 * @param newID			true if new card should have a unique ID number
	 */
	protected PlayingCard(final PlayingCard card) {
		this(card.face, card.value, card.group, card.id);
	}
	
/*------------------------------------------------
    Accessors
 ------------------------------------------------*/
	/**
	 * Returns the unique id on this <tt>PlayingCard</tt>.
	 * 
	 * @return the unique id on this card
	 */
	public final int getID() {
		return id;
	}
	
	/**
	 * Returns the face on this <tt>PlayingCard</tt>.
	 * 
	 * @return the face on this card
	 */
	public final String getFace() {
		return face;
	}

	/**
	 * Returns the numerical value of this <tt>PlayingCard</tt>.
	 * 
	 * @return the numerical value of this card
	 */
	public final int getValue() {
		return value;
	}
	
	/**
	 * Returns the numerical value of this <tt>PlayingCard</tt>.
	 * 
	 * @return the numerical value of this card
	 */
	public final int getRank() {
		return value;
	}
	
	/**
	 * Returns the group of this <tt>PlayingCard</tt>.
	 * 
	 * @return the group of this card
	 */
	public final String getGroup() {
		return group;
	}
	
/*------------------------------------------------
    Utility Methods
 ------------------------------------------------*/
	/**
 * Compare this <tt>PlayingCard</tt> to that <tt>PlayingCard</tt> using their attributes.
 * Returns <tt>true</tt> if the specified card has the same face and group as this card.
 *
 * @param that the that
 * @return true if the specified card has the same face and group as this card.
 */
	public boolean matches(final PlayingCard that) {
		return 	this.face.equals(that.face) &&
				this.matchesGroupWith(that);
	}
	
	/**
	 * Compare this <tt>PlayingCard</tt> to that <tt>PlayingCard</tt> using their attributes.
	 * Returns <tt>true</tt> if the specified card has the same group as this card.
	 *
	 * @param that the that
	 * @return true if the specified card has the same group as this card.
	 */
	public boolean matchesGroupWith(final PlayingCard that) {
		return 	this.group.equals(that.group) ||
				(this.group == null && that.group == null);
	}
	
	/**
	 * Compare this <tt>PlayingCard</tt> to that <tt>PlayingCard</tt> using their attributes.
	 * Returns <tt>true</tt> if the specified card has the same face as this card.
	 *
	 * @param that the that
	 * @return true if the specified card has the same face as this card.
	 */
	public boolean matchesFaceWith(final PlayingCard that) {
		return this.face.equals(that.group);
	}
	
	/**
	 * Compare this <tt>PlayingCard</tt> to that <tt>PlayingCard</tt> using their values.
	 * Returns <tt>true</tt> if the specified card has the same value as this card.
	 *
	 * @param that the that
	 * @return true if the specified card has the same value as this card.
	 */
	public boolean matchesValueWith(final PlayingCard that) {
		return this.value == that.value;
	}
	
	/**
	 * Returns a new <tt>PlayingCard</tt> by copying this <tt>PlayingCard</tt>.
	 * New card will have a new ID number.
	 * 
	 * @return new card copy of this card, with new ID
	 */
	public final PlayingCard copy() {
		return new PlayingCard(this.face, this.value, this.group);
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
	@Override public final Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
	
    /**
     * Compare this <tt>PlayingCard</tt> to that <tt>PlayingCard</tt> using their values.
     *
     * @param that the that
     * @return        -1,0,1 if this <,==,> that
     */
	@Override public int compareTo(final PlayingCard that) {
        if(this.value == that.getValue()) {
        	return 0;
        }
        else if(this.value < that.getValue()) {
        	return -1;
        }
        else {
        	return 1;
        }
	}
	
	/**
	 * Compares the specified object with this <tt>PlayingCard</tt> for equality.  
	 * Returns <tt>true</tt> if the given object is non-null and is this <tt>PlayingCard</tt>.
	 * The copmareTo() method should be used for value comparisons.
	 *
	 * @param other   object to be compared for equality with this <tt>PlayingCard</tt>
	 * @return        <tt>true</tt> if the specified object is equal to this <tt>PlayingCard</tt>
	 */
	@Override public boolean equals(final Object other) {
		return 	other != null && 
				this.getClass() == other.getClass() &&
				this.id == ((PlayingCard)other).getID();
	}
	
	/**
	 * Returns the hash code associated with this <tt>PlayingCard</tt>.
	 *
	 * @return  the hashCode associated with this card
	 */
	@Override public int hashCode() {
		int hash = 17;
		hash = hash * 31 + this.id;
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
		final StringBuilder str = new StringBuilder();
		str.append("Playing Card: ").append(this.face);
		if (this.group != null) {
			str.append(" (").append(this.group).append(')');
		}
		str.append(" = ").append(this.value);
		return str.toString();
	}
}

