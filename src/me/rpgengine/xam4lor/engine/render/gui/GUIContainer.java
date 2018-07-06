package me.rpgengine.xam4lor.engine.render.gui;

import java.awt.Graphics;
import java.util.HashMap;

import me.rpgengine.xam4lor.engine.Game;
import me.rpgengine.xam4lor.engine.render.RenderHandler;
import me.rpgengine.xam4lor.engine.structure.GameObject;
import me.rpgengine.xam4lor.world.Tiles;

/**
 * Liste des GUIs
 */
public class GUIContainer implements GameObject {
	private HashMap<Integer, GUI> gui;
	private HashMap<Integer, Boolean> showGui;
	
	/**
	 * Liste des GUIs
	 * @param game
	 * 	Instance de jeu
	 * @param tileSet
	 * 	Liste des tiles
	 */
	public GUIContainer(Game game, Tiles tileSet) {
		this.gui = new HashMap<Integer, GUI>();
		this.showGui = new HashMap<Integer, Boolean>();
		
		this.instantiateGUIList(game, tileSet);
	}
	
	
	
	@Override
	public void update(Game game) {
		for (int i = 0; i < this.gui.size(); i++)
			if(this.showGui.get(i))
				this.updateGUI(i, game);
	}


	@Override
	public void render(RenderHandler renderer, int xZoom, int yZoom) {
		for (int i = 0; i < this.gui.size(); i++)
			if(this.showGui.get(i))
				this.renderGUI(i, renderer, xZoom, yZoom);
	}
	
	@Override
	public void postRender(Graphics graphics, int xZoom, int yZoom) {
		for (int i = 0; i < this.gui.size(); i++)
			if(this.showGui.get(i))
				this.postRenderGUI(i, graphics, xZoom, yZoom);
	}

	
	
	



	/**
	 * Instanciation de la liste des GUIs
	 * @param game
	 * 	Instance de jeu
	 * @param tileSet
	 * 	Liste des tiles
	 */
	private void instantiateGUIList(Game game, Tiles tileSet) {
		this.gui.put(this.gui.size(), new Dialog(this.gui.size(), game, tileSet, game.getWidth() / 2 - (7 * 16 * 3), game.getHeight() * 6 / 9, 14, 3));
		
		for (int i = 0; i < this.gui.size(); i++)
			this.showGui.put(i, false);
	}
	
	
	
	/**
	 * Update d'une GUI
	 * @param guiID
	 * 	ID de la GUI
	 * @param game
	 * 	Session de jeu principale
	 */
	private void updateGUI(int guiID, Game game) {
		this.gui.get(guiID).update(game);
	}
	
	/**
	 * Render d'une GUI
	 * @param guiID
	 * 	ID de la GUI
	 * @param renderer
	 * 	Renderer principal
	 * @param xZoom
	 * 	Zoom en X
	 * @param yZoom
	 * 	Zoom en Y
	 */
	private void renderGUI(int guiID, RenderHandler renderer, int xZoom, int yZoom) {
		this.gui.get(guiID).render(renderer, xZoom, yZoom);
	}
	
	/**
	 * Render d'une GUI
	 * @param guiID
	 * 	ID de la GUI
	 * @param graphics
	 * 	Graphics de jeu
	 * @param xZoom
	 * 	Zoom en X
	 * @param yZoom
	 * 	Zoom en Y
	 */
	private void postRenderGUI(int guiID, Graphics graphics, int xZoom, int yZoom) {
		this.gui.get(guiID).postRender(graphics, xZoom, yZoom);
	}
	
	
	
	/**
	 * Affichage d'une GUI ou non
	 * @param guiID
	 * 	ID de la GUI
	 * @param show
	 * 	true : affichage de la GUI
	 */
	public void showGUI(int guiID, boolean show) {
		this.gui.get(guiID).setShow(show);
	}

	/**
	 * @return le hashmap showGUI
	 */
	public HashMap<Integer, Boolean> getShowGUI() {
		return this.showGui;
	}


	/**
	 * Affichage d'un message
	 * @param string
	 * 	Messages à afficher (chaque ligne séparée par un '\n')
	 */
	public void displayMessage(String[] string) {
		((Dialog) this.gui.get(0)).displayText(string);
	}
}
