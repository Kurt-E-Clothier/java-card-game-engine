/***********************************************************************//**
* @file			Performable.java
* @author		Kurt E. Clothier
* @date			December 6, 2015
* 
* @breif		API for performable engine components
*
* @pre			Compiler: Eclipse - Mars Release (4.5.0)
* @pre			Java: JRE 7 or greater
*
* @see			http://www.projectsbykec.com/
*
* @copyright	The MIT License (MIT) - see LICENSE.txt
****************************************************************************/

package games.engine;

public interface Performable extends EngineComponent {
	
	/**
	 * Returns the operation of this <tt>Performable</tt> component.
	 * For a component to be <tt>Performable</tt>, it must have a valid <tt>Operation</tt>
	 * to be performed. This method gets that operation.
	 * 
	 * @return the operation of this <tt>Performable</tt> component
	 */
	public Operation getOperation();
	
	/**
	 * Returns the parameters for this <tt>Performable</tt> component.
	 * For a component to be <tt>Performable</tt>, it must have a parameters to go along
	 * with it's valid <tt>Operation</tt>. These parameters models are stored as Strings.
	 * This method gets those parameters.
	 * 
	 * @return the parameters for this <tt>Performable</tt> component
	 */
	public String[] getParams();
	
	/**
	 * Returns the description for this <tt>Performable</tt> action component.
	 * Action objects have an optional description, so it's possinle this will
	 * return an empty String. For conditional actions, the description of the
	 * true action should be returned.
	 * 
	 * @return the description for this <tt>Performable</tt> action component
	 */
	public String getDescription();

}
