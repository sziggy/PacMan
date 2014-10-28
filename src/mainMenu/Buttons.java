package mainMenu;


import game.Game;
import game.Resources;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;



public class Buttons{
	
	
	//private graphics.settings settings;
	
	
	
	
	
	/** in this class we draw the buttons that are showed on the main_menu screen
	 *  try to get them working in a separate class
	 */
	
	public Buttons(Container pane)
	{
		pane.setLayout(null);
		
		Game.startButton.addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
            	Game.MAIN_MENU = false;
            	Game.GAME = true;
            	Game.frame.remove(Game.startButton);
        		Game.frame.remove(Game.settingsButton);
        		Game.frame.remove(Game.exitButton);
                Game.frame.revalidate();
            	System.out.println("Start Button Clicked");
            }
        } );
		
		
		Game.settingsButton.addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
            	Game.frame.remove(Game.startButton);
        		Game.frame.remove(Game.settingsButton);
        		Game.frame.remove(Game.exitButton);
                Game.frame.revalidate();
                Game.frame.repaint();
                //settings = new settings(Game.frame.getContentPane());
            	System.out.println("Settings Button Clicked");
            	}
        } );
		
		
		
		Game.exitButton.addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
            	System.out.println("Exit Button Clicked");
            	System.exit(0);
            }
        } );
		
		
		
		pane.add(Game.startButton);
		pane.add(Game.settingsButton);
		pane.add(Game.exitButton);
		
		
		Insets insets = pane.getInsets();
        Dimension size = Game.startButton.getPreferredSize();
        
        Game.startButton.setBackground(new Color(0, 0, 0));
        Game.startButton.setForeground(Color.CYAN);
        Game.startButton.setFocusPainted(false);
        Game.startButton.setFont(new Font("Calabri", Font.BOLD, 16));
        
        Game.settingsButton.setBackground(new Color(0, 0, 0));
        Game.settingsButton.setForeground(Color.RED);
        Game.settingsButton.setFocusPainted(false);
        Game.settingsButton.setFont(new Font("Calabri", Font.BOLD, 16));
        
        Game.exitButton.setBackground(new Color(0, 0, 0));
        Game.exitButton.setForeground(Color.YELLOW);
        Game.exitButton.setFocusPainted(false);
        Game.exitButton.setFont(new Font("Calabri", Font.BOLD, 16));
        
        Game.startButton.setBounds(500 + insets.left, 10 + insets.top,
                size.width + 50, size.height + 10);

        
        Game.settingsButton.setBounds(500 + insets.left, 55 + insets.top,
                size.width + 50, size.height + 10);
        
        Game.exitButton.setBounds(500 + insets.left, 100 + insets.top,
                size.width + 50, size.height + 10);
    }
}
