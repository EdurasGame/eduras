package de.illonis.eduras.gameclient.gui.hud;

import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;

/**
 * Triggers tooltip actions and receives mouse position after move.
 * 
 * @author illonis
 * 
 */
public interface TooltipTriggerer {

	/**
	 * Notifies triggerer that mouse is on his trigger area, this is only
	 * triggered when {@link #isActive()} returns true.
	 * 
	 * @param p
	 *            mouse point relative to trigger area's origin.
	 */
	void onMouseOver(Vector2f p);

	/**
	 * Checks whether this triggerer is active.
	 * 
	 * @return true when this triggerer is active.
	 */
	boolean isActive();

	/**
	 * Returns area on that tooltip is triggered relative to game area. This
	 * must not overlap any of other trigger areas.
	 * 
	 * @return trigger area.
	 */
	Rectangle getTriggerArea();
}
