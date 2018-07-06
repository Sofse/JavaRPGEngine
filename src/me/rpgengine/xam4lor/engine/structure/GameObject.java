package me.rpgengine.xam4lor.engine.structure;

import java.awt.Graphics;

import me.rpgengine.xam4lor.engine.Game;
import me.rpgengine.xam4lor.engine.render.RenderHandler;

/**
 * Constructeur d'un gameObject
 */
public interface GameObject {
	/**
	 * Appel�e autant de fois que de FPS par seconde
	 * @param game
	 * 	Classe du jeu
	 */
	public void update(Game game);
	
	/**
	 * Appel� autant que possible 
	 * @param renderer
	 * 	Renderer de la fen�tre
	 * @param xZoom
	 * 	Zoom en X
	 * @param yZoom
	 * 	Zoom en Y
	 */
	public void render(RenderHandler renderer, int xZoom, int yZoom);
	
	/**
	 * Appel� apr�s le render
	 * @param graphics
	 * 	Graphics de la fen�tre
	 * @param xZoom
	 * 	Zoom en X
	 * @param yZoom
	 * 	Zoom en Y
	 */
	public void postRender(Graphics graphics, int xZoom, int yZoom);
}
