package de.illonis.eduras.events;

import de.illonis.eduras.gameobjects.GameObject.Visibility;

/**
 * Indicates a visibility attribute change for an object.
 * 
 * @author illonis
 * 
 */
public class SetVisibilityEvent extends ObjectEvent {

	private final Visibility newVisibility;

	/**
	 * 
	 * @param objectId
	 *            the id of the object.
	 * @param newMode
	 *            the new mode.
	 */
	public SetVisibilityEvent(int objectId, Visibility newMode) {
		super(GameEventNumber.SET_VISIBLE, objectId);
		this.newVisibility = newMode;
		putArgument(newMode.toString());
	}

	/**
	 * @return the new mode.
	 */
	public Visibility getNewVisibility() {
		return newVisibility;
	}

}
