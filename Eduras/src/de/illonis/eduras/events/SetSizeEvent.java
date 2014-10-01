package de.illonis.eduras.events;

import de.illonis.eduras.gameobjects.TriggerArea;

/**
 * Announces size change of a {@link TriggerArea}.
 * 
 * @author illonis
 * 
 */
public class SetSizeEvent extends ObjectEvent {
	private final float width, height;

	/**
	 * Creates a new event.
	 * 
	 * @param id
	 *            the id of the object changing.
	 * @param newWidth
	 *            the new width.
	 * @param newHeight
	 *            the new height.
	 */
	public SetSizeEvent(int id, float newWidth, float newHeight) {
		super(GameEventNumber.SET_SIZE, id);
		width = newWidth;
		height = newHeight;
		putArgument(width);
		putArgument(height);
	}

	/**
	 * @return the new width.
	 */
	public float getWidth() {
		return width;
	}

	/**
	 * @return the new height.
	 */
	public float getHeight() {
		return height;
	}

}
