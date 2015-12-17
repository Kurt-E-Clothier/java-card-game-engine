/***********************************************************************//**
* @file			ControlledAction.java
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

public class ControlledAction implements Performable, Repeatable, Conditional, EngineComponent {

/*------------------------------------------------
 	Constants and Attributes
 ------------------------------------------------*/
	private final String name;
	private final Condition condition;
	private final int timesToRepeat;
	private final Action tAction;
	private final Action fAction;
	
/*------------------------------------------------
 	Constructor(s)
 ------------------------------------------------*/
	/**
	 * Constructs a new <tt>ControlledAction</tt> with the specified attributes.
	 * 
	 * @param name the name of this <tt>ControlledAction</tt>
	 * @param condition the <tt>Condition</tt> to associate with this <tt>ControlledAction</tt>
	 * @param action the <tt>Action</tt> to be performed if the <tt>Condition</tt> is <tt>true</tt>
	 */
	public ControlledAction(final String name, final Condition condition, final Action action) {
		this(name, 0, condition, action, null);
	}
	
	/**
	 * Constructs a new <tt>ControlledAction</tt> with the specified attributes.
	 * 
	 * @param name the name of this <tt>ControlledAction</tt>
	 * @param timesToRepeat number of times this check and action should repeated
	 * @param condition the <tt>Condition</tt> to associate with this <tt>ControlledAction</tt>
	 * @param action the <tt>Action</tt> to be performed if the <tt>Condition</tt> is <tt>true</tt>
	 */
	public ControlledAction(final String name, final int timesToRepeat, final Condition condition, final Action action) {
		this(name, timesToRepeat, condition, action, null);
	}
	
	/**
	 * Constructs a new <tt>ControlledAction</tt> with the specified attributes.
	 * 
	 * @param name the name of this <tt>ControlledAction</tt>
	 * @param condition the <tt>Condition</tt> to associate with this <tt>ControlledAction</tt>
	 * @param tAction the <tt>Action</tt> to be performed if the <tt>Condition</tt> is <tt>true</tt>
	 * @param fAction the <tt>Action</tt> to be performed if the <tt>Condition</tt> is <tt>false</tt>
	 */
	public ControlledAction(final String name, final Condition condition, final Action tAction, final Action fAction) {
		this(name, 0, condition, tAction, fAction);
	}
	
	/**
	 * Constructs a new <tt>ControlledAction</tt> with the specified attributes.
	 * 
	 * @param name the name of this <tt>ControlledAction</tt>
	 * @param timesToRepeat number of times this check and action should repeated
	 * @param condition the <tt>Condition</tt> to associate with this <tt>ControlledAction</tt>
	 * @param tAction the <tt>Action</tt> to be performed if the <tt>Condition</tt> is <tt>true</tt>
	 * @param fAction the <tt>Action</tt> to be performed if the <tt>Condition</tt> is <tt>false</tt>
	 */
	public ControlledAction(final String name, final int timesToRepeat, final Condition condition, final Action tAction, final Action fAction) {
		this.name = name;
		this.timesToRepeat = timesToRepeat;
		this.condition = condition;
		this.tAction = tAction;
		this.fAction = fAction;
	}
	
/*------------------------------------------------
 	Accessors
 ------------------------------------------------*/
	/**
	 * Returns the name of this <tt>ControlledAction</tt>.
	 * 
	 * @return the name of this <tt>ControlledAction</tt>
	 */
	@Override public String getName() {
		return name;
	}

	/**
	 * Returns the <tt>Operation</tt> for the true <tt>Action</tt> associated with this <tt>ControlledAction</tt>.
	 * 
	 * @return the <tt>Operation</tt> for the true <tt>Action</tt> associated with this <tt>ControlledAction</tt>
	 */
	@Override public Operation getOperation() {
		return tAction.getOperation();
	}
	
	/**
	 * Returns the <tt>Operation</tt> for the true or false <tt>Action</tt>.
	 * 
	 * @param whichAction set as true to return true action operation (opposite for false)
	 * @return the <tt>Operation</tt> for the true or false <tt>Action</tt>
	 */
	public Operation getOperation(final boolean whichAction) {
		return whichAction ? tAction.getOperation() : fAction.getOperation();
	}
	
	/**
	 * Returns the Description for the true <tt>Action</tt>.
	 * 
	 * @return the description for the true <tt>Action</tt>
	 */
	@Override public String getDescription() {
		return tAction.getDescription();
	}
	
	/**
	 * Returns the Description for the true or false <tt>Action</tt>.
	 * 
	 * @param whichAction set as true to return true action operation (opposite for false)
	 * @return the description for the true or false <tt>Action</tt>
	 */
	public String getDescription(final boolean whichAction) {
		return whichAction? tAction.getDescription() : fAction.getDescription();
	}

	/**
	 * Returns the parameters for the <tt>Action</tt> associated with this <tt>ControlledAction</tt>.
	 * 
	 * @return the parameters for the <tt>Action</tt> associated with this <tt>ControlledAction</tt>
	 */
	@Override public String[] getParams() {
		return tAction.getParams();
	}
	
	/**
	 * Returns the parameters for the true or false <tt>Action</tt>.
	 * 
	 * @param whichAction set as true to return true action parameters (opposite for false)
	 * @return the parameters for the true or false <tt>Action</tt>
	 */
	public String[] getParams(final boolean whichAction) {
		return whichAction ? tAction.getParams() : fAction.getParams();
	}
	
	/**
	 * Returns the <tt>Condition</tt> associated with this <tt>ControlledAction</tt>.
	 * 
	 * @return the <tt>Condition</tt> associated with this <tt>ControlledAction</tt>
	 */
	@Override public Condition getCondition() {
		return condition;
	}
	
	/**
	 * Returns the true <tt>Action</tt> associated with this <tt>ControlledAction</tt>.
	 * 
	 * @return the true <tt>Action</tt> associated with this <tt>ControlledAction</tt>
	 */
	@Override public Action getTrueAction() {
		return tAction;
	}
	
	/**
	 * Returns the false <tt>Action</tt> associated with this <tt>ControlledAction</tt>.
	 * 
	 * @return the false <tt>Action</tt> associated with this <tt>ControlledAction</tt>
	 */
	@Override public Action getFalseAction() {
		return fAction;
	}
	
	/**
	 * Returns <tt>true</tt> if this <tt>ControlledAction</tt> has a false action.
	 * 
	 * @return <tt>true</tt> if this <tt>ControlledAction</tt> has a false action
	 */
	@Override public boolean hasFalseAction() {
		return fAction != null;
	}
	
	/**
	 * Returns <tt>true</tt> if this <tt>ControlledAction</tt> should be repeated.
	 * 
	 * @return <tt>true</tt> if this <tt>ControlledAction</tt> should be repeated
	 */
	@Override public boolean shouldBeRepeated() {
		return timesToRepeat == 0 ? false : true;
	}
	
	/**
	 * Returns the number of times to repeat this <tt>ControlledAction</tt>.
	 * If the return value is -1, this this <tt>ControlledAction</tt> should be
	 * repeated continuously until the condition returns false.
	 * 
	 * @return the number of times to repeat this <tt>ControlledAction</tt>
	 */
	@Override  public int getNumberOfRepetitions() {
		return timesToRepeat;
	}
	
	/**
	 * Returns information about this <tt>ControlledAction</tt>.
	 * 
	 * @return information about this <tt>ControlledAction</tt>
	 */
	@Override  public String toString() {
		final StringBuilder str = new StringBuilder();
		str.append("Controlled Action: ").append(name).append(Strings.NEW_LINE)
		   .append("Should be repeated: ").append(this.shouldBeRepeated());
		if (this.shouldBeRepeated()) {
			str.append(" (").append(this.getNumberOfRepetitions()).append(')');
		}
		str.append(Strings.NEW_LINE).append(condition)
		   .append("If TRUE, Do ").append(tAction);
		if (fAction != null) {
			str.append("If FALSE, Do ").append(fAction);
		}
		return str.toString();
	}
}
