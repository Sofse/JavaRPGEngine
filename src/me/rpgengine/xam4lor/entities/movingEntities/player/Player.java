package me.rpgengine.xam4lor.entities.movingEntities.player;

import me.rpgengine.xam4lor.engine.Game;
import me.rpgengine.xam4lor.engine.render.sprites.AnimatedSprite;
import me.rpgengine.xam4lor.engine.structure.Rectangle;
import me.rpgengine.xam4lor.entities.movingEntities.MovingEntity;
import me.rpgengine.xam4lor.listener.KeyBoardListener;

/**
 * Classe du joueur
 */
public class Player extends MovingEntity {
	/**
	 * Classe du joueur
	 * @param sprite
	 * 	Sprite du joueur
	 */
	public Player(AnimatedSprite sprite) {
		super(sprite, 8, 3, 16, 16, 16, 16);
	}
	

	@Override
	public void update(Game game) {
		super.update(game);
		
		this.updateCamera(game.getRenderer().getCamera());
	}
	
	
	@Override
	protected int getMovingDirection(Game game) {
		KeyBoardListener keyListener = game.getKeyListener();

		if(keyListener.right())
			return 0;
		
		if(keyListener.left())
			return 1;
		
		if(keyListener.up())
			return 2;
		
		if(keyListener.down())
			return 3;
		
		return -1;
	}

	
	/**
	 * Mise à jour de la caméra en fonction de la position du joueur
	 * @param camera
	 * 	Caméra principale
	 */
	public void updateCamera(Rectangle camera) {
		camera.x = this.entityRectangle.x - (camera.w / 2);
		camera.y = this.entityRectangle.y - (camera.h / 2);
	}
}