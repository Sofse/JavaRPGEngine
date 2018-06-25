package me.rpgengine.xam4lor.listener;

import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import me.rpgengine.xam4lor.engine.Game;

/**
 * Listener de la souris
 */
public class MouseEventListener implements MouseListener, MouseMotionListener {
	private Game game;

	/**
	 * Listener de la souris
	 * @param game
	 * 	Classe du jeu
	 */
	public MouseEventListener(Game game) {
		this.game = game;
	}

	@Override
	public void mousePressed(MouseEvent event) {
		if(event.getButton() == MouseEvent.BUTTON1)
			game.leftClick(event.getX(), event.getY());

		if(event.getButton() == MouseEvent.BUTTON3)
			game.rightClick(event.getX(), event.getY());
	}

	@Override
	public void mouseDragged(MouseEvent arg0) {}

	@Override
	public void mouseMoved(MouseEvent e) {}

	@Override
	public void mouseClicked(MouseEvent e) {}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}

	@Override
	public void mouseReleased(MouseEvent e) {}
}