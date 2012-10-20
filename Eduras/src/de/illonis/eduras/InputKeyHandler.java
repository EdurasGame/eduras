package de.illonis.eduras;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import de.illonis.eduras.MoveableGameObject.Direction;
import de.illonis.eduras.test.YellowCircle;

/**
 * This class handles user input and passes them to logic.
 * 
 * @author illonis
 * 
 */
public class InputKeyHandler implements KeyListener {

	private Game game;

	public InputKeyHandler(Game g) {
		this.game = g;
	}

	@Override
	public void keyTyped(KeyEvent e) {
		System.out.println("Key typed: " + e.getKeyCode() + " (\""
				+ e.getKeyChar() + "\")");
	}

	@Override
	public void keyPressed(KeyEvent e) {

		if (e.getKeyCode() == KeyEvent.VK_W) {
			((YellowCircle) game.getObjects().get(0))
					.startMoving(Direction.TOP);
		} else if (e.getKeyCode() == KeyEvent.VK_A) {
			((YellowCircle) game.getObjects().get(0))
					.startMoving(Direction.LEFT);
		} else if (e.getKeyCode() == KeyEvent.VK_S) {
			((YellowCircle) game.getObjects().get(0))
					.startMoving(Direction.BOTTOM);
		} else if (e.getKeyCode() == KeyEvent.VK_D) {
			((YellowCircle) game.getObjects().get(0))
					.startMoving(Direction.RIGHT);
		}
		System.out.println("Key pressed: " + e.getKeyCode() + " (\""
				+ e.getKeyChar() + "\")");
	}

	@Override
	public void keyReleased(KeyEvent e) {
		System.out.println("Key released: " + e.getKeyCode() + " (\""
				+ e.getKeyChar() + "\")");
		if (e.getKeyCode() == KeyEvent.VK_W) {
			((YellowCircle) game.getObjects().get(0)).stopMoving(Direction.TOP);
		} else if (e.getKeyCode() == KeyEvent.VK_A) {
			((YellowCircle) game.getObjects().get(0))
					.stopMoving(Direction.LEFT);
		} else if (e.getKeyCode() == KeyEvent.VK_S) {
			((YellowCircle) game.getObjects().get(0))
					.stopMoving(Direction.BOTTOM);
		} else if (e.getKeyCode() == KeyEvent.VK_D) {
			((YellowCircle) game.getObjects().get(0))
					.stopMoving(Direction.RIGHT);
		}
	}
}
