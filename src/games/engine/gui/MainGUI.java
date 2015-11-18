package games.engine.gui;

import java.awt.Color;
import java.awt.BorderLayout;
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