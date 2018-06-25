package me.rpgengine.xam4lor.world;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import me.rpgengine.xam4lor.engine.Game;
import me.rpgengine.xam4lor.engine.render.RenderHandler;
import me.rpgengine.xam4lor.engine.structure.GameObject;
import me.rpgengine.xam4lor.engine.structure.Rectangle;
import me.rpgengine.xam4lor.entities.Entities;

/**
 * Création d'un monde
 */
public class World implements GameObject {
	private Tiles tileSet;
	private int fillTileID = -1;
	private String levelName;

	private ArrayList<MappedTile> mappedTiles;
	private Entities entities;

	/**
	 * Création d'un monde
	 * @param game
	 * @param mapFile
	 * 	Fichier du monde de base
	 * @param tileSet
	 * 	Liste des tiles
	 */
	public World(Game game, File mapFile, Tiles tileSet) {
		this.tileSet = tileSet;
		this.entities = new Entities(game);
		
		this.loadWorld(mapFile);
	}

	
	
	@Override
	public void update(Game game) {
		this.entities.update(game);
	}
	
	@Override
	public void render(RenderHandler renderer, int xZoom, int yZoom) {
		this.renderWorld(renderer, xZoom, yZoom);
		
		this.entities.render(renderer, xZoom, yZoom);
	}
	
	
	
	
	/**
	 * Charge un monde
	 * @param mapFile
	 * 	Fichier du monde
	 */
	public void loadWorld(File mapFile) {
		this.mappedTiles = new ArrayList<MappedTile>();
		
		try {
			JSONObject level = new JSONObject(new String(Files.readAllBytes(mapFile.toPath()), "UTF-8"));
			JSONArray tiles = level.getJSONArray("tiles");
			JSONObject entities = level.getJSONObject("entities");
			JSONArray npc = entities.getJSONArray("npc");
			
			this.fillTileID = level.getInt("fill_tile_ID");
			this.levelName = level.getString("world_name");
			
			// Ajout des tiles
			for (int i = 0; i < tiles.length(); i++) {
				JSONObject tile = tiles.getJSONObject(i);
				mappedTiles.add(new MappedTile(tile.getInt("id"), tile.getInt("x"), tile.getInt("y")));
			}
			
			// Ajout des entités
			this.entities.clearEntities();
			this.entities.addPlayer(entities.getJSONObject("player").getInt("x"), entities.getJSONObject("player").getInt("y"));
			
			for (int i = 0; i < npc.length(); i++)
				this.entities.addEntity(npc.getJSONObject(i));
		}
		catch (JSONException | IOException e) {
			e.printStackTrace();
		}
	}
	
	
	
	/**
	 * Rendu du monde
	 * @param renderer
	 * 	Renderer principal
	 * @param xZoom
	 * 	Zoom en X
	 * @param yZoom
	 * 	Zoom en Y
	 */
	private void renderWorld(RenderHandler renderer, int xZoom, int yZoom) {
		int tileWidth = Tiles.TILE_SIZE * xZoom;
		int tileHeight = Tiles.TILE_SIZE * yZoom;

		if(fillTileID >= 0) {
			Rectangle camera = renderer.getCamera();

			for(int y = camera.y - tileHeight - (camera.y % tileHeight); y < camera.y + camera.h; y+= tileHeight) {
				for(int x = camera.x - tileWidth - (camera.x % tileWidth); x < camera.x + camera.w; x+= tileWidth) {
					tileSet.renderTile(fillTileID, renderer, x, y, xZoom, yZoom);
				}
			}
		}

		for(int tileIndex = 0; tileIndex < mappedTiles.size(); tileIndex++) {
			MappedTile mappedTile = mappedTiles.get(tileIndex);
			tileSet.renderTile(mappedTile.id, renderer, mappedTile.x * tileWidth, mappedTile.y * tileHeight, xZoom, yZoom);
		}
	}
	
	
	
	
	
	/**
	 * Modification d'une tile
	 * @param tileX
	 * 	Position en X
	 * @param tileY
	 * 	Position en Y
	 * @param tileID
	 * 	ID de la tile
	 */
	public void setTile(int tileX, int tileY, int tileID) {
		boolean foundTile = false;

		try {
			MappedTile mappedTile = mappedTiles.get(tileID);
			if(mappedTile.x == tileX && mappedTile.y == tileY) {
				mappedTile.id = tileID;
				foundTile = true;
			}
		}
		catch(IndexOutOfBoundsException e) {}

		if(!foundTile)
			mappedTiles.add(new MappedTile(tileID, tileX, tileY));
	}
	
	/**
	 * Retourne une tile via sa position
	 * @param tileX
	 * 	Position en X
	 * @param tileY
	 * 	Position en Y
	 * @return la MappedTile à la position
	 */
	public MappedTile getTileAt(int tileX, int tileY) {
		for (MappedTile mappedTile : this.mappedTiles)
			if(mappedTile.x == tileX && mappedTile.y == tileY)
				return mappedTile;
		
		return null;
	}
	
	
	

	/**
	 * Supression d'une tile
	 * @param tileX
	 * 	Position en X
	 * @param tileY
	 * 	Position en Y
	 */
	public void removeTile(int tileX, int tileY) {
		for(int i = 0; i < mappedTiles.size(); i++) {
			MappedTile mappedTile = mappedTiles.get(i);
			if(mappedTile.x == tileX && mappedTile.y == tileY) {
				mappedTiles.remove(i);
			}
		}
	}
	
	
	

	/**
	 * Tile ID dans le tileSet ainsi que les positions de la tile sur la carte
	 */
	public class MappedTile {
		private int id, x, y;
		
		/**
		 * Tile ID dans le tileSet ainsi que les positions de la tile sur la carte
		 * @param id
		 * 	ID de la tile
		 * @param x 
		 * 	Position en X de la tile
		 * @param y 
		 * 	Position en Y de la tile
		 */
		public MappedTile(int id, int x, int y) {
			this.id = id;
			this.x = x;
			this.y = y;
		}

		/**
		 * @return l'ID de la tile actuelle
		 */
		public int getID() {
			return this.id;
		}
	}
	
	
	
	
	/**
	 * @return la tile par défault (tile créée lors du premier appel à cette fonction)
	 */
	public int getDefaultTileID() {
		return this.fillTileID;
	}
	
	/**
	 * @return le nom du monde
	 */
	public String getWorldName() {
		return this.levelName;
	}
}