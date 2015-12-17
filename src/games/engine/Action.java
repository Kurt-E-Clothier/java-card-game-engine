/***********************************************************************//**
* @file			Action.java
* @author		Kurt E. Clothier
* @date			December 6, 2015
* 
* @breif		Specific action which can take place
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
 * The Action Class
 * 	- Actions can be player moves
 * 	- Actions can be automated engine events
 * 	- Actions must contain a valid <tt>Operation</tt> and parameters
 * 	- The engine is responsible for performing actions
 ********************************************************************/

public class Action implements EngineComponent, Performable {
	
/*------------------------------------------------
 	Constants and Attributes
 ------------------------------------------------*/
	private final String name;
	private final String description;
	private final Operation operation;
	private final String[] params;
	
/*------------------------------------------------
 	Constructor(s)
 ------------------------------------------------*/
	public Action (final String name, final Operation operation, final String...params) {
		this(name, "", operation, params);
	}
	
	public Action (final String name, final String description, final Operation operation, final String...params) {
		this.name = name;
		this.description = description == null ? "" : description;
		this.operation = operation;
		this.params = params == null || params.length == 0 ? new String[0] : Arrays.copyOf(params, params.length);
	}
	
	public Action (final Action action) {
		this.name = action.name;
		this.description = action.description;
		this.operation = action.operation;
		this.params = action.params;
	}
	
/*------------------------------------------------
    Accessors
 ------------------------------------------------*/
	/**
	 * Returns the name of this <tt>Action</tt>.
	 * 
	 * @return the name of this <tt>Action</tt>
	 */
	@Override public String getName() {
		return name;
	}
	
	/**
	 * Returns the description of this <tt>Action</tt>.
	 * 
	 * @return the description of this <tt>Action</tt>
	 */
	@Override public String getDescription() {
		return description;
	}
	
	/**
	 * Returns the operation of this <tt>Action</tt>.
	 * 
	 * @return the operation of this <tt>Action</tt>
	 */
	@Override public Operation getOperation() {
		return operation;
	}
	
	/**
	 * Returns the parameters for this <tt>Action</tt>.
	 * 
	 * @return the parameters for this <tt>Action</tt>
	 */
	@Override public String[] getParams() {
		return Arrays.copyOf(params, params.length);
	}
	
/*------------------------------------------------
	Overridden Methods
 ------------------------------------------------*/
	/**
	 * Returns information about this <tt>Action</tt>.
	 * 
	 * @return information about this <tt>Action</tt>
	 */
	@Override  public String toString() {
		final StringBuilder str = new StringBuilder();
		str.append("Action: ").append(name).append(Strings.NEW_LINE);
		if (!description.isEmpty()) {
			str.append("Description: ").append(description).append(Strings.NEW_LINE);
		}
		str.append("Operation: ").append(operation).append(Strings.NEW_LINE)
		   .append("Paramaters:");
		for (String s : params) {
			str.append(' ').append(s);
		}
		return str.append(Strings.NEW_LINE).toString();
	}
}
