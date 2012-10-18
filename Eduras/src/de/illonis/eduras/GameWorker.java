package de.illonis.eduras;

import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;

import de.illonis.eduras.MoveableGameObject.Direction;
import de.illonis.eduras.gui.GameRenderer;
import de.illonis.eduras.gui.GameWorldPanel;

/**
 * A Gameworker represents game's main loop. It updates game, renders screen and
 * calls repaint.
 * 
 * @author illonis
 * 
 */
public class GameWorker implements Runnable {

	/**
	 * Number of frames with a delay of 0ms before the animation thread yields
	 * to other running threads.
	 */
	private static final int NO_DELAYS_PER_YIELD = 16;

	private boolean running = false;
	private Game game;
	private GameWorldPanel gameWorldPanel;
	private GameRenderer renderer;
	private int period = 15;

	public GameWorker(Game game, GameWorldPanel gameWorldPanel) {
		this.game = game;
		this.renderer = new GameRenderer(game, gameWorldPanel);
		this.gameWorldPanel = gameWorldPanel;
	}

	/**
	 * Returns game this gameworker is assigned to.
	 * 
	 * @return assigned game.
	 */
	public Game getGame() {
		return game;
	}

	@Override
	public void run() {
		running = true;

		long beforeTime, afterTime, timeDiff, sleepTime;
		long overSleepTime = 0L;
		int noDelays = 0;

		beforeTime = System.nanoTime();

		while (running) {
			gameUpdate();
			gameRender();
			paintScreen();
			afterTime = System.nanoTime();
			timeDiff = afterTime - beforeTime;

			sleepTime = (period - timeDiff) - overSleepTime;

			if (sleepTime > 0) {
				try {
					Thread.sleep(sleepTime / 1000000L);
				} catch (InterruptedException e) {
				}
				overSleepTime = (System.nanoTime() - afterTime) - sleepTime;
			} else {
				overSleepTime = 0L;

				if (++noDelays >= NO_DELAYS_PER_YIELD) {
					System.out.println("skip");
					Thread.yield();
					noDelays = 0;
				}
			}

			beforeTime = System.nanoTime();
		}
		// exit
	}

	private void paintScreen() {
		renderer.paintGame();
	}

	/**
	 * Stops gameworker.
	 */
	public void stop() {
		running = false;
	}

	/**
	 * Render game.
	 */
	private void gameRender() {
		renderer.render(gameWorldPanel.getWidth(), gameWorldPanel.getHeight());
	}

	// moves yellow ball into mouse's direction
	private void gameUpdate() {
		PointerInfo info = MouseInfo.getPointerInfo();
		Point location = info.getLocation();
		Point p = gameWorldPanel.getLocationOnScreen();
		int x = location.x - p.x;
		int y = location.y - p.y;

		GameObject o = game.getObjects().get(0);
		int ox = o.getXPosition();
		int oy = o.getYPosition();

		if (x > ox)
			((YellowCircle) o).onMove(Direction.RIGHT);
		else if (x < ox)
			((YellowCircle) o).onMove(Direction.LEFT);
		if (y > oy)
			((YellowCircle) o).onMove(Direction.DOWN);
		else if (y < oy)
			((YellowCircle) o).onMove(Direction.UP);
	}

}
