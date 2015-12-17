/***********************************************************************//**
* @file			Condition.java
* @author		Kurt E. Clothier
* @date			December 5, 2015
* 
* @breif		Specific boolean condition
*
* @pre			Compiler: Eclipse - Mars Release (4.5.0)
* @pre			Java: JRE 7 or greater
*
* @see			http://www.projectsbykec.com/
*
* @copyright	The MIT License (MIT) - see LICENSE.txt
****************************************************************************/

package games.engine;

import java.util.Arrays;
import games.Strings;

/******************************************************************//**
 * The Condition Class
 * 	- Actions can be player moves
 * 	- Actions can be automated engine events
 * 	- Actions must contain a valid <tt>Operation</tt> and parameters
 * 	- The engine is responsible for performing actions
 ********************************************************************/

public class Condition implements EngineComponent {

	/** Logic used with conditional statements */
	public static enum Logic {
		/** Two statements must both be true */
		AND,
		/** One of two statements must be true */
		OR,
		/** One or the other must be true, but not both */
		XOR,
		/** The statement must be false */
		NOT;
		/** Return an enum member as a converted string */
		@Override public String toString() {
			return Operation.convertEnumToString(super.toString());
		}
	}
	
/*------------------------------------------------
 	Constants and Attributes
 ------------------------------------------------*/
	private final String name;
	private final boolean isInverted;
	private final ConditionalStatement[] statements;
	
/*------------------------------------------------
 	Constructor(s)
 ------------------------------------------------*/
	/**
	 * Constructs a <tt>Condition</tt> with the specified attributes.
	 * 
	 * @param name what to call this condition
	 * @param statements individual conditional statements
	 */
	public Condition(final String name, final ConditionalStatement...statements) {
		if (statements == null || statements.length == 0) {
			throw new IllegalArgumentException("Condition must contain at least one statement.");
		}
		this.name = name;
		this.statements = Arrays.copyOf(statements, statements.length);
		this.isInverted = false;
	}
	
	/**
	 * Constructs a <tt>Condition</tt> by copying the specified <tt>Condition</tt>.
	 * 
	 * @param shouldInvert set to <tt>true</tt> to invert this condition
	 * @param condition the <tt>Condition</tt> to copy
	 */
	public Condition(final boolean isInverted, final Condition condition) {
		if (condition == null) {
			throw new IllegalArgumentException("Condition cannot be null.");
		}
		this.name = condition.getName();
		this.statements = condition.getStatements();
		this.isInverted = isInverted;
	}
	
/*------------------------------------------------
 	Accessors
 ------------------------------------------------*/
	/**
	 * Returns the name of this <tt>Condition</tt>.
	 * 
	 * @return the name of this <tt>Condition</tt>
	 */
	@Override public String getName() {
		return name;
	}
	
	/**
	 * Returns the <tt>ConditionalStatements</tt> for this <tt>Condition</tt>.
	 * 
	 * @return the <tt>ConditionalStatements</tt> for this <tt>Condition</tt>
	 */
	public ConditionalStatement[] getStatements() {
		return Arrays.copyOf(statements, statements.length);
	}
	
	/**
	 * Returns <tt>true</tt> if this <tt>Condition</tt> should be inverted.
	 * 
	 * @return <tt>true</tt> if this <tt>Condition</tt> should be inverted
	 */
	public boolean isInverted() {
		return isInverted;
	}
	
	/**
	 * Returns information about this <tt>Condition</tt>.
	 * 
	 * @return information about this <tt>Condition</tt>
	 */
	@Override  public String toString() {
		final StringBuilder str = new StringBuilder();
		str.append("Condition: ").append(name).append(Strings.NEW_LINE)
		   .append("Should be inverted: ").append(isInverted).append(Strings.NEW_LINE);
		for (final ConditionalStatement s : statements) {
			str.append(s.toString()).append(Strings.NEW_LINE);
		}
		return str.toString();
	}
	
}