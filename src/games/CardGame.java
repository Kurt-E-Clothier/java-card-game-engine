/***********************************************************************//**
* @file			CardGame.java
* @author		Kurt E. Clothier
* @date			November 9, 2015
*
* @breif		The public interface for a single card game
*
* @pre			Compiler: Eclipse - Mars Release (4.5.0)
* @pre			Java: JRE 7 or greater
*
* @see			http://www.projectsbykec.com/
*
* @copyright	The MIT License (MIT) - see LICENSE.txt
****************************************************************************/

package games;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.io.File;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;


// TODO: Auto-generated Javadoc
/**
 * **********************************************************************
 * The CardGame Class
 * - This class serves as the public interface for a card game
 * - The engine and UI are initiated by the card game
 * **********************************************************************.
 */
public final class CardGame {
	
	
/** The Constant WIDTH. */
/*------------------------------------------------
 	Constants and Attributes
 ------------------------------------------------*/
	private final static int WIDTH = 500;
	
	/** The Constant HEIGHT. */
	private final static int HEIGHT = 500;
	
	/** The header font. */
	private final Font HEADER_FONT = new Font("Verdana", Font.BOLD, 12);
	
	/** The margin space. */
	private final Dimension MARGIN_SPACE = new Dimension(WIDTH, 10);
	
	/** The button size. */
	private final Dimension BUTTON_SIZE = new Dimension(WIDTH/2, 20);
	
/*------------------------------------------------
 	Constructor(s)
 ------------------------------------------------*/
	/**
 * Construct a <tt>CardGame</tt> using the rules file.
 */
	public CardGame() {
		
		/*
		
		// Get available games to play
		String[] gameNames = FileIO.listFileNames(FileIO.Type.RULES);
		int numGames = gameNames.length;
		
		// get user screen size
		int screenWidth = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth();
		int screenHeight = (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight();
		
		// Build JFrame
		JFrame frame = new JFrame("Ultimate Card Game Collection");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(CardGame.WIDTH, CardGame.HEIGHT);
		frame.setLocation((screenWidth - WIDTH)/2, (screenHeight - HEIGHT)/2);
		
		JPanel panel = new JPanel(new GridLayout(numGames*2 + 3, 1));
		panel.setSize(new Dimension(WIDTH/2, HEIGHT));
		
		//panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		JLabel header = new JLabel ("Select a game to play!");
		
		//header.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		header.setHorizontalAlignment(JLabel.CENTER);
        header.setFont(HEADER_FONT);
        
        panel.add(Box.createRigidArea(MARGIN_SPACE));
        panel.add(header);
        panel.add(Box.createRigidArea(MARGIN_SPACE));
        
        JButton[] buttons = new JButton[numGames];
        
        for (int i = 0; i < numGames; i++) {
        	buttons[i] = new JButton(FileIO.strip(gameNames[i]));
        	buttons[i].setAlignmentX(JLabel.CENTER_ALIGNMENT);
        	//buttons[i].setPreferredSize(BUTTON_SIZE);
        	
        	panel.add(Box.createRigidArea(MARGIN_SPACE));
        	panel.add(buttons[i]);
        }
        
        	
  
        	
        	
        
        //header.setVisible(true);
		
		//frame.dispose();
		
		frame.add(panel);
		
		
		frame.setVisible(true);
		
		// Greet User
		// Display List of available card games (rules text files in plugin dir)
		
		// Allow user to select a single game
		
		// Create rules FileCopy
		
		// Read number of players
		
		// Allow user to select number of players
		
		// Prompt for player names (and possibly if CPU)
		
		// Start CardGameBuilder with rules and Players
		
		// Build GUI by passing CardGameBuilder to it
		 * 
		 */
		
	}
	
/*------------------------------------------------
    Accessors and Mutators
 ------------------------------------------------*/

	
/**
 * The main method.
 *
 * @param args the arguments
 */
/*------------------------------------------------
    Main Method for Testing
 ------------------------------------------------*/	
	public static void main(String[] args) {
		System.out.println(" --- Card Game Test Bench ---\n");
		
		//CardGame game = new CardGame();

	}
	
}

