package de.illonis.eduras.test;

import de.illonis.eduras.Game;
import de.illonis.eduras.GameObject;

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
	/**
	 * Number of frames that can be skipped in any one animation loop.
	 */
	private static final int MAX_FRAME_SKIPS = 20;

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

	/**
	 * Repeatedly update, render and sleep so loop takes close to period nsecs.
	 * Sleep inaccuracies are handled.
	 */
	@Override
	public void run() {
		running = true;

		long beforeTime, afterTime, timeDiff, sleepTime;
		long overSleepTime = 0L;
		int noDelays = 0;
		long excess = 0L;

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
				excess -= sleepTime;
				overSleepTime = 0L;
				if (++noDelays >= NO_DELAYS_PER_YIELD) {
					Thread.yield(); // let other threads do something
					noDelays = 0;
				}
			}
			beforeTime = System.nanoTime();

			// skip frames (update game without rendering it)
			int skips = 0;
			while ((excess > period) && (skips < MAX_FRAME_SKIPS)) {
				excess -= period;
				gameUpdate();
				skips++;
			}
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

	private long lastUpdate;

	// moves yellow ball into mouse's direction
	private synchronized void gameUpdate() {
		if (lastUpdate <= 0)
			lastUpdate = System.nanoTime();
		// delta in seconds
		long delta = (System.nanoTime() - lastUpdate) / 1000000L;
		if (delta == 0)
			return;
		lastUpdate = System.nanoTime();
		/*
		 * PointerInfo info = MouseInfo.getPointerInfo(); Point location =
		 * info.getLocation(); Point p = gameWorldPanel.getLocationOnScreen();
		 * int x = location.x - p.x; int y = location.y - p.y;
		 * 
		 * GameObject o = game.getObjects().get(0); double ox =
		 * o.getXPosition(); double oy = o.getYPosition();
		 * 
		 * if (x > ox) ((YellowCircle) o).startMoving(Direction.RIGHT); else if
		 * (x < ox) ((YellowCircle) o).startMoving(Direction.LEFT); if (y > oy)
		 * ((YellowCircle) o).startMoving(Direction.BOTTOM); else if (y < oy)
		 * ((YellowCircle) o).startMoving(Direction.TOP); else ((YellowCircle)
		 * o).stopMoving();
		 */
		for (GameObject o : game.getObjects()) {

			if (o instanceof YellowCircle)
				((YellowCircle) o).onMove(delta);
		}

	}

}
