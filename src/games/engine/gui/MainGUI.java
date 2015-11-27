package games.engine.gui;

import java.awt.event.*;
import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import java.net.*;
import javax.swing.JComponent.*;
import java.awt.geom.*;
import java.io.*;

/*-------------------------------
Program for card game GUI (Partial)
Author: Group 5
 -------------------------------*/
 

class MainGUI extends JFrame implements ActionListener, MouseMotionListener, MouseListener, WindowListener {

    JLabel image;
    JTextArea msg;
    JPanel panel;
    JTextField input;
    JScrollPane scrollPane;

    int mouseX, mouseY;

    JMenuBar menuBar;
    JMenu menu;
    JMenuItem menuItem;
    JRadioButtonMenuItem rbMenuItem;

    Image cardspic;
    Image back;
    Image backSW;
    Image title;
    Image pointer[] = new Image[4];
    Image burntPic;

    Graphics g;
    BufferedImage offscreen;
    ImageIcon imageI;
    
    Image offscreen2;
    Graphics g2;
    Graphics2D g2d;

    boolean player1 = false; 
    boolean player2 = false; 

    boolean myTurn = false; 
    
    Message message = null;
    Dealer dealer = null;
    Player player = null;
    Score score;
    
    String playersName = "unknown";

    Hand hand;

    Point pointplayer1[] = new Point[3];
    Point pointplayer2[] = new Point[3];
    Point pointplayer3[] = new Point[3];
    Point pointplayer4[] = new Point[3];

    Point centre1;

    Dimension screenSize;


    MainGUI() {

        MediaTracker tracker = new MediaTracker(this);
        Toolkit toolkit = Toolkit.getDefaultToolkit();

        cardspic = toolkit.getImage(this.getClass().getResource("Images/cards.gif"));
        back = toolkit.getImage(this.getClass().getResource("Images/back.gif"));
        backSW = toolkit.getImage(this.getClass().getResource("Images/backSW.gif"));
        title = toolkit.getImage(this.getClass().getResource("Images/Title.jpg"));
        pointer[0] = toolkit.getImage(this.getClass().getResource("Images/pointer.gif"));
        burntPic = toolkit.getImage(this.getClass().getResource("Images/burnt.jpg"));

        tracker.addImage(cardspic, 1);
        tracker.addImage(back, 1);
        tracker.addImage(backSW, 1);
        tracker.addImage(title, 1);
        tracker.addImage(pointer[0], 1);
        tracker.addImage(burntPic, 1);

        try {
            tracker.waitForAll();
        } catch (InterruptedException e) {
            msg.setText("Image load Error " + e);
        }
        
        pointer[3] = rotatePointer(pointer[0]);
        pointer[2] = rotatePointer(pointer[3]);
        pointer[1] = rotatePointer(pointer[2]);
        
        offscreen = new BufferedImage(450, 550, BufferedImage.TYPE_3BYTE_BGR);
        g = offscreen.getGraphics();
        g.drawImage(title, -40, 120, this);
        g.setColor(Color.white);
        g.drawLine(0, 450, 450, 450);

        screenSize = Toolkit.getDefaultToolkit().getScreenSize();

         if(screenSize.width < 1024){
            offscreen2 = new BufferedImage(338, 413, BufferedImage.TYPE_3BYTE_BGR);

            g2 = offscreen2.getGraphics();
            g2d = (Graphics2D) g2;
            AffineTransform at = new AffineTransform();
            at.scale(0.75, 0.75);
            g2d.transform(at);
            g2d.drawImage(offscreen, 0, 0, null);
            imageI = new ImageIcon(offscreen2);
        }else
            imageI = new ImageIcon(offscreen);

        image = new JLabel(imageI);

        addMouseMotionListener(this);
        addMouseListener(this);
        requestFocus();
        
        menuBar = new JMenuBar();

        //Build the first menu.
        menu = new JMenu("File");
        menu.setMnemonic(KeyEvent.VK_F);
        menu.getAccessibleContext().setAccessibleDescription(
            "Game Options");
        menuBar.add(menu);

        //File group of JMenuItems
        menuItem = new JMenuItem("2 Player",
                             KeyEvent.VK_P);
        menuItem.getAccessibleContext().setAccessibleDescription(
                "Start Single Player Game");
        menuItem.addActionListener( this );
        menu.add(menuItem);

        menuItem = new JMenuItem("Scoreboard",
                KeyEvent.VK_B);
		menuItem.getAccessibleContext().setAccessibleDescription(
		   "Veiw Scoreboard");
		menuItem.addActionListener( this );
		menu.add(menuItem);

		menuItem = new JMenuItem("Quit",
		                KeyEvent.VK_Q);
		menuItem.getAccessibleContext().setAccessibleDescription(
		   "Quit Game");
		menuItem.addActionListener( this );
		menu.add(menuItem);

		menu = new JMenu("Options");
		menu.getAccessibleContext().setAccessibleDescription(
		"Game Options");
		menuBar.add(menu);
		
		menuItem = new JMenuItem("Redeal",
		                KeyEvent.VK_R);
		menuItem.getAccessibleContext().setAccessibleDescription(
		   "Redeal the deck");
		menuItem.addActionListener( this );
		menu.add(menuItem);

		menuItem = new JMenuItem("Start Game",
		                KeyEvent.VK_S);
		menuItem.getAccessibleContext().setAccessibleDescription(
		   "Start game now");
		menuItem.addActionListener( this );
		menu.add(menuItem);

		 menu = new JMenu("About");
		    menu.getAccessibleContext().setAccessibleDescription(
		        "About the Game");
		    menuBar.add(menu);

		    menuItem = new JMenuItem("Rules",
		                         KeyEvent.VK_R);
		    menuItem.getAccessibleContext().setAccessibleDescription(
		            "How to play");
		    menuItem.addActionListener( this );
		    menu.add(menuItem);

		    menuItem = new JMenuItem("Home Page",
		                         KeyEvent.VK_H);
		    menuItem.getAccessibleContext().setAccessibleDescription(
		            "View Home Page");
		    menuItem.addActionListener( this );
		    menu.add(menuItem);

		    menuItem = new JMenuItem("About",
		                         KeyEvent.VK_A);
		    menuItem.getAccessibleContext().setAccessibleDescription(
		            "About the Game");
		    menuItem.addActionListener( this );
		    menu.add(menuItem);

    }

    public static void main(String[] args) {
        try {
            MainGUI frame = new MainGUI();
            frame.setTitle("Card Game");
            frame.setResizable(false);

            frame.pack();
            frame.setVisible(true);
        } catch (Exception e) {
            System.out.println("Game Error: " + e);
        }
    }
}