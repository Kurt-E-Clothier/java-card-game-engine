/***********************************************************************//**
 * @file		BuildablePanel.java
 * @author		Kurt E. Clothier
 * @date		December 9, 2015
 * 
 * @breif		Base class for buildable JPanel objects
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

import javax.swing.JPanel;

public abstract class BuildablePanel extends JPanel implements Buildable{

	private static final long serialVersionUID = 6796138568240547383L;
	private final BPanel panel;
	
	/**
	 * Constructs a new <tt>BuildablePanel</tt> with a double buffer and flow layout.
	 */
	protected BuildablePanel() {
		super();
		panel = new BPanel(this);
	}
	
	/**
	 * Build this Panel.
	 * Subclasses implementing this method must include a call to {@link #canBeBuilt() canBeBuilt()}
	 * as a prerequisite for performing any actions in order to fulfill the contract of {@link Buildable#build() build()}.
	 */
	@Override public abstract void build();

	/**
	 * Rebuild this panel; this panel will be destroyed and built again.
	 * Calling this method before {@link #build() build()} will have no effect.
	 */
	@Override public void rebuild() {
		panel.rebuild();
	}

	/** 
	 * Returns <tt>true</tt> if and only if this panel has been built. 
	 * 
	 * @return <tt>true</tt> if and only if this panel has been built
	 */
	@Override public boolean hasBeenBuilt() {
		return panel.hasBeenBuilt();
	}
	
	/** 
	 * Returns <tt>true</tt> if and only if this panel has NOT already been built.
	 * Only use this method in subclasses as a check before performing operations
	 * in the {@link Buildable#build() build} method.
	 * 
	 * @return <tt>true</tt> if and only if this panel has NOT already been built
	 */
	protected boolean canBeBuilt() {
		return panel.canBeBuilt();
	}

	/* Helper class allows the extension of JPanel while also using the predefined BuildableContainer methods. */
	private class BPanel extends BuildableContainer<BuildablePanel> {
		private BPanel(final BuildablePanel panel) {
			super(panel);
		}
		@Override public void build() {}
		@Override public void refresh() {}
	}
}
