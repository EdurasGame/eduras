package de.illonis.eduras.logic;

import de.illonis.eduras.GameInformation;

/**
 * An operation on the (server-)logic that is performed after a given delay.<br>
 * <br>
 * Each {@link DelayedLogicAction} will be performed within the
 * {@link LogicGameWorker} context once when the delay has been lapsed.
 * 
 * @author illonis
 * 
 */
public abstract class DelayedLogicAction {

	private long oldDelay;
	private boolean done;
	private long delay;

	/**
	 * Creates a delayed action with the specified delay.
	 * 
	 * @param delay
	 *            the delay in ms.
	 */
	public DelayedLogicAction(long delay) {
		done = false;
		this.oldDelay = delay;
		this.delay = delay;
	}

	/**
	 * Sets the delay for this task.<br>
	 * This also affects already queued actions and their delay will count
	 * again.
	 * 
	 * @param delay
	 *            the delay in ms.
	 * @throws IllegalStateException
	 *             if the task is completed already.
	 */
	public void setDelay(long delay) throws IllegalStateException {
		if (done)
			throw new IllegalStateException(
					"This action is already done. Use reset() to mark this task undone.");
		this.delay = delay;
		this.oldDelay = delay;
	}

	/**
	 * Returns the remaining delay this action has.
	 * 
	 * @return the delay in ms.
	 */
	public final long getDelay() {
		return delay;
	}

	/**
	 * Indicates that a span of time has passed.
	 * 
	 * @param timeSpan
	 *            the amount of time passed (in ms)
	 * @return true if this task is due now, false otherwise.
	 */
	public final boolean timePassed(long timeSpan) {
		delay -= timeSpan;
		return (delay <= 0);
	}

	/**
	 * Puts this action on queue.
	 */
	public final void schedule() {
		DelayedActionQueue.addAction(this);
	}

	/**
	 * Removes this action from queue (usually when it is completed).
	 */
	public final void unschedule() {
		DelayedActionQueue.removeAction(this);
	}

	/**
	 * Performs the action of this task and unschedules it.
	 * 
	 * @param info
	 *            game information.
	 */
	public final void performAction(GameInformation info) {
		done = true;
		execute(info);
		unschedule();
	}

	/**
	 * @return true when this action is done, false otherwise.
	 */
	public final boolean isDone() {
		return done;
	}

	/**
	 * The action that is performed once the delay-time has passed.
	 */
	protected abstract void execute(GameInformation info);

	/**
	 * Resets this action so it can be scheduled again. The previously set
	 * cooldown is maintained.
	 */
	public final void reset() {
		unschedule();
		done = false;
		delay = oldDelay;
	}

}
