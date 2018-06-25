package me.rpgengine.xam4lor.world;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

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

	private ArrayList<MappedTile> mappedTiles;
	private HashMap<Integer, String> comments;
	
	private Entities entities;

	private File mapFile;

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
		this.mapFile = mapFile;
		
		this.mappedTiles = new ArrayList<MappedTile>();
		this.comments = new HashMap<Integer, String>();
		
		try {
			Scanner scanner = new Scanner(mapFile);
			int currentLine = 0;
			while(scanner.hasNextLine()) {
				String line = scanner.nextLine();
				if(!line.startsWith("//")) {
					if(line.contains(":")) {
						String[] splitString = line.split(":");
						if(splitString[0].equalsIgnoreCase("Fill")) {
							fillTileID = Integer.parseInt(splitString[1]);
							continue;
						}
					}


					String[] splitString = line.split(",");
					if(splitString.length >= 3) {
						MappedTile mappedTile = new MappedTile(Integer.parseInt(splitString[0]),
															   Integer.parseInt(splitString[1]),
															   Integer.parseInt(splitString[2]));
						mappedTiles.add(mappedTile);
					}
				}
				else {
					comments.put(currentLine, line);
				}
				currentLine++;
			}
			
			scanner.close();
		}
		catch(FileNotFoundException e) {
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

		MappedTile mappedTile = mappedTiles.get(tileID);
		if(mappedTile.x == tileX && mappedTile.y == tileY) {
			mappedTile.id = tileID;
			foundTile = true;
		}

		if(!foundTile)
			mappedTiles.add(new MappedTile(tileID, tileX, tileY));
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
	 * Sauvegarde la carte
	 */
	public void saveMap() {
		try {
			int currentLine = 0;
			if(mapFile.exists()) 
				mapFile.delete();
			mapFile.createNewFile();

			PrintWriter printWriter = new PrintWriter(mapFile);

			if(fillTileID >= 0) {
				if(comments.containsKey(currentLine)) {
					printWriter.println(comments.get(currentLine));
					currentLine++;
				}
				printWriter.println("Fill:" + fillTileID);
			}

			for(int i = 0; i < mappedTiles.size(); i++) {
				if(comments.containsKey(currentLine))
					printWriter.println(comments.get(currentLine));

				MappedTile tile = mappedTiles.get(i);
				printWriter.println(tile.id + "," + tile.x + "," + tile.y);
				currentLine++;
			}

			printWriter.close();
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Tile ID dans le tileSet ainsi que les positions de la tile sur la carte
	 */
	class MappedTile {
		public int id, x, y;
		
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
	}
}