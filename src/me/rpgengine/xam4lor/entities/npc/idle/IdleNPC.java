package me.rpgengine.xam4lor.entities.npc.idle;

import org.json.JSONObject;

import me.rpgengine.xam4lor.engine.render.sprites.AnimatedSprite;
import me.rpgengine.xam4lor.entities.npc.NPC;

/**
 * NPC idle
 */
public class IdleNPC extends NPC {
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
	public IdleNPC(String name, AnimatedSprite sprite, int x, int y, JSONObject options) {
		super(name, sprite, 8, 3, x, y, 16, 16, options);
	}
}
