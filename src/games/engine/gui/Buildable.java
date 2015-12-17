/***********************************************************************//**
 * @file		Buildable.java
 * @author		Kurt E. Clothier
 * @date		December 9, 2015
 * 
 * @breif		API for buildable GUI components
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

public interface Buildable {
	
	/**
	 * Build this GUI component.
	 * This method must ensure that all inner components are created as well.
	 * This component must not not be built before this method is called.
	 * Calling this method more than one time will have no effect.
	 */
	public void build();
	
	/**
	 * Rebuild this GUI component.
	 * This method must ensure that all inner components are removed
	 * and everything is built again.
	 * Calling this method before {@link #build() build} will have no effect.
	 */
	public void rebuild();
	
	/**
	 * Returns <tt>true</tt> if and only if this GUI component has been built.
	 * This boolean value must only return <tt>true</tt> after the {@link #build() build}
	 * method has been called.
	 * 
	 * @return <tt>true</tt> iff this component has been built.
	 */
	public boolean hasBeenBuilt();
	
	/**
	 * Refresh (update) all content in this this GUI component.
	 * Calling this method must ensure that all inner components are updated as well.
	 */
	public void refresh();
}
