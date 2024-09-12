package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import Entity.Player;
import tile.TileManager;

// Configures the screen settings.
public class GamePanel extends JPanel implements Runnable {
	// SCREEN SETTINGS
	
	final int originalTileSize = 16; // 16x16 pixel tile size and map map tiles.
	final int scale = 3;
	
	public final int tileSize = originalTileSize * scale; // 48x48 actual tile size.
	public final int maxScreenCol = 16;
	public final int maxScreenRow = 12;
	final int screenWidth = tileSize * maxScreenCol; // 768 pixels
	final int screenHeight = tileSize * maxScreenRow; // 576 pixels
	
	TileManager tileM = new TileManager(this);
	KeyHandler keyH = new KeyHandler();
	Thread gameThread;
	
	Player player = new Player(this, keyH);
	
	int FPS = 60;
	
	int playerX = 100;
	int playerY = 100;
	int playerSpeed = 4;
	
	public GamePanel() {
		this.setPreferredSize(new Dimension(screenWidth, screenHeight));
		this.setBackground(Color.black);
		this.setDoubleBuffered(true);
		this.addKeyListener(keyH);
		this.setFocusable(true);
	}

	public void startGameThread() {
		gameThread = new Thread(this);
		gameThread.start();
	}
	
	@Override
//	public void run() {
//		// repeats some process while the game loop thread is running.
//		double drawInterval = 1000000000/FPS; // The amount of time between draws.
//		double nextDrawTime = System.nanoTime() + drawInterval; // Set an initial next drawTime.
//		
//		while (gameThread != null) {
//			// Two possible implementations for FPS. 
//			
//			// 1. UPDATE: Update the information such as character and map appearance
//			update();
//			// 2. DRAW: Draw the new state of the map based on the movement.
//			repaint(); // This is how you call the paintComponent method
//			
//			try { // Get's the amount of time until next drawTime, sleeps until next drawTime, then resets next 
//				double remainingTime = nextDrawTime - System.nanoTime();
//				remainingTime = remainingTime/1000000;
//				
//				if(remainingTime < 0) {
//					remainingTime = 0;
//				}
//				
//				Thread.sleep((long)remainingTime);
//				
//				nextDrawTime += drawInterval;
//			} catch (InterruptedException e){
//				e.printStackTrace();
//			}
//			
//		}
//	}
	
	// DELTA UPDATE METHOD OF GAME LOOP
	public void run() {
		double drawInterval = 1000000000/FPS;
		double delta = 0;
		long lastTime = System.nanoTime();
		long currentTime;
		long timer = 0;
		int drawCount = 0;
		
		while(gameThread != null) {
			currentTime = System.nanoTime();
			
			delta += (currentTime - lastTime) / drawInterval;
			timer += (currentTime - lastTime);
			lastTime = currentTime;
			
			if(delta >= 1) {
				update();
				repaint();
				delta--;
				drawCount++;
			}
			
			if(timer >= 1000000000) {
				System.out.println("FPS: " + drawCount);
				drawCount = 0;
				timer = 0;
			}
		}
	}
	
	// Limit the update speed so we don't do a million frames per keypress.
	public void update() {
		player.update();
	}
	
	public void paintComponent(Graphics g) { // Java in house function.
		
		super.paintComponent(g);
		
		// Converts graphics to 2D for easier manipulation;
		Graphics2D g2 = (Graphics2D)g;
		
		// Draw tiles below player.
		tileM.draw(g2);
		player.draw(g2);
		
		g2.dispose(); // Remove the drawing to save memory resources after it's created
	}
}
