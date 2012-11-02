/**
 * 
 */
package de.illonis.eduras;

/**
 * This class is responsible for updating the current game information triggered
 * by a timer.
 * 
 * @author Florian Mai <florian.ren.mai@googlemail.com>
 * 
 */
public class LogicGameWorker implements Runnable {

	/**
	 * Tells how much time shall be between two updates.
	 */
	private static final long TICKRATE = 100000;

	private static final int NO_DELAYS_PER_YIELD = 16;

	private boolean running = false;

	private final GameInformation gameInformation;

	private long lastUpdate;

	public LogicGameWorker(GameInformation gameInfo) {
		this.gameInformation = gameInfo;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		running = true;

		long beforeTime, afterTime, timeDiff, sleepTime;
		long overSleepTime = 0L;
		int noDelays = 0;

		beforeTime = System.nanoTime();

		while (running) {
			gameUpdate();
			afterTime = System.nanoTime();
			timeDiff = afterTime - beforeTime;

			sleepTime = (TICKRATE - timeDiff) - overSleepTime;

			if (sleepTime > 0) {
				try {
					Thread.sleep(sleepTime / 1000000L);
				} catch (InterruptedException e) {
				}
				overSleepTime = (System.nanoTime() - afterTime) - sleepTime;
			} else {
				overSleepTime = 0L;
				if (++noDelays >= NO_DELAYS_PER_YIELD) {
					Thread.yield(); // let other threads do something
					noDelays = 0;
				}
			}
			beforeTime = System.nanoTime();
		}

	}

	/**
	 * Lets all moveable objects move relative to the time passed.
	 * 
	 */
	private synchronized void gameUpdate() {
		if (lastUpdate <= 0)
			lastUpdate = System.nanoTime();
		// delta in seconds
		long delta = (System.nanoTime() - lastUpdate) / 1000000L;
		if (delta == 0)
			return;
		lastUpdate = System.nanoTime();

		for (GameObject o : gameInformation.getObjects()) {

			if (o instanceof MoveableGameObject)
				((MoveableGameObject) o).onMove(delta);
		}

	}

}
