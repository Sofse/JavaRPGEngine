package me.rpgengine.xam4lor.engine.render;

import java.awt.Graphics;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import me.rpgengine.xam4lor.engine.Game;
import me.rpgengine.xam4lor.engine.render.sprites.Sprite;
import me.rpgengine.xam4lor.engine.structure.Rectangle;

/**
 * Rendu de la fenêtre
 * 
 * <br><br>Valeurs de zIndex dans les fonctions de rendu :
 * <br>-        zIndex > 0      en général
 * <br>-        zIndex = 10     pour les tiles de background
 * <br>-        zIndex = 15     pour les tiles au dessus du background
 * <br>- 5000 > zIndex > 1000   pour toutes les entités
 * <br>-        zIndex = 5010   pour tous les rectangles des entités de debug
 * <br>-        zIndex = 5015   pour toutes les tiles au dessus des entités
 * <br>-        zIndex = 6100   pour tous les graphiques de GUI ou au dessus de tout
 */
public class RenderHandler {
	private BufferedImage view;
	private Rectangle camera;
	private int[] pixels;
	private int maxScreenWidth, maxScreenHeight;
	private int[] pixelsZIndex;

	/**
	 * Rendu de la fenêtre
	 * @param width
	 * 	Largeur de la fenêtre
	 * @param height
	 * 	Hauteur de la fenêtre
	 */
	public RenderHandler(int width, int height) {
		GraphicsDevice[] graphicsDevices = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices();

		for(int i = 0; i < graphicsDevices.length; i++) {
			if(maxScreenWidth < graphicsDevices[i].getDisplayMode().getWidth())
				maxScreenWidth = graphicsDevices[i].getDisplayMode().getWidth();

			if(maxScreenHeight < graphicsDevices[i].getDisplayMode().getHeight())
				maxScreenHeight = graphicsDevices[i].getDisplayMode().getHeight();
		}
		

		view   = new BufferedImage(maxScreenWidth, maxScreenHeight, BufferedImage.TYPE_INT_RGB);
		camera = new Rectangle(0, 0, width, height);
		
		pixels 		 = ((DataBufferInt) view.getRaster().getDataBuffer()).getData();
		pixelsZIndex = new int[pixels.length];
		
		for (int i = 0; i < pixelsZIndex.length; i++) {
			pixelsZIndex[i] = 0;
		}
	}

	/**
	 * Render le tableau de pixels à l'écran 
	 * @param graphics
	 * 	Graphics de rendu de la fenêtre
	 */
	public void render(Graphics graphics) {
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
	 * @param zIndex
	 * 	Index de layer ({@link RenderHandler})
	 */
	public void renderArray(int[] renderPixels, int renderWidth, int renderHeight, int xPosition, int yPosition, int xZoom, int yZoom, int zIndex) {
		for(int y = 0; y < renderHeight; y++)
			for(int x = 0; x < renderWidth; x++)
				for(int yZoomPosition = 0; yZoomPosition < yZoom; yZoomPosition++)
					for(int xZoomPosition = 0; xZoomPosition < xZoom; xZoomPosition++)
						setPixel(renderPixels[x + y * renderWidth], (x * xZoom) + xPosition + xZoomPosition, ((y * yZoom) + yPosition + yZoomPosition), zIndex);
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
	 * @param zIndex
	 * 	Index de layer ({@link RenderHandler})
	 */
	public void renderImage(BufferedImage image, int xPosition, int yPosition, int xZoom, int yZoom, int zIndex) {
		int[] imagePixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
		this.renderArray(imagePixels, image.getWidth(), image.getHeight(), xPosition, yPosition, xZoom, yZoom, zIndex);
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
	 * @param zIndex
	 * 	Index de layer ({@link RenderHandler})
	 */
	public void renderSprite(Sprite sprite, int xPosition, int yPosition, int xZoom, int yZoom, int zIndex) {
		this.renderArray(sprite.getPixels(), sprite.getWidth(), sprite.getHeight(), xPosition, yPosition, xZoom, yZoom, zIndex);
	}

	/**
	 * DEBUG : Mise à jour du tableau de pixels par un rectangle
	 * @param rectangle
	 * 	Rectangle à afficher
	 * @param xZoom
	 * 	Zoom en X
	 * @param yZoom
	 * 	Zoom en Y
	 * @param zIndex
	 * 	Index de layer ({@link RenderHandler})
	 */
	public void renderRectangle(Rectangle rectangle, int xZoom, int yZoom, int zIndex) {
		int[] rectanglePixels = rectangle.getPixels();
		if(rectanglePixels != null)
			this.renderArray(rectanglePixels, rectangle.w, rectangle.h, rectangle.x, rectangle.y, xZoom, yZoom, zIndex);	
	}

	/**
	 * Mise à jour du pixel s'il est contenu dans la fenêtre
	 * @param pixel
	 * 	Pixel à afficher
	 * @param x
	 * 	Position en x
	 * @param y
	 * 	Position en y
	 * @param zIndex
	 * 	Index de layer ({@link RenderHandler})
	 */
	private void setPixel(int pixel, int x, int y, int zIndex) {
		if(x >= this.camera.x && y >= this.camera.y && x <= this.camera.x + this.camera.w && y <= this.camera.y + this.camera.h) {
			int pixelIndex = (x - this.camera.x) + (y - this.camera.y) * this.view.getWidth();
			if(this.pixels.length > pixelIndex && pixel != Game.alpha && zIndex >= this.pixelsZIndex[pixelIndex]) {
				this.pixelsZIndex[pixelIndex] = zIndex;
				this.pixels      [pixelIndex] = pixel;
			}
		}
	}

	/**
	 * @return la largeur maximum de l'écran
	 */
	public int getMaxWidth() {
		return maxScreenWidth;
	}

	/**
	 * @return la hauteur maximum de l'écran
	 */
	public int getMaxHeight() {
		return maxScreenHeight;
	}
	
	/**
	 * @return la caméra de render
	 */
	public Rectangle getCamera() {
		return this.camera;
	}
	
	
	
	

	/**
	 * Vide les pixels
	 */
	public void clear() {
		for(int i = 0; i < pixels.length; i++) {
			this.pixels[i] = 0;
			this.pixelsZIndex[i] = 0;
		}
	}
}