/***********************************************************************//**
* @file			ConditionalStatement.java
* @author		Kurt E. Clothier
* @date			December 10, 2015
* 
* @breif		Statement in a condition
*
* @pre			Compiler: Eclipse - Mars Release (4.5.0)
* @pre			Java: JRE 7 or greater
*
* @see			http://www.projectsbykec.com/
*
* @copyright	The MIT License (MIT) - see LICENSE.txt
****************************************************************************/

package games.engine;

import games.engine.plugin.PluginPattern;

public final class ConditionalStatement {

/*------------------------------------------------
 	Constants and Attributes
 ------------------------------------------------*/
	private final String[] parts;
	
/*------------------------------------------------
 	Constructor(s)
 ------------------------------------------------*/
	/**
	 * Constructs a new <tt>ConditionalStatment</tt> using the specified String.
	 * 
	 * @param statement a string to used as a conditional statement
	 */
	public ConditionalStatement(final String statement) {
		this.parts = statement.split(PluginPattern.WHITESPACE.toString());
	}
	
/*------------------------------------------------
 	Accessors
 ------------------------------------------------*/
	/**
	 * Returns the number of parts that make up this statement.
	 * 
	 * @return the number of parts that make up this statement
	 */
	public int getNumberOfParts() {
		return parts.length;
	}
	
	/**
	 * Returns <tt>true</tt> if this <tt>ConditionalStatement</tt> is inverted.
	 * 
	 * @return <tt>true</tt> if this <tt>ConditionalStatement</tt> is inverted
	 */
	public boolean isInverted() {
		return parts[0].equalsIgnoreCase(Condition.Logic.NOT.toString());
	}
	
	/**
	 * Returns <tt>true</tt> if this <tt>ConditionalStatement</tt> contains an operation.
	 * A <tt>false</tt> return implies this statement contains an embedded <tt>Condition</tt>.
	 * 
	 * @return <tt>true</tt> if this <tt>ConditionalStatement</tt> contains an operation
	 */
	public boolean containsOperation() {
		return (!this.isInverted() && Engine.stringIsMemberOf(parts[0], Operation.class)) ||
				(this.isInverted() && Engine.stringIsMemberOf(parts[1], Operation.class));
	}
	
	/**
	 * Returns the <tt>Operation</tt> or <tt>Condition</tt> contained in this statement.
	 * 
	 * @return the <tt>Operation</tt> or <tt>Condition</tt> contained in this statement
	 */
	public String getOperationOrCondition() {
		return this.isInverted() ? parts[1] : parts[0];
	}
	
	/**
	 * Returns <tt>true</tt> if this <tt>ConditionalStatement</tt> contains a trailing logical operator.
	 * 
	 * @return <tt>true</tt> if this <tt>ConditionalStatement</tt> contains a trailing logical operator
	 */
	public boolean containsLogic() {
		return Engine.stringIsMemberOf(parts[parts.length-1], Condition.Logic.class);
	}
	
	/**
	 * Returns the <tt>Condition.Logic</tt> operator at the end of this <tt>ConditionalStatement</tt>.
	 * Returns <tt>null</tt> if no operator is found.
	 * 
	 * @return trailing Condition.Logic operator, or null if none is found
	 */
	public Condition.Logic getLogic() {
		return Engine.stringToEnumMember(parts[parts.length-1], Condition.Logic.class);
	}
	
	/**
	 * Returns <tt>true</tt> if this <tt>ConditionalStatement</tt> contains parameters.
	 * 
	 * @return <tt>true</tt> if this <tt>ConditionalStatement</tt> contains parameters
	 */
	public boolean containsParams() {
		return getNumberOfParams() > 0;
	}
	
	/**
	 * Returns the number of parameters found in this <tt>ConditionalStatement</tt>.
	 * 
	 * @return the number of parameters found in this <tt>ConditionalStatement</tt>
	 */
	public int getNumberOfParams() {
		return getParams().length;
	}
	
	/**
	 * Returns a String array of the parameters found in this <tt>ConditionalStatement</tt>.
	 * 
	 * @return a String array of the parameters found in this <tt>ConditionalStatement</tt>
	 */
	public String[] getParams() {
		int start = 0;	// parts index for first param
		int end = 0;	// index after last param
		if (isInverted() && containsLogic() && parts.length >= 4) {
			start = 2;
			end = parts.length - 1;
		}
		else if (isInverted()) {
			start = 2;
			end = parts.length;
		}
		else if (containsLogic()) {
			start = 1;
			end = parts.length - 1;
		}
		else {
			start = 1;
			end = parts.length;
		}
		String[] params = new String[end - start];
		if (params.length > 0) {
			for (int i = start; i < end; i++) {
				params[i - start] = parts[i];
			}
		}
		return params;
	}

	/**
	 * Returns the original string used to make this statement.
	 * 
	 * @return the original string used to make this statement
	 */
	@Override public String toString() {
		final StringBuilder str = new StringBuilder();
		for (String s : parts) {
			str.append(s).append(' ');
		}
		return str.toString();
	}
}
