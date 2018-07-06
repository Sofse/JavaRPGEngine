package me.rpgengine.xam4lor.engine;

import java.awt.Canvas;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
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
	
	/**
	 * Zoom en X
	 */
	public int xZoom = 3;
	
	/**
	 * Zoom en Y
	 */
	public int yZoom = 3;

	
	private Canvas canvas;
	private RenderHandler renderer;
	private Font mainFont;

	
	private Tiles tiles;
	private World world;

	private KeyBoardListener keyListener = new KeyBoardListener(this);
	private MouseEventListener mouseListener = new MouseEventListener(this);

	

	
	/**
	 * Classe de jeu principale
	 */
	public Game()  {
		this.createWindow();
		this.loadAssetsAndTiles();

		world = new World(this, new File("res/config/levels/house_hero_E1.txt"), tiles);
		

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
		
		if(!this.world.isWorldChanging()) {
			this.world.render(renderer, xZoom, yZoom);
			renderer.render(graphics);
			
			this.world.postRender(graphics, xZoom, yZoom);
		}

		graphics.dispose();
		bufferStrategy.show();
		renderer.clear();
	}

	/**
	 * Fonction d'update du jeu
	 */
	public void update() {
		if(!this.world.isWorldChanging() && !this.world.shouldWorldChange()) {
			this.world.update(this);
		}
		else if(this.world.shouldWorldChange())
			this.world.loadWorld(this.world.getWorldChangingFile());
	}
	
	

	
	
	
	
	
	/**
	 * Lorsque la touche CTRL est maintenue
	 * @param keys
	 * 	Liste des touches selectionnées
	 */
	public void handleCTRL(boolean[] keys) {}

	/**
	 * Lors d'un click gauche de la souris
	 * @param x
	 * 	Position en X absolue
	 * @param y
	 * 	Position en Y absolue
	 */
	public void leftClick(int x, int y) {}

	/**
	 * Lors d'un click droit de la souris
	 * @param x
	 * 	Position en X absolue
	 * @param y
	 * 	Position en Y absolue
	 */
	public void rightClick(int x, int y) {}
	
	
	
	
	
	
	
	

	
	
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
	 * Charge les assets et les tiles du jeu
	 */
	private void loadAssetsAndTiles() {
		// tiles + sprites
		tiles = new Tiles();
		
		try {
			File file = new File("res/config/tiles/tiles.txt");
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
		
		// fonts
	    try {
			this.mainFont = Font.createFont(Font.TRUETYPE_FONT, new File("res/font/PokemonFont.ttf")).deriveFont(Font.PLAIN, 19);
	    }
	    catch (FontFormatException | IOException e) {
			e.printStackTrace();
		}
	}
	
	
	private void createWindow() {
		this.canvas = new Canvas();
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(0,0, 1000, 800);
		setLocationRelativeTo(null);
		add(canvas);
		setVisible(true);

		canvas.createBufferStrategy(3);
		renderer = new RenderHandler(getWidth(), getHeight());
		
		addComponentListener(new ComponentListener() {
			public void componentResized(ComponentEvent e) {
				int newWidth = canvas.getWidth();
				int newHeight = canvas.getHeight();

				if(newWidth > renderer.getMaxWidth())
					newWidth = renderer.getMaxWidth();

				if(newHeight > renderer.getMaxHeight())
					newHeight = renderer.getMaxHeight();

				renderer.getCamera().w = newWidth;
				renderer.getCamera().h = newHeight;
				canvas.setSize(newWidth, newHeight);
				pack();
			}

			public void componentHidden(ComponentEvent e) {}
			public void componentMoved(ComponentEvent e) {}
			public void componentShown(ComponentEvent e) {}
		});
		
		canvas.requestFocus();
	}
	
	
	/**
	 * Chargement d'une image en convertion en image RGB
	 * @param path
	 * 	Nom de la ressource
	 * @return l'image chargée
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
	
	/**
	 * @return la classe du monde
	 */
	public World getWorld() {
		return this.world;
	}

	/**
	 * @return les tiles
	 */
	public Tiles getTiles() {
		return this.tiles;
	}
	
	/**
	 * @return le font par défault
	 */
	public Font getMainFont() {
		return mainFont;
	}
}