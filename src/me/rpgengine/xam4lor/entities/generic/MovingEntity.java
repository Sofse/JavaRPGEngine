package me.rpgengine.xam4lor.entities.generic;

import org.json.JSONException;
import org.json.JSONObject;

import me.rpgengine.xam4lor.engine.Game;
import me.rpgengine.xam4lor.engine.render.sprites.AnimatedSprite;
import me.rpgengine.xam4lor.world.Tiles;
import me.rpgengine.xam4lor.world.World.MappedTile;

/**
 * Entité se déplaçant
 */
public abstract class MovingEntity extends Entity {
	/**
	 * Vitesse de déplacement de l'entité
	 */
	protected int speed;
	
	protected boolean movedLastUpdate;
	protected boolean collisionLastUpdate;
	protected int targetX;
	protected int targetY;
	
	private int lastDir;
	private long lastDeplacementChanged;
	
	
	
	
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
	 * @param options
	 * 	Options de l'entité
	 */
	public MovingEntity(String name, AnimatedSprite sprite, int framePerAnim, int initDirection, int xInit, int yInit, int width, int height, JSONObject options) {
		super(name, sprite, framePerAnim, initDirection, xInit, yInit, width, height, options);
		
		this.targetX = 0;
		this.targetY = 0;
		this.movedLastUpdate = false;
		this.collisionLastUpdate = false;
		
		this.speed = 1;
		try {
			if(this.options != null)
				this.speed = this.options.getInt("speed");
		}
		catch(JSONException e) {}
	}
	
	
	@Override
	public void update(Game game) {
		int dir = this.getMovingDirection(game);
		boolean move = false;
		long now = System.currentTimeMillis();
		
		this.movedLastUpdate = false;
		this.collisionLastUpdate = false;
		
		// check des déplacements
		if(dir != -1 && (dir == lastDir || (dir != lastDir && now - lastDeplacementChanged > 150))) {
			switch(dir) {
				case 0:
					this.targetX = 1;
					this.targetY = 0;
					lastDir = 0;
					break;
					
				case 1:
					this.targetX = -1;
					this.targetY = 0;
					lastDir = 1;
					break;
					
				case 2:
					this.targetY = -1;
					this.targetX = 0;
					lastDir = 2;
					break;
					
				case 3:
					this.targetY = 1;
					this.targetX = 0;
					lastDir = 3;
					break;
					
				case -1:
					lastDir = -1;
			}
			
			lastDeplacementChanged = now;
		}
		
		
		// reinitialisation des sprites
		if(this.targetX == 0 && this.targetY == 0)
			this.animatedSprite.reset();
		
		
		// check si mouvement possible
		Entity entity = this.colideWithEntity(game, this.currentX + this.targetX, this.currentY + this.targetY);
		if(entity != null) {
			this.collisionLastUpdate = true;
			this.targetX = 0;
			this.targetY = 0;
			entity.onCollideWithEntity(game, this);
		}
		else {
			if(!this.isTileSolid(game, this.currentX + this.targetX, this.currentY + this.targetY)) {
				move = true;
			}
			else {
				this.collisionLastUpdate = true;
				this.targetX = 0;
				this.targetY = 0;
			}
		}
			
		if(move) {
			// mouvement
			if(this.targetX != 0) {
				float realX = (float) this.entityRectangle.x / Tiles.TILE_SIZE / game.xZoom;
				float targetTempX = (float) this.currentX + this.targetX;
				
				if(this.targetX > 0) {
					if(realX >= targetTempX) {
						this.currentX = (int) targetTempX;
						this.targetX = 0;
						move = false;
					}
					else {
						this.movedLastUpdate = true;
						this.entityRectangle.x += this.speed * this.targetX;
					}
				}
				else {
					if(realX <= targetTempX) {
						this.currentX = (int) targetTempX;
						this.entityRectangle.x = this.currentX * Tiles.TILE_SIZE * game.xZoom;
						this.targetX = 0;
						move = false;
					}
					else {
						this.movedLastUpdate = true;
						this.entityRectangle.x += this.speed * this.targetX;
					}
				}
			}
			else if(this.targetY != 0) {
				float realY = (float) this.entityRectangle.y / Tiles.TILE_SIZE / game.yZoom;
				float targetTempY = (float) this.currentY + this.targetY;
				
				if(this.targetY > 0) {
					if(realY >= targetTempY) {
						this.currentY = (int) targetTempY;
						this.entityRectangle.y = this.currentY * Tiles.TILE_SIZE * game.yZoom;
						this.targetY = 0;
						move = false;
					}
					else {
						this.movedLastUpdate = true;
						this.entityRectangle.y += this.speed * this.targetY;
					}
				}
				else {
					if(realY <= targetTempY) {
						this.currentY = (int) targetTempY;
						this.entityRectangle.y = this.currentY * Tiles.TILE_SIZE * game.yZoom;
						this.targetY = 0;
						move = false;
					}
					else {
						this.movedLastUpdate = true;
						this.entityRectangle.y += this.speed * this.targetY;
					}
				}
			}
		}
		else {
			this.animatedSprite.reset();
		}
		
		// correction de la position actuelle
		if(this.targetX == 0 && this.targetY == 0) {
			this.entityRectangle.x = this.currentX * Tiles.TILE_SIZE * game.xZoom;
			this.entityRectangle.y = this.currentY * Tiles.TILE_SIZE * game.yZoom;
		}
		
		
		// direction
		if(dir != this.lastDirection && dir != -1)
			this.setDirection(dir);
		
		// textures
		if(move)
			this.animatedSprite.update(game);
	}
	
	
	
	
	
	/**
	 * @param game
	 * 	Instance de jeu
	 * @return l'entité si celle en instance collide avec une autre / retourne null
	 */
	private Entity colideWithEntity(Game game, int x, int y) {
		for (Entity entity : game.getWorld().getEntities().getEntities()) {
			if(!entity.getName().equals(this.getName()) && entity.currentX == x && entity.currentY == y) {
				return entity;
			}
		}
		
		return null;
	}

	
	
	/**
	 * Check si l'entité collide avec une tile
	 * @param game
	 * @param dir
	 * 	Direction de l'entité
	 * @return true s'il y a collision
	 */
	/*private boolean colideWithTile(Game game, int dir) {
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
	}*/
	
	
	
	@Override
	protected int getMovingDirection(Game game) {
		return -1;
	}
	
	
	
	
	
	
	/**
	 * Attention : tronque la position en X et Y
	 * @param game
	 * 	Instance du jeu
	 * @param posX
	 * 	Position en X
	 * @param posY
	 * 	Position en Y
	 * @return true si la tile est solide
	 */
	private boolean isTileSolid(Game game, int posX, int posY) {
		MappedTile tile = game.getWorld().getTileAt(posX, posY);
		
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
}
