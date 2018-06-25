package me.rpgengine.xam4lor.world;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import me.rpgengine.xam4lor.engine.render.RenderHandler;
import me.rpgengine.xam4lor.engine.render.sprites.Sprite;
import me.rpgengine.xam4lor.engine.render.sprites.SpriteSheet;

/**
 * Liste des tiles
 */
public class Tiles {
	/**
	 * Taille d'une Tile
	 */
	public final static int TILE_SIZE = 16;
	
	private HashMap<Integer, Tile> tilesList;

	
	/**
	 * Liste des tiles
	 */
	public Tiles() {
		this.tilesList = new HashMap<Integer, Tile>();
	}
	

	/**
	 * Charge une spriteSheet
	 * @param spriteSheet
	 * 	SpriteSheet contenant les textures (Attention : le spriteSheet doit déjà avoir été initialisé)
	 * @param sprites
	 * 	Liste des sprites du tableau JSON
	 */
	public void loadTiles(SpriteSheet spriteSheet, JSONArray sprites) {
		for (int i = 0; i < sprites.length(); i++) {
			JSONObject tile = sprites.getJSONObject(i);
			int id = tile.getInt("id");
			String tileName = tile.getString("tile_name");
			
			if(!this.tilesList.containsKey(id))
				this.tilesList.put(id, new Tile(tileName, spriteSheet.getSprite(tile.getInt("x"), tile.getInt("y"))));
			else
				System.out.println("La tile à l'ID " + id + " du nom de '" + tileName + "' a le même ID qu'une autre Tile.");
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
		if(tileID >= 0 && tilesList.size() > tileID)
			renderer.renderSprite(tilesList.get(tileID).sprite, xPosition, yPosition, xZoom, yZoom);
		else
			System.out.println("TileID " + tileID + " is not within range " + tilesList.size() + ".");
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