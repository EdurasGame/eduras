package de.illonis.eduras.logic;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;

import de.illonis.eduras.GameInformation;
import de.illonis.eduras.gameobjects.GameObject;
import de.illonis.eduras.gameobjects.TimedEventHandler;
import de.illonis.eduras.gameobjects.TimingSource;
import de.illonis.eduras.interfaces.GameEventListener;
import de.illonis.eduras.math.ShapeGeometry;
import de.illonis.eduras.math.SimpleGeometry;
import de.illonis.eduras.settings.S;

/**
 * This class is responsible for updating the current game information triggered
 * by a timer.
 * 
 * @author Florian Mai <florian.ren.mai@googlemail.com>
 * 
 */
public abstract class LogicGameWorker implements Runnable, TimingSource {

	/**
	 * Tells how much time shall be between two updates in nanoseconds.
	 */
	private static final long TICKRATE = 15000000L;

	private static final int NO_DELAYS_PER_YIELD = 16;

	private boolean running = false;

	protected final GameInformation gameInformation;
	protected final ListenerHolder<? extends GameEventListener> listenerHolder;
	private final HashMap<Integer, Float> oldRotation;
	private final Map<TimedEventHandler, Long> timingTargets;

	protected final ShapeGeometry geometry;

	private long lastUpdate;

	/**
	 * Creates a new logic game worker.
	 * 
	 * @param gameInfo
	 *            information used.
	 * @param listenerHolder
	 *            a placeholder for later attached listener.
	 */
	protected LogicGameWorker(GameInformation gameInfo,
			ListenerHolder<? extends GameEventListener> listenerHolder) {
		this.gameInformation = gameInfo;
		this.listenerHolder = listenerHolder;
		oldRotation = new HashMap<Integer, Float>();
		lastUpdate = 0;
		geometry = new SimpleGeometry(gameInfo.getObjects());
		timingTargets = new HashMap<TimedEventHandler, Long>();
	}

	@Override
	public void run() {
		running = true;

		long beforeTime, afterTime, timeDiff, sleepTime;
		long overSleepTime = 0L;
		int noDelays = 0;

		beforeTime = System.nanoTime();

		while (running) {
			if (lastUpdate <= 0)
				lastUpdate = System.nanoTime();
			// delta in milliseconds
			long delta = (System.nanoTime() - lastUpdate) / 1000000L;
			if (delta > 0) {
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
				notifyTimingTargets(delta);
				gameUpdate(delta);
			}
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

	private void notifyTimingTargets(long delta) {
		Collection<Entry<TimedEventHandler, Long>> timingTargetsCopy = new LinkedList<Entry<TimedEventHandler, Long>>(
				timingTargets.entrySet());

		for (Entry<TimedEventHandler, Long> element : timingTargetsCopy) {
			long value = element.getValue();
			value += delta;
			if (value > element.getKey().getInterval()) {
				element.getKey().onIntervalElapsed(value);
				value = 0L;
			}
			element.setValue(value);
		}
	}

	@Override
	public final boolean isRunning() {
		return running;
	}

	/**
	 * Stops LogicGameWorker.
	 */
	public final void stop() {
		running = false;
	}

	/**
	 * Lets all moveable objects move relative to the time passed. Resetts the
	 * GameSettings' remaining time.
	 * 
	 * @param delta
	 *            the time elapsed since last update in ms.
	 * 
	 */
	public abstract void gameUpdate(long delta);

	protected final boolean hasRotated(GameObject o) {
		if (!oldRotation.containsKey(o.getId())) {
			oldRotation.put(o.getId(), o.getRotation());
			return true;
		}

		if (Math.abs(oldRotation.get(o.getId()) - o.getRotation()) > S.Server.sv_performance_rotationdelta_minimum) {
			oldRotation.put(o.getId(), o.getRotation());
			return true;
		}
		return false;
	}

	@Override
	public final void addTimedEventHandler(TimedEventHandler eventHandler) {
		timingTargets.put(eventHandler, 0L);

	}

	@Override
	public final void removeTimedEventHandler(TimedEventHandler eventHandler) {
		timingTargets.remove(eventHandler);
	}
}
