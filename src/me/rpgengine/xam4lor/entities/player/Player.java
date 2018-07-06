package me.rpgengine.xam4lor.entities.player;

import org.json.JSONException;
import org.json.JSONObject;

import me.rpgengine.xam4lor.engine.Game;
import me.rpgengine.xam4lor.engine.render.sprites.AnimatedSprite;
import me.rpgengine.xam4lor.engine.structure.Rectangle;
import me.rpgengine.xam4lor.entities.generic.Entity;
import me.rpgengine.xam4lor.entities.generic.MovingEntity;
import me.rpgengine.xam4lor.listener.KeyBoardListener;
import me.rpgengine.xam4lor.world.World.MappedTile;

/**
 * Classe du joueur
 */
public class Player extends MovingEntity {
	/**
	 * Classe du joueur
	 * @param sprite
	 * 	Sprite du joueur
	 * @param speed
	 * 	Vitesse de déplacement du joueur
	 */
	public Player(AnimatedSprite sprite, int speed) {
		super("player", sprite, 8, 3, 0, 0, 16, 16, null);
		
		this.speed = speed;
	}
	
	
	
	

	@Override
	public void update(Game game) {
		super.update(game);
		
		MappedTile t = this.getCurrentTile(game);
		if(t != null && t.getID() == 81) { // stairs_0_0_1
			try {
				JSONObject config = t.getOptions();
				game.getWorld().loadWorld(config.getString("loadLevel"));
			}
			catch(JSONException e) {}
		}
			
		// TODO si sur escalier, changer level en fct propriétés tile
		
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
	
	@Override
	public void onCollideWithEntity(Game game, Entity entity) {}

	
	
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