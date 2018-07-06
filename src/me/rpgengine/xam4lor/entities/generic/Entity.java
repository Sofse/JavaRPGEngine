package me.rpgengine.xam4lor.entities.generic;

import java.awt.Graphics;

import org.json.JSONObject;

import me.rpgengine.xam4lor.engine.Game;
import me.rpgengine.xam4lor.engine.render.RenderHandler;
import me.rpgengine.xam4lor.engine.render.sprites.AnimatedSprite;
import me.rpgengine.xam4lor.engine.structure.GameObject;
import me.rpgengine.xam4lor.engine.structure.Rectangle;
import me.rpgengine.xam4lor.world.Tiles;
import me.rpgengine.xam4lor.world.World.MappedTile;

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
	private final String name;
	
	
	/**
	 * 0 = RIGHT
	 * 1 = LEFT
	 * 2 = UP
	 * 3 = DOWN
	 */
	protected int direction;
	
	/**
	 * Derni�re direction de l'entit�
	 */
	protected int lastDirection;
	
	/**
	 * Nombre de frames par animation
	 */
	protected int framePerAnim;
	
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
	 * Options de l'entit�
	 */
	protected JSONObject options;
	
	/**
	 * Tile actuelle en X
	 */
	protected int currentX;
	
	/**
	 * Tile actuelle en Y
	 */
	protected int currentY;
	
	
	
	
	/**
	 * Entit�
	 * @param name
	 * 	Nom de l'entit�
	 * @param sprite
	 * 	Liste des sprites de l'entit� (chaque ligne = une animation respective aux valeurs de la constante direction)
	 * @param framePerAnim
	 * 	Nombre de frames par animation
	 * @param initDirection
	 * 	Direction initiale :  0 = RIGHT, 1 = LEFT, 2 = UP, 3 = DOWN
	 * @param xInit
	 * 	Position initiale en X
	 * @param yInit
	 * 	Position initale en Y
	 * @param width
	 * 	Largeur de l'entit�
	 * @param height
	 * 	Hauteur de l'entit�
	 * @param options
	 * 	Options de l'entit�
	 */
	public Entity(String name, AnimatedSprite sprite, int framePerAnim, int initDirection, int xInit, int yInit, int width, int height, JSONObject options) {
		this.name = UUID + "_" + name;
		Entity.UUID++;
		
		this.animatedSprite = sprite;
		this.showRectangle = false;
		this.framePerAnim = framePerAnim;
		this.direction = initDirection;
		this.options = options;
		this.lastDirection = -1;
		
		this.entityRectangle = new Rectangle(0, 0, width, height);
		this.entityRectangle.generateGraphics(1, 0xFF00FF90);
	}

	
	
	
	
	@Override
	public void update(Game game) {}
	
	@Override
	public void render(RenderHandler renderer, int xZoom, int yZoom) {
		if(this.animatedSprite == null || this.showRectangle)
			renderer.renderRectangle(this.entityRectangle, xZoom, yZoom, 5010, false);
		else
			renderer.renderSprite(this.animatedSprite, this.entityRectangle.x, this.entityRectangle.y - this.entityRectangle.h * yZoom, xZoom, yZoom, 7500 + this.entityRectangle.y / Tiles.TILE_SIZE / yZoom, false);
	}
	
	@Override
	public void postRender(Graphics graphics, int xZoom, int yZoom) {}
	
	
	
	
	/**
	 * Lors d'une collision avec l'entit� en instance
	 * @param game
	 * 	Instance de jeu
	 * @param entity
	 * 	Entit� qui collisionne
	 */
	public abstract void onCollideWithEntity(Game game, Entity entity);
	
	
	
	
	
	
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
		this.currentX = x;
		this.currentY = y;
	}
	
	
	/**
	 * @return le nom de l'entit�
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * @return la direction de l'entit�
	 */
	public int getDirection() {
		return this.direction;
	}
	
	
	
	
	
	/**
	 * Direction du joueur (appel�e depuis update) :
	 * 	-1 = IDLE
	 * 	0  = RIGHT
	 * 	1  = LEFT
	 * 	2  = UP
	 * 	3  = DOWN
	 * @param game
	 * 	Classe du jeu
	 * @return la direction actuelle du joueur (-1 par d�faut)
	 */
	protected int getMovingDirection(Game game) {
		return -1;
	}
	
	
	/**
	 * @param dir
	 * 	Nouvelle direction de l'entit�
	 */
	protected void setDirection(int dir) {
		this.direction = dir;
		this.lastDirection = dir;
		this.updateDirection();
	}
	
	
	/**
	 * Mise � jour de la texture de la direction
	 */
	protected void updateDirection() {
		if(this.animatedSprite != null) {
			this.animatedSprite.setAnimationRange(this.direction * this.framePerAnim, this.direction * this.framePerAnim + (this.framePerAnim - 1));
		}
	}
	
	/**
	 * @return le rectangle de l'entit�
	 */
	public Rectangle getEntityRectangle() {
		return entityRectangle;
	}
	
	/**
	 * @param game
	 * 	Instance du jeu
	 * @return la tile actuelle sous l'entit�
	 */
	public MappedTile getCurrentTile(Game game) {
		return game.getWorld().getTileAt(this.currentX, this.currentY);
	}
}
