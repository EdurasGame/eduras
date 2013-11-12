package de.illonis.eduras.gameclient.gui.hud;

import java.awt.Point;
import java.awt.Rectangle;

/**
 * A clickable gui element.
 * 
 * @author illonis
 * 
 */
public interface ClickableGuiElementInterface {

	/**
	 * Fired on a click in gui and returns whether event is consumed or not. If
	 * event is consumed, no other elements are notified.
	 * 
	 * @param p
	 *            click position.
	 * @return true if event is consumed, false otherwise.
	 */
	boolean onClick(Point p);

	/**
	 * Returns bounds of the clickable element relative to game screen.
	 * 
	 * @return bounds.
	 */
	Rectangle getBounds();
}
