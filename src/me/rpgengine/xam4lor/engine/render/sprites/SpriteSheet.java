package me.rpgengine.xam4lor.engine.render.sprites;

import java.awt.image.BufferedImage;

/**
 * SpriteSheet
 */
public class SpriteSheet {
	/**
	 * Taille en X de la Sprite
	 */
	public final int SIZEX;
	
	/**
	 * Taille en Y de la Sprite
	 */
	public final int SIZEY;
	
	private int[] pixels;
	private BufferedImage image;
	private Sprite[] loadedSprites = null;
	private boolean spritesLoaded = false;

	private int spriteSizeX;

	
	/**
	 * SpriteSheet
	 * @param sheetImage
	 * 	Image de la spriteSheet
	 */
	public SpriteSheet(BufferedImage sheetImage) {
		image = sheetImage;
		SIZEX = sheetImage.getWidth();
		SIZEY = sheetImage.getHeight();

		pixels = new int[SIZEX*SIZEY];
		pixels = sheetImage.getRGB(0, 0, SIZEX, SIZEY, pixels, 0, SIZEX);
	}

	/**
	 * Chargement des sprites
	 * @param spriteSizeX
	 * 	Taille des sprites en X
	 * @param spriteSizeY
	 * 	Taille des sprites en Y
	 */
	public void loadSprites(int spriteSizeX, int spriteSizeY){
		this.spriteSizeX = spriteSizeX;
		loadedSprites = new Sprite[(SIZEX / spriteSizeX) * (SIZEY / spriteSizeY)];

		int spriteID = 0;
		for(int y = 0; y < SIZEY; y += spriteSizeY) {
			for(int x = 0; x < SIZEX; x += spriteSizeX) {
				loadedSprites[spriteID] = new Sprite(this, x, y, spriteSizeX, spriteSizeY);
				spriteID++;
			}
		}

		spritesLoaded = true;
	}

	/**
	 * Retourne une sprite
	 * @param x
	 * 	Position en X de la sprite
	 * @param y
	 * 	Position en Y de la sprite
	 * @return la sprite
	 */
	public Sprite getSprite(int x, int y) {
		if(spritesLoaded) {
			int spriteID = x + y * (SIZEX / spriteSizeX);

			if(spriteID < loadedSprites.length) 
				return loadedSprites[spriteID];
			else
				System.out.println("SpriteID of " + spriteID + " is out of the range with a length of " + loadedSprites.length + ".");
		}
		else
			System.out.println("SpriteSheet could not get a sprite with no loaded sprites.");

		return null;
	}

	
	/**
	 * @return les sprites chargées
	 */
	public Sprite[] getLoadedSprite() {
		return this.loadedSprites;
	}
	
	/**
	 * @return les pixels
	 */
	public int[] getPixels() {
		return this.pixels;
	}
	
	/**
	 * @return l'image de la SpriteSheet
	 */
	public BufferedImage getImage() {
		return this.image;
	}

}