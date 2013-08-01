package de.illonis.eduras.logic;

import de.illonis.eduras.GameInformation;
import de.illonis.eduras.events.GameEvent.GameEventNumber;
import de.illonis.eduras.events.SetBooleanGameObjectAttributeEvent;
import de.illonis.eduras.gameobjects.GameObject;
import de.illonis.eduras.gameobjects.MoveableGameObject;
import de.illonis.eduras.interfaces.GameEventListener;
import de.illonis.eduras.items.Lootable;
import de.illonis.eduras.items.Usable;

/**
 * This class is responsible for updating the current game information triggered
 * by a timer.
 * 
 * @author Florian Mai <florian.ren.mai@googlemail.com>
 * 
 */
public class LogicGameWorker implements Runnable {

	/**
	 * Tells how much time shall be between two updates in nanoseconds.
	 */
	private static final long TICKRATE = 15000000L;

	private static final int NO_DELAYS_PER_YIELD = 16;

	private boolean running = false;

	private final GameInformation gameInformation;
	private ListenerHolder<? extends GameEventListener> listenerHolder;

	private long lastUpdate;

	/**
	 * Creates a new logic game worker.
	 * 
	 * @param gameInfo
	 *            information used.
	 * @param listenerHolder
	 *            a placeholder for later attached listener.
	 */
	public LogicGameWorker(GameInformation gameInfo,
			ListenerHolder<? extends GameEventListener> listenerHolder) {
		this.gameInformation = gameInfo;
		this.listenerHolder = listenerHolder;
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
		// delta in milliseconds
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
			if (o instanceof Usable) {
				((Usable) o).reduceCooldown(delta);
			}
			if (o instanceof Lootable) {

				boolean rs = ((Lootable) o).reduceRespawnRemaining(delta);
				if (rs) {
					SetBooleanGameObjectAttributeEvent sc = new SetBooleanGameObjectAttributeEvent(
							GameEventNumber.SET_COLLIDABLE, o.getId(), true);
					SetBooleanGameObjectAttributeEvent sv = new SetBooleanGameObjectAttributeEvent(
							GameEventNumber.SET_VISIBLE, o.getId(), true);
					o.setCollidable(true);
					o.setVisible(true);
					if (listenerHolder.hasListener()) {
						listenerHolder.getListener().onObjectStateChanged(sv);
						listenerHolder.getListener().onObjectStateChanged(sc);

					}
				}
			}
			if (o instanceof MoveableGameObject) {
				if (!((MoveableGameObject) o).getSpeedVector().isNull()) {
					((MoveableGameObject) o).onMove(delta);
					if (listenerHolder.hasListener())
						listenerHolder.getListener().onNewObjectPosition(o);

				}
			}
		}
	}
}