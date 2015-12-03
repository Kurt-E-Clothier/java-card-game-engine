package games.engine.gui;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
 /*
  *Likely be the main class to start game
  *To do:
  *	Add list of available games using plugin methods
  *	Resize screen when game starts 
  * @author group 5
  *
  */
public class FileName
{
  static JFrame frame;
 
  public static void main(String[] args)
  {
    SwingUtilities.invokeLater(new Runnable()
    {
      public void run()
      {
        displayJFrame();
      }
    });
  }

  
  static void displayJFrame()
  {
    frame = new JFrame("Available Games");
 
    JButton showDialogButton = new JButton("Game one");
     
    showDialogButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
    	  MainGUI gui=new MainGUI();
    	  gui.show();
    	  gui.setVisible(true);
       
      }
    });
 
    frame.getContentPane().setLayout(new FlowLayout());
    frame.add(showDialogButton);
    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    frame.pack();
    frame.setLocationRelativeTo(null);
    frame.setVisible(true);
  }
}