package me.rpgengine.xam4lor.engine;

import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import me.rpgengine.xam4lor.engine.render.RenderHandler;
import me.rpgengine.xam4lor.engine.render.sprites.AnimatedSprite;
import me.rpgengine.xam4lor.engine.render.sprites.SpriteSheet;
import me.rpgengine.xam4lor.engine.structure.GameObject;
import me.rpgengine.xam4lor.engine.structure.Rectangle;
import me.rpgengine.xam4lor.entities.Player;
import me.rpgengine.xam4lor.listener.KeyBoardListener;
import me.rpgengine.xam4lor.listener.MouseEventListener;
import me.rpgengine.xam4lor.world.Tiles;
import me.rpgengine.xam4lor.world.World;

/**
 * Classe de jeu principale
 */
public class Game extends JFrame implements Runnable {
	private static final long serialVersionUID = 1L;

	/**
	 * Couleur ALPHA
	 */
	public static int alpha = 0xFFFF00DC;

	
	private Canvas canvas = new Canvas();
	private RenderHandler renderer;

	private SpriteSheet sheet;

	private Rectangle testRectangle = new Rectangle(30, 30, 100, 100);

	private Tiles tiles;
	private World map;

	private GameObject[] objects;
	private KeyBoardListener keyListener = new KeyBoardListener(this);
	private MouseEventListener mouseListener = new MouseEventListener(this);

	private Player player;
	private SpriteSheet playerSheet;
	private AnimatedSprite playerAnim;

	private int xZoom = 3;
	private int yZoom = 3;

	
	/**
	 * Classe de jeu principale
	 */
	public Game()  {
		//Make our program shutdown when we exit out.
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		//Set the position and size of our frame.
		setBounds(0,0, 1000, 800);

		//Put our frame in the center of the screen.
		setLocationRelativeTo(null);

		//Add our graphics compoent
		add(canvas);

		//Make our frame visible.
		setVisible(true);

		//Create our object for buffer strategy.
		canvas.createBufferStrategy(3);

		renderer = new RenderHandler(getWidth(), getHeight());

		//Load Assets
		BufferedImage sheetImage = loadImage("Tiles1.png");
		sheet = new SpriteSheet(sheetImage);
		sheet.loadSprites(Tiles.TILE_SIZE, Tiles.TILE_SIZE);

		//Load Tiles
		tiles = new Tiles(new File("res/Tiles.txt"),sheet);

		//Load Map
		map = new World(new File("res/Map.txt"), tiles);

		//testImage = loadImage("GrassTile.png");
		//testSprite = sheet.getSprite(4,1);

		testRectangle.generateGraphics(2, 12234);


		//Load Objects
		objects = new GameObject[1];
		
		BufferedImage playerSheetImage = loadImage("Player.png");
		playerSheet = new SpriteSheet(playerSheetImage);
		playerSheet.loadSprites(20, 26);
	
		playerAnim = new AnimatedSprite(playerSheet, 5);
		this.player = new Player(playerAnim);
		
		objects[0] = player;
		

		//Add Listeners
		canvas.addKeyListener(keyListener);
		canvas.addFocusListener(keyListener);
		canvas.addMouseListener(mouseListener);
		canvas.addMouseMotionListener(mouseListener);
	}

	/**
	 * Fonction d'update du jeu
	 */
	public void update() {
		for(int i = 0; i < objects.length; i++) 
			objects[i].update(this);
	}


	/**
	 * Chargement d'une image en convertion en image RGB
	 * @param path
	 * 	Nom de la ressource
	 */
	private BufferedImage loadImage(String path) {
		try {
			BufferedImage loadedImage = ImageIO.read(new File("res/" + path));
			BufferedImage formattedImage = new BufferedImage(loadedImage.getWidth(), loadedImage.getHeight(), BufferedImage.TYPE_INT_RGB);
			formattedImage.getGraphics().drawImage(loadedImage, 0, 0, null);

			return formattedImage;
		}
		catch(IOException exception) {
			exception.printStackTrace();
			return null;
		}
	}

	/**
	 * Lorsque la touche CTRL est maintenue
	 * @param keys
	 * 	Liste des touches selectionnées
	 */
	public void handleCTRL(boolean[] keys) {
		if(keys[KeyEvent.VK_S])
			map.saveMap();
	}

	/**
	 * Lors d'un click gauche de la souris
	 * @param x
	 * 	Position en X absolue
	 * @param y
	 * 	Position en Y absolue
	 */
	public void leftClick(int x, int y) {
		x = (int) Math.floor((x + renderer.getCamera().x)/(Double.parseDouble(Tiles.TILE_SIZE + "") * xZoom));
		y = (int) Math.floor((y + renderer.getCamera().y)/(Double.parseDouble(Tiles.TILE_SIZE + "") * yZoom));
		map.setTile(x, y, 2);
	}

	/**
	 * Lors d'un click droit de la souris
	 * @param x
	 * 	Position en X absolue
	 * @param y
	 * 	Position en Y absolue
	 */
	public void rightClick(int x, int y) {
		x = (int) Math.floor((x + renderer.getCamera().x)/(Double.parseDouble(Tiles.TILE_SIZE + "") * xZoom));
		y = (int) Math.floor((y + renderer.getCamera().y)/(Double.parseDouble(Tiles.TILE_SIZE + "") * yZoom));
		map.removeTile(x, y);
	}

	/**
	 * Fonction de render du jeu
	 */
	public void render() {
			BufferStrategy bufferStrategy = canvas.getBufferStrategy();
			Graphics graphics = bufferStrategy.getDrawGraphics();
			super.paint(graphics);

			map.render(renderer, xZoom, yZoom);

			for(int i = 0; i < objects.length; i++) 
				objects[i].render(renderer, xZoom, yZoom);

			renderer.render(graphics);

			graphics.dispose();
			bufferStrategy.show();
			renderer.clear();
	}

	@Override
	public void run() {
		long lastTime = System.nanoTime(); //long 2^63
		double nanoSecondConversion = 1000000000.0 / 60; //60 frames per second
		double changeInSeconds = 0;

		while(true) {
			long now = System.nanoTime();

			changeInSeconds += (now - lastTime) / nanoSecondConversion;
			while(changeInSeconds >= 1) {
				update();
				changeInSeconds--;
			}

			render();
			lastTime = now;
		}

	}

	
	
	/**
	 * @return le listener du clavier
	 */
	public KeyBoardListener getKeyListener() {
		return this.keyListener;
	}
	
	/**
	 * @return le listener de la souris
	 */
	public MouseEventListener getMouseListener() {
		return this.mouseListener;
	}
	
	/**
	 * @return le renderer du jeu
	 */
	public RenderHandler getRenderer() {
		return this.renderer;
	}
}