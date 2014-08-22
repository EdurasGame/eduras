package de.illonis.eduras.gameclient.gui.hud;

import org.newdawn.slick.geom.Rectangle;

/**
 * A clickable gui element.
 * 
 * @author illonis
 * 
 */
public interface ClickableGuiElementInterface {

	/**
	 * Fired on a click in gui that was within {@link #getBounds()}. Returns
	 * whether event is consumed or not. If event is consumed, no other elements
	 * are notified.
	 * 
	 * @param button
	 *            the mouse button clicked.
	 * @param x
	 *            the x-coordinate of clicked point.
	 * @param y
	 *            the y-coordinate of clicked point.
	 * @param clickCount
	 *            number of clicks.
	 * 
	 * @return true if event is consumed, false otherwise.
	 */
	public boolean mouseClicked(int button, int x, int y, int clickCount);

	/**
	 * Fired when mouse is dragged on this element. Returns whether event is
	 * consumed or not. If event is consumed, no other elements are notified.
	 * 
	 * @param oldx
	 *            old x-position.
	 * @param oldy
	 *            old y-position.
	 * @param newx
	 *            new x-position.
	 * @param newy
	 *            new y-position.
	 * @return true if event is consumed, false otherwise.
	 */
	public boolean mouseDragged(int oldx, int oldy, int newx, int newy);

	/**
	 * Fired when mouse is moved above this element. Returns whether event is
	 * consumed or not. If event is consumed, no other elements are notified.
	 * 
	 * @param oldx
	 *            old x-position.
	 * @param oldy
	 *            old y-position.
	 * @param newx
	 *            new x-position.
	 * @param newy
	 *            new y-position.
	 * @return true if event is consumed, false otherwise.
	 */
	public boolean mouseMoved(int oldx, int oldy, int newx, int newy);

	/**
	 * Fired when mouse is pressed on this element. Returns whether event is
	 * consumed or not. If event is consumed, no other elements are notified.
	 * 
	 * @param button
	 *            the mouse button used.
	 * @param x
	 *            the x-coordinate of mouse point.
	 * @param y
	 *            the y-coordinate of mouse point.
	 * 
	 * @return true if event is consumed, false otherwise.
	 */
	public boolean mousePressed(int button, int x, int y);

	/**
	 * Fired when mouse is released on this element. Returns whether event is
	 * consumed or not. If event is consumed, no other elements are notified.
	 * 
	 * @param button
	 *            the mouse button used.
	 * @param x
	 *            the x-coordinate of mouse point.
	 * @param y
	 *            the y-coordinate of mouse point.
	 * 
	 * @return true if event is consumed, false otherwise.
	 */
	public boolean mouseReleased(int button, int x, int y);

	/**
	 * Fired if mouse wheel was moved while mouse is on this element. Returns
	 * whether event is consumed or not. If event is consumed, no other elements
	 * are notified.
	 * 
	 * @param change
	 *            the scroll amount.
	 * 
	 * @return true if event is consumed, false otherwise.
	 */
	public boolean mouseWheelMoved(int change);

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
