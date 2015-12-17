/***********************************************************************//**
 * @file		Constants.java
 * @author		Kurt E. Clothier
 * @date		December 15, 2015
 * 
 * @breif		Constants for use in game engine GUIs
 * 
 * @pre			Compiler: Eclipse - Mars Release (4.5.0)
 * @pre			Java: JRE 7 or greater
 * 
 * @see			http://www.projectsbykec.com/
 * 
 * @copyright	The MIT License (MIT) - see LICENSE.txt
 ***************************************************************************/
package games.engine.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;

public enum Constants {
	
	INSTANCE;

	static final Color BACKGROUND_COLOR = Color.lightGray;
	static final Color TABLE_COLOR = new Color(0, 100, 0);
	
	static final Font HEADER_FONT = new Font("Verdana", Font.BOLD, 18);
	static final Font SUBHEADER_FONT = new Font("Verdana", Font.BOLD, 14);
	
	static final Dimension SPACER = new Dimension(10,10);
	static final Dimension TAB = new Dimension(10,0);
	static final Dimension MARGIN_SPACE = new Dimension(50,80);
	
	static final Border ETCHED = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
	static final Border THICK_LINE = BorderFactory.createMatteBorder(2, 2, 2, 2, Color.BLACK);
	static final Border BORDER_SPACE = BorderFactory.createEmptyBorder(6, 6, 6, 6);
	static final Border BORDER_THICK = BorderFactory.createMatteBorder(4, 4, 4, 4, Color.BLACK);
	static final Border BORDER_INSET_LINE = BorderFactory.createCompoundBorder(BORDER_SPACE, BORDER_THICK);

}
