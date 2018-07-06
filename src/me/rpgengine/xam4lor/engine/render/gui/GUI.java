package me.rpgengine.xam4lor.engine.render.gui;

import java.awt.Graphics;
import java.util.HashMap;
import java.util.Map.Entry;

import me.rpgengine.xam4lor.engine.Game;
import me.rpgengine.xam4lor.engine.render.RenderHandler;
import me.rpgengine.xam4lor.engine.render.sprites.Sprite;
import me.rpgengine.xam4lor.engine.structure.GameObject;
import me.rpgengine.xam4lor.world.Tiles;

/**
 * GUI de render
 */
public abstract class GUI implements GameObject {
	private boolean show;
	private int id;
	
	protected Tiles tileSet;
	protected Game game;
	
	private HashMap<int[], Sprite> sprites;
	
	
	
	/**
	 * GUI de render
	 * @param id
	 * 	ID dans le tileContainer
	 * @param game
	 *  Instance de jeu
	 * @param tileSet
	 * 	Liste des tiles
	 */
	public GUI(int id, Game game, Tiles tileSet) {
		this.sprites = new HashMap<int[], Sprite>();
		this.tileSet = tileSet;
		this.game = game;
		this.id = id;
	}
	
	
	
	
	@Override
	public void update(Game game) {}

	@Override
	public void render(RenderHandler renderer, int xZoom, int yZoom) {
		for (Entry<int[], Sprite> sprite : this.sprites.entrySet())
			renderer.renderSprite(sprite.getValue(), sprite.getKey()[0], sprite.getKey()[1], xZoom, yZoom, 60100, true);
	}
	
	@Override
	public void postRender(Graphics graphics, int xZoom, int yZoom) {}
	
	
	
	protected void addSprite(Sprite sprite, int positionX, int positionY) {
		this.sprites.put(new int[] {positionX, positionY}, sprite);
	}
	
	
	
	/**
	 * @return true si la GUI est affichée
	 */
	public boolean isShown() {
		return show;
	}
	
	/**
	 * @param show
	 * 	true : affichage de la GUI
	 */
	public void setShow(boolean show) {
		this.show = show;
		this.game.getWorld().getGUIContainer().getShowGUI().replace(this.id, show);
	}
}
