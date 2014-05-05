package de.illonis.eduras.logic.delayedactions;

import de.illonis.eduras.GameInformation;
import de.illonis.eduras.gameobjects.GameObject;
import de.illonis.eduras.logic.DelayedLogicAction;

/**
 * Removes a gameobject later on.
 * 
 * @author illonis
 * 
 */
public class RemoveObjectLaterAction extends DelayedLogicAction {

	private final GameObject gameObject;

	/**
	 * @param object
	 *            the object to remove.
	 * @param delay
	 *            the delay (in ms).
	 */
	public RemoveObjectLaterAction(GameObject object, long delay) {
		super(delay);
		this.gameObject = object;
	}

	@Override
	protected void execute(GameInformation info) {
		info.removeObject(gameObject);
	}
}
