/**
 * 
 */
package de.illonis.eduras.logic;

import java.util.ArrayList;

import de.illonis.eduras.GameInformation;
import de.illonis.eduras.gameobjects.GameObject;
import de.illonis.eduras.gameobjects.MoveableGameObject;
import de.illonis.eduras.interfaces.GameEventListener;

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
	private static final long TICKRATE = 15000000L;

	private static final int NO_DELAYS_PER_YIELD = 16;

	private boolean running = false;

	private final GameInformation gameInformation;
	private ArrayList<GameEventListener> listenerList;

	private long lastUpdate;

	public LogicGameWorker(GameInformation gameInfo,
			ArrayList<GameEventListener> listenerList) {
		this.gameInformation = gameInfo;
		this.listenerList = listenerList;
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
	 * Stops LogicGameWorker.
	 */
	public void stop() {
		running = false;
	}

	/**
	 * Lets all moveable objects move relative to the time passed. Resetts the
	 * GameSettings' remaining time.
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

		// check game settings
		long gameRemainingTime = gameInformation.getGameSettings()
				.getRemainingTime();

		if (gameRemainingTime <= 0) {
			gameInformation.getGameSettings().getGameMode().onTimeUp();
		} else {
			gameInformation.getGameSettings().changeTime(
					gameRemainingTime - delta);
		}

		for (GameObject o : gameInformation.getObjects().values()) {

			if (o instanceof MoveableGameObject) {
				if (!((MoveableGameObject) o).getSpeedVector().isNull()) {
					((MoveableGameObject) o).onMove(delta);
					for (GameEventListener listener : listenerList) {
						listener.onNewObjectPosition(o);
					}
				}
			}
		}
	}
}