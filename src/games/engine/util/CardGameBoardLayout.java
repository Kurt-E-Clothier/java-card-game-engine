/***********************************************************************//**
* @file			CardPileLayout.java
* @author		Kurt E. Clothier
* @date			November 19, 2015
*
* @breif		Parameters used to create a card game board
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

import games.Strings;

public final class CardGameBoardLayout implements Serializable {

	private static final long serialVersionUID = 1764359157290987814L;

	/*------------------------------------------------
 	Keyword Parameter Enumerations
 ------------------------------------------------*/
	/**
	 * Specifies the shape of this board.
	 * SIDED: Polygon shape with as many sides as players (and possibly dealer)
	 * CIRCLE: Circle with a dynamic radius
	 * SEMI-CIRCLE: half of a circle with a dynamic radius
	 */
	public static enum Shape {SIDED, CIRCLE, SEMICIRCLE}
	
	/**
	 * Specifies how the players are arranged.
	 * SPREAD: spread out around perimeter of playing area
	 * SEMISPREAD: equally spaced around a single side of the playing area
	 */
	public static enum Player_Layout {SPREAD, SEMISPREAD}
	
/*------------------------------------------------
 	Constants and Attributes
 ------------------------------------------------*/
	private final CardGameBoardLayout.Shape shape;
	private final CardGameBoardLayout.Player_Layout layout;
	private final boolean isDealerPresent;
	private final CardPileLayout commonPileLayout;
	private final CardPileLayout playerPileLayout;
	
/*------------------------------------------------
 	Constructor(s)
 ------------------------------------------------*/
	/**
	 * Construct a <tt>CardPileParameter</tt> with the specified attributes.
	 * */
	public CardGameBoardLayout(final CardGameBoardLayout.Shape shape, 
							   final CardGameBoardLayout.Player_Layout layout,
							   final boolean isDealerPresent,
							   final CardPileLayout commonPileLayout,
							   final CardPileLayout playerPileLayout) {
		this.shape = shape;
		this.layout = layout;
		this.isDealerPresent = isDealerPresent;
		this.commonPileLayout = commonPileLayout;
		this.playerPileLayout = playerPileLayout;
	}
	
/*------------------------------------------------
    Accessors
 ------------------------------------------------*/
	/**
	 * Returns the shape of this <tt>CardGameBoardLayout</tt>.
	 * 
	 * @return the shape of this <tt>CardGameBoardLayout</tt>
	 */
	public CardGameBoardLayout.Shape getShape() {
		return shape;
	}
	
	/**
	 * Returns the player layout of this <tt>CardGameBoardLayout</tt>.
	 * 
	 * @return the player layout of this <tt>CardGameBoardLayout</tt>
	 */
	public CardGameBoardLayout.Player_Layout getPlayerLayout() {
		return layout;
	}
	
	/**
	 * Returns <tt>true</tt> if a dealer is present in this <tt>CardGameBoardLayout</tt>.
	 * 
	 * @return <tt>true</tt> if a dealer is present in this <tt>CardGameBoardLayout</tt>
	 */
	public boolean dealerIsPresent() {
		return isDealerPresent;
	}
	
	/**
	 * Returns the <tt>CardPileLayout</tt> for the specified owner.
	 * 
	 * @param owner who owns the piles in this layout
	 * @return the <tt>CardPileLayout</tt> for the specified owner
	 */
	public CardPileLayout getLayout(final CardPileParameter.Owner owner) {
		CardPileLayout pileLayout = null;
		switch (owner) {
		case COMMON:
			pileLayout = this.commonPileLayout;
			break;
		default:
			pileLayout = this.playerPileLayout;
			break;
		}
		return pileLayout;
	}
	
	/**
	 * Return information.
	 *
	 * @return string containing information
	 */
	@Override public String toString() {
		final StringBuilder str = new StringBuilder();
		str.append("Shape: ").append(shape).append(Strings.NEW_LINE.toString())
		   .append("Player Layout: ").append(layout).append(Strings.NEW_LINE.toString())
		   .append("Dealer Present: ").append(isDealerPresent).append(Strings.NEW_LINE.toString())
		   .append(" - Common Layout -").append(Strings.NEW_LINE.toString())
		   .append(commonPileLayout.toString()).append(Strings.NEW_LINE.toString())
		   .append(" - Player Layout -").append(Strings.NEW_LINE.toString())
		   .append(playerPileLayout.toString()).append(Strings.NEW_LINE.toString());
		return str.toString();
	}
}
