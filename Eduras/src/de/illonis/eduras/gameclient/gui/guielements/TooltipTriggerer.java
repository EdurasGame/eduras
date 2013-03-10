package de.illonis.eduras.gameclient.gui.guielements;

import java.awt.Point;
import java.awt.Rectangle;

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
	void onMouseOver(Point p);

	/**
	 * Notifies triggerer that mouse left trigger area. Triggerer should hide
	 * his tooltip here.
	 */
	void onMouseLeft();

	/**
	 * Returns area on that tooltip is triggered.
	 * 
	 * @return trigger area.
	 */
	Rectangle getTriggerArea();
}
