package me.rpgengine.xam4lor.entities.movingEntities;

import me.rpgengine.xam4lor.engine.Game;
import me.rpgengine.xam4lor.engine.render.sprites.AnimatedSprite;
import me.rpgengine.xam4lor.entities.Entity;
import me.rpgengine.xam4lor.world.Tiles;
import me.rpgengine.xam4lor.world.World.MappedTile;

/**
 * Entité se déplaçant
 */
public abstract class MovingEntity extends Entity {
	/**
	 * 0 = RIGHT
	 * 1 = LEFT
	 * 2 = UP
	 * 3 = DOWN
	 */
	private int direction;
	
	/**
	 * Nombre de frames par animation
	 */
	private int framePerAnim;
	
	/**
	 * Vitesse de déplacement de l'entité
	 */
	protected int speed;
	
	
	
	/**
	 * Entité se déplaçant
	 * @param name
	 * 	Nom de l'entité
	 * @param sprite
	 * 	Liste des sprites de l'entité (chaque ligne = une animation respective aux valeurs de la constante direction)
	 * @param framePerAnim
	 * 	Nombre de frames par animation
	 * @param initDirection
	 * 	Direction initiale :  0 = RIGHT, 1 = LEFT, 2 = UP, 3 = DOWN
	 * @param xInit
	 * 	Position initiale en X
	 * @param yInit
	 * 	Position initale en Y
	 * @param width
	 * 	Largeur de l'entité
	 * @param height
	 * 	Hauteur de l'entité
	 */
	public MovingEntity(String name, AnimatedSprite sprite, int framePerAnim, int initDirection, int xInit, int yInit, int width, int height) {
		super(name, sprite, xInit, yInit, width, height);
		
		this.framePerAnim = framePerAnim;
		this.direction = initDirection;
		this.updateDirection();
		
		this.speed = 10;
	}
	
	
	@Override
	public void update(Game game) {
		boolean didMove = false;
		int newDir = this.getDirection();

		if(this.getMovingDirection(game) == 0 && !this.colideWith(game, 0)) {
			this.entityRectangle.x += this.speed;
			newDir = 0;
			didMove = true;
		}
		
		if(this.getMovingDirection(game) == 1 && !this.colideWith(game, 1)) {
			this.entityRectangle.x -= this.speed;
			newDir = 1;
			didMove = true;
		}
		
		if(this.getMovingDirection(game) == 2 && !this.colideWith(game, 2)) {
			this.entityRectangle.y -= this.speed;
			newDir = 2;
			didMove = true;
		}
		
		if(this.getMovingDirection(game) == 3 && !this.colideWith(game, 3)) {
			this.entityRectangle.y += this.speed;
			newDir = 3;
			didMove = true;
		}
		
		if(newDir != this.getDirection())
			this.setDirection(newDir);
		
		
		if(didMove)
			this.animatedSprite.update(game);
		else
			this.animatedSprite.reset();
	}
	
	
	
	
	/**
	 * Check si l'entité collide avec une tile
	 * @param dir
	 * 	Direction de l'entité
	 * @return true s'il y a collision
	 */
	private boolean colideWith(Game game, int dir) {
		float posFloatX = this.getWorldPosRelativeX(game);
		float posFloatY = this.getWorldPosRelativeY(game);
		float sizeX = (float) this.entityRectangle.w / Tiles.TILE_SIZE / 2.0f;
		float sizeY = (float) this.entityRectangle.h / Tiles.TILE_SIZE / 2.0f;
		
		switch (dir) {
			case 0:
				if(
					this.isTileSolid(
						game,
						posFloatX + sizeX,
						posFloatY + sizeY
					)
					||
					this.isTileSolid(
						game,
						posFloatX + sizeX,
						posFloatY
					)
					||
					this.isTileSolid(
						game,
						posFloatX + sizeX,
						posFloatY - sizeY
					)
				) {
					return true;
				}
				
				return false;
				
			case 1:
				if(
					this.isTileSolid(
						game,
						posFloatX - sizeX,
						posFloatY + sizeY
					)
					||
					this.isTileSolid(
						game,
						posFloatX - sizeX,
						posFloatY
					)
					||
					this.isTileSolid(
						game,
						posFloatX - sizeX,
						posFloatY - sizeY
					)
				) {
					return true;
				}
				
				return false;
					
					
			case 2:
				if(
					this.isTileSolid(
						game,
						posFloatX + sizeX,
						posFloatY - sizeY
					)
					||
					this.isTileSolid(
						game,
						posFloatX,
						posFloatY - sizeY
					)
					||
					this.isTileSolid(
						game,
						posFloatX - sizeX,
						posFloatY - sizeY
					)
				) {
					return true;
				}
					
				return false;
					
			case 3:
				if(
					this.isTileSolid(
						game,
						posFloatX + sizeX,
						posFloatY + sizeY
					)
					||
					this.isTileSolid(
						game,
						posFloatX,
						posFloatY + sizeY
					)
					||
					this.isTileSolid(
						game,
						posFloatX - sizeX,
						posFloatY + sizeY
					)
				) {
					return true;
				}
					
				return false;
		}
		
		return false;
	}
	
	
	/**
	 * Attention : tronque la position en X et Y
	 * @param game
	 * 	Instance du jeu
	 * @param posFloatX
	 * 	Position en X
	 * @param posFloatY
	 * 	Position en Y
	 * @return true si la tile est solide
	 */
	private boolean isTileSolid(Game game, float posFloatX, float posFloatY) {
		MappedTile tile = game.getWorld().getTileAt((int) posFloatX, (int) posFloatY);
		
		int tileID = -1;
		if(tile == null)
			tileID = game.getWorld().getDefaultTileID();
		else
			tileID = tile.getID();
		
		if(game.getTiles().getTile(tileID).isSolid())
			return true;
		else
			return false;
	}
	
	
	
	
	


	/**
	 * Direction du joueur (appelée depuis update) :
	 * 	-1 = IDLE
	 * 	0  = RIGHT
	 * 	1  = LEFT
	 * 	2  = UP
	 * 	3  = DOWN
	 * @param game
	 * 	Classe du jeu
	 * @return la direction actuelle du joueur (-1 par défaut)
	 */
	protected int getMovingDirection(Game game) {
		return -1;
	}
	
	
	/**
	 * @param dir
	 * 	Nouvelle direction de l'entité
	 */
	protected void setDirection(int dir) {
		this.direction = dir;
		this.updateDirection();
	}
	
	/**
	 * Retourne la direction de l'entité
	 */
	protected int getDirection() {
		return this.direction;
	}
	
	
	/**
	 * Mise à jour de la texture de la direction
	 */
	private void updateDirection() {
		if(this.animatedSprite != null) {
			this.animatedSprite.setAnimationRange(this.direction * this.framePerAnim, this.direction * this.framePerAnim + (this.framePerAnim - 1));
		}
	}
}
