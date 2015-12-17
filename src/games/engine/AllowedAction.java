/***********************************************************************//**
* @file			AllowedAction.java
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

public class AllowedAction implements Performable, EngineComponent {

/*------------------------------------------------
 	Constants and Attributes
 ------------------------------------------------*/
	private final Performable action;
	private final EngineComponentSet<Performable> responseActions;
	private final Condition precondition;
	
/*------------------------------------------------
 	Constructor(s)
 ------------------------------------------------*/
	/**
	 * Constructs a new <tt>AllowedAction</tt> with the specified attributes.
	 * 
	 * @param action a <tt>Performable</tt> action
	 */
	public AllowedAction(final Performable action) {
		this(action, null, null);
	}
	
	/**
	 * Constructs a new <tt>AllowedAction</tt> with the specified attributes.
	 * 
	 * @param action a <tt>Performable</tt> action
	 * @param precondition a <tt>Condition</tt> which must be true to allow this action
	 */
	public AllowedAction(final Performable action, final Condition precondition) {
		this(action, precondition, null);
	}
	
	/**
	 * Constructs a new <tt>AllowedAction</tt> with the specified attributes.
	 * 
	 * @param action a <tt>Performable</tt> action
	 * @param responseActions a set of actions to perform in response to this action
	 */
	public AllowedAction(final Performable action, final EngineComponentSet<Performable> responseActions) {
		this(action, null, responseActions);
	}
	
	/**
	 * Constructs a new <tt>AllowedAction</tt> with the specified attributes.
	 * 
	 * @param action a <tt>Performable</tt> action
	 * @param precondition a <tt>Condition</tt> which must be true to allow this action
	 * @param responseActions a set of actions to perform in response to this action
	 */
	public AllowedAction(final Performable action, final Condition precondition, final EngineComponentSet<Performable> responseActions) {
		this.action = action;
		this.precondition = precondition;
		this.responseActions = responseActions;
	}
	
/*------------------------------------------------
 	Accessors
 ------------------------------------------------*/
	/**
	 * Returns the name of this <tt>AllowedAction</tt>.
	 * 
	 * @return the name of this <tt>AllowedAction</tt>
	 */
	@Override public String getName() {
		return action.getName();
	}
	
	/**
	 * Returns the description of this <tt>AllowedAction</tt>.
	 * 
	 * @return the description of this <tt>AllowedAction</tt>
	 */
	@Override public String getDescription() {
		return action.getDescription();
	}

	/**
	 * Returns the <tt>Operation</tt> associated with this <tt>AllowedAction</tt>.
	 * 
	 * @return the <tt>Operation</tt> associated with this <tt>AllowedAction</tt>
	 */
	@Override public Operation getOperation() {
		return action.getOperation();
	}

	/**
	 * Returns the parameters for the <tt>Operation</tt> associated with this <tt>AllowedAction</tt>.
	 * 
	 * @return the parameters for the <tt>Operation</tt> associated with this <tt>AllowedAction</tt>
	 */
	@Override public String[] getParams() {
		return action.getParams();
	}
	
	/**
	 * Returns <tt>true</tt> if this <tt>AllowedAction</tt> has a precondition.
	 * 
	 * @return <tt>true</tt> if this <tt>AllowedAction</tt> has a precondition
	 */
	public boolean hasPrecondition() {
		return precondition != null;
	}
	
	/**
	 * Returns the <tt>Condition</tt> associated with this <tt>AllowedAction</tt>.
	 * 
	 * @return the <tt>Condition</tt> associated with this <tt>AllowedAction</tt>
	 */
	public Condition getPrecondition() {
		return precondition;
	}
	
	/**
	 * Returns the <tt>Performable</tt> that makes up this <tt>AllowedAction</tt>.
	 * 
	 * @return the <tt>Performable</tt> that makes up this <tt>AllowedAction</tt>
	 */
	public Performable getPerformable() {
		return action;
	}
	
	/**
	 * Returns <tt>true</tt> if this <tt>AllowedAction</tt> has response actions.
	 * 
	 * @return <tt>true</tt> if this <tt>AllowedAction</tt> has response actions
	 */
	public boolean hasResponseActions() {
		return this.responseActions == null ? false : true;
	}
	
	/**
	 * Returns the response actions for this <tt>AllowedAction</tt>.
	 * 
	 * @return the response actions for this <tt>AllowedAction</tt>
	 */
	public EngineComponentSet<Performable> getResponseActions() {
		return this.responseActions;
	}
	
	/**
	 * Returns information about this <tt>AllowedAction</tt>.
	 * 
	 * @return information about this <tt>AllowedAction</tt>
	 */
	@Override  public String toString() {
		final StringBuilder str = new StringBuilder();
		str.append("Allow Action: ").append(action);
		if (responseActions != null) {
			str.append("In response, Do... ").append(Strings.NEW_LINE).append(responseActions);
		}
		return str.toString();
	}

}