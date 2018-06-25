package me.rpgengine.xam4lor.engine.structure;

import me.rpgengine.xam4lor.engine.Game;
import me.rpgengine.xam4lor.engine.render.RenderHandler;

/**
 * Constructeur d'un gameObject
 */
public interface GameObject {
	/**
	 * Appelée autant de fois que de FPS par seconde
	 * @param game
	 * 	Classe du jeu
	 */
	public void update(Game game);
	
	/**
	 * Appelé autant que possible 
	 * @param renderer
	 * 	Renderer de la fenêtre
	 * @param xZoom
	 * 	Zoom en X
	 * @param yZoom
	 * 	Zoom en Y
	 */
	public void render(RenderHandler renderer, int xZoom, int yZoom);
}
