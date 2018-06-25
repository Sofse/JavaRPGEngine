package me.rpgengine.xam4lor.entities;

import me.rpgengine.xam4lor.engine.Game;
import me.rpgengine.xam4lor.engine.render.RenderHandler;
import me.rpgengine.xam4lor.engine.render.sprites.AnimatedSprite;
import me.rpgengine.xam4lor.engine.structure.GameObject;
import me.rpgengine.xam4lor.engine.structure.Rectangle;
import me.rpgengine.xam4lor.world.Tiles;

/**
 * Classe d'une entité
 */
public abstract class Entity implements GameObject {
	/**
	 * UUID unique des entités
	 */
	private static int UUID = 0;
	
	/**
	 * Nom de l'entité
	 */
	private String name;
	
	
	/**
	 * true : affichage du rectangle de collision pour le debug
	 */
	protected boolean showRectangle;
	
	/**
	 * Sprite d'animation de l'entité
	 */
	protected AnimatedSprite animatedSprite;
	
	/**
	 * Rectangle de l'entité
	 */
	protected Rectangle entityRectangle;
	
	
	
	
	/**
	 * Création du d'une entité
	 * @param name
	 * 	Nom de l'entité
	 * @param sprite
	 * 	Liste des sprites de l'entité
	 * @param xInit
	 * 	Position initiale en X
	 * @param yInit
	 * 	Position initale en Y
	 * @param width
	 * 	Largeur de l'entité
	 * @param height
	 * 	Hauteur de l'entité
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
	 * Modification de la position de l'entité
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
	 * @return le nom de l'entité
	 */
	public String getName() {
		return this.name;
	}
	
	
	
	/**
	 * @param game
	 * 	Instance de jeu
	 * @return la tile en X de l'entité
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
	 * @return la tile en X de l'entité
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
