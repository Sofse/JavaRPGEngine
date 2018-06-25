package me.rpgengine.xam4lor.engine.render.sprites;

import java.awt.image.BufferedImage;

/**
 * Sprite
 */
public class Sprite {
	protected int width, height;
	protected int[] pixels;

	/**
	 * Sprite
	 * @param sheet
	 * 	Liste des Sprites
	 * @param startX
	 * 	Début de la sprite en X
	 * @param startY
	 *  Début de la sprite en Y
	 * @param width
	 * 	Largeur de la sprite
	 * @param height
	 * 	Hauteur de la sprite
	 */
	public Sprite(SpriteSheet sheet, int startX, int startY, int width, int height)  {
		this.width = width;
		this.height = height;

		pixels = new int[width*height];
		sheet.getImage().getRGB(startX, startY, width, height, pixels, 0, width);
	}
	
	/**
	 * Sprite
	 * @param image
	 * 	Image de texture
	 */
	public Sprite(BufferedImage image)  {
		width = image.getWidth();
		height = image.getHeight();

		pixels = new int[width*height];
		image.getRGB(0, 0, width, height, pixels, 0, width);
	}
	
	/**
	 * Sprite
	 */
	public Sprite() {}


	/**
	 * @return la largeur de la sprite
	 */
	public int getWidth() {
		return this.width;
	}
	
	/**
	 * @return la hauteur de la sprite
	 */
	public int getHeight() {
		return this.height;
	}
	
	/**
	 * @return les pixels de la sprite
	 */
	public int[] getPixels() {
		return this.pixels;
	}
}