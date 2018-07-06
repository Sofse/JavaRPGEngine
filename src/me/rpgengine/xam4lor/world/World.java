package me.rpgengine.xam4lor.world;

import java.awt.Graphics;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import me.rpgengine.xam4lor.engine.Game;
import me.rpgengine.xam4lor.engine.render.RenderHandler;
import me.rpgengine.xam4lor.engine.render.gui.GUIContainer;
import me.rpgengine.xam4lor.engine.structure.GameObject;
import me.rpgengine.xam4lor.engine.structure.Rectangle;
import me.rpgengine.xam4lor.entities.Entities;
import me.rpgengine.xam4lor.world.Tiles.Tile;

/**
 * Création d'un monde
 */
public class World implements GameObject {
	private Tiles tileSet;
	private int fillTileID = -1;
	private String levelName;

	private ArrayList<MappedTile> mappedTiles;
	private Entities entities;
	private GUIContainer gui;
	
	private boolean worldUpdating;
	
	private boolean worldChanging;
	private boolean shouldWorldChange;
	private File worldChangingFile;

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
		this.gui = new GUIContainer(game, tileSet);
		
		this.worldChanging = true;
		this.loadWorld(mapFile);
		this.worldChanging = false;
		
		this.worldUpdating = true;
	}

	
	
	@Override
	public void update(Game game) {
		if(this.worldUpdating) {
			this.entities.update(game);
		}
		
		this.gui.update(game);
	}
	
	@Override
	public void render(RenderHandler renderer, int xZoom, int yZoom) {
		this.renderWorld(renderer, xZoom, yZoom);
		
		this.entities.render(renderer, xZoom, yZoom);
		this.gui.render(renderer, xZoom, yZoom);
	}
	
	@Override
	public void postRender(Graphics graphics, int xZoom, int yZoom) {
		this.entities.postRender(graphics, xZoom, yZoom);
		this.gui.postRender(graphics, xZoom, yZoom);
	}
	
	
	/**
	 * Charge un autre monde
	 * @param mapFile
	 * 	Fichier du monde
	 */
	public void changeWorld(File mapFile) {
		this.worldChangingFile = mapFile;
		this.shouldWorldChange = true;		
	}
	
	/**
	 * Chargement du monde
	 * @param worldName
	 * 	Nom du monde
	 */
	public void changeWorld(String worldName) {
		this.changeWorld(new File("res/config/levels/" + worldName + ".txt"));
	}
	
	
	
	
	/**
	 * Charge un monde
	 * @param mapFile
	 * 	Fichier du monde
	 */
	public void loadWorld(File mapFile) {
		this.mappedTiles = new ArrayList<MappedTile>();
		this.shouldWorldChange = false;
		this.worldChanging = true;
		
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
				mappedTiles.add(new MappedTile(tile.getInt("id"), tile.getInt("x"), tile.getInt("y"), tile.has("options") ? tile.getJSONObject("options") : null));
			}
			
			// Ajout des entités
			this.entities.clearEntities();
			
			JSONObject player = entities.getJSONObject("player");
			this.entities.addPlayer(player.getInt("x"), player.getInt("y"), player.getInt("speed"));
			
			for (int i = 0; i < npc.length(); i++)
				this.entities.addEntity(npc.getJSONObject(i));
		}
		catch (JSONException | IOException e) {
			e.printStackTrace();
		}
		
		this.worldChanging = false;
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
			mappedTiles.add(new MappedTile(tileID, tileX, tileY, null));
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
	 * MappedTile
	 */
	public class MappedTile {
		private int id, x, y;
		private JSONObject options;
		
		/**
		 * MappedTile
		 * @param id
		 * 	ID de la tile
		 * @param x 
		 * 	Position en X de la mappedTile
		 * @param y 
		 * 	Position en Y de la mappedTile
		 * @param options
		 * 	Configuration de la mappedTile : null pour sans configuration particulière
		 */
		public MappedTile(int id, int x, int y, JSONObject options) {
			this.id = id;
			this.x = x;
			this.y = y;
			this.options = options;
		}

		/**
		 * @return l'ID de la mappedTile actuelle
		 */
		public int getID() {
			return this.id;
		}
		
		/**
		 * @return la configuration de la mappedTile
		 */
		public JSONObject getOptions() {
			return this.options;
		}
		
		/**
		 * @param game
		 * 	Instance du jeu
		 * @return la tile du jeu
		 */
		public Tile getTile(Game game) {
			return game.getTiles().getTile(this.id);
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
	
	/**
	 * @return le GUI container
	 */
	public GUIContainer getGUIContainer() {
		return gui;
	}
	
	/**
	 * @param worldUpdating
	 * 	true : le world s'update
	 */
	public void setWorldUpdating(boolean worldUpdating) {
		this.worldUpdating = worldUpdating;
	}
	
	/**
	 * @return true si le world s'update
	 */
	public boolean isWorldUpdating() {
		return worldUpdating;
	}
	
	/**
	 * @return la liste des entités
	 */
	public Entities getEntities() {
		return entities;
	}
	
	/**
	 * @return true si le monde est en train de changer
	 */
	public boolean isWorldChanging() {
		return worldChanging;
	}
	
	/**
	 * @return true si le monde doit changer
	 */
	public boolean shouldWorldChange() {
		return this.shouldWorldChange;
	}
	
	/**
	 * @return le fichier du nouveau monde
	 */
	public File getWorldChangingFile() {
		return worldChangingFile;
	}
}