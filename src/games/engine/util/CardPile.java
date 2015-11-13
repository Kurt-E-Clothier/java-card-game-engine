/***********************************************************************//**
* @file			CardPile.java
* @author		Kurt E. Clothier
* @date			November 13, 2015
*
* @breif		A single pile of cards in a game
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

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/******************************************************************//**
 * The CardPile Class
 * - An individual pile of Playing Cards used in a game
 * - Basic attributes are immutable, Playing Cards in pile may change
 * - Cards are Last In - First Out
 * TODO
 * - Finish sorting methods (and testing)
 * - Integration testing with Board text file
 **********************************************************************/
public class CardPile {
	
/*------------------------------------------------
 	Keyword Parameter Enumerations
 ------------------------------------------------*/
	
	/**
	 * Specifies who owns this card pile.
	 * PLAYER: a single player of the game
	 * COMMON: all players of the game
	 */
	public static enum Owner { PLAYER, COMMON }
	
	/**
	 * Specifies who can view this card pile.
	 * ALL: anyone
	 * OWNER: only the owner 
	 * OTHER: anyone but the owner
	 * NONE: no one
	 */
	public static enum Visibility { ALL, OWNER, OTHER, NONE }
	
	/**
	 * Specifies the number of face up cards on top of the pile.
	 * NUMBER: a number is specified [0, n]
	 * NONE: no cards are visible
	 * ALL: all cards are visible
	 * TOP: just the top card is face up
	 */
	public static enum Visible { NUMBER, NONE, ALL, TOP }
	
	/**
	 * Specifies how the pile should be arranged.
	 * STACKED: all cards in a single stack
	 * SPREAD: cards slightly spread out
	 * SPACED: cards do not touch one another
	 * MESSY: cards are in a messy pile
	 */
	public static enum Placement { STACK, SPREAD, SPACED, MESSY }
	
	/**
	 * Specifies how the piles are oriented on the table.
	 * LANDSCAPE: Cards are placed sideways
	 * PORTRAIT: Cards are placed upright
	 */
	public static enum Orientation { LANDSCAPE, PORTRAIT }
	
	/**
	 * Specifies how the cards are placed with respect to the owner.
	 * HORIZONTAL: cards are placed from left to right
	 * VERTICAL: cards are placed from bottom to top
	 */
	public static enum Tiling { HORIZONTAL, VERTICAL }
	
	/**
	 * Specifies how card(s) are removed(played) from the pile.
	 * TOP: Top card removed first
	 * BOTTOM: Bottom card removed first
	 * RANDOM: a random card is removed
	 * ANY: Any card (user selected) is removed
	 * NONE: Cards are never removed from this pile
	 * ALL: All cards are removed at once
	 */
	public static enum Removal {TOP, BOTTOM, RANDOM, ANY, NONE, ALL}
	
/*------------------------------------------------
 	Constants and Attributes
 ------------------------------------------------*/	
	private static final String NEW_LINE = System.getProperty("line.separator");
	private final String name;
	private final CardPile.Owner owner;
	private final CardPile.Visibility visibility;
	private final CardPile.Visible visible;
	private final CardPile.Placement placement;
	private final CardPile.Orientation orientation;
	private final CardPile.Tiling tiling;
	private final CardPile.Removal removal;
	private final int numVisible;
	private final List<PlayingCard> cards;
	
/*------------------------------------------------
 	Constructor(s)
 ------------------------------------------------*/

	
	/**
	 * Construct an empty <tt>CardPile</tt>.
	 * 
	 * @param pile	a file copy containing information about this card pile
	 * @param deck	reference to the deck of cards
	 * @throws FileParameterException	missing keywords or invalid parameters
	 */
	
	/**
	 * Construct an empty <tt>CardPile</tt> with the specified attributes.
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
	public CardPile(final String name, final CardPile.Owner owner, 
					final CardPile.Visibility visibility, final CardPile.Visible visible, final int numVisible,
					final CardPile.Placement placement, final CardPile.Orientation orientation,
					final CardPile.Tiling tiling, final CardPile.Removal removal) {
		this.name = name;
		this.owner = owner;
		this.visibility = visibility;
		this.visible = visible;
		this.numVisible = numVisible;
		this.placement = placement;
		this.orientation = orientation;
		this.tiling = tiling;
		this.removal = removal;
		
		cards = new LinkedList<PlayingCard>();
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
	 * Returns the current size of this <tt>CardPile</tt>.
	 * 
	 * @return the current size of this CardPile
	 */
	public int getSize() {
		return cards.size();
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
	 * Returns the number of visible Cards in this <tt>CardPile</tt>.
	 * 
	 * @return the number of visible Cards in this CardPile
	 */
	public int getNumVisible() {
		return numVisible;
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
	
/*------------------------------------------------
    Card Utility Methods
 ------------------------------------------------*/
	/**
	 * Add the <tt>PlayingCard(s)</tt> to this <tt>CardPile</tt>.
	 * It is undefined behavior to use this card pile as the specified array.
	 * 
	 * @param cards	the playing card(s) to be added
	 */
	public void add(final PlayingCard...cards) {
		for (final PlayingCard card : cards){
			this.cards.add(card);
		}
	}
	
	/**
	 * Add the collection of <tt>PlayingCards</tt> to this <tt>CardPile</tt>.
	 * It is undefined behavior to use this card pile as the specified collection.
	 * 
	 * @param cards	the playing cards to be added
	 * @throws NullPointerException - if the specified collection is null
	 */
	public void add(final Collection<PlayingCard> cards) throws NullPointerException {
		this.cards.addAll(cards);
	}
	
	/**
	 * Returns <tt>true</tt> if the specified <tt>PlayingCard</tt> is in this <tt>CardPile</tt>.
	 * 
	 * @param card the playing card to be located
	 * @return true is the specified card is found in this pile
	 */
	public boolean contains(final PlayingCard card) {
		return this.cards.contains(card);
	}
	
	/**
	 * Returns <tt>true</tt> if this <tt>CardPile</tt> contains a card with the specified value.
	 * 
	 * @param value	the value of the playing card to be searched for
	 * @return true if this pile contains a card with the specified value
	 */
	public boolean contains(final int value) {
		boolean isContained = false;
		for (final PlayingCard c : cards) {
			if (c.getValue() == value) {
				isContained = true;
				break;
			}
		}
		return isContained;
	}
	
	/**
	 * Removes the specified <tt>PlayingCard</tt> from this <tt>CardPile</tt>.
	 * Returns <tt>false</tt> if the specified card is not found.
	 * 
	 * @param card the playing card to be located
	 * @return false if the specified card is not found
	 */
	public boolean remove(final PlayingCard card) {
		return ((LinkedList<PlayingCard>) cards).removeFirstOccurrence(card);
	}
	
	/**
	 * Removes and returns the first <tt>PlayingCard</tt> with the specified value from this <tt>CardPile</tt>.
	 * Returns <tt>null</tt> if no such card is found.
	 * 
	 * @param value	the value of the playing card to be searched for
	 * @return a PlayingCard with that value or null
	 */
	public PlayingCard remove(final int value) {
		PlayingCard card = null;
		for (final PlayingCard c : cards) {
			if (c.getValue() == value) {
				card = c;
				break;
			}
		}
		return card;
	}
	
	/**
	 * Returns the top <tt>PlayingCard</tt> from this <tt>CardPile</tt>.
	 * The top card is that last one that was added.
	 * 
	 * @return the top PlayingCard from this CardPile
	 * @throws NoSuchElementException - if this card pile is empty
	 */
	public PlayingCard getTop() throws NoSuchElementException {
		return ((LinkedList<PlayingCard>) cards).getLast();
	}
	
	/**
	 * Removes and returns the top <tt>PlayingCard</tt> from this <tt>CardPile</tt>.
	 * The top card is that last one that was added.
	 * 
	 * @return the top PlayingCard from this CardPile
	 * @throws NoSuchElementException - if this card pile is empty
	 */
	public PlayingCard removeTop() throws NoSuchElementException {
		return ((LinkedList<PlayingCard>) cards).removeLast();
	}
	
	/**
	 * Returns the top <tt>PlayingCard</tt> from this <tt>CardPile</tt>.
	 * The top card is that last one that was added.
	 * 
	 * @return the top PlayingCard from this CardPile
	 * @throws NoSuchElementException - if this card pile is empty
	 */
	public PlayingCard getBottom() throws NoSuchElementException {
		return ((LinkedList<PlayingCard>) cards).getFirst();
	}
	
	/**
	 * Removes and returns the top <tt>PlayingCard</tt> from this <tt>CardPile</tt>.
	 * The top card is that last one that was added.
	 * 
	 * @return the top PlayingCard from this CardPile
	 * @throws NoSuchElementException - if this card pile is empty
	 */
	public PlayingCard removeBottom() throws NoSuchElementException {
		return ((LinkedList<PlayingCard>) cards).removeFirst();
	}
	
	/**
	 * Returns all of the <tt>PlayingCards</tt> from this <tt>CardPile</tt>.
	 * 
	 * @return all of the PlayingCards from this CardPile
	 */
	public PlayingCard[] getAll() {
		return this.cards.toArray(new PlayingCard[cards.size()]);
	}
	
	/**
	 * Removes and returns all of the <tt>PlayingCards</tt> from this <tt>CardPile</tt>.
	 * 
	 * @return all of the PlayingCards from this CardPile
	 */
	public PlayingCard[] removeAll() {
		final PlayingCard[] temp = this.getAll();
		cards.clear();
		return temp;
	}
	
	/**
	 * Removes and returns the next <tt>PlayingCards</tt> according to the <tt>Removal</tt> attribute.
	 * 
	 * @return the next playing card according to the Removal attribute
	 */
	public PlayingCard remove() {
		// TODO
		return null;
	}
	
	/**
	 * Shuffle the cards in this <tt>CardPile</tt>.
	 */
	public void shuffle() {
		int ndx = 0;
		PlayingCard card = null;
		final Random rand = ThreadLocalRandom.current();
		// Durstenfeld updated Fisher-Yates shuffle
		for (int i = cards.size() - 1; i > 0; i--) {
			ndx = rand.nextInt(i + 1);
			card = cards.get(ndx);
			cards.set(ndx, cards.get(i));
			cards.set(i, card);
		}
	}
	
	/**
	 * Sort this <tt>CardPile</tt> by increasing value of the Cards, if applicable.
	 */
	public void sortByIncreasingValue() {
		this.sortByValue(true);
	}
	
	/**
	 * Sort this <tt>CardPile</tt> by decreasing value of the Cards, if applicable.
	 */
	public void sortByDecreasingValue() {
		this.sortByValue(false);
	}
	
	/*
	 * 
	 */
	private void sortByValue(final boolean direction) {
		// TODO
	}
	
	/**
	 * Sort this <tt>CardPile</tt> by faces (put like faces together).
	 */
	public void sortByFace() {
		// TODO
	}
	
	/**
	 * Sort this <tt>CardPile</tt> by group (put like groups together), if applicable.
	 */
	public void sortByGroup() {
		// TODO
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
		str.append("CardPile: ").append(name).append(NEW_LINE)
		   .append("Owner: ").append(owner).append(NEW_LINE)
		   .append("Visibility: ").append(visibility).append(NEW_LINE)
		   .append("Visible: ").append(visible).append(" (")
		   .append(numVisible).append(')').append(NEW_LINE)
		   .append("Placement: ").append(placement).append(NEW_LINE)
		   .append("Orientation: ").append(orientation).append(NEW_LINE)
		   .append("Tiling: ").append(tiling).append(NEW_LINE)
		   .append("Removal: ").append(removal).append(NEW_LINE)
		   .append("Top Card: ").append(cards.isEmpty() ? "NONE" : 
			   		((LinkedList<PlayingCard>) cards).getLast().toString());
		return str.toString();
	}
}

