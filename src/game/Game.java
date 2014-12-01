package game;
import graphics.GameMap;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import mainMenu.Buttons;
import mainMenu.MainMenu;

public class Game extends Canvas implements Runnable {
    public static int playerHeight = 20;
    public static int playerWidth = 20;
    public static int movementSpeed = 2;

    public boolean running = false;
    public boolean INTRO = false;
    public static boolean MAIN_MENU = false;
    public static boolean GAME = true;
    public static boolean LoadButtons = true;

    public static JFrame frame;
    protected Image bufferImage;
    public static Graphics bufferGraphics;
    private Input input;
    public static Buttons buttons;
    private MainMenu mainMenu;
    public Direction direction = Direction.LEFT;

    private Point playerLocation = null;

    private GameMap currentLevel;

    public static JButton startButton = new JButton("Start"); //Start
	public static JButton settingsButton = new JButton("Settings"); //Settings
	public static JButton exitButton = new JButton("Exit"); //Exit
	
    public Game() {
        Resources.loadResources();
        loadLevel(new GameMap.Level1());

        frame = new JFrame("PacMan"); // creates the frame for our game
        input = new Input();
        mainMenu = new MainMenu();
        
        
        Game.buttons = new Buttons(Game.frame.getContentPane());
        frame.setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // ends program on
                                                                // click of the
                                                                // Exit button
                                                                // in the top
                                                                // right corner
        
        
        
        frame.setLocationByPlatform(true);
        frame.addKeyListener(new Input());
        frame.add(this, BorderLayout.CENTER);
        frame.pack(); // keeps size correct
        frame.setResizable(false); // keep this false, if set to true whenever
                                    // someone resizes it our code will not
                                    // function correctly
        frame.setVisible(true);

        this.addKeyListener(input);
        this.createBufferStrategy(2);
    }

    /**
     * Loads the level into the game, also changes the dimensions of the window to fit the game
     * @param gameMap
     */
    private void loadLevel(GameMap gameMap) {
        currentLevel = gameMap;

        playerLocation = convertMapLocationToScreenLocation(currentLevel.getPlayerStartLocation());

        Dimension canvasSize = new Dimension(currentLevel.getWidth()*Resources.TILE_WIDTH, currentLevel.getHeight()*Resources.TILE_HEIGHT);

        setMinimumSize(canvasSize);
        setMaximumSize(canvasSize);
        setPreferredSize(canvasSize);

    }

    
    
    public void run() {
        long lastTime = System.nanoTime();
        double nsPerTick = 1000000000 / 60D;
        long lastTimer = System.currentTimeMillis();
        double delta = 0;

        int frames = 0;
        int ticks = 0;

        while (running == true) {
            long now = System.nanoTime();
            delta += (now - lastTime) / nsPerTick;
            lastTime = now;
            boolean render = false;

            while (delta >= 1) {
                ticks++;
                tick();
                delta -= 1;
                render = true;

            }

            try {
                Thread.sleep(20); // keep the Frames from going to high
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (render == true) {
                frames++;
                render();
            }

            if (System.currentTimeMillis() - lastTimer >= 1000) {
                lastTimer += 1000;

                frames = 0;
                ticks = 0;
            }
        }

        try {
            Thread.sleep(15); // keep the Frames from going to high
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public synchronized void start() {
        new Thread(this).start();
        running = true;

    }

    public synchronized void stop() {
        running = false;
    }

    public void render() {
        Graphics g = getGraphics();
        if (bufferImage == null) {

            bufferImage = createImage(this.getSize().width, this.getSize().height);
            bufferGraphics = bufferImage.getGraphics();

        }

        bufferGraphics.setColor(Color.BLACK);
        bufferGraphics.fillRect(0, 0, this.getSize().width, this.getSize().height);
        
        if(MAIN_MENU && !GAME) {
        	MainMenu.MAIN = true;
        	mainMenu = new MainMenu();
        }
        
        if(GAME) {
        	drawGame(bufferGraphics);
        }
        
        g.drawImage(bufferImage, 0, 0, this);
        
    }

    //renamed from paint, the paint method is used by AWT objects. This caused a serious bug
    public void drawGame(Graphics g) {

        for (int x = 0; x < currentLevel.getWidth(); x++) {
            for (int y = 0; y < currentLevel.getHeight(); y++) {
                int tile = currentLevel.getTileAt(x, y);
                int tileX = x * Resources.TILE_WIDTH;
                int tileY = y * Resources.TILE_HEIGHT;

                if (tile == GameMap.TILE_WALL) {
                    g.drawImage(Resources.WALL, tileX, tileY, Resources.TILE_WIDTH, Resources.TILE_HEIGHT, null);
                }

                if (tile == GameMap.TILE_NOTHING) {
                    g.drawImage(Resources.POINT_BACK, tileX, tileY, Resources.TILE_WIDTH, Resources.TILE_HEIGHT, null);
                }
                
                if (tile == GameMap.TILE_BIG_POINT) {
                	g.drawImage(Resources.BIG_POINT, tileX, tileY, Resources.TILE_WIDTH, Resources.TILE_HEIGHT, null);
                }
                
                if (tile == GameMap.TILE_GHOST_SPAWN) {
                    g.drawImage(Resources.POINT, tileX, tileY, Resources.TILE_WIDTH, Resources.TILE_HEIGHT, null);
                }
            }
        }
        
        switch(direction) {
        case UP:
        	g.drawImage(Resources.PACMANUP, playerLocation.x, playerLocation.y, playerWidth, playerHeight, null);
        	break;
        	
        case DOWN:
        	g.drawImage(Resources.PACMANDOWN, playerLocation.x, playerLocation.y, playerWidth, playerHeight, null);
        	break;
        
        case RIGHT:
        	g.drawImage(Resources.PACMANRIGHT, playerLocation.x, playerLocation.y, playerWidth, playerHeight, null);
        	break;
        	
        case LEFT:
        	g.drawImage(Resources.PACMANLEFT, playerLocation.x, playerLocation.y, playerWidth, playerHeight, null);
        	break;
        }
    }

   public void tick() {

        int nextPlayerPosX = playerLocation.x;
        int nextPlayerPosY = playerLocation.y;
        
        if(nextPlayerPosX < 21) {
        	nextPlayerPosX = nextPlayerPosX + 601;
        }
        if(nextPlayerPosX > 605){
        	nextPlayerPosX = nextPlayerPosX - 600;
        }
        
       
        if (input.key_up) {
        	 if(!doesPlayerCollideWith(nextPlayerPosX , nextPlayerPosY -6, GameMap.TILE_WALL)&& !doesPlayerCollideWith(nextPlayerPosX, nextPlayerPosY, GameMap.TILE_GHOST_SPAWN)) {
        		 nextPlayerPosY -= movementSpeed;
        		 direction= Direction.UP;
        		 points(nextPlayerPosX, nextPlayerPosY);
                 playerLocation.setLocation(nextPlayerPosX, nextPlayerPosY);
                 
        	 } else if(doesPlayerCollideWith(nextPlayerPosX, nextPlayerPosY-6, GameMap.TILE_WALL)) {
        		points(nextPlayerPosX, nextPlayerPosY);
        		 direction(nextPlayerPosX,nextPlayerPosY); 
        	 }
        }
            
   

        if (input.key_down) {
        	 if(!doesPlayerCollideWith(nextPlayerPosX, nextPlayerPosY + 6, GameMap.TILE_WALL)) {
        		 nextPlayerPosY += movementSpeed;
        		 direction = Direction.DOWN;
        		 points(nextPlayerPosX, nextPlayerPosY);
                 playerLocation.setLocation(nextPlayerPosX, nextPlayerPosY);
                 
        	 }else if(doesPlayerCollideWith(nextPlayerPosX, nextPlayerPosY+6, GameMap.TILE_WALL)) {
        	 	points(nextPlayerPosX, nextPlayerPosY);
        		 direction(nextPlayerPosX,nextPlayerPosY); 
        	 }
        		 
            
        
        }

        if (input.key_left) {
        	 if(!doesPlayerCollideWith(nextPlayerPosX -3, nextPlayerPosY, GameMap.TILE_WALL)&& !doesPlayerCollideWith(nextPlayerPosX, nextPlayerPosY, GameMap.TILE_GHOST_SPAWN)) {
        		 nextPlayerPosX -= movementSpeed;
        		 direction = Direction.LEFT;
        		 points(nextPlayerPosX, nextPlayerPosY);
                 playerLocation.setLocation(nextPlayerPosX, nextPlayerPosY);
                 
        	 }else if(doesPlayerCollideWith(nextPlayerPosX -3, nextPlayerPosY, GameMap.TILE_WALL)) {
        	 	points(nextPlayerPosX, nextPlayerPosY);
        		direction(nextPlayerPosX,nextPlayerPosY); 
        	 }
       
        }
        if (input.key_right) {
        	 if(!doesPlayerCollideWith(nextPlayerPosX+ 3, nextPlayerPosY, GameMap.TILE_WALL)&& !doesPlayerCollideWith(nextPlayerPosX, nextPlayerPosY, GameMap.TILE_GHOST_SPAWN)) {
        		 nextPlayerPosX += movementSpeed;
        		 direction = Direction.RIGHT;
        		 points(nextPlayerPosX, nextPlayerPosY);
                 playerLocation.setLocation(nextPlayerPosX, nextPlayerPosY);
                 
        	 }else if(doesPlayerCollideWith(nextPlayerPosX+3, nextPlayerPosY, GameMap.TILE_WALL)) {
        	 	points(nextPlayerPosX, nextPlayerPosY);
        		 direction(nextPlayerPosX,nextPlayerPosY);
        	 }
        	
       
       }
        	
      }
    
   public void direction(int nextPlayerPosX ,int nextPlayerPosY){
        switch(direction) {
        case UP:
        	if(!doesPlayerCollideWith(nextPlayerPosX , nextPlayerPosY-6, GameMap.TILE_WALL)){//&& !doesPlayerCollideWith(nextPlayerPosX, nextPlayerPosY, GameMap.TILE_GHOST_SPAWN)){
        	 nextPlayerPosY -= movementSpeed;
             playerLocation.setLocation(nextPlayerPosX, nextPlayerPosY);
         	break;
        }
         	
         case DOWN:
        	 if(!doesPlayerCollideWith(nextPlayerPosX , nextPlayerPosY+6 , GameMap.TILE_WALL)){//&& !doesPlayerCollideWith(nextPlayerPosX, nextPlayerPosY, GameMap.TILE_GHOST_SPAWN)){
        	 nextPlayerPosY += movementSpeed;
             playerLocation.setLocation(nextPlayerPosX, nextPlayerPosY);
 	       	break;
        	 }
 	       case RIGHT:
 	    	  if (!doesPlayerCollideWith(nextPlayerPosX+3 , nextPlayerPosY, GameMap.TILE_WALL)){//&& !doesPlayerCollideWith(nextPlayerPosX, nextPlayerPosY, GameMap.TILE_GHOST_SPAWN)){
 	 		 nextPlayerPosX += movementSpeed;
             playerLocation.setLocation(nextPlayerPosX, nextPlayerPosY);
         	break;
 	    	  }
 	      
 	       case LEFT:
 	    	  if(!doesPlayerCollideWith(nextPlayerPosX -3, nextPlayerPosY, GameMap.TILE_WALL)){//&& !doesPlayerCollideWith(nextPlayerPosX, nextPlayerPosY, GameMap.TILE_GHOST_SPAWN)){
 	    	  nextPlayerPosX -= movementSpeed;
              playerLocation.setLocation(nextPlayerPosX, nextPlayerPosY);
 	        	break;
 	    	  }
 	        
        }
   }
      public boolean points( int pTileX, int pTileY){
		int x =0;
		int y = 0;
		
		int[][] level = GameMap.LEVEL_1_DATA;
		Rectangle p = new Rectangle(pTileX, pTileY, playerWidth, playerHeight);
		
		
		 switch(direction) {
	        case UP:
	        	x = pTileX / 23;
				y = (pTileY - 1) /29;
				
				if(level[y][x] == 1){
				Rectangle r = new Rectangle(pTileX,pTileY - 1, Resources.TILE_WIDTH, Resources.TILE_WIDTH);
					if( r.contains(pTileX, pTileY - 1)){
 						points= points + 1;
 						System.out.println(points);
 						level[y][x] = 4;
 						return true;
 					}
	         	break;
	        }
	         	
	         case DOWN:
	     		x = pTileX / 23;
				y = (pTileY + playerHeight) /29;
				
				if(level[y][x] == 1){
						
				Rectangle r = new Rectangle(pTileX,pTileY + 1, Resources.TILE_WIDTH, Resources.TILE_WIDTH);
					if( r.contains(pTileX, pTileY + 1)){
						points= points + 1;
 						System.out.println(points);
 						level[y][x] = 4;
 						return true;
 					}
				}
	 	       	break;
	        	 
	 	       case RIGHT:
	 	    		x = (pTileX +  playerWidth) / 23;
	 				y = pTileY / 29;
	 				
	 				if(level[y][x] == 1){
	 					Rectangle r = new Rectangle(pTileX,pTileY, Resources.TILE_WIDTH, Resources.TILE_WIDTH);
	 					if( r.contains(pTileX, pTileY)){
	 						points++;
	 						level[y][x] = 4;
	 						return true;
	 					}
	 	    	  }
	 	      
	 	       case LEFT:
	 	    	  x = (pTileX - 1) / 23;
	 				y = pTileY / 29;
	 				
	 				if(level[y][x] == 1){
	 					Rectangle r = new Rectangle(pTileX - 1,pTileY, Resources.TILE_WIDTH, Resources.TILE_WIDTH);
	 					if( r.contains(pTileX - 1, pTileY)){
	 						points++;
	 						level[y][x] = 4;
	 						return true;
	 					}
	 	        	break;
	 	    	  }
	 	        
	        }
		
			return false;
		}
	
      

    /**
     * Looks at the players screen location and gets the map tiles for each corner.
     * @param screenX
     * @param screenY
     * @return the 4 map tiles for each corner of the pac man given the screenX and screenY
     */
    private int[] getPlayerCornerCollisions(int screenX, int screenY) {
        int[] corners = new int[4];
        Point tileLocation = convertScreenLocationToMapLocation(screenX, screenY);
        corners[0] = currentLevel.getTileAt(tileLocation.x, tileLocation.y);

        tileLocation = convertScreenLocationToMapLocation(screenX + playerWidth, screenY);
        corners[1] = currentLevel.getTileAt(tileLocation.x, tileLocation.y);

        tileLocation = convertScreenLocationToMapLocation(screenX, screenY + playerHeight);
        corners[2] = currentLevel.getTileAt(tileLocation.x, tileLocation.y);

        tileLocation = convertScreenLocationToMapLocation(screenX + playerWidth, screenY + playerHeight);
        corners[3] = currentLevel.getTileAt(tileLocation.x, tileLocation.y);
        return corners;
    }
    
 
    /**
     * Checks if any corners of the player intersects with the given mapTileType
     * @param screenX
     * @param screenY
     * @param mapTileType
     * @return true if the player intersects with the given map tile type
     */
    public boolean doesPlayerCollideWith(int screenX, int screenY, int mapTileType) {
        for(int tileType : getPlayerCornerCollisions(screenX, screenY)) {
            if(tileType == mapTileType) {
                return true;
            }
        }
        return false;
    }

    /**
     * Takes the screen location and converts it to a coordinate in the map
     * @param location
     * @return
     */
    public Point convertScreenLocationToMapLocation(Point location) {
        return convertScreenLocationToMapLocation(location.x, location.y);
    }
    public Point convertScreenLocationToMapLocation(int x, int y) {
        return new Point(x/Resources.TILE_WIDTH, y/Resources.TILE_HEIGHT);
    }

    public Point convertMapLocationToScreenLocation(Point location) {
        return convertMapLocationToScreenLocation(location.x, location.y);
    }
    public Point convertMapLocationToScreenLocation(int x, int y) {
        return new Point(x*Resources.TILE_WIDTH, y*Resources.TILE_HEIGHT);
    }

    public static void main(String[] args) {
        new Game().start();
    }
}
