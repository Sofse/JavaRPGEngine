package me.rpgengine.xam4lor.engine.render;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import me.rpgengine.xam4lor.engine.Game;
import me.rpgengine.xam4lor.engine.render.sprites.Sprite;
import me.rpgengine.xam4lor.engine.structure.Rectangle;

/**
 * Rendu de la fenêtre
 */
public class RenderHandler {
	private BufferedImage view;
	private Rectangle camera;
	private int[] pixels;

	/**
	 * Rendu de la fenêtre
	 * @param width
	 * 	Largeur de la fenêtre
	 * @param height
	 * 	Hauteur de la fenêtre
	 */
	public RenderHandler(int width, int height) {
		//Create a BufferedImage that will represent our view.
		view = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

		camera = new Rectangle(0, 0, width, height);

		//Create an array for pixels
		pixels = ((DataBufferInt) view.getRaster().getDataBuffer()).getData();

	}

	/**
	 * Render le tableau de pixels à l'écran 
	 * @param graphics
	 * 	Graphics de rendu de la fenêtre
	 */
	public void render(Graphics graphics){
		graphics.drawImage(view, 0, 0, view.getWidth(), view.getHeight(), null);
	}
	
	
	
	/**
	 * Mise à jour du tableau de pixels par un nouveau tableau de pixels
	 * @param renderPixels
	 * 	Nouveaux pixels
	 * @param xPosition
	 * 	Position en x
	 * @param yPosition
	 * 	Position en y
	 * @param renderWidth
	 * 	Largeur de render
	 * @param renderHeight
	 * 	Hauteur de render
	 * @param xZoom
	 * 	Zoom en X
	 * @param yZoom
	 * 	Zoom en Y
	 */
	public void renderArray(int[] renderPixels, int renderWidth, int renderHeight, int xPosition, int yPosition, int xZoom, int yZoom) {
		for(int y = 0; y < renderHeight; y++)
			for(int x = 0; x < renderWidth; x++)
				for(int yZoomPosition = 0; yZoomPosition < yZoom; yZoomPosition++)
					for(int xZoomPosition = 0; xZoomPosition < xZoom; xZoomPosition++)
						setPixel(renderPixels[x + y * renderWidth], (x * xZoom) + xPosition + xZoomPosition, ((y * yZoom) + yPosition + yZoomPosition));
	}
	
	

	/**
	 * Mise à jour du tableau de pixels par une image
	 * @param image
	 * 	Image à render
	 * @param xPosition
	 * 	Padding en x
	 * @param yPosition
	 * 	Padding en y
	 * @param xZoom
	 * 	Valeur de zoom en x
	 * @param yZoom
	 * 	Valeur de zoom en y
	 */
	public void renderImage(BufferedImage image, int xPosition, int yPosition, int xZoom, int yZoom){
		int[] imagePixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
		renderArray(imagePixels, image.getWidth(), image.getHeight(), xPosition, yPosition, xZoom, yZoom);
	}

	/**
	 * Mise à jour du tableau de pixels par une sprite
	 * @param sprite
	 * 	Sprite à render
	 * @param xPosition
	 * 	Padding en x
	 * @param yPosition
	 * 	Padding en y
	 * @param xZoom
	 * 	Valeur de zoom en x
	 * @param yZoom
	 * 	Valeur de zoom en y
	 */
	public void renderSprite(Sprite sprite, int xPosition, int yPosition, int xZoom, int yZoom) {
		renderArray(sprite.getPixels(), sprite.getWidth(), sprite.getHeight(), xPosition, yPosition, xZoom, yZoom);
	}

	/**
	 * DEBUG : Mise à jour du tableau de pixels par un rectangle
	 * @param rectangle
	 * 	Rectangle à afficher
	 * @param xZoom
	 * 	Zoom en X
	 * @param yZoom
	 * 	Zoom en Y
	 */
	public void renderRectangle(Rectangle rectangle, int xZoom, int yZoom){
		int[] rectanglePixels = rectangle.getPixels();
		if(rectanglePixels != null)
			renderArray(rectanglePixels, rectangle.w, rectangle.h, rectangle.x, rectangle.y, xZoom, yZoom);	
	}

	/**
	 * Mise à jour du pixel s'il est contenu dans la fenêtre
	 * @param pixel
	 * 	Pixel à afficher
	 * @param x
	 * 	Position en x
	 * @param y
	 * 	Position en y
	 * @param fixed
	 * 	false : position relative à la caméra
	 */
	private void setPixel(int pixel, int x, int y) {
		if(x >= camera.x && y >= camera.y && x <= camera.x + camera.w && y <= camera.y + camera.h)
		{
			int pixelIndex = (x - camera.x) + (y - camera.y) * view.getWidth();
			if(pixels.length > pixelIndex && pixel != Game.alpha)
				pixels[pixelIndex] = pixel;
		}
	}

	/**
	 * @return la caméra de render
	 */
	public Rectangle getCamera() {
		return camera;
	}

	/**
	 * Vide les pixels
	 */
	public void clear() {
		for(int i = 0; i < pixels.length; i++)
			pixels[i] = 0;
	}

}