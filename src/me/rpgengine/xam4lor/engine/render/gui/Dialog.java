package me.rpgengine.xam4lor.engine.render.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;

import me.rpgengine.xam4lor.engine.Game;
import me.rpgengine.xam4lor.engine.render.RenderHandler;
import me.rpgengine.xam4lor.world.Tiles;

/**
 * Classe d'un dialogue
 */
public class Dialog extends GUI {
	private int initX;
	private int initY;
	private int sizeX;
	private int sizeY;
	
	private String[] textList;
	private int currentTextID;
	private String displayingText;
	private boolean isDisplayingText;
	
	private long lastTime;
	private boolean arrowShowState;
	
	
	
	/**
	 * Classe d'un dialogue
	 * @param id
	 * 	ID dans le tileContainer
	 * @param game
	 * 	Instance de jeu
	 * @param tileSet
	 * 	Liste des tiles
	 * @param initX
	 * 	Position initiale en X
	 * @param initY
	 * 	Position initiale en Y
	 * @param sizeX
	 * 	Nombre de tiles en X
	 * @param sizeY
	 * 	Nombre de tiles en Y
	 */
	public Dialog(int id, Game game, Tiles tileSet, int initX, int initY, int sizeX, int sizeY) {
		super(id, game, tileSet);
		
		this.initX = initX;
		this.initY = initY;
		this.sizeX = sizeX;
		this.sizeY = sizeY;
		
		// environ 25 chars par ligne
		this.textList = new String[] {"undefined"};
		this.currentTextID = 0;
		this.displayingText = "";
		this.isDisplayingText = false;
		this.arrowShowState = false;
		this.lastTime = System.currentTimeMillis();
		
		// sprite
		int tileSizeX = Tiles.TILE_SIZE * game.xZoom;
		int tileSizeY = Tiles.TILE_SIZE * game.yZoom;
		
		for (int y = 0; y <= sizeY; y++) {
			for (int x = 0; x <= sizeX; x++) {
				if(x == 0) {
					if(y == 0)
						this.addSprite(tileSet.getTile(27).getSprite(), initX + x * tileSizeX, initY + y * tileSizeY);
					else if(y == sizeY)
						this.addSprite(tileSet.getTile(29).getSprite(), initX + x * tileSizeX, initY + y * tileSizeY);
					else
						this.addSprite(tileSet.getTile(28).getSprite(), initX + x * tileSizeX, initY + y * tileSizeY);
				}
				else if(y == 0) {
					if(x == sizeX)
						this.addSprite(tileSet.getTile(33).getSprite(), initX + x * tileSizeX, initY + y * tileSizeY);
					else
						this.addSprite(tileSet.getTile(30).getSprite(), initX + x * tileSizeX, initY + y * tileSizeY);
				}
				else if(x == sizeX) {
					if(y == sizeY)
						this.addSprite(tileSet.getTile(35).getSprite(), initX + x * tileSizeX, initY + y * tileSizeY);
					else
						this.addSprite(tileSet.getTile(34).getSprite(), initX + x * tileSizeX, initY + y * tileSizeY);
				}
				else if(y == sizeY) {
						this.addSprite(tileSet.getTile(32).getSprite(), initX + x * tileSizeX, initY + y * tileSizeY);
				}
				else {
						this.addSprite(tileSet.getTile(31).getSprite(), initX + x * tileSizeX, initY + y * tileSizeY);
				}
			}
		}
	}
	
	
	
	@Override
	public void update(Game game) {
		super.update(game);
		
		if(game.getKeyListener().isKeyPressed(KeyEvent.VK_ENTER)) {
			if(!this.isDisplayingText()) {
				if(this.currentTextID < this.textList.length - 1) {
					if(this.displayingText.length() == this.textList[this.currentTextID].length()) {
						this.currentTextID++;
						this.arrowShowState = false;
						this.displayText();
					}
				}
				else {
					this.setShow(false);
				}
			}
		}
		
		if(this.displayingText.length() == this.textList[this.currentTextID].length()) {
			long currentTime = System.currentTimeMillis();
			
			if(currentTime - this.lastTime > 400) {
				if(this.arrowShowState)
					this.arrowShowState = false;
				else
					this.arrowShowState = true;
				
				this.lastTime = currentTime;
			}
		}
	}
	
	@Override
	public void render(RenderHandler renderer, int xZoom, int yZoom) {
		super.render(renderer, xZoom, yZoom);		
		
		if(this.arrowShowState) {
			this.game.getRenderer().renderSprite(tileSet.getTile(26).getSprite(), this.initX + this.sizeX * Tiles.TILE_SIZE * xZoom, this.initY + this.sizeY * Tiles.TILE_SIZE * yZoom, 1, 1, 100000, true);
		}
	}
	
	
	@Override
	public void postRender(Graphics graphics, int xZoom, int yZoom) {
		super.postRender(graphics, xZoom, yZoom);
		
		graphics.setFont(this.game.getMainFont());
		graphics.setColor(Color.BLACK);
		
		int addY = 0;
		for (String s : this.displayingText.split("\n")) {
			graphics.drawString(s, this.initX + Tiles.TILE_SIZE * xZoom * 2 / 3, addY + this.initY + Tiles.TILE_SIZE * yZoom);
			addY += graphics.getFontMetrics().getHeight();
		}
		
		
		if(!(this.displayingText.length() > this.textList[this.currentTextID].length() - 1))
			this.displayingText += this.textList[this.currentTextID].charAt(this.displayingText.length());
		else
			this.isDisplayingText = false;
	}
	
	
	
	
	
	/**
	 * @param text
	 * 	Nouvelle valeur du texte
	 */
	public void setText(String[] text) {
		this.textList = text;
	}
	
	/**
	 * @param id
	 * 	ID du texte
	 * @param text
	 * 	Nouvelle valeur du texte
	 */
	public void setText(int id, String text) {
		this.textList[id] = text;
	}
	
	
	
	/**
	 * Affichage du text
	 */
	public void displayText() {
		this.displayingText = "";
		this.isDisplayingText = true;
	}
	
	/**
	 * Affichage du texte
	 * @param text
	 * 	Textes à afficher (chaque ligne séparée par un '\n')
	 */
	public void displayText(String[] text) {
		this.setText(text);
		this.setShow(true);
		this.displayText();
	}
	
	
	
	/**
	 * @return true si le text est en train de défiler
	 */
	public boolean isDisplayingText() {
		return this.isDisplayingText;
	}
	
	@Override
	public void setShow(boolean show) {
		super.setShow(show);
		
		if(show) {
			this.game.getWorld().setWorldUpdating(false);
		}
		else {
			this.currentTextID = 0;
			this.displayingText = "";
			this.isDisplayingText = false;
			this.arrowShowState = false;
			this.game.getWorld().setWorldUpdating(true);
		}
	}
}
