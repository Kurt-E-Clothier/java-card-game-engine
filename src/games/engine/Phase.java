/***********************************************************************//**
* @file			Phase.java
* @author		Kurt E. Clothier
* @date			December 7, 2015
* 
* @breif		A conditional action (named differently for uniqueness)
*
* @pre			Compiler: Eclipse - Mars Release (4.5.0)
* @pre			Java: JRE 7 or greater
*
* @see			http://www.projectsbykec.com/
*
* @copyright	The MIT License (MIT) - see LICENSE.txt
****************************************************************************/

package games.engine;

import games.Strings;

public class Phase implements EngineComponent {

/*------------------------------------------------
 	Constants and Attributes
 ------------------------------------------------*/
	private final String name;
	private final EngineComponentSet<Performable> startActions;
	private final EngineComponentSet<Performable> endActions;
	private final EngineComponentSet<AllowedAction> allowedActions; 
	
/*------------------------------------------------
 	Constructor(s)
 ------------------------------------------------*/
	/**
	 * Constructs a new <tt>Phase</tt> with the specified attributes.
	 * 
	 * @param action a <tt>Performable</tt> action
	 */
	public Phase(final String name, final EngineComponentSet<Performable> startActions,
									final EngineComponentSet<AllowedAction> allowedActions,
									final EngineComponentSet<Performable> endActions) { 
		this.name = name;
		this.startActions = startActions;
		this.endActions = endActions;
		this.allowedActions = allowedActions;
	}
	
/*------------------------------------------------
 	Accessors
 ------------------------------------------------*/
	/**
	 * Returns the name of this <tt>Phase</tt>.
	 * 
	 * @return the name of this <tt>Phase</tt>
	 */
	@Override public String getName() {
		return name;
	}
	
	/**
	 * Returns <tt>true</tt> if this <tt>Phase</tt> has start actions to perform.
	 * 
	 * @return <tt>true</tt> if this <tt>Phase</tt> has start actions to perform
	 */
	public boolean hasStartActions() {
		return startActions == null ? false : true;
	}
	
	/**
	 * Returns the set of start actions for this <tt>Phase</tt>.
	 * 
	 * @return the set of start actions for this <tt>Phase</tt>
	 */
	public EngineComponentSet<Performable> getStartActions() {
		return startActions;
	}
	
	/**
	 * Returns <tt>true</tt> if this <tt>Phase</tt> has end actions to perform.
	 * 
	 * @return <tt>true</tt> if this <tt>Phase</tt> has end actions to perform
	 */
	public boolean hasEndActions() {
		return endActions == null ? false : true;
	}
	
	/**
	 * Returns the set of end actions for this <tt>Phase</tt>.
	 * 
	 * @return the set of end actions for this <tt>Phase</tt>
	 */
	public EngineComponentSet<Performable> getEndActions() {
		return endActions;
	}
	
	/**
	 * Returns <tt>true</tt> if this <tt>Phase</tt> has allowed actions.
	 * 
	 * @return <tt>true</tt> if this <tt>Phase</tt> has allowed actions
	 */
	public boolean hasAllowedActions() {
		return allowedActions == null ? false : true;
	}
	
	/**
	 * Returns the set of allowed actions for this <tt>Phase</tt>.
	 * 
	 * @return the set of allowed actions for this <tt>Phase</tt>
	 */
	public EngineComponentSet<AllowedAction> getAllowedActions() {
		return allowedActions;
	}
	
	/**
	 * Returns information about this <tt>Phase</tt>.
	 * 
	 * @return information about this <tt>Phase</tt>
	 */
	@Override  public String toString() {
		final StringBuilder str = new StringBuilder();
		str.append("-----------------------------").append(Strings.NEW_LINE)
		   .append("  Phase: ").append(name).append(Strings.NEW_LINE)
		   .append("-----------------------------").append(Strings.NEW_LINE);
		if (this.hasStartActions()) {
			str.append("<<< Starting Actions >>>").append(Strings.NEW_LINE).append(startActions);
		}
		if (this.hasAllowedActions()) {
			str.append("<<< Allowed Actions >>>").append(Strings.NEW_LINE).append(allowedActions);
		}
		if (this.hasEndActions()) {
			str.append("<<< Ending Actions >>>").append(Strings.NEW_LINE).append(endActions);
		}
		str.append("-----------------------------").append(Strings.NEW_LINE)
		   .append("  End of Phase").append(Strings.NEW_LINE)
		   .append("-----------------------------").append(Strings.NEW_LINE);
		return str.toString();
	}
}