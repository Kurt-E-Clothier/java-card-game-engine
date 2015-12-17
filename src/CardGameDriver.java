/***********************************************************************//**
 * @file		WelcomeGUI.java
 * @author		Kurt E. Clothier
 * @date		December 17, 2015
 * 
 * @breif		Driver for the card game engine
 * 
 * @pre			Compiler: Eclipse - Mars Release (4.5.0)
 * @pre			Java: JRE 7 or greater
 * 
 * @see			http://www.projectsbykec.com/
 * 
 * @copyright	The MIT License (MIT) - see LICENSE.txt
 ***************************************************************************/

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;

import games.engine.CardGameEngine;
import games.engine.EngineException;
import games.engine.EngineExceptionDialog;
import games.engine.EngineFactory;
import games.engine.gui.ExceptionDialog;
import games.engine.gui.SimpleGUI;
import games.engine.gui.WelcomeGUI;
import games.engine.plugin.PluginException;
import games.engine.plugin.PluginExceptionDialog;

public final class CardGameDriver implements ActionListener {
	
	private final WelcomeGUI gui;
	
	/* Contructs this driver */
	private CardGameDriver() {
		gui = new WelcomeGUI(this);
	}
	
	/* Display the WelcomeGUI with this driver as the ActionListener */
	private void displayWelcomeGUI() {
		gui.display();
	}
	
	/**
	 * Perform actions in response to an event.
	 * This listener is responsible for creating the game engine and any
	 * other sort of user interfaces.
	 */
	@Override public void actionPerformed(ActionEvent event) {
		try {
			final CardGameEngine engine = EngineFactory.INSTANCE.createCardGameEngine(gui.getRulesPlugin(), gui.getGamePlayers());
			final SimpleGUI simpleGUI = new SimpleGUI(engine);
			gui.close(JFrame.DISPOSE_ON_CLOSE);
			simpleGUI.display();
			simpleGUI.startGame();
		} catch (PluginException e) {
			final PluginExceptionDialog dialog = new PluginExceptionDialog(e);
			dialog.display();
		} catch (EngineException e) {
			final EngineExceptionDialog dialog = new EngineExceptionDialog(e);
			dialog.display();
		} catch (Exception e) {
			final ExceptionDialog dialog = new ExceptionDialog(e);
			dialog.display();
		}

	}
	
	/**
	 * Run this <tt>CardGameDriver</tt>.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		final CardGameDriver driver = new CardGameDriver();
		driver.displayWelcomeGUI();
	}
}
