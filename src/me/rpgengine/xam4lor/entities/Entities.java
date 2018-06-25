package me.rpgengine.xam4lor.entities;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import me.rpgengine.xam4lor.engine.Game;
import me.rpgengine.xam4lor.engine.render.RenderHandler;
import me.rpgengine.xam4lor.engine.render.sprites.AnimatedSprite;
import me.rpgengine.xam4lor.engine.render.sprites.SpriteSheet;
import me.rpgengine.xam4lor.engine.structure.GameObject;
import me.rpgengine.xam4lor.entities.movingEntities.npc.WalkerNPC;
import me.rpgengine.xam4lor.entities.movingEntities.player.Player;

/**
 * Liste des entités
 */
public class Entities implements GameObject {
	private ArrayList<Entity> entities;
	
	/**
	 * Liste des entités
	 * @param game
	 * 	Instance de jeu
	 */
	public Entities(Game game) {
		this.entities = new ArrayList<Entity>();
		
		this.initEntities(game);
	}
	
	@Override
	public void update(Game game) {
		for (Entity entity : this.entities) {
			entity.update(game);
		}
	}
	
	@Override
	public void render(RenderHandler renderer, int xZoom, int yZoom) {
		for (Entity entity : this.entities) {
			entity.render(renderer, xZoom, yZoom);
		}
	}
	
	
	
	private void initEntities(Game game) {
		BufferedImage playerSheetImage = game.loadImage("Player.png");
		SpriteSheet playerSheet = new SpriteSheet(playerSheetImage);
		playerSheet.loadSprites(20, 26);
	
		AnimatedSprite playerAnim = new AnimatedSprite(playerSheet, 5);
		this.entities.add(new Player(playerAnim));
		
		AnimatedSprite playerAnim2 = new AnimatedSprite(playerSheet, 5);
		this.entities.add(new WalkerNPC(playerAnim2));
	}
}
