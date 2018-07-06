package me.rpgengine.xam4lor.entities.npc.moving;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import me.rpgengine.xam4lor.engine.Game;
import me.rpgengine.xam4lor.engine.render.sprites.AnimatedSprite;
import me.rpgengine.xam4lor.entities.npc.NPC;

/**
 * NPC walker
 */
public class WalkerNPC extends NPC {
	private int[] targets;
	private int targetID;
	private boolean isTargeting;
	private boolean infinite;
	
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
		
		this.targets = new int[0];
		this.targetID = 0;
		this.infinite = false;
		try {
			if(this.options != null) {
				JSONArray path = this.options.getJSONArray("path");
				
				this.targets = new int[path.length()];
				for (int i = 0; i < path.length(); i++) {
					this.targets[i] = path.getInt(i);
				}
				
				this.infinite = this.options.getBoolean("infinite");
			}
		}
		catch(JSONException e) {}
		
		// TODO chemin de déplacements lors d'évènements
		this.isTargeting = true;
	}
	
	@Override
	public void update(Game game) {
		super.update(game);
		
		if(this.targetX == 0 && this.targetY == 0 && !this.movedLastUpdate && !this.collisionLastUpdate) {
			this.targetID++;
			
			if(this.targetID > this.targets.length - 1) {
				this.targetID = 0;
				
				if(this.infinite)
					this.isTargeting = true;
				else
					this.isTargeting = false;
			}
		}
	}
	
	@Override
	protected int getMovingDirection(Game game) {
		if(isTargeting && this.targetX == 0 && this.targetY == 0 && !this.movedLastUpdate)
			return this.targets[this.targetID];
		
		return -1;
	}
}
