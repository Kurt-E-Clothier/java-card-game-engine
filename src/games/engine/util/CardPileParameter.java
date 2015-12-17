/***********************************************************************//**
* @file			CardPileParameter.java
* @author		Kurt E. Clothier
* @date			November 19, 2015
*
* @breif		Parameters used to create a card pile
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

import games.Strings;

public final class CardPileParameter {
	
/*------------------------------------------------
 	Keyword Parameter Enumerations
 ------------------------------------------------*/
	/** Specifies who owns this card pile. */
	public static enum Owner {
		/** a single player of the game owns this pile */
		PLAYER, 
		/** all players of the game owns this pile */
		COMMON }
	
	/** Specifies who can view this card pile. */
	public static enum Visibility { 
		/** anyone can view this card pile */
		ALL, 
		/** only the owner can view this card pile */
		OWNER, 
		/** anyone but the owner can view this card pile */
		OTHER, 
		/** no one can view this card pile */
		NONE }
	
	/** Specifies the number of face up cards on top of the pile. */
	public static enum Visible {
		/** a number is specified [0, n] */
		NUMBER, 
		/** no cards are visible */
		NONE, 
		/** all cards are visible */
		ALL, 
		/** just the top card is face up */
		TOP }
	
	/** Specifies how the pile should be arranged. */
	public static enum Placement {
		/** all cards in a single stack */
		STACK, 
		/** cards slightly spread out */
		SPREAD, 
		/** cards do not touch one another */
		SPACED, 
		/** cards are in a messy pile */
		MESSY }
	
	/** Specifies how the piles are oriented on the table. */
	public static enum Orientation {
		/** Cards are placed sideways */
		LANDSCAPE, 
		/** Cards are placed upright */
		PORTRAIT }
	
	/** Specifies how the cards are placed with respect to the owner. */
	public static enum Tiling {
		/** cards are placed from left to right */
		HORIZONTAL, 
		/** cards are placed from bottom to top */
		VERTICAL }
	
	/** Specifies how card(s) are removed(played) from the pile. */
	public static enum Removal {
		/** Top card removed first */
		TOP, 
		/** Bottom card removed first */
		BOTTOM, 
		/** a random card is removed */
		RANDOM, 
		/** Any card (user selected) is removed */
		ANY, 
		/** Cards are never removed from this pile */
		NONE, 
		/** All cards are removed at once */
		ALL}
	
/*------------------------------------------------
 	Constants and Attributes
 ------------------------------------------------*/	
	private final String name;
	private final CardPileParameter.Owner owner;
	private final CardPileParameter.Visibility visibility;
	private final CardPileParameter.Visible visible;
	private final CardPileParameter.Placement placement;
	private final CardPileParameter.Orientation orientation;
	private final CardPileParameter.Tiling tiling;
	private final CardPileParameter.Removal removal;
	final int numVisible;
	
/*------------------------------------------------
 	Constructor(s)
 ------------------------------------------------*/
	/**
	 * Construct a <tt>CardPileParameter</tt> with the specified attributes.
	 * 
	 * @param name	what this card pile is called
	 * @param owner who owns this card pile
	 * @param visibility who can see the cards in this pile
	 * @param visible how many cards are visible in this pile
	 * @param placement how are the cards in this pile placed
	 * @param orientation how are the cards in this pile orientated
	 * @param tiling how are the cards in this pile tiled
	 * @param removal how are cards removed from this pile
	 */
	public CardPileParameter(final String name, final CardPileParameter.Owner owner, final CardPileParameter.Visibility visibility, 
							 final CardPileParameter.Visible visible, final int numVisible, 
							 final CardPileParameter.Placement placement, final CardPileParameter.Orientation orientation,
							 final CardPileParameter.Tiling tiling, final CardPileParameter.Removal removal) {
		
		this.name = name;
		this.owner = owner;
		this.visibility = visibility;
		this.visible = visible;
		this.placement = placement;
		this.orientation = orientation;
		this.tiling = tiling;
		this.removal = removal;
		this.numVisible = numVisible;
	}
	
/*------------------------------------------------
    Accessors
 ------------------------------------------------*/
	
	/**
	 * Returns the name of this <tt>CardPile</tt>.
	 * 
	 * @return the name of this CardPile
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Returns the <tt>Owner</tt> of this <tt>CardPile</tt>.
	 * 
	 * @return the Owner of this CardPile
	 */
	public Owner getOwner() {
		return owner;
	}
	
	/**
	 * Returns the <tt>Visibility</tt> of this <tt>CardPile</tt>.
	 * 
	 * @return the Visibility of this CardPile
	 */
	public Visibility getVisibility() {
		return visibility;
	}
	
	/**
	 * Returns the <tt>Visible</tt> of this <tt>CardPile</tt>.
	 * 
	 * @return the Visible of this CardPile
	 */
	public Visible getVisible() {
		return visible;
	}
	
	/**
	 * Returns the <tt>Placement</tt> of this <tt>CardPile</tt>.
	 * 
	 * @return the Placement of this CardPile
	 */
	public Placement getPlacement() {
		return placement;
	}
	
	/**
	 * Returns the <tt>Orientation</tt> of this <tt>CardPile</tt>.
	 * 
	 * @return the Orientation of this CardPile
	 */
	public Orientation getOrientation() {
		return orientation;
	}
	
	/**
	 * Returns the <tt>Tiling</tt> of this <tt>CardPile</tt>.
	 * 
	 * @return the Tiling of this CardPile
	 */
	public Tiling getTiling() {
		return tiling;
	}
	
	/**
	 * Returns the <tt>Removal</tt> of this <tt>CardPile</tt>.
	 * 
	 * @return the Removal of this CardPile
	 */
	public Removal getRemoval() {
		return removal;
	}
	
	/**
	 * Returns the number of visible Cards in this <tt>CardPile</tt>.
	 * Note, this number only applies when <tt>Visible</tt>
	 * has been set to Visible.NUMBER.
	 * 
	 * Otherwise, the number of visible cards depends on numerous
	 * other factors which are unknown to this class.
	 * 
	 * @return the number of visible Cards in this CardPile
	 */
	public int getNumVisible() {
		return numVisible;
	}
	
/*------------------------------------------------
    Overridden Methods
 ------------------------------------------------*/	
	/**
	 * Return information about this <tt>CardPile</tt>.
	 * 
	 * @return information about this CardPile
	 */
	@Override  public String toString() {
		final StringBuilder str = new StringBuilder();
		str.append("CardPile: ").append(name).append(Strings.NEW_LINE)
		   .append("Owner: ").append(owner).append(Strings.NEW_LINE)
		   .append("Visibility: ").append(visibility).append(Strings.NEW_LINE)
		   .append("Visible: ").append(visible).append(" (")
		   .append(numVisible).append(')').append(Strings.NEW_LINE)
		   .append("Placement: ").append(placement).append(Strings.NEW_LINE)
		   .append("Orientation: ").append(orientation).append(Strings.NEW_LINE)
		   .append("Tiling: ").append(tiling).append(Strings.NEW_LINE)
		   .append("Removal: ").append(removal).append(Strings.NEW_LINE);
		return str.toString();
	}	

}
