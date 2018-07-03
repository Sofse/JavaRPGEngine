package me.rpgengine.xam4lor.entities.idleEntities.npc;

import me.rpgengine.xam4lor.engine.render.sprites.AnimatedSprite;
import me.rpgengine.xam4lor.entities.Entity;

/**
 * NPC idle
 */
public class IdleNPC extends Entity {
	/**
	 * Classe du NPC
	 * @param name
	 * 	Nom de l'entité
	 * @param sprite
	 * 	Sprite du NPC
	 * @param x
	 * 	Position initiale en X
	 * @param y
	 * 	Position initiale en Y
	 */
	public IdleNPC(String name, AnimatedSprite sprite, int x, int y) {
		super(name, sprite, x, y, 16, 16);
	}
}
