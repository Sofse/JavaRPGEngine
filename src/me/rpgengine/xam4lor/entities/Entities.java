package me.rpgengine.xam4lor.entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import org.json.JSONObject;

import me.rpgengine.xam4lor.engine.Game;
import me.rpgengine.xam4lor.engine.render.RenderHandler;
import me.rpgengine.xam4lor.engine.render.sprites.AnimatedSprite;
import me.rpgengine.xam4lor.engine.render.sprites.SpriteSheet;
import me.rpgengine.xam4lor.engine.structure.GameObject;
import me.rpgengine.xam4lor.entities.generic.Entity;
import me.rpgengine.xam4lor.entities.npc.idle.IdleNPC;
import me.rpgengine.xam4lor.entities.npc.moving.WalkerNPC;
import me.rpgengine.xam4lor.entities.player.Player;

/**
 * Liste des entit�s
 */
public class Entities implements GameObject {
	private ArrayList<Entity> entities;
	private Game game;
	
	/**
	 * Liste des entit�s
	 * @param game
	 * 	Instance de jeu
	 */
	public Entities(Game game) {
		this.entities = new ArrayList<Entity>();
		this.game = game;
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
	
	@Override
	public void postRender(Graphics graphics, int xZoom, int yZoom) {}
	
	
	
	/**
	 * Ajout d'une entit�
	 * @param entityObject
	 * 	Objet JSON de l'entit�
	 */
	public void addEntity(JSONObject entityObject) {
		// Chargement de la sprite
		JSONObject sprite = entityObject.getJSONObject("sprite");
		
		SpriteSheet sheet = new SpriteSheet(this.game.loadImage(sprite.getString("image")));
		sheet.loadSprites(sprite.getInt("sprite_size_x"), sprite.getInt("sprite_size_y"));
		
		AnimatedSprite animatedSprite = new AnimatedSprite(sheet, sprite.getInt("sprite_anim_time"));
		
		
		// Chargement de l'entit�
		if(entityObject.getString("type").equals("WalkerNPC"))
			this.addEntity(new WalkerNPC(entityObject.getString("name"), animatedSprite, entityObject.getInt("x"), entityObject.getInt("y"), entityObject.getJSONObject("options")), entityObject.getInt("x"), entityObject.getInt("y"));
		else if(entityObject.getString("type").equals("IdleNPC"))
			this.addEntity(new IdleNPC(entityObject.getString("name"), animatedSprite, entityObject.getInt("x"), entityObject.getInt("y"), entityObject.getJSONObject("options")), entityObject.getInt("x"), entityObject.getInt("y"));
	}
	

	/**
	 * Ajout du joueur
	 * @param x
	 * 	Position en X
	 * @param y
	 * 	Position en Y
	 * @param speed
	 * 	Vitesse de d�placement du joueur
	 */
	public void addPlayer(int x, int y, int speed) {
		BufferedImage playerSheetImage = this.game.loadImage("Player.png");
		SpriteSheet playerSheet = new SpriteSheet(playerSheetImage);
		playerSheet.loadSprites(20, 26);
	
		AnimatedSprite playerAnim = new AnimatedSprite(playerSheet, 5);
		Player player = new Player(playerAnim, speed);
		player.setPosition(this.game, x, y);
		
		this.entities.add(player);
	}
	
	/**
	 * Ajout d'une entit�
	 * @param entity
	 * 	Entit�
	 * @param x
	 * 	Position en X
	 * @param y
	 * 	Position en Y
	 */
	public void addEntity(Entity entity, int x, int y) {
		entity.setPosition(this.game, x, y);
		this.entities.add(entity);
	}
	
	/**
	 * Vide la liste des entit�s
	 */
	public void clearEntities() {
		this.entities.clear();
	}
	
	/**
	 * @return la liste des entit�s
	 */
	public ArrayList<Entity> getEntities() {
		return entities;
	}
}
