/***********************************************************************//**
* @file			EngineComponent.java
* @author		Kurt E. Clothier
* @date			December 5, 2015
* 
* @breif		API for engine components
*
* @pre			Compiler: Eclipse - Mars Release (4.5.0)
* @pre			Java: JRE 7 or greater
*
* @see			http://www.projectsbykec.com/
*
* @copyright	The MIT License (MIT) - see LICENSE.txt
****************************************************************************/

package games.engine;

public interface EngineComponent {
	
	/**
	 * Returns the name of this <tt>EngineComponent</tt>.
	 * Components must have unique names; therefore, each should have a method
	 * of retrieving this unique identifier.
	 * 
	 * @return the name of this <tt>EngineComponent</tt>
	 */
	public String getName();

}
