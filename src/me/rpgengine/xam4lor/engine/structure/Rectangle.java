package me.rpgengine.xam4lor.engine.structure;

import me.rpgengine.xam4lor.engine.Game;

/**
 * Classe d'un rectangle
 */
public class Rectangle {
	/**
	 * Position en x
	 */
	public int x;
	
	/**
	 * Position en y
	 */
	public int y;
	
	/**
	 * Largeur
	 */
	public int w;
	
	/**
	 * Hauteur
	 */
	public int h;
	
	private int[] pixels;

	/**
	 * Classe d'un rectangle
	 * @param x
	 * 	Position en x
	 * @param y
	 * 	Position en y
	 * @param w
	 * 	Largeur
	 * @param h
	 * 	Hauteur
	 */
	public Rectangle(int x, int y, int w, int h) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
	}

	/**
	 * Classe d'un rectangle
	 */
	public Rectangle() {
		this(0,0,0,0);
	}

	/**
	 * DEBUG : affiche un rectangle plein sur l'écran et génère le tableau pixels[]
	 * @param color
	 * 	Couleur d'affichage
	 */
	public void generateGraphics(int color) {
		pixels = new int[w*h];
		for(int y = 0; y < h; y++)
			for(int x = 0; x < w; x++)
				pixels[x + y * w] = color;
	}

	/**
	 * DEBUG : affiche les bords d'un rectangle sur l'écran et génère le tableau pixels[]
	 * @param borderWidth
	 * 	Largeur de la bordure
	 * @param color
	 * 	Couleur d'affichage
	 */
	public void generateGraphics(int borderWidth, int color) {
		pixels = new int[w*h];
		
		for(int i = 0; i < pixels.length; i++)
			pixels[i] = Game.alpha;

		for(int y = 0; y < borderWidth; y++)
			for(int x = 0; x < w; x++)
				pixels[x + y * w] = color;

		for(int y = 0; y < h; y++)
			for(int x = 0; x < borderWidth; x++)
				pixels[x + y * w] = color;

		for(int y = 0; y < h; y++)
			for(int x = w - borderWidth; x < w; x++)
				pixels[x + y * w] = color;

		for(int y = h - borderWidth; y < h; y++)
			for(int x = 0; x < w; x++)
				pixels[x + y * w] = color;
		
	}

	/**
	 * @return les pixels du rectangle
	 */
	public int[] getPixels() {
		if(pixels != null)
			return pixels;
		else
			System.out.println("Attempted to retrive pixels from a Rectangle without generated graphics.");

		return null;
	}
}