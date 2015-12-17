/***********************************************************************//**
 * @file		WelcomeGUI.java
 * @author		Kurt E. Clothier
 * @date		December 16, 2015
 * 
 * @breif		Welcome screen, select game and get user data
 * 
 * @pre			Compiler: Eclipse - Mars Release (4.5.0)
 * @pre			Java: JRE 7 or greater
 * 
 * @see			http://www.projectsbykec.com/
 * 
 * @copyright	The MIT License (MIT) - see LICENSE.txt
 ***************************************************************************/
package games.engine.gui;

import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;

import javax.swing.AbstractButton;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

import games.Strings;
import games.engine.Engine;
import games.engine.plugin.Plugin;
import games.engine.plugin.PluginException;
import games.engine.plugin.PluginExceptionDialog;
import games.engine.plugin.PluginFile;
import games.engine.plugin.PluginFilename;
import games.engine.plugin.PluginKeyword;
import games.engine.plugin.PluginList;
import games.engine.plugin.PluginPattern;
import games.engine.util.GamePlayer;

public class WelcomeGUI extends BuildableContainer<JFrame> implements Displayable, ActionListener {
	
	//GUI Variables
	private final JFrame frame;
	private final PluginFilenameButtonGroup group;
	private final ActionListener listener;
	private JPanel mainPanel;
	private BuildablePanel gamePanel;
	private BuildablePanel briefPanel;
	private JButton playButton;
	private Plugin plugin;
	private GamePlayer[] players;
	
/*------------------------------------------------
 	Constructor(s)
 ------------------------------------------------*/
	public WelcomeGUI(final ActionListener listener) {
		super(new JFrame(Engine.GUI_TITLE));
		this.frame = super.getContainer();
		this.group = new PluginFilenameButtonGroup();
		this.listener = listener;
	}
	
	/** Display this GUI */
	@Override public void display() {
		build();
		//frame.pack();
		Resolution.setWindowResolutuion(frame, Resolution.Standard.SVGA, true);
		Resolution.centerWindow(frame);
		frame.setVisible(true);
	}
	
	/** Hide this GUI */
	@Override public void hide() {
		frame.setVisible(false);
	}
	
	/**
	 * Close this GUI, and performs the specified operation.
	 * Only the following operations are valid:
	 * <ul>
	 * <li><tt>DO_NOTHING_ON_CLOSE</tt> Don't do anything.</li>
	 * <li><tt>HIDE_ON_CLOSE</tt> Automatically hide the frame.</li>
	 * <li><tt>DISPOSE_ON_CLOSE</tt> Automatically hide and dispose the frame.</li>
	 * <li><tt>EXIT_ON_CLOSE</tt> Exit the application.</li>
	 * </ul>
	 * 
	 * @param operation the operation to perform when this GUI is closed.
	 * @throws IllegalArgumentException if defaultCloseOperation value isn't one of the above valid values
	 * @throws SecurityException if EXIT_ON_CLOSE has been specified and the SecurityManager will not allow the caller to invoke System.exit
	 * @see javax.swing.JFrame
	 */
	public void close(final int operation) throws IllegalArgumentException, SecurityException {
		frame.setDefaultCloseOperation(operation);
		frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
	}
	
	/** Build this GUI */
	@Override public void build() {
		if (super.canBeBuilt()) {
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			gamePanel = buildGamePanel();
			briefPanel = buildBriefPanel();
			mainPanel = buildMainPanel();
			frame.add(mainPanel);
			group.addListener(this);
		}
	}

	/** Refresh this GUI */
	@Override public void refresh() {
		gamePanel.refresh();
		group.addListener(this);
	}
	
	/** Fired automatically when an action occurs */
	@Override public void actionPerformed(ActionEvent event) {
		final Object source = event.getSource();
		if (source instanceof PluginFilenameRadioButton) {
			briefPanel.refresh();
		}
		else if (source instanceof JButton) {
			final JButton button = (JButton)source;
			if (button.equals(playButton)) {
				try {
					createGame();
				} catch (PluginException e) {
					final PluginExceptionDialog dialog = new PluginExceptionDialog(e);
					dialog.display();
				}
			}
		}
	}
	
	/**
	 * Returns the <tt>GamePlayer</tt> array created by this GUI.
	 * 
	 * @return the <tt>GamePlayer</tt> array created by this GUI
	 */
	public GamePlayer[] getGamePlayers() {
		return players;
	}
	
	/**
	 * Returns the rules <tt>Plugin</tt> created by this GUI.
	 * 
	 * @return the rules <tt>Plugin</tt>  created by this GUI
	 */
	public Plugin getRulesPlugin() {
		return plugin;
	}
	
	/* Create and return the playButton */
	private void createAndAddPlayButton(final Container con) {
		if (playButton == null) {
			playButton = new JButton("Play");
			playButton.addActionListener(this);
		}
		final JPanel panel = new JPanel();
		panel.setBackground(con.getBackground());
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		panel.add(Box.createHorizontalGlue());
		panel.add(playButton);
		panel.add(Box.createRigidArea(Constants.TAB));
		con.add(panel);
	}
	
	/* Build and return the game panel */
	private BuildablePanel buildBriefPanel() {
		final BuildablePanel panel = new BriefPanel();
		panel.build();
		return panel;
	}
	
	/* Build and return the game panel */
	private BuildablePanel buildGamePanel() {
		final BuildablePanel panel = new GamePanel();
		panel.build();
		return panel;
	}
	
	/* Build and return the main panel */
	private JPanel buildMainPanel() {
		final JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		createAndAddHeader(panel, "Card Game Selection", Constants.HEADER_FONT, true);
		panel.add(new JSeparator(SwingConstants.HORIZONTAL));
		panel.add(Box.createRigidArea(Constants.SPACER));
		panel.add(gamePanel);
		panel.add(Box.createVerticalGlue());
		panel.add(briefPanel);
		panel.add(Box.createVerticalGlue());
		panel.setVisible(true);
		return panel;
	}
	
	/* Create and a Header label and add label to the container */
	protected static JLabel createAndAddHeader(final Container con, final String string) {
		return createAndAddHeader(con, string, null, false);
	}
	
	/* Create and a Header label with specified font and add label to the container */
	protected static JLabel createAndAddHeader(final Container con, final String string, final Font font) {
		return createAndAddHeader(con, string, font, false);
	}
		
	/* Create and a Header label with specified font and add label to the container */
	protected static JLabel createAndAddHeader(final Container con, final String string, final Font font, final boolean isCentered) {
		JLabel label = new JLabel(string);
		label.setFont(font == null ? Constants.SUBHEADER_FONT : font);
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		if (isCentered) {
			panel.add(Box.createHorizontalGlue());
		}
		else {
			panel.add(Box.createRigidArea(Constants.TAB));
		}
		panel.add(label);
		panel.add(Box.createHorizontalGlue());
		con.add(Box.createRigidArea(Constants.SPACER));
		con.add(panel);
		con.add(Box.createRigidArea(Constants.SPACER));
		return label;
	}
	
	/* Get the necessary engine components and create the engine */
	private void createGame() throws PluginException {
		plugin = new Plugin(group.getSelectedButton().getFilename());
		createGamePlayers(getNumPlayers());
		if (players != null && players.length > 0) {
			
			// Call the action listener!
			this.listener.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null));
			//JOptionPane.showMessageDialog(null, "The Engine is ALIVE!", Engine.GUI_TITLE, JOptionPane.INFORMATION_MESSAGE);
		}
	}
	
	/* Get game players for the game */
	private void createGamePlayers(final int numPlayers) {
		players = new GamePlayer[numPlayers];
		final StringBuilder str = new StringBuilder();
		for (int i = 0; i < players.length; i++) {
			str.setLength(0);
			str.append("What is the name of player ").append(i + 1).append('?');			
			boolean shouldRepeat;
			do {
				shouldRepeat = false;
				final String name = (String)JOptionPane.showInputDialog(null, str.toString(), Engine.GUI_TITLE, JOptionPane.QUESTION_MESSAGE);
				if (name == null) {
					players = null;
					return;
				}
				else if (name.length() > 0) {
					players[i] = new GamePlayer(name);
				}
				else {
					JOptionPane.showMessageDialog(null, "Please enter a valid name!", Engine.GUI_TITLE, JOptionPane.ERROR_MESSAGE);
					shouldRepeat = true;
				}
			} while(shouldRepeat);
		}
	}
	
	/* Get Game number of game players, letting user select from options in plugin */
	private int getNumPlayers() throws PluginException {
		final String line = plugin.checkParamsFor(PluginKeyword.PLAYERS);
		int numPlayers = 0;
		if (line.contains(PluginPattern.DASH.toString())) {
			final String[] parts = line.split(PluginPattern.DASH.toString());
			if (parts.length != 2) {
				throw PluginException.create(PluginException.Type.INVALID_PARAMETER, plugin, PluginKeyword.PLAYERS.toString(), line);
			}
			final int min = convertString(parts[0], plugin);
			final int max = convertString(parts[1], plugin);
			if (min >= max) {
				throw PluginException.create(PluginException.Type.INVALID_PARAMETER, plugin, PluginKeyword.PLAYERS.toString(), line);
			}
			final int[] nums = new int[max - min + 1];
			for (int i = 0; i < nums.length; i++) {
				nums[i] = i + min;
			}
			numPlayers = getNumPlayers(nums);
		}
		else if (line.contains(PluginPattern.COMMA.toString())) {
			final String[] parts = line.split(PluginPattern.COMMA.toString());
			if (parts.length < 2) {
				throw PluginException.create(PluginException.Type.INVALID_PARAMETER, plugin, PluginKeyword.PLAYERS.toString(), line);
			}
			final int[] nums = new int[parts.length];
			for (int i = 0; i < nums.length; i++) {
				nums[i] = convertString(parts[i], plugin);
			}
			numPlayers = getNumPlayers(nums);
		}
		else {
			numPlayers = 0;
		}
		return numPlayers;
	}
	
	/* Get the number of players by displaying a dialog, asking the user for input. */
	private int getNumPlayers(final int[] nums) {
		final NumPlayersDialog dialog = new NumPlayersDialog(nums);
		dialog.display();
		return dialog.getSelectedNum();
	}
	
	/* Convert the string to an integer */
	private int convertString(final String string, final Plugin plugin) throws PluginException {
		try {
			return Integer.valueOf(string);
		} catch (NumberFormatException e) {
			throw PluginException.create(PluginException.Type.DATA_REPRESENTATION, e, plugin, string);
		}
	}
	
	/* **********************************************************************
	 * Nested Class NumPlayersDialog
	 * 	- Displays all possible parameter options for a selected action
	 * 	- User selects options, which are used as action parameters
	 ************************************************************************/
		private class NumPlayersDialog implements Displayable {
			
			private JPanel numPanel;
			private final int[] nums;
			private final JButtonGroup<JRadioButton> bGroup;
			
			public NumPlayersDialog(final int[] nums) {
				this.nums = Arrays.copyOf(nums, nums.length);
				bGroup = new JButtonGroup<JRadioButton>();
			}

			/** Display this panel */
			@Override public void display() {
				createPanel();
				JOptionPane.showMessageDialog(null, numPanel, Engine.GUI_TITLE, JOptionPane.QUESTION_MESSAGE);
			}

			/** Hide this panel */
			@Override public void hide() {
				numPanel.setVisible(false);
			}
			
			/* Returns the selected number from this dialog */
			public int getSelectedNum() {
				int groupNdx = 0;
				for (Enumeration<AbstractButton> buttons = bGroup.getElements(); buttons.hasMoreElements();) {
					AbstractButton button = buttons.nextElement();
					if (button.isSelected()) {
						return nums[groupNdx];
			        }
					++groupNdx;
			    }
				return 0;
			}
			
			/* Create a Panel for this dialog */
			private void createPanel() {
				numPanel = new JPanel();
				numPanel.setLayout(new BoxLayout(numPanel, BoxLayout.Y_AXIS));
				createAndAddHeader(numPanel, "How many players are there?");
				numPanel.add(Box.createRigidArea(Constants.SPACER));
				JRadioButton button;
				JPanel panel;
				for (int n : nums) {
					button = new JRadioButton("   " + Integer.toString(n));
					panel = new JPanel();
					panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
					panel.add(Box.createRigidArea(Constants.TAB));
					panel.add(button);
					panel.add(Box.createHorizontalGlue());
					bGroup.add(button);
					numPanel.add(panel);
					numPanel.add(Box.createRigidArea(Constants.SPACER));
				}
				bGroup.setFirstButtonAsSelected();
			}
		}
	
	/* **********************************************************************
	 * Nested Class BriefPanel
	 * 	- Displays all possible games by searching the plugin directory for rules files.
	 ************************************************************************/
	private class BriefPanel extends BuildablePanel {
		private static final long serialVersionUID = -5718462133391696745L;
		private JLabel filenameLabel;
		private JPanel innerPanel;
		private JScrollPane scrollPane;
		private List<String> lines;
		private PluginFile file;
		
		@Override public void refresh() {
			if (group == null) return;
			if (file != null && file.getFilename().equals(group.getSelectedButton().getFilename())) {
				filenameLabel.setText(Strings.toCamelCase(file.getFilename().getRawName()));
				scrollPane.setVisible(true);
			}
			else try {
				file = group.getSelectedButton().getFilename().toFile(Plugin.Type.BRIEF);
				if (file.exists()) {
					innerPanel.removeAll();
					lines = Plugin.readPluginFile(file);
					Plugin.processFileLines(lines, false);
					filenameLabel.setText(Strings.toCamelCase(file.getFilename().getRawName()));
					add(Box.createRigidArea(Constants.SPACER));
					for (final String s : lines) {
						createAndAddText(s);
					}
					scrollPane.setVisible(true);
				}
				else {
					filenameLabel.setText(Strings.toCamelCase(group.getSelectedButton().getFilename().getRawName()));
					scrollPane.setVisible(false);
				}
			} catch (PluginException e) {
				final PluginExceptionDialog dialog = new PluginExceptionDialog(e);
				dialog.display();
			}
		}

		@Override public void build() {
			if (super.canBeBuilt()) {
				setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
				setBorder(Constants.BORDER_SPACE);
				innerPanel = new JPanel();
				innerPanel.setLayout(new BoxLayout(innerPanel, BoxLayout.Y_AXIS));
				scrollPane = new JScrollPane(innerPanel,
			            JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
			            JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
				scrollPane.getVerticalScrollBar().setUnitIncrement(16);
				add(Box.createRigidArea(Constants.SPACER));
				filenameLabel = createAndAddHeader(this, "", Constants.SUBHEADER_FONT);
				add(Box.createRigidArea(Constants.SPACER));
				add(scrollPane);
				add(Box.createRigidArea(Constants.SPACER));
				createAndAddPlayButton(this);
				add(Box.createVerticalGlue());
				refresh();
			}
		}
		
		/* Create a text area and add to panel */
		private void createAndAddText(final String string) {
			final JTextArea text = new JTextArea(string);
			text.setLineWrap(true);
			text.setWrapStyleWord(true);
			innerPanel.add(text);
			innerPanel.add(Box.createRigidArea(Constants.SPACER));
		}
	}
	
	/* **********************************************************************
	 * Nested Class GamePanel
	 * 	- Displays all possible games by searching the plugin directory for rules files.
	 ************************************************************************/
	private class GamePanel extends BuildablePanel {
		private static final long serialVersionUID = 2660893882809373859L;
		private JPanel innerPanel;
		
		/** Refresh this panel */
		@Override public void refresh() {
			innerPanel.removeAll();
			group.removeAll();
			group.createAndAddButtons(Plugin.Type.RULES, innerPanel);
			group.setFirstButtonAsSelected();
			//innerPanel.add(Box.createVerticalGlue());
		}

		/** Build this panel */
		@Override public void build() {
			if (super.canBeBuilt()) {
				innerPanel = new JPanel();
				setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
				innerPanel.setLayout(new BoxLayout(innerPanel, BoxLayout.Y_AXIS));
				final JScrollPane scrollPane = new JScrollPane(innerPanel,
			            JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
			            JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
				scrollPane.getVerticalScrollBar().setUnitIncrement(16);
				add(Box.createRigidArea(Constants.SPACER));
				createAndAddHeader(this, "Choose a game to play!");
				add(Box.createRigidArea(Constants.SPACER));
				add(scrollPane);
				//add(Box.createVerticalGlue());
				refresh();
			}
		}
	}
	
	/* **********************************************************************
	 * Nested Class PluginFilenameRadioButton
	 * 	- Displays all possible games by searching the plugin directory for rules files.
	 ************************************************************************/
	private class PluginFilenameRadioButton extends JRadioButton {
		private static final long serialVersionUID = -8313382674357896731L;
		private final PluginFilename filename;
		
		/** Construct this RadioButton with the specified PluginFilename. */
		public PluginFilenameRadioButton(final PluginFilename filename) {
			super(Strings.toCamelCase(filename.getRawName()));
			this.filename = filename;
		}
		
		/** Returns the PluginFilename associated with this RadioButton */
		public PluginFilename getFilename() {
			return filename;
		}
	}
	
	/* **********************************************************************
	 * Nested Class JButtonGroup
	 * 	- A better button group
	 ************************************************************************/
	private class JButtonGroup<T extends AbstractButton> extends ButtonGroup {
		private static final long serialVersionUID = 4908368765030724758L;

		/** Removes all buttons from this group */
		public void removeAll() {
			for (Enumeration<AbstractButton> buttons = this.getElements(); buttons.hasMoreElements();) {
				AbstractButton button = buttons.nextElement();
				button.setSelected(false);
				this.remove(button);
		    }
		}
		
		/** Add listener to all buttons in this group */
		public void addListener(final ActionListener listener) {
			for (Enumeration<AbstractButton> buttons = this.getElements(); buttons.hasMoreElements();) {
				AbstractButton button = buttons.nextElement();
				button.addActionListener(listener);
		    }
		}
		
		/** Sets the first button in this group as selected; returns false if no buttons in group. */
		public boolean setFirstButtonAsSelected() {
			if (this.getButtonCount() > 0) {
				this.getElements().nextElement().setSelected(true);
				return true;
			}
			return false;
		}
		
		/** Return the selected button from this group */
		@SuppressWarnings("unchecked")
		public T getSelectedButton() {
			for (Enumeration<AbstractButton> buttons = this.getElements(); buttons.hasMoreElements();) {
				AbstractButton button = buttons.nextElement();
				if (button.isSelected()) {
					return (T)button;
		        }
		    }
			return null;
		}
	}
	
	/* **********************************************************************
	 * Nested Class PluginFilenameButtonGroup
	 * 	- Group of PluginFilenameRadioButtons
	 ************************************************************************/
	private class PluginFilenameButtonGroup extends JButtonGroup<PluginFilenameRadioButton> {
		
		private static final long serialVersionUID = 6857152407119045203L;

		/** Create PluginFilenameRadioButtons of the specified type and add to this group. */
		public void createAndAddButtons(final Plugin.Type type, final Container con) {
			final PluginList pList = new PluginList(type);
			for (final PluginFilename fName : pList.get()) {
				PluginFilenameRadioButton button = new PluginFilenameRadioButton(fName);
				button.setBackground(con.getBackground());
				JPanel panel = new JPanel();
				panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
				panel.setBackground(con.getBackground());
				panel.add(Box.createRigidArea(Constants.TAB));
				panel.add(button);
				panel.add(Box.createHorizontalGlue());
				this.add(button);
				con.add(panel);
				con.add(Box.createRigidArea(Constants.SPACER));
			}
		}
	}
}
