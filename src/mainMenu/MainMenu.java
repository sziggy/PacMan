package mainMenu;

import game.Game;
import game.Resources;

import java.awt.Color;
import java.awt.Graphics;

import javax.annotation.Resource;

public class MainMenu {

	public MainMenu() {
		drawMainMenu(Game.bufferGraphics);
	} 
	
	public void drawMainMenu(Graphics g) {
		g.drawImage(Resources.BACKGROUND, 0, 0, Resources.BACKGROUND.getWidth(), Resources.BACKGROUND.getHeight(), null);
	}

}
