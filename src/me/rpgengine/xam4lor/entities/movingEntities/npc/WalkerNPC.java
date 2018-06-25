package me.rpgengine.xam4lor.entities.movingEntities.npc;

import me.rpgengine.xam4lor.engine.Game;
import me.rpgengine.xam4lor.engine.render.sprites.AnimatedSprite;
import me.rpgengine.xam4lor.entities.movingEntities.MovingEntity;

/**
 * NPC walker
 */
public class WalkerNPC extends MovingEntity {
	/**
	 * Classe du NPC
	 * @param sprite
	 * 	Sprite du NPC
	 */
	public WalkerNPC(AnimatedSprite sprite) {
		super(sprite, 8, 3, 16, 16, 16, 16);
		
		this.speed = 1;
	}
	
	
	@Override
	protected int getMovingDirection(Game game) {
		double proba = 0.001;
		double r = Math.random();
		
		
		if(r < proba / 4)
			return 0;
		else if(r < proba / 4 * 2)
			return 1;
		else if(r < proba / 4 * 3)
			return 2;
		else
			return 3;
	}
}
