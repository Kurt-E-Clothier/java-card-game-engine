/***********************************************************************//**
 * @file		BuildableContainer.java
 * @author		Kurt E. Clothier
 * @date		December 9, 2015
 * 
 * @breif		Base class for a non-GUI component to be Buildable
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

import java.awt.Container;

/***********************************************************************//**
 * <pre> 
 * The <tt>BuildableContainer</tt> Class
 * 
 * This class can be used to create a <tt>Buildable</tt> GUI object that is
 * not itself an actual <tt>Container</tt>. This can be used for encapsulation
 * purposes, especially with objects like <tt>JFrame</tt> which rarely need
 * to be extended.
 * 
 * Recommended usage example, using <tt>JFrame</tt>:
 * {@code
 * 	public class SimpleGUI extends BuildableContainer<JFrame> {
 * 		private final JFrame frame;
 * 
 * 		// Constructor
 * 		public SimpleGUI() {
 * 			super(new JFrame());
 * 			this.frame = super.getContainer();
 * 		}
 * 
 * 		@Override public void build() {
 * 			if (super.canBeBuilt()) {
 * 				// ...
 * 			}
 * 		}
 * 
 * 		@Override public void refresh() {
 * 			// ...
 * 		}
 *	}
 * } </pre>
 ***************************************************************************/
public abstract class BuildableContainer<T extends Container> implements Buildable {
	
	private final T con;
	private boolean isBuilt;
	
	/**
	 * Constructs a new <tt>BuildableContainer</tt> with the specified <tt>Container</tt> subtype.
	 * 
	 * @param the <tt>Container</tt> to make <tt>Buildable</tt>
	 */
	protected BuildableContainer(final T con) {
		this.con = con;
		isBuilt = false;
	}
	
	/** 
	 * Returns <tt>true</tt> if and only if this container has been built. 
	 * 
	 * @return <tt>true</tt> if and only if this container has been built
	 */
	@Override public boolean hasBeenBuilt() {
		return isBuilt;
	}
	
	/** 
	 * Returns <tt>true</tt> if and only if this container has NOT already been built.
	 * Only use this method in subclasses as a check before performing operations
	 * in the {@link Buildable#build() build} method.
	 * 
	 * @return <tt>true</tt> if and only if the container has NOT already been built
	 */
	protected boolean canBeBuilt() {
		boolean canBuild = false;
		if (!isBuilt) {
			isBuilt = true;
			canBuild = true;
		}
		return canBuild;
	}
	
	/**
	 * Build this GUI component.
	 * Subclasses implementing this method must include a call to {@link #canBeBuilt() canBeBuilt()}
	 * as a prerequisite for performing any actions in order to fulfill the contract of {@link Buildable#build() build()}.
	 */
	@Override public abstract void build();
	
	/**
	 * Rebuild this GUI component; this container will be destroyed and built again.
	 * Calling this method before {@link #build() build()} will have no effect.
	 */
	@Override public void rebuild() {
		if (isBuilt) {
			con.removeAll();
			isBuilt = false;
			build();
		}
	}
	
	/**
	 * Returns the <tt>Container</tt> associated with this <tt>BuildableContainer</tt>.
	 * Use this method to retrieve the <tt>Container</tt> object used in the constructor.
	 * 
	 * @return the <tt>Container</tt> associated with this <tt>BuildableContainer</tt>.
	 */
	protected T getContainer() {
		return con;
	}

}
