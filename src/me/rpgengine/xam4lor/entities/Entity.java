package me.rpgengine.xam4lor.entities;

import me.rpgengine.xam4lor.engine.render.RenderHandler;
import me.rpgengine.xam4lor.engine.render.sprites.AnimatedSprite;
import me.rpgengine.xam4lor.engine.structure.GameObject;
import me.rpgengine.xam4lor.engine.structure.Rectangle;

/**
 * Classe d'une entit�
 */
public abstract class Entity implements GameObject {
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
	public Entity(AnimatedSprite sprite, int xInit, int yInit, int width, int height) {
		this.animatedSprite = sprite;
		
		this.entityRectangle = new Rectangle(xInit, yInit, width, height);
		this.entityRectangle.generateGraphics(3, 0xFF00FF90);
	}
	
	
	
	@Override
	public void render(RenderHandler renderer, int xZoom, int yZoom) {
		if(this.animatedSprite != null)
			renderer.renderSprite(this.animatedSprite, this.entityRectangle.x, this.entityRectangle.y, xZoom, yZoom);
		else
			renderer.renderRectangle(this.entityRectangle, xZoom, yZoom);
	}
}
