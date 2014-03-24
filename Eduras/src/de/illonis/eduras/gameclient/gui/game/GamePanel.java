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
	 * @throws SlickException
	 *             Indicates a failure during game execution.
	 * 
	 */
	public void start(LogicGameWorker worker) throws SlickException {
		((SlickGame) game).setWorker(worker);
		super.start();
	}
}
