package me.rpgengine.xam4lor.listener;

import java.awt.event.KeyListener;

import me.rpgengine.xam4lor.engine.Game;

import java.awt.event.KeyEvent;
import java.awt.event.FocusListener;
import java.awt.event.FocusEvent;

/**
 * Listener du clavier
 */
public class KeyBoardListener implements KeyListener, FocusListener {
	private Game game;
	private boolean[] keys;
	
	/**
	 * Listener du clavier
	 * @param game
	 * 	Classe du jeu
	 */
	public KeyBoardListener(Game game) {
		this.game = game;
		this.keys = new boolean[120];
	}
	
	@Override
	public void keyPressed(KeyEvent ev) {
		int keyCode = ev.getKeyCode();
		
		if(keyCode < keys.length)
			keys[keyCode] = true;
		
		if(keys[KeyEvent.VK_CONTROL])
			this.game.handleCTRL(this.keys);
	}
	
	@Override
	public void keyReleased(KeyEvent ev) {
		int keyCode = ev.getKeyCode();
		
		if(keyCode < keys.length)
			keys[keyCode] = false;
	}
	
	@Override
	public void focusLost(FocusEvent ev) {
		for (int i = 0; i < keys.length; i++) {
			keys[i] = false;
		}
	}
	
	
	
	/**
	 * @return true si le joueur appuie sur les touches pour aller vers le haut
	 */
	public boolean up() {
		return keys[KeyEvent.VK_Z] || keys[KeyEvent.VK_UP];
	}
	
	/**
	 * @return true si le joueur appuie sur les touches pour aller vers le bas
	 */
	public boolean down() {
		return keys[KeyEvent.VK_S] || keys[KeyEvent.VK_DOWN];
	}
	
	/**
	 * @return true si le joueur appuie sur les touches pour aller vers la droite
	 */
	public boolean right() {
		return keys[KeyEvent.VK_D] || keys[KeyEvent.VK_RIGHT];
	}
	
	/**
	 * @return true si le joueur appuie sur les touches pour aller vers la gauche
	 */
	public boolean left() {
		return keys[KeyEvent.VK_Q] || keys[KeyEvent.VK_LEFT];
	}
	
	
	
	
	
	@Override
	public void focusGained(FocusEvent ev) {}

	@Override
	public void keyTyped(KeyEvent ev) {}
}