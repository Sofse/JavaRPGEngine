package me.rpgengine.xam4lor.engine.render.sprites;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import me.rpgengine.xam4lor.engine.Game;
import me.rpgengine.xam4lor.engine.render.RenderHandler;
import me.rpgengine.xam4lor.engine.render.sprites.Sprite;
import me.rpgengine.xam4lor.engine.render.sprites.SpriteSheet;
import me.rpgengine.xam4lor.engine.structure.GameObject;
import me.rpgengine.xam4lor.engine.structure.Rectangle;

/**
 * Sprite animée
 */
public class AnimatedSprite extends Sprite implements GameObject {
	private Sprite[] sprites;
	private int currentSprite;
	private int speed;
	private int counter;
	
	private int startSprite = 0;
	private int endSprite;
	
	
	
	/**
	 * Sprite animée
	 * @param sheet
	 * 	Liste des sheets
	 * @param positions
	 * 	Position de chaque sprite
	 * @param speed
	 * 	Nombre de frames entre chaque changement de sprite
	 */
	public AnimatedSprite(SpriteSheet sheet, Rectangle[] positions, int speed) {
		this.sprites = new Sprite[positions.length];
		this.speed = speed;
		this.endSprite = positions.length - 1;
		
		for (int i = 0; i < positions.length; i++) {
			this.sprites[i] = new Sprite(
					sheet,
					(int) positions[i].x,
					(int) positions[i].y,
					(int) positions[i].w,
					(int) positions[i].h
			);
		}
	}
	
	/**
	 * Sprite animée
	 * @param sheet
	 * 	Liste des sheets
	 * @param speed
	 * 	Nombre de frames entre chaque changement de sprite
	 */
	public AnimatedSprite(SpriteSheet sheet, int speed) {
		this.currentSprite = 0;
		this.sprites = sheet.getLoadedSprite();
		this.speed = speed;
		this.counter = 0;
		this.endSprite = sprites.length - 1;
	}
	
	/**
	 * Création d'une sprite animée
	 * @param images
	 * 	Liste des images de la sprite
	 * @param speed
	 * 	Nombre de frames entre chaque changement de sprite
	 */
	public AnimatedSprite(BufferedImage[] images, int speed) {
		this.sprites = new Sprite[images.length];
		this.speed = speed;
		this.startSprite = images.length - 1;
		
		for (int i = 0; i < images.length; i++) {
			this.sprites[i] = new Sprite(images[i]);
		}
	}

	

	@Override
	public void update(Game game) {
		this.counter++;
		
		if(this.counter >= this.speed) {
			this.counter = 0;
			this.incrementSprite();
		}
	}
	
	@Override
	public void render(RenderHandler renderer, int xZoom, int yZoom) {}
	
	@Override
	public void postRender(Graphics graphics, int xZoom, int yZoom) {}
	
	
	
	/**
	 * Modification des animations actuelles
	 * @param startSprite
	 * 	Sprite de début
	 * @param endSprite
	 * 	Sprite de fin
	 */
	public void setAnimationRange(int startSprite, int endSprite) {
		this.startSprite = startSprite;
		this.endSprite = endSprite;
		
		this.reset();
	}
	
	/**
	 * Reset l'animation
	 */
	public void reset() {
		this.counter = 0;
		this.currentSprite = startSprite;
	}
	
	/**
	 * Passage à la prochaine sprite
	 */
	private void incrementSprite() {
		this.currentSprite++;
		
		if(this.currentSprite >= this.endSprite)
			this.currentSprite = this.startSprite;
	}

	
	
	
	@Override
	public int getWidth() {
		return this.sprites[this.currentSprite].getWidth();
	}
	
	@Override
	public int getHeight() {
		return this.sprites[this.currentSprite].getHeight();
	}
	
	@Override
	public int[] getPixels() {
		return this.sprites[this.currentSprite].getPixels();
	}
}