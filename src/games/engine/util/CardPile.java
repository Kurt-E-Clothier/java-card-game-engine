/***********************************************************************//**
* @file			CardPile.java
* @author		Kurt E. Clothier
* @date			November 19, 2015
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

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import games.engine.util.PlayingCard;

/******************************************************************//**
 * The CardPile Class
 * - An individual pile of Playing Cards used in a game
 * - Basic attributes are immutable, Playing Cards in pile may change
 * - Cards are Last In - First Out
 * TODO
 * - Finish sorting methods (and testing)
 * - Integration testing with Board text file
 **********************************************************************/
public final class CardPile implements Serializable{

	//private static final long serialVersionUID = 6688290548979452850L;

/*------------------------------------------------
 	Constants and Attributes
 ------------------------------------------------*/	
	private final CardPileParameter parameters;
	private final List<PlayingCard> cards;
	
/*------------------------------------------------
 	Constructor(s)
 ------------------------------------------------*/
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
	public CardPile(final CardPileParameter parameters) {
		this.parameters = parameters;
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
	public CardPileParameter getParameters() {
		return parameters;
	}
	
	/**
	 * Returns the current size of this <tt>CardPile</tt>.
	 * 
	 * @return the current size of this CardPile
	 */
	public int getSize() {
		return cards.size();
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
		str.append(parameters.toString())
		   .append("Top Card: ").append(cards.isEmpty() ? "NONE" : 
			   		((LinkedList<PlayingCard>) cards).getLast().toString());
		return str.toString();
	}
}

