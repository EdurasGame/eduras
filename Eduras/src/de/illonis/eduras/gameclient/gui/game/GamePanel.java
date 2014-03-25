package de.illonis.eduras.gameclient.gui.game;

import java.awt.Color;

import org.newdawn.slick.CanvasGameContainer;
import org.newdawn.slick.SlickException;

import de.illonis.eduras.gameclient.SlickGame;
import de.illonis.eduras.logic.LogicGameWorker;

/**
 * The panel where the game is drawn on.
 * 
 * @author illonis
 * 
 */
public class GamePanel extends CanvasGameContainer {

	private static final long serialVersionUID = 1L;

	GamePanel(SlickGame game) throws SlickException {
		super(game);
		setFocusable(true);
		setFocusTraversalKeysEnabled(false);
		setBackground(Color.black);
	}

	/**
	 * Starts the game using the given worker.
	 * 
	 * @param worker
	 *            the worker to use.
	 * @param renderer
	 *            the renderer that renders the game.
	 * @throws SlickException
	 *             Indicates a failure during game execution.
	 * 
	 */
	public void start(LogicGameWorker worker, GameRenderer renderer)
			throws SlickException {
		((SlickGame) game).setWorker(worker, renderer);
		super.start();
	}

	/**
	 * exits the game.
	 */
	public void exit() {
		getContainer().exit();
	}
}
