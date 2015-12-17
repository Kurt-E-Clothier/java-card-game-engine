/***********************************************************************//**
* @file			Conditional.java
* @author		Kurt E. Clothier
* @date			December 6, 2015
* 
* @breif		API for conditional engine components
*
* @pre			Compiler: Eclipse - Mars Release (4.5.0)
* @pre			Java: JRE 7 or greater
*
* @see			http://www.projectsbykec.com/
*
* @copyright	The MIT License (MIT) - see LICENSE.txt
****************************************************************************/
package games.engine;

public interface Conditional {
	
	/**
	 * Returns the <tt>Condition</tt> associated with this engine component.
	 * Conditional components must be associated with a known boolean condition.
	 * 
	 * @return the <tt>Condition</tt> associated with this engine component
	 */
	public Condition getCondition();
	
	/**
	 * Returns the true <tt>Action</tt> associated with this <tt>Conditional</tt>.
	 * This action should be performed if the condition is true.
	 * 
	 * @return the true <tt>Action</tt> associated with this <tt>Conditional</tt>
	 */
	public Action getTrueAction();
	
	/**
	 * Returns the false <tt>Action</tt> associated with this <tt>Conditional</tt>.
	 * This action should be performed if the condition is false.
	 * 
	 * @return the false <tt>Action</tt> associated with this <tt>Conditional</tt>
	 */
	public Action getFalseAction();
	
	/**
	 * Returns <tt>true</tt> if this <tt>Conditional</tt> has a false action.
	 * This is a <tt>Performable</tt> which should be performed in case
	 * the condition returns false;
	 * 
	 * @return <tt>true</tt> if this <tt>Conditional</tt> has a false action
	 */
	public boolean hasFalseAction();

}
