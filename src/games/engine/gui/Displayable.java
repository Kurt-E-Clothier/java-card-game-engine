/***********************************************************************//**
 * @file		Displayable.java
 * @author		Kurt E. Clothier
 * @date		December 9, 2015
 * 
 * @breif		API for displayable GUI components
 * 
 * @pre			Compiler: Eclipse - Mars Release (4.5.0)
 * @pre			Java: JRE 7 or greater
 * 
 * @see			http://www.projectsbykec.com/
 * @see			PlayingCard
 * 
 * @copyright	The MIT License (MIT) - see LICENSE.txt
 ***************************************************************************/
package games.engine.gui;

public interface Displayable {
	
	/**
	 * Display this component.
	 * This method should cause the GUI component to be displayed, and should
	 * perform whatever is necessary to make that happen.
	 */
	public void display();
	
	/**
	 * Stop displaying this component.
	 * This method should cause the GUI component to  become invisible, but
	 * that does not necessarily mean it stops existing.
	 */
	public void hide();

}
