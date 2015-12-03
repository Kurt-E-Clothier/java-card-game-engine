package games.engine.gui;

import java.awt.event.*;
import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import java.net.*;
import javax.swing.JComponent.*;

import games.engine.util.CardDealer;
import games.engine.util.CardPlayer;

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
    
    CardDealer dealer = null;
    CardPlayer player = null;
    //Score score;
    
    String playersName = "unknown";

    //Hand hand;

    Point pointplayer1[] = new Point[3];
    Point pointplayer2[] = new Point[3];
    Point pointplayer3[] = new Point[3];
    Point pointplayer4[] = new Point[3];

    Point centre1;

    Dimension screenSize;


    MainGUI() {

        MediaTracker tracker = new MediaTracker(this);
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        //might need to change the image path depending on build
        cardspic = toolkit.getImage(this.getClass().getResource("games/engine/images/cards.gif"));
        back = toolkit.getImage(this.getClass().getResource("games/engine/images/back.gif"));
        backSW = toolkit.getImage(this.getClass().getResource("games/engine/images/backSW.gif"));
        title = toolkit.getImage(this.getClass().getResource("games/engine/images/Title.jpg"));
        pointer[0] = toolkit.getImage(this.getClass().getResource("games/engine/images/pointer.gif"));
        burntPic = toolkit.getImage(this.getClass().getResource("games/engine/images/burnt.jpg"));

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
		    
		    addWindowListener(this);

		     msg = new JTextArea("Welcome to the card game\n", 4, 20);
		     msg.setLineWrap(true);
		     msg.setEditable(false);
		     msg.setDisabledTextColor(Color.black);

		    input = new JTextField();
		    input.addActionListener(this);

		    scrollPane = new JScrollPane(msg, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);


		     panel = new JPanel();
		     GridBagLayout gridbag = new GridBagLayout();
		     GridBagConstraints c = new GridBagConstraints();
		     panel.setLayout(gridbag);
		     c.anchor = GridBagConstraints.WEST;
		     c.fill = GridBagConstraints.BOTH; 
		     panel.setBackground(Color.white);
		     getContentPane().add(panel);

		     panel.add(menuBar, c);
		     c.gridy = 1;
		     panel.add(image, c);
		     c.gridy = 2;
		     panel.add(scrollPane, c);
		     c.gridy = 3;
		     panel.add(input, c);

		    addMsg("Detected Screen Size: " + screenSize.width  + "x" + screenSize.height);
		    if(screenSize.width < 1024)
		        addMsg("For optimal graphics use 1024x768 resolution");

		    //score = new Score(this);

		   }
    public void actionPerformed(ActionEvent event){
        String label = event.getActionCommand();

        if (label.equals("Quit")){

            System.exit(0);      
        }

        else if(label.equals("2 Player")){
            addMsg("Two Player Option");
                if(dealer != null || player != null){
                addMsg("You already have an ongoing game");
                }else{
               // CardDealer dealerD = new CardDealer(this);
                //dealerD.show();
               // playersName = dealerD.getName();
                if(playersName != "cancel#*#")
                {
                   // if(dealerD.swap())
                        addMsg("Card Swap at start of game Selected");
                   //more to add for game playing
                }
                }
            }
        
        else if(label.equals("Redeal")){
            if(dealer != null);
                //dealer.redeal();
            }

            else if(label.equals("Start Game")){
            if(dealer != null);
               // dealer.start();
            }
            
            else if(label.equals("Scoreboard")){
           // score.display();
            }

            else if(label.equals("About")){
            Info info = new Info(this);
            info.show();
            }

            else if(label.equals("Rules")){
            try{
                Runtime.getRuntime().exec("cmd.exe /c start iexplore https://cardgames.io/idiot/#rules");
            }catch(Exception e){ addMsg("Error launching Internet Explorer.  Please visit https://cardgames.io/idiot/#rules");}
            }

            else if(label.equals("Home Page")){
            try{
                Runtime.getRuntime().exec("cmd.exe /c start iexplore https://cardgames.io/idiot/");
            }catch(Exception e){ 
            	addMsg("Error launching Internet Explorer.  Please visit https://cardgames.io/idiot/");
            	}
            }
            }
        
        private Image rotatePointer(Image img)
        {

        Image rot = null;

        int buffer[] = new int[15 * 15];
        int rotate[] = new int[15 * 15];


          PixelGrabber grabber =
            new PixelGrabber(img, 0, 0, 15, 15, buffer, 0, 15);
            try {
            grabber.grabPixels();
              }
          catch(InterruptedException e) {
             addMsg("Rotate image error " + e);
             }
          for(int y = 0; y < 15; y++) {
            for(int x = 0; x < 15; x++) {
              rotate[((15-x-1)*15)+y] = buffer[(y*15)+x];
              }
            }
          rot = createImage(new MemoryImageSource(15, 15, rotate, 0, 15));

        return rot;
        }
        
        public void addMsg(String message)
        {
        if(message != null)
        msg.append(message + "\n");
        try { 
        Rectangle current = msg.getVisibleRect();
        int scrollunitinc = msg.getScrollableUnitIncrement(current, SwingConstants.VERTICAL, 1);
        current.setRect(current.getX(), (msg.getLineCount() + 1) * scrollunitinc , current.getWidth(), current.getHeight());
        msg.scrollRectToVisible(current);
        } catch(Exception ex) {
        System.out.println("\n Error scrolling to end " + ex);
        }

        }
        
        public void mousePressed( MouseEvent me) {
            if(myTurn){
              //  int selection = hand.mouseClick(mouseX, mouseY);
               // if(dealer != null && selection != -1)
                 //   dealer.cardSelection(selection);
                //if(player != null && selection != -1)
                  //  player.cardSelection(selection);   
            }
            }

            public void setmyTurn(boolean myTurn)
            {
            this.myTurn = myTurn;
            }

            public void scalepic()
            {
               g2.drawImage(offscreen, 0, 0, null);
            }

            public boolean smallscreen()
            {
            if(screenSize.width < 1024)
                return true;
            return false;
            }

            public void repaint()
            {
            //if(screenSize.width < 1024){
               //offscreen2 = offscreen.getScaledInstance(338, 413, Image.SCALE_FAST);
               //imageI.setImage(offscreen2);
            //}
                panel.repaint();
                //panel.update();
            }

            public void mouseMoved(MouseEvent me) {
                //ajusting so mouse points are over image
                mouseX = me.getX() - 5;
                mouseY = me.getY() - 45;
                if(screenSize.width < 1024){//scaling mouse movement if screen to big
                    mouseX = mouseX * 100/75;
                    mouseY = mouseY * 100/75;
                }
                //addMsg("X: " + mouseX + " Y: " + mouseY);
            }

            public void mouseDragged(MouseEvent e) {}
            public void mouseEntered( MouseEvent me ) {}
            public void mouseExited( MouseEvent me) {}
            public void mouseClicked( MouseEvent me) {}
            public void mouseReleased( MouseEvent me){} 

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
    
    public void windowClosing(WindowEvent e) {
        System.exit(0);  
   }

public void windowClosed(WindowEvent e) {
}

public void windowOpened(WindowEvent e) {
}

public void windowIconified(WindowEvent e) {
}

public void windowDeiconified(WindowEvent e) {
}

public void windowActivated(WindowEvent e) {
}

public void windowDeactivated(WindowEvent e) {
}

}