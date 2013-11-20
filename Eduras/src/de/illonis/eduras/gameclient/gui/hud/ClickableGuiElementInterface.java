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
	 * Fired on a click in gui that was within the are returned by
	 * {@link #getBounds()}. Returns whether event is consumed or not. If event
	 * is consumed, no other elements are notified.
	 * 
	 * @param p
	 *            click position.
	 * @return true if event is consumed, false otherwise.
	 */
	boolean onClick(Point p);

	/**
	 * Returns bounds of the clickable element relative to game screen. This
	 * element will only receive clicks within these bounds.
	 * 
	 * @return bounds.
	 */
	Rectangle getBounds();

	/**
	 * Returns whether this element is active and reacts on clicks or not.<br>
	 * A gui element may be inactive if there are unavailabilities regarding
	 * cooldowns or whatever.
	 * 
	 * @return true if this element is active, false otherwise.
	 */
	boolean isActive();
}
