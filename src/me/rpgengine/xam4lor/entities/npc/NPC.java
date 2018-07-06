package me.rpgengine.xam4lor.entities.npc;

import org.json.JSONException;
import org.json.JSONObject;

import me.rpgengine.xam4lor.engine.Game;
import me.rpgengine.xam4lor.engine.render.sprites.AnimatedSprite;
import me.rpgengine.xam4lor.entities.generic.Entity;
import me.rpgengine.xam4lor.entities.generic.MovingEntity;
import me.rpgengine.xam4lor.entities.player.Player;

/**
 * Classe d'un NPC
 */
public abstract class NPC extends MovingEntity {
	protected String[] messages;
	
	/**
	 * Classe d'un NPC
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
	public NPC(String name, AnimatedSprite sprite, int framePerAnim, int initDirection, int xInit, int yInit, int width, int height, JSONObject options) {
		super(name, sprite, framePerAnim, initDirection, xInit, yInit, width, height, options);
		
		try {
			JSONObject text = this.options.getJSONObject("text");
			this.messages = new String[text.length()];
			
			for (int i = 0; i < text.length(); i++) {
				this.messages[i] = text.getString(i + "");
			}
		}
		catch(JSONException e) {
			this.messages = new String[0];
		}
	}

	
	@Override
	public void onCollideWithEntity(Game game, Entity entity) {
		if(entity instanceof Player) {
			int dir = -1;
			switch (entity.getDirection()) {
				case 0:
					dir = 1;
					break;
	
				case 1:
					dir = 0;
					break;
					
				case 2:
					dir = 3;
					break;
	
				case 3:
					dir = 2;
					break;
			}
			
			this.setDirection(dir);
			
			if(this.messages.length > 0)
				game.getWorld().getGUIContainer().displayMessage(this.messages);
		}
	}
}
