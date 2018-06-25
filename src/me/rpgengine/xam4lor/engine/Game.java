package me.rpgengine.xam4lor.engine;

import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import me.rpgengine.xam4lor.engine.render.RenderHandler;
import me.rpgengine.xam4lor.engine.render.sprites.SpriteSheet;
import me.rpgengine.xam4lor.listener.KeyBoardListener;
import me.rpgengine.xam4lor.listener.MouseEventListener;
import me.rpgengine.xam4lor.world.Tiles;
import me.rpgengine.xam4lor.world.World;

/**
 * Classe de jeu principale
 */
public class Game extends JFrame implements Runnable {
	/**
	 * SERIAL VERSION UID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Couleur ALPHA
	 */
	public static int alpha = 0xFFFF00DC;

	
	private Canvas canvas;
	private RenderHandler renderer;

	

	private Tiles tiles;
	private World world;

	private KeyBoardListener keyListener = new KeyBoardListener(this);
	private MouseEventListener mouseListener = new MouseEventListener(this);

	private int xZoom = 3;
	private int yZoom = 3;

	
	/**
	 * Classe de jeu principale
	 */
	public Game()  {
		this.createWindow();
		this.loadAssetsAndTiles();

		world = new World(this, new File("res/levels/test_world.txt"), tiles);
		

		//Add Listeners
		canvas.addKeyListener(keyListener);
		canvas.addFocusListener(keyListener);
		canvas.addMouseListener(mouseListener);
		canvas.addMouseMotionListener(mouseListener);
	}
	
	
	
	
	/**
	 * Fonction de render du jeu
	 */
	public void render() {
		BufferStrategy bufferStrategy = canvas.getBufferStrategy();
		Graphics graphics = bufferStrategy.getDrawGraphics();
		super.paint(graphics);

		this.world.render(renderer, xZoom, yZoom);

		renderer.render(graphics);

		graphics.dispose();
		bufferStrategy.show();
		renderer.clear();
	}

	/**
	 * Fonction d'update du jeu
	 */
	public void update() {
		this.world.update(this);
	}

	
	
	
	
	
	/**
	 * Lorsque la touche CTRL est maintenue
	 * @param keys
	 * 	Liste des touches selectionn�es
	 */
	public void handleCTRL(boolean[] keys) {
		if(keys[KeyEvent.VK_S])
			this.world.saveMap();
		this.world.loadWorld(new File("res/levels/test_world2.txt"));
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
		this.world.setTile(x, y, 2);
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
		this.world.removeTile(x, y);
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

	
	
	
	
	
	private void loadAssetsAndTiles() {
		tiles = new Tiles();
		
		try {
			File file = new File("res/tiles/tiles.txt");
			JSONObject tilesList = new JSONObject(new String(Files.readAllBytes(file.toPath()), "UTF-8"));
			JSONArray tilesheets = tilesList.getJSONArray("tilesheets");
			
			for (int i = 0; i < tilesheets.length(); i++) {
				JSONObject tileSheet = tilesheets.getJSONObject(i);
				BufferedImage sheetImage = loadImage(tileSheet.getString("ressource_file_name") + "." + tileSheet.getString("extension"));
				SpriteSheet sheet = new SpriteSheet(sheetImage);
				
				sheet.loadSprites(Tiles.TILE_SIZE, Tiles.TILE_SIZE);
				tiles.loadTiles(sheet, tileSheet.getJSONArray("sprites"));
			}
		}
		catch (JSONException | IOException e) {
			e.printStackTrace();
		}
	}
	
	
	private void createWindow() {
		this.canvas = new Canvas();
		
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
	}
	
	
	/**
	 * Chargement d'une image en convertion en image RGB
	 * @param path
	 * 	Nom de la ressource
	 * @return l'image charg�e
	 */
	public BufferedImage loadImage(String path) {
		try {
			BufferedImage loadedImage = ImageIO.read(new File("res/sprites/" + path));
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