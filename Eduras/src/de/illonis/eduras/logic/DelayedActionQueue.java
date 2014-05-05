package de.illonis.eduras.logic;

import java.util.LinkedList;

/**
 * A queue that holds all delayed actions that should be performed.
 * 
 * @author illonis
 * 
 */
public class DelayedActionQueue {

	private static DelayedActionQueue instance;

	private final LinkedList<DelayedLogicAction> actions = new LinkedList<DelayedLogicAction>();

	private static DelayedActionQueue getInstance() {
		if (instance == null)
			instance = new DelayedActionQueue();
		return instance;
	}

	/**
	 * Adds an action to queue.
	 * 
	 * @param action
	 *            the action to add.
	 */
	static void addAction(DelayedLogicAction action) {
		getInstance().actions.add(action);
	}

	/**
	 * Removes an action from queue.
	 * 
	 * @param action
	 *            the action to remove.
	 */
	static void removeAction(DelayedLogicAction action) {
		getInstance().actions.remove(action);
	}

	static LinkedList<DelayedLogicAction> getActions() {
		return new LinkedList<DelayedLogicAction>(getInstance().actions);
	}
}
