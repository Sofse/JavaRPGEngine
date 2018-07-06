package me.rpgengine.xam4lor.entities.npc.moving;

import org.json.JSONObject;

import me.rpgengine.xam4lor.engine.render.sprites.AnimatedSprite;
import me.rpgengine.xam4lor.entities.npc.NPC;

/**
 * NPC walker
 */
public class WalkerNPC extends NPC {
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
	 * @param options
	 * 	Options de l'entité
	 */
	public WalkerNPC(String name, AnimatedSprite sprite, int x, int y, JSONObject options) {
		super(name, sprite, 8, 3, x, y, 16, 16, options);
		
		this.targetID = 0;
	}
}
