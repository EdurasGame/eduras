package de.illonis.eduras.gui.guielements;

import java.awt.Point;

/**
 * Triggers tooltip actions and receives mouse position after move.
 * 
 * @author illonis
 * 
 */
public interface TooltipTriggerer {

	/**
	 * Handles new mouse position.
	 * 
	 * @param p
	 *            new mouse point.
	 */
	void onMouseAt(Point p);
}
