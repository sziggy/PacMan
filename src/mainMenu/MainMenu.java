package mainMenu;

import game.Game;
import game.Resources;

import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.JPanel;

public class MainMenu {
	public static boolean MAIN = false;
	public static boolean SETTINGS = false;
	
	
	private int h, w;
	
	public MainMenu() {
		
		if(MAIN){
		drawMainMenu(Game.bufferGraphics);
		} else if(SETTINGS){
			drawSettingsMenu(Game.bufferGraphics);
		}
	} 
	
	public void drawMainMenu(Graphics g) {
		Rectangle r = Game.frame.getBounds();
		h = r.height;
		w = r.width;
		g.drawImage(Resources.BACKGROUND, 0, 0, w, h, null);
		Buttons.pane.add(Game.startButton);
		Buttons.pane.add(Game.settingsButton);
		Buttons.pane.add(Game.exitButton);
	}

	
	
	
	
	public void drawSettingsMenu(Graphics g) {
		Rectangle r = Game.frame.getBounds();
		h = r.height;
		w = r.width;
		g.drawImage(Resources.SETTINGS_BACKGROUND, 0, 0, w, h, null);
	}
}
