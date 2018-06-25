package me.rpgengine.xam4lor.world;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import me.rpgengine.xam4lor.engine.render.RenderHandler;
import me.rpgengine.xam4lor.engine.render.sprites.Sprite;
import me.rpgengine.xam4lor.engine.render.sprites.SpriteSheet;

/**
 * Chargement une spriteSheet, contenant la liste des tiles
 */
public class Tiles {
	/**
	 * Taille d'une TILE
	 */
	public final static int TILE_SIZE = 16;
	
	private ArrayList<Tile> tilesList = new ArrayList<Tile>();

	/**
	 * Charge une spriteSheet
	 * @param tilesFile
	 * 	Fichier contenant les références tiles-positions
	 * @param spriteSheet
	 * 	SpriteSheet contenant les textures (Attention : le spriteSheet doit déjà avoir été initialisé)
	 */
	public Tiles(File tilesFile, SpriteSheet spriteSheet) {
		try {
			Scanner scanner = new Scanner(tilesFile);
			while(scanner.hasNextLine()) {
				String line = scanner.nextLine();
				if(!line.startsWith("//")) {
					String[] splitString = line.split("-");
					String tileName = splitString[0];
					int spriteX = Integer.parseInt(splitString[1]);
					int spriteY = Integer.parseInt(splitString[2]);
					Tile tile = new Tile(tileName, spriteSheet.getSprite(spriteX, spriteY));
					tilesList.add(tile);
				}
			}
			scanner.close();
		} 
		catch(FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Affichage d'une tile
	 * @param tileID
	 * 	ID de la tile
	 * @param renderer
	 * 	Renderer du jeu
	 * @param xPosition
	 * 	Position en X
	 * @param yPosition
	 * 	Position en Y
	 * @param xZoom
	 * 	Zoom en X
	 * @param yZoom
	 * 	Zoom en Y
	 */
	public void renderTile(int tileID, RenderHandler renderer, int xPosition, int yPosition, int xZoom, int yZoom) {
		if(tileID >= 0 && tilesList.size() > tileID) {
			renderer.renderSprite(tilesList.get(tileID).sprite, xPosition, yPosition, xZoom, yZoom);
		}
		else {
			System.out.println("TileID " + tileID + " is not within range " + tilesList.size() + ".");
		}
	}

	/**
	 * @return la taille de tilesList
	 */
	public int size() {
		return this.tilesList.size();
	}
	
	/**
	 * @return les sprites des tiles
	 */
	public Sprite[] getSprites() {
		Sprite[] sprites = new Sprite[this.size()];
		
		for (int i = 0; i < sprites.length; i++) {
			sprites[i] = tilesList.get(i).sprite;
		}
		
		return sprites;
	}
	
	
	/**
	 * Tile
	 */
	class Tile {
		public String tileName;
		public Sprite sprite;
		
		/**
		 * Tile
		 * @param tileName
		 * 	Nom de la tile
		 * @param sprite
		 * 	Sprite de la tile
		 */
		public Tile(String tileName, Sprite sprite) {
			this.tileName = tileName;
			this.sprite = sprite;
		}
	}
}