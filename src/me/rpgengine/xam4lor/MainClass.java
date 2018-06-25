package me.rpgengine.xam4lor;

import me.rpgengine.xam4lor.engine.Game;

/**
 * Classe de jeu principale
 */
public class MainClass {
	/**
	 * Classe de jeu principale
	 * @param args
	 * 	Arguments initiaux
	 */
	public static void main(String[] args) {
		Game game = new Game();
		Thread gameThread = new Thread(game);
		gameThread.start();
	}
}
