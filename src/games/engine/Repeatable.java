/***********************************************************************//**
* @file			Repeatable.java
* @author		Kurt E. Clothier
* @date			December 6, 2015
* 
* @breif		API for repeatable engine components
*
* @pre			Compiler: Eclipse - Mars Release (4.5.0)
* @pre			Java: JRE 7 or greater
*
* @see			http://www.projectsbykec.com/
*
* @copyright	The MIT License (MIT) - see LICENSE.txt
****************************************************************************/
package games.engine;

public interface Repeatable extends Performable {
	
	/**
	 * Returns <tt>true</tt> if this <tt>Performable</tt> should be repeated.
	 * 
	 * @return <tt>true</tt> if this <tt>Performable</tt> should be repeated
	 */
	public boolean shouldBeRepeated();
	
	/**
	 * Returns the number of times to repeat this <tt>Performable</tt>.
	 * If the return value is -1, this this <tt>Performable</tt> should be
	 * repeated continuously until the condition returns false.
	 * 
	 * @return the number of times to repeat this <tt>Performable</tt>
	 */
	public int getNumberOfRepetitions();
}