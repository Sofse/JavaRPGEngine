package me.rpgengine.xam4lor.entities;

import me.rpgengine.xam4lor.engine.Game;
import me.rpgengine.xam4lor.engine.render.RenderHandler;
import me.rpgengine.xam4lor.engine.render.sprites.AnimatedSprite;
import me.rpgengine.xam4lor.engine.structure.GameObject;
import me.rpgengine.xam4lor.engine.structure.Rectangle;
import me.rpgengine.xam4lor.world.Tiles;

/**
 * Classe d'une entit�
 */
public abstract class Entity implements GameObject {
	/**
	 * UUID unique des entit�s
	 */
	private static int UUID = 0;
	
	/**
	 * Nom de l'entit�
	 */
	private String name;
	
	
	/**
	 * true : affichage du rectangle de collision pour le debug
	 */
	protected boolean showRectangle;
	
	/**
	 * Sprite d'animation de l'entit�
	 */
	protected AnimatedSprite animatedSprite;
	
	/**
	 * Rectangle de l'entit�
	 */
	protected Rectangle entityRectangle;
	
	
	
	
	/**
	 * Cr�ation du d'une entit�
	 * @param name
	 * 	Nom de l'entit�
	 * @param sprite
	 * 	Liste des sprites de l'entit�
	 * @param xInit
	 * 	Position initiale en X
	 * @param yInit
	 * 	Position initale en Y
	 * @param width
	 * 	Largeur de l'entit�
	 * @param height
	 * 	Hauteur de l'entit�
	 */
	public Entity(String name, AnimatedSprite sprite, int xInit, int yInit, int width, int height) {
		this.name = UUID + "_" + name;
		Entity.UUID++;
		
		this.animatedSprite = sprite;
		this.showRectangle = false;
		
		this.entityRectangle = new Rectangle(50 * xInit, 50 * yInit, width, height);
		this.entityRectangle.generateGraphics(2, 0xFF00FF90);
	}
	
	
	
	@Override
	public void render(RenderHandler renderer, int xZoom, int yZoom) {
		if(this.animatedSprite == null || this.showRectangle)
			renderer.renderRectangle(this.entityRectangle, xZoom, yZoom, 5010);
		else
			renderer.renderSprite(this.animatedSprite, this.entityRectangle.x, this.entityRectangle.y, xZoom, yZoom, 7500 + this.entityRectangle.y / Tiles.TILE_SIZE / yZoom);
	}
	
	
	/**
	 * Modification de la position de l'entit�
	 * @param game
	 * 	Instance du jeu
	 * @param x
	 * 	Nouvelle position en X
	 * @param y
	 * 	Nouvelle position en Y
	 */
	public void setPosition(Game game, int x, int y) {
		this.entityRectangle.x = x * game.xZoom * Tiles.TILE_SIZE;
		this.entityRectangle.y = y * game.yZoom * Tiles.TILE_SIZE;
	}
	
	
	/**
	 * @return le nom de l'entit�
	 */
	public String getName() {
		return this.name;
	}
	
	
	
	/**
	 * @param game
	 * 	Instance de jeu
	 * @return la tile en X de l'entit�
	 */
	public int getWorldPosX(Game game) {
		float realX = (float) this.entityRectangle.x / Tiles.TILE_SIZE / game.xZoom;
		int tileX = (int) (realX + (float) this.entityRectangle.w / Tiles.TILE_SIZE / 2.0f);
		
		if(realX * 2 < -1) {
			tileX -= 1;
		}
		
		return tileX;
	}
	
	/**
	 * @param game
	 * 	Instance de jeu
	 * @return la tile en X de l'entit�
	 */
	public int getWorldPosY(Game game) {
		float realY = (float) this.entityRectangle.y / Tiles.TILE_SIZE / game.yZoom;
		int tileY = (int) (realY + (float) this.entityRectangle.h / Tiles.TILE_SIZE / 2.0f);
		
		if(realY * 2 < -1) {
			tileY -= 1;
		}
		
		return tileY;
	}
}
