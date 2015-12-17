/***********************************************************************//**
 * @file		ExceptionDialog.java
 * @author		Kurt E. Clothier
 * @date		December 9, 2015
 * 
 * @breif		Custom dialog box for displaying  Exceptions
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

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

import games.engine.gui.Displayable;

public final class ExceptionDialog implements Displayable{
	
	private static final Font HEADER_FONT = new Font("Verdana", Font.BOLD, 14);
	private static final Dimension SPACER = new Dimension(10,10);
	private static final Dimension TAB = new Dimension(10,0);
	private final Exception e;
	
	/**
	 * Construct a <tt>ExceptionDialog</tt> using the specified <tt>Exception</tt>.
	 * 
	 * @param e a <tt>Exception</tt> to be used in this dialog.
	 */
	public ExceptionDialog(final Exception e) {
		this.e = e;
	}
	
	/**
	 * Display this <tt>ExceptionDialog</tt>.
	 * Terminates the running application when closed.
	 */
	@Override public void display() {
		JOptionPane.showMessageDialog(null, createDisplayPanel(e), " Exception", JOptionPane.ERROR_MESSAGE);
		System.exit(0);
	}
	
	/** No way to hide this dialog */
	@Override public void hide() {}
	
	/* Create and return a JPanel for this exception dialog */
	private static JPanel createDisplayPanel(final Exception e) {
		final JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		createAndAddHeader(panel, "A fatal Exception has occured!   ");
		createAndAddLabel(panel, "Exception: " + e.getClass().getSimpleName());
		createAndAddLabel(panel, e.getMessage());
		panel.add(new JSeparator(SwingConstants.HORIZONTAL));
		panel.add(Box.createRigidArea(SPACER));
		createAndAddLabel(panel, "Exception detal:");
		createAndAddStackTrace(panel, e);
		return panel;
	}
	
	/* Create and a Header label and add label to the container */
	private static void createAndAddHeader(final Container con, final String string) {
		JLabel label = new JLabel(string);
		label.setFont(HEADER_FONT);
		JPanel headerPanel = new JPanel();
		headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.X_AXIS));
		headerPanel.add(Box.createRigidArea(TAB));
		headerPanel.add(label);
		headerPanel.add(Box.createHorizontalGlue());
		con.add(Box.createRigidArea(SPACER));
		con.add(headerPanel);
		con.add(Box.createRigidArea(SPACER));
	}
	
	/* Create and a label and add label to the container */
	private static void createAndAddLabel(final Container con, final String string) {
		JLabel label = new JLabel(string);
		JPanel labelPanel = new JPanel();
		labelPanel.setLayout(new BoxLayout(labelPanel, BoxLayout.X_AXIS));
		labelPanel.setBackground(con.getBackground());
		labelPanel.add(Box.createRigidArea(TAB));
		labelPanel.add(label);
		labelPanel.add(Box.createHorizontalGlue());
		con.add(labelPanel);
		con.add(Box.createRigidArea(SPACER));
		label.setVisible(true);
	}
	
	/* Get the stack trace and add it to the container */
	private static void createAndAddStackTrace(final Container con, final Exception e) {
		final StackTraceElement[] elements = e.getStackTrace();
		final JPanel tracePanel = new JPanel();
		tracePanel.setLayout(new BoxLayout(tracePanel, BoxLayout.Y_AXIS));
		JPanel panel;
		JLabel label;
		for (final StackTraceElement element : elements) {
			label = new JLabel(element.toString());
			label.setForeground(Color.RED);
			label.setVisible(true);
			panel = new JPanel();
			panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
			panel.add(Box.createRigidArea(TAB));
			panel.add(Box.createRigidArea(TAB));
			panel.add(label);
			panel.add(Box.createHorizontalGlue());
			tracePanel.add(panel);
		}
		con.add(tracePanel);
	}
}
