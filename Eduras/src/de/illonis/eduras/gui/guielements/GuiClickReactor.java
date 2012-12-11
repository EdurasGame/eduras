package de.illonis.eduras.gui.guielements;

import java.awt.Point;

public interface GuiClickReactor {

	/**
	 * Fired on a click in gui and returns whether event is consumed or not. If
	 * event is consumed, no other elements are notified.
	 * 
	 * @param p
	 *            click position.
	 * @return true if event is consumed, false otherwise.
	 */
	boolean onClick(Point p);
}
