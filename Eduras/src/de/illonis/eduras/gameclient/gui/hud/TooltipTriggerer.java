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
	 * Notifies triggerer that mouse is on his trigger area.
	 * 
	 * @param p
	 *            mouse point relative to trigger area's origin.
	 */
	void onMouseOver(Vector2f p);

	/**
	 * Returns area on that tooltip is triggered relative to game area. This
	 * must not overlap any of other trigger areas.
	 * 
	 * @return trigger area.
	 */
	Rectangle getTriggerArea();
}
