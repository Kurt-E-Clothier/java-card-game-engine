/***********************************************************************//**
 * @file		SimpleGUI.java
 * @author		Kurt E. Clothier
 * @date		December 16, 2015
 * 
 * @breif		A very simple, text-based GUI (seriously)
 * 
 * @pre			Compiler: Eclipse - Mars Release (4.5.0)
 * @pre			Java: JRE 7 or greater
 * 
 * @see			http://www.projectsbykec.com/
 * 
 * @copyright	The MIT License (MIT) - see LICENSE.txt
 ***************************************************************************/
package games.engine.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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
import javax.swing.SwingConstants;

import games.engine.AllowedAction;
import games.engine.CardGameEngine;
import games.engine.Engine;
import games.engine.EngineException;
import games.engine.EngineExceptionDialog;
import games.engine.EngineFactory;
import games.engine.Operation;
import games.engine.plugin.Plugin;
import games.engine.plugin.PluginException;
import games.engine.plugin.PluginExceptionDialog;
import games.engine.util.CardGameBoard;
import games.engine.util.CardPile;
import games.engine.util.CardPileCollection;
import games.engine.util.CardPileParameter.Placement;
import games.engine.util.CardPlayer;
import games.engine.util.GamePlayer;
import games.engine.util.PlayingCard;

public class SimpleGUI extends BuildableContainer<JFrame> implements Displayable  {
	
/*------------------------------------------------
 	Constants and Attributes
 ------------------------------------------------*/
	private static final List<Operation.Parameter.Value> PARAMS = new ArrayList<Operation.Parameter.Value>();
	private static final Map<AllowedAction, JPanel> BUTTON_PANELS = new ConcurrentHashMap<AllowedAction, JPanel>();
	
	// Card Pile Collection Areas
	private static final double ROW_WEIGHT_CARD_PILES = 0.8;
	private static final double ROW_WEIGHT_ALLOWED_ACTIONS = 0.3;
	
	// Info Panel
	private static final Color INFO_TEXT_COLOR = Color.WHITE;
	private static final Color INFO_BACKGROUND_COLOR = Color.DARK_GRAY;
	private static final Font INFO_FONT = new Font("Verdana", Font.BOLD, 20);
	
	
	// GUI Variables
	private final JFrame frame;
	private final GUIListener listener;
	private final Resolution resolution;
	private final InfoPanel infoPanel;
	private final MainPanel mainPanel;
	private final CardGameEngine engine;
	private final CardGameBoard board;
	
/*------------------------------------------------
 	Constructor(s)
 ------------------------------------------------*/
	public SimpleGUI(final CardGameEngine engine) {
		super(new JFrame(Engine.GUI_TITLE));
		this.frame = super.getContainer();
		this.resolution = Resolution.getInstance();
		this.engine = engine;
		this.board = engine.getGameBoard();
		this.infoPanel = new InfoPanel();
		this.mainPanel = new MainPanel();
		this.listener = new GUIListener();
	}
	
/*------------------------------------------------
	GUI Creation and Related Methods
 ------------------------------------------------*/
	/** Build the GUI */
	@Override public void build() {
		if (super.canBeBuilt()) {
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			resolution.maximizeFrame(frame);
			infoPanel.build();
			mainPanel.build();
			frame.add(infoPanel, BorderLayout.NORTH);
			frame.add(mainPanel, BorderLayout.CENTER);
		}
	}
	
	/** Refresh the GUI */
	@Override public void refresh() {
		infoPanel.refresh();
		mainPanel.refresh();
		frame.revalidate();
		frame.repaint();
	}
	
	/** Display the GUI */
	@Override public void display() {
		build();
		frame.pack();
		frame.setVisible(true);
	}
	
	/** Hide the GUI */
	@Override public void hide() {
		frame.setVisible(false);
	}
	
	public void startGame() throws NoSuchFieldException, IllegalArgumentException, EngineException, PluginException {
		final Object[] options = {"Deal Cards", "Exit"};
		if (JOptionPane.showOptionDialog(null, "Get ready to play!", engine.getName(), JOptionPane.YES_NO_OPTION, 
											JOptionPane.INFORMATION_MESSAGE, null, options, options[0])  == JOptionPane.OK_OPTION) {
			engine.start();
			refresh();
		}
		else {
			System.exit(0);
		}
	}
		
/*------------------------------------------------
	Game Engine Control
 ------------------------------------------------*/
	
/* **********************************************************************
 * Nested Class GUIListener
 * 	- Responds to events in the GUI
 * 	- Controls the program flow by running the game engine
 ************************************************************************/
	private class GUIListener implements ActionListener {
		
		private boolean shouldPerformAction;
		
		/**
		 * Perform events when a button is pressed.
		 * Catches any exceptions and displays the appropriate dialog.
		 * 
		 * @param e the ActionEvent that triggered this listener
		 */
		@Override public void actionPerformed(ActionEvent e) {
			
			try {
				if (e.getSource() instanceof ActionButton) {
					final ActionButton button = (ActionButton)e.getSource();
					final AllowedAction action = button.getAllowedAction();
					button.setSelected(false);
					getAndDisplayOptions(action);
				}
			} catch (EngineException e1) {
				EngineExceptionDialog dialog = new EngineExceptionDialog(e1);
				dialog.display();
			} catch (PluginException e1) {
				PluginExceptionDialog dialog = new PluginExceptionDialog(e1);
				dialog.display();
			} catch (Exception e1) {
				ExceptionDialog dialog = new ExceptionDialog(e1);
				dialog.display();
			}
		}
		
		/* get action parameter options and create display dialog */
		private void getAndDisplayOptions(final AllowedAction action) throws EngineException, PluginException, NoSuchFieldException {
			shouldPerformAction = true;
			final CardPlayer player = engine.getCurrentPlayer();
			final List<Operation.Parameter.OptionList> list = engine.getOptions(action);
			PARAMS.clear();
			for (final Operation.Parameter.OptionList optionList : list) {
				OptionDialog dialog = new OptionDialog(optionList, "Action: " + action.getDescription());
				dialog.display();
				if (dialog.userCanceled()) {
					shouldPerformAction = false;
					break;
				}
			}
			if (shouldPerformAction) {
				if (engine.perform(action, PARAMS)) {
					if (player.hasWon()) {
						refresh();
						final Object[] options = {"Play Again!", "Quit"};
						if (JOptionPane.showOptionDialog(null, "Player " + player.getName() + " has won!", Engine.GUI_TITLE, JOptionPane.OK_CANCEL_OPTION, 
															JOptionPane.INFORMATION_MESSAGE, null, options, options[0])  == JOptionPane.OK_OPTION) {
							PARAMS.clear();
							engine.reset();
							engine.start();
						}
						else {
							System.exit(0);
						}
					}
					else if (player.equals(engine.getCurrentPlayer())) {
						JOptionPane.showMessageDialog(null, "Action successul!\n\nIt is still your turn...", Engine.GUI_TITLE, JOptionPane.INFORMATION_MESSAGE);
					}
					else {
						JOptionPane.showMessageDialog(null, "Action successul!\n\nNext player's turn...", Engine.GUI_TITLE, JOptionPane.INFORMATION_MESSAGE);
					}
				}
				else {
					JOptionPane.showMessageDialog(null, "Sorry, that move isn't valid!\n\nIt is still your turn...", Engine.GUI_TITLE, JOptionPane.ERROR_MESSAGE);
				}
			}
			else {
				JOptionPane.showMessageDialog(null, "Action canceled!\n\nIt is still your turn...", Engine.GUI_TITLE, JOptionPane.WARNING_MESSAGE);
			}
			refresh();
		}
	}  // End of nested class: GUIListener
	
/* **********************************************************************
 * Nested Class OptionDialog
 * 	- Displays all possible parameter options for a selected action
 * 	- User selects options, which are used as action parameters
 ************************************************************************/
	private class OptionDialog implements Displayable {
		private final Operation.Parameter.OptionList optionList;
		private final String message;
		private ButtonGroup group;
		private JPanel optionPanel;
		private JPanel buttonPanel;
		private boolean userCanceled;
		
		public OptionDialog (final Operation.Parameter.OptionList optionList, final String message) {
			this.optionList = optionList;
			this.message = message;
		}
		
		/** Hide this panel */
		@Override public void hide() {
			optionPanel.setVisible(false);
		}
		
		/** Display this <tt>OptionsDialog</tt>. */
		@Override public void display() {
			createDisplayPanel();
			optionPanel.setVisible(true);
			boolean shouldRepeat;
			do {
				shouldRepeat = false;
				userCanceled = false;
				int response = JOptionPane.CANCEL_OPTION;
				if (!PARAMS.isEmpty() && optionList.isMultiple() && !optionList.getOptions().isEmpty()) {
					update();
					Object[] options = {"Choose More", "Done", "Cancel"};
					response = JOptionPane.showOptionDialog(null, optionPanel, "Action Parameter Options", JOptionPane.YES_NO_CANCEL_OPTION, 
															JOptionPane.PLAIN_MESSAGE, null, options, options[1]);
				}
				else {
					response = JOptionPane.showConfirmDialog(null, optionPanel, "Action Parameter Options", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE); 
				}
				
				switch (response) {
				//case JOptionPane.OK_OPTION:	// Same as Yes option
				case JOptionPane.YES_OPTION:
					if (optionList.isMultiple() && !optionList.getOptions().isEmpty()) {
						shouldRepeat = true;
					}
				case JOptionPane.NO_OPTION:		// This is really "Done"
					PARAMS.add(getAndRemoveSelectedParameter());
					break;
				case JOptionPane.CANCEL_OPTION:	// Cancel this move!
					shouldRepeat = false;
					userCanceled = true;
					PARAMS.clear();
				default:
					break;
				}
			} while(shouldRepeat);
		}
		
		/** Returns <tt>true</tt> if the user canceled the action. */
		public boolean userCanceled() {
			return userCanceled;
		}
		
		/* Return the selected parameter by finding the index of the selected option */
		private Operation.Parameter.Value getSelectedParameter() {
			int groupNdx = 0;
			for (Enumeration<AbstractButton> buttons = group.getElements(); buttons.hasMoreElements();) {
				AbstractButton button = buttons.nextElement();
				if (button.isSelected()) {
					return optionList.getOptions().get(groupNdx);
		        }
				++groupNdx;
		    }
			return null;
		}
		
		/* Return the selected parameter by finding the index of the selected option */
		private Operation.Parameter.Value getAndRemoveSelectedParameter() {
			Operation.Parameter.Value value = getSelectedParameter();
			optionList.getOptions().remove(value);
			return value;
		}
		
		private void update() {
			buttonPanel.removeAll();
			createAndAddRadioButtons(buttonPanel);
			setFirstButtonAsDefault();
			optionPanel.revalidate();
			optionPanel.repaint();
		}
		
		/* Create a JPanel for this exception dialog */
		private void createDisplayPanel() {
			buttonPanel = new JPanel();
			optionPanel = new JPanel();
			optionPanel.setLayout(new BoxLayout(optionPanel, BoxLayout.Y_AXIS));
			buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
			createAndAddHeader(optionPanel, message);
			createAndAddHeader(optionPanel, optionList.getUserMessage());
			optionPanel.add(new JSeparator(SwingConstants.HORIZONTAL));
			optionPanel.add(Box.createRigidArea(Constants.SPACER));
			optionPanel.add(buttonPanel);
			update();
		}
		
		/* Sets the first button as the default selection, if any exist. */
		private void setFirstButtonAsDefault() {
			if (group.getButtonCount() > 0) {
				group.getElements().nextElement().setSelected(true);
			}
		}
		
		/* Create and a Header label and add label to the container */
		private void createAndAddHeader(final Container con, final String string) {
			JLabel label = new JLabel(string);
			label.setFont(Constants.SUBHEADER_FONT);
			JPanel headerPanel = new JPanel();
			headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.X_AXIS));
			headerPanel.add(Box.createRigidArea(Constants.TAB));
			headerPanel.add(label);
			headerPanel.add(Box.createHorizontalGlue());
			con.add(Box.createRigidArea(Constants.SPACER));
			con.add(headerPanel);
			con.add(Box.createRigidArea(Constants.SPACER));
		}
		
		/* Create RadioButtons for each Value in the optionList, and add to button group and container. */
		private void createAndAddRadioButtons(final Container con) {
			group = new ButtonGroup();
			for (Operation.Parameter.Value v : optionList.getOptions()) {
				createAndAddRadioButton(con, group, optionList.isVisible() ? v.toString() : "HIDDEN VALUE");
			}
		}
		
		/* Create and a RadioButton and add button to the container and button group*/
		private void createAndAddRadioButton(final Container con, final ButtonGroup group, final String string) {
			JRadioButton button = new JRadioButton(string);
			JPanel panel = new JPanel();
			panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
			panel.setBackground(con.getBackground());
			panel.add(Box.createRigidArea(Constants.TAB));
			panel.add(button);
			panel.add(Box.createHorizontalGlue());
			group.add(button);
			con.add(panel);
			con.add(Box.createRigidArea(Constants.SPACER));
		}
	}  // End of nested class: OptionDialog
	
/* **********************************************************************
 * Nested Class ActionButton
 * 	- Special JButton with a reference to an AllowedAction
 ************************************************************************/
	private class ActionButton extends JButton {

		private static final long serialVersionUID = 4936228178910504535L;
		private final AllowedAction action;
		
		/** Constructs this <tt>ActionButton</tt> using the specified <tt>AllowedAction</tt>. */
		public ActionButton(final AllowedAction action) {
			super(action.getDescription());
			this.action = action;
		}
		
		/** Returns the <tt>AllowedAction</tt> referenced by this button. */
		public AllowedAction getAllowedAction() {
			return action;
		}
	}  // End of nested class: ActionButton
		

/* **********************************************************************
 * Nested Class ActionPanel
 * 	- Displays all possible moves (actions) the current player can make
 * 	- Interacts with engine to perform moves (if valid)
 ************************************************************************/
	private class ActionsPanel extends BuildablePanel {
		
		private static final long serialVersionUID = 4863288349418522569L;
		private final JPanel aPanel;
		
		/** Construct this panel */
		private ActionsPanel() {
			super();
			aPanel = new JPanel();
		}
		
		/** Build this panel */
		@Override public void build() {
			if (super.canBeBuilt()) {
				this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
				this.setBackground(Constants.TABLE_COLOR);
				this.setBorder(Constants.BORDER_INSET_LINE);
				aPanel.setLayout(new BoxLayout(aPanel, BoxLayout.Y_AXIS));
				aPanel.setBackground(Constants.TABLE_COLOR);
				createAndAddHeader(this, "Moves you can make...");
				add(aPanel);
			}
		}
		
		/** Update this panel */
		@Override public void refresh() {
			aPanel.removeAll();
			try {
				for (final AllowedAction action : engine.getAllowedActions().toArray()) {
					createAndAddButton(aPanel, action);
				}
			} catch (EngineException e1) {
				EngineExceptionDialog dialog = new EngineExceptionDialog(e1);
				dialog.display();
			} catch (PluginException e1) {
				PluginExceptionDialog dialog = new PluginExceptionDialog(e1);
				dialog.display();
			} catch (Exception e1) {
				ExceptionDialog dialog = new ExceptionDialog(e1);
				dialog.display();
			}
		}
		
		/* Create a Header label and add label to the container */
		private void createAndAddHeader(final Container con, final String string) {
			JLabel label = new JLabel(string);
			label.setFont(Constants.HEADER_FONT);
			JPanel headerPanel = new JPanel();
			headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.X_AXIS));
			headerPanel.add(Box.createRigidArea(Constants.TAB));
			headerPanel.add(label);
			headerPanel.add(Box.createHorizontalGlue());
			con.add(Box.createRigidArea(Constants.SPACER));
			con.add(headerPanel);
			con.add(Box.createRigidArea(Constants.SPACER));
		}
		
		/* Create an ActionButton and add button to the container */
		private void createAndAddButton(final Container con, final AllowedAction action) {
			JPanel panel = null;
			// Using Flyweight pattern here, since the same button will be reused!
			if (BUTTON_PANELS.containsKey(action)) {
				panel = BUTTON_PANELS.get(action);
			}
			else {
				final ActionButton button = new ActionButton(action);
				button.addActionListener(listener);
				panel = new JPanel();
				panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
				panel.setBackground(con.getBackground());
				panel.add(Box.createRigidArea(Constants.SPACER));
				panel.add(Box.createRigidArea(Constants.SPACER));
				panel.add(button);
				panel.add(Box.createHorizontalGlue());
				BUTTON_PANELS.put(action, panel);
			}
			con.add(Box.createRigidArea(Constants.SPACER));
			con.add(panel);
			con.add(Box.createRigidArea(Constants.SPACER));
		}
	}  // End of nested class: ActionPanel
	
/* **********************************************************************
 * Nested Class CardPileCollectionPanel
 * 	- Displays all of the card piles in a collection
 * 	- A collection is defined by the owner (common or a player) 
 ************************************************************************/
	private class CardPileCollectionPanel extends BuildablePanel {

		private static final long serialVersionUID = -1591624686181302800L;
		private final CardPileCollection pileCollection;
		private final String title;
		private final CardPlayer player;
		private final CardPilePanel[] pilePanels;
		
		/** Construct this panel with the specified CardGameBoard */
		private CardPileCollectionPanel(final CardGameBoard board) {
			this("Common Card Piles", board.getCommonPiles(), null);
		}
		
		/** Construct this panel with the specified CardPlayer */
		private CardPileCollectionPanel(final CardPlayer player) {
			this("Card Piles for " + player.getName(), player.getPlayerPiles(), player);
		}
		
		/** Construct this panel with the specified attributes */
		private CardPileCollectionPanel(final String title, final CardPileCollection pileCollection, final CardPlayer player) {
			super();
			this.pileCollection = pileCollection;
			this.title = title;
			this.player = player;
			this.pilePanels = new CardPilePanel[pileCollection.getSize()];
		}
		
		/** Build this panel */
		@Override public void build() {
			if (super.canBeBuilt()) {
				final JPanel panelA = new JPanel();
				final JPanel panelB = new JPanel();
				final JPanel panelC = new JPanel();
				this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
				this.setBackground(Constants.TABLE_COLOR);
				this.setBorder(Constants.BORDER_INSET_LINE);
				panelA.setLayout(new BoxLayout(panelA, BoxLayout.X_AXIS));
				panelB.setLayout(new BoxLayout(panelB, BoxLayout.Y_AXIS));
				panelC.setLayout(new BoxLayout(panelC, BoxLayout.Y_AXIS));
				panelA.setBackground(Constants.TABLE_COLOR);
				panelB.setBackground(Constants.TABLE_COLOR);
				panelC.setBackground(Constants.TABLE_COLOR);
				populateSidePanels(panelB, panelC);
				panelA.add(Box.createRigidArea(Constants.SPACER));
				panelA.add(panelB);
				panelA.add(Box.createRigidArea(Constants.SPACER));
				panelA.add(panelC);
				panelA.add(Box.createRigidArea(Constants.SPACER));
				createAndAddHeader(this, title);
				this.add(panelA);
			}
		}
		
		/** Update this panel */
		@Override public void refresh() {
			for (CardPilePanel panel : pilePanels) {
				panel.refresh();
			}
		}
		
		/* Create CardPilePanels and add to left and ride panels */
		private void populateSidePanels(final JPanel left, final JPanel right) {
			CardPilePanel panel;
			boolean isTimeToSwitchPanels = true;
			CardPile[] piles = pileCollection.get();
			for (int i = 0; i < piles.length; i++) {
				panel = new CardPilePanel(piles[i], player);
				pilePanels[i] = panel;
				panel.build();
				if(isTimeToSwitchPanels) {
					left.add(panel);
					left.add(Box.createRigidArea(Constants.SPACER));
				}
				else {
					right.add(panel);
					right.add(Box.createRigidArea(Constants.SPACER));
				}
				isTimeToSwitchPanels = !isTimeToSwitchPanels;
			}
		}
		
		/* Create and return a Header label; and add label to panel */
		private JLabel createAndAddHeader(final Container con, final String string) {
			JLabel label = new JLabel(string);
			label.setFont(Constants.HEADER_FONT);
			JPanel headerPanel = new JPanel();
			headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.X_AXIS));
			headerPanel.add(Box.createRigidArea(Constants.TAB));
			headerPanel.add(label);
			headerPanel.add(Box.createHorizontalGlue());
			con.add(Box.createRigidArea(Constants.SPACER));
			con.add(headerPanel);
			con.add(Box.createRigidArea(Constants.SPACER));
			return label;
		}
	}  // End of nested class: CardPileCollectionPanel
	
/* **********************************************************************
 * Nested Class CardPilePanel
 * 	- Displays primary statistics of a CardPile
 * 	- Displayed cards will change as the current player changes (card pile owner)
 ************************************************************************/
	private class CardPilePanel extends BuildablePanel {

		private static final long serialVersionUID = -1847249228085216794L;
		private final JPanel cardPanel;
		private final CardPlayer player;
		private final CardPile pile;
		private JLabel labelOwner;
		
		/** Construct this panel with the specified CardPile */
		private CardPilePanel(final CardPile pile) {
			this(pile, null);
		}
		
		/** Construct this panel with the specified CardPile owned by the player */
		private CardPilePanel(final CardPile pile, final CardPlayer player) {
			super();
			this.pile = pile;
			this.player = player;
			this.cardPanel = new JPanel();
		}
		
		/** Build this panel */
		@Override public void build() {
			if (super.canBeBuilt()) {
				this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
				this.setBackground(Constants.BACKGROUND_COLOR);
				this.setBorder(Constants.THICK_LINE);
				cardPanel.setLayout(new BoxLayout(cardPanel, BoxLayout.Y_AXIS));
				cardPanel.setBackground(Constants.BACKGROUND_COLOR);
				labelOwner = createAndAddHeader(this, "");
				add(cardPanel);
				this.add(Box.createRigidArea(Constants.SPACER));
				refresh();
			}
		}
		
		/** Update this panel */
		@Override public void refresh() {
			final StringBuilder str = new StringBuilder();
			str.append(pile.getParameters().getOwner()).append(" card pile - ").append(pile.getParameters().getName())
			   .append(": ").append(pile.getSize()).append(" Cards");
			labelOwner.setText(str.toString());
			cardPanel.removeAll();
			for (PlayingCard card : pile.get(getNumVisibleCards())) {
				createAndAddLabel(cardPanel, card.toString());
			}
		}
		
		/* Calculate and return the number of cards to display */
		private int getNumVisibleCards() {
			int num = engine.getNumberOfVisibleCards(pile, player);
			if (num > 0 && pile.getParameters().getPlacement() == Placement.STACK) {
				num = 1;
			}
			return num;
		}
		
		/* Create and return a Header label; and add label to the container */
		private JLabel createAndAddHeader(final Container con, final String string) {
			JLabel label = new JLabel(string);
			label.setFont(Constants.SUBHEADER_FONT);
			JPanel headerPanel = new JPanel();
			headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.X_AXIS));
			headerPanel.add(Box.createRigidArea(Constants.TAB));
			headerPanel.add(label);
			headerPanel.add(Box.createHorizontalGlue());
			con.add(Box.createRigidArea(Constants.SPACER));
			con.add(headerPanel);
			con.add(Box.createRigidArea(Constants.SPACER));
			return label;
		}
		
		/* Create and return a JLabel; and add label to the container */
		private JLabel createAndAddLabel(final Container con, final String string) {
			JLabel label = new JLabel(string);
			JPanel labelPanel = new JPanel();
			labelPanel.setLayout(new BoxLayout(labelPanel, BoxLayout.X_AXIS));
			labelPanel.setBackground(con.getBackground());
			labelPanel.add(Box.createRigidArea(Constants.TAB));
			labelPanel.add(label);
			labelPanel.add(Box.createHorizontalGlue());
			con.add(labelPanel);
			label.setVisible(true);
			return label;
		}
	}  // End of nested class: CardPilePanel
	
/* **********************************************************************
 * Nested Class MainPanel
 * 	- Holds the main GUI panels
 * 	- Split into 3 sections:
 * 		1. Common Piles
 * 		2. Player Piles
 * 		3. Moves the current player can take
 ************************************************************************/
	private class MainPanel extends BuildablePanel {

		private static final long serialVersionUID = -9095879642841692108L;
		private final CardPileCollectionPanel commonPilePanel;
		private final ActionsPanel actionsPanel;
		private CardPileCollectionPanel[] playerPanels;
	   
		/** Construct this Panel. */
		public MainPanel() {
			super();
			commonPilePanel = new CardPileCollectionPanel(board);
			actionsPanel = new ActionsPanel();
		}
		
		/** Build this panel */
		@Override public void build() {
			if (super.canBeBuilt()) {
				setLayout(new GridBagLayout());
				populateLayout();
			}
		}

		/** Update this panel */
		@Override public void refresh() {
			commonPilePanel.refresh();
			for (CardPileCollectionPanel p : playerPanels) {
				p.refresh();
			}
			actionsPanel.refresh();
		}
		
		/* Build and return a panel to hold all of the player card collections. */
		private JPanel buildPlayerPanel() {
			final CardPlayer[] players = engine.getPlayers();
			playerPanels = new CardPileCollectionPanel[players.length];
			final JPanel playerPanel = new JPanel();
			playerPanel.setLayout(new BoxLayout(playerPanel, BoxLayout.Y_AXIS));
			playerPanel.setBackground(Constants.TABLE_COLOR);
			CardPileCollectionPanel panel;
			for (int i = 0; i < players.length; i++) {
				panel = new CardPileCollectionPanel(players[i]);
				panel.build();
				playerPanel.add(panel);
				playerPanels[i] = panel;
				playerPanel.add(Box.createRigidArea(Constants.SPACER));
			}
			playerPanel.add(Box.createVerticalGlue());
			return playerPanel;
		}
		
		/* Put the sub-panels in the appropriate places. */
		private void populateLayout() {
			GridBagConstraints c = new GridBagConstraints();
	        c.fill = GridBagConstraints.BOTH;
	        c.weightx = 0.5;
			c.weighty = ROW_WEIGHT_CARD_PILES;
			c.gridx = 0;
			c.gridy = 0;
			commonPilePanel.build();
			add(commonPilePanel, c);
			c.gridheight = 2;
			c.gridx = 1;
			final JScrollPane scrollPane = new JScrollPane(buildPlayerPanel(),
		            JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
		            JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			scrollPane.getVerticalScrollBar().setUnitIncrement(16);
			//add(buildPlayerPanel(), c);
			add(scrollPane, c);
			c.weighty = ROW_WEIGHT_ALLOWED_ACTIONS;
			c.gridheight = 1;
			c.gridx = 0;
			c.gridy = 1;
			actionsPanel.build();
			add(actionsPanel, c);
		}
	}  // End of nested class: MainPanel
	
/* **********************************************************************
 * Nested Class InfoPanel
 * 	- Displays the name of the current user
 * 	- Displays the current turn number
 ************************************************************************/
	private class InfoPanel extends BuildablePanel {
	   
		private static final long serialVersionUID = -3015023905631764954L;
		private JLabel labelCurrentPlayer;
		private JLabel labelPhase;
		private JLabel labelTurn;
	   
		/** Construct this Panel */
		public InfoPanel() {
			super();
		}
		
		/** Build this panel */
		@Override public void build() {
			if (super.canBeBuilt()) {
				setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
				setBorder(Constants.ETCHED);
				setBackground(INFO_BACKGROUND_COLOR);
				labelCurrentPlayer = createEmptyLabel();
				labelPhase = createEmptyLabel();
				labelTurn = createEmptyLabel();
				add(Box.createHorizontalGlue());
				add(labelCurrentPlayer);
				add(Box.createRigidArea(Constants.MARGIN_SPACE));
				add(labelPhase);
				add(Box.createRigidArea(Constants.MARGIN_SPACE));
				add(labelTurn);
				add(Box.createHorizontalGlue());
				refresh();
			}
		}

		/** Update this panel */
		@Override public void refresh() {
			final StringBuilder str = new StringBuilder();
			str.append("Current Player: ").append(engine.getCurrentPlayer().getName());
			labelCurrentPlayer.setText(str.toString());
			str.setLength(0);
			str.append("Phase: ").append(engine.getCurrentPlayer().getPhase().getName());
			labelPhase.setText(str.toString());
			str.setLength(0);
			str.append("Turn Number: ").append(engine.getTurnNumber());
			labelTurn.setText(str.toString());
		}
		
		/* Create and return a JLabel */
		private JLabel createEmptyLabel() {
			JLabel label = new JLabel();
			label.setHorizontalAlignment(JLabel.CENTER);
			label.setFont(INFO_FONT);
			label.setForeground(INFO_TEXT_COLOR);
			label.setVisible(true);
			return label;
		}
		
	}  // End of nested class: InfoPanel
	
/*------------------------------------------------
    Main Method for Testing
------------------------------------------------*/
	public static void main(String[] args) {
		System.out.println(" --- Card Game Test Bench ---\n");
		
		// Rules Plugin will be created by main application and used to determine number of players and such...
		Plugin plugin = null;
		try {
			plugin = new Plugin(Plugin.Type.RULES, "test");
		} catch (PluginException e) {
			final PluginExceptionDialog dialog = new PluginExceptionDialog(e);
			dialog.display();
		}
		
		// These users need to be created and then sent to the engine with the rules plugin!
		// Read rules to get number of players
		final GamePlayer[] gamePlayers = new GamePlayer[2];
		gamePlayers[0] = new GamePlayer("Kurt");
		gamePlayers[1] = new GamePlayer("Kyle");
		
		CardGameEngine engine = null;
		
		try {
			engine = EngineFactory.INSTANCE.createCardGameEngine(plugin, gamePlayers);
		} catch (PluginException e) {
			final PluginExceptionDialog dialog = new PluginExceptionDialog(e);
			dialog.display();
		}
		
		SimpleGUI gui = new SimpleGUI(engine);
		gui.display();
		try {
			gui.startGame();
		} catch (PluginException e) {
			final PluginExceptionDialog dialog = new PluginExceptionDialog(e);
			dialog.display();
		} catch (EngineException e) {
			final EngineExceptionDialog dialog = new EngineExceptionDialog(e);
			dialog.display();
		} catch (NoSuchFieldException | IllegalArgumentException e) {
			final ExceptionDialog dialog = new ExceptionDialog(e);
			dialog.display();
		}
	}
}
