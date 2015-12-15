package games.engine.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
/**
 * Dialog for displaying game information
 @Author Group 5
 */
public class Info extends JDialog
{

    JLabel title = new JLabel("CARD GAME", JLabel.CENTER);

    JLabel version = new JLabel("Version 1.0", JLabel.CENTER);

    JLabel creator = new JLabel("Created by Group 5", JLabel.CENTER);

    JLabel sent1 = new JLabel("For more information on this game", JLabel.CENTER);

    JLabel sent2 = new JLabel("Please Search on Google :)", JLabel.CENTER);

    JPanel panel;


    public Info(JFrame parent)
    {

        super(parent, "About", true);
        setSize(280, 180);
        Point p = parent.getLocation();
        setLocation((int)p.getX() + 80,(int)p.getY() + 100);

        title.setForeground(Color.black);
        creator.setForeground(Color.black);
        panel = new JPanel();
        GridBagLayout gridbag = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        panel.setLayout(gridbag);
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.BOTH;
        c.ipadx = 2;
        getContentPane().add(panel);

        c.gridy = 1;
        panel.add(title, c);

        c.gridy = 2;
        panel.add(version, c);

        c.gridy = 3;
        panel.add(creator, c);

        c.gridy = 4;
        panel.add(sent1, c);

        c.gridy = 5;
        panel.add(sent2, c);


    }

}
