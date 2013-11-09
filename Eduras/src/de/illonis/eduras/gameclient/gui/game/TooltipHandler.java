package de.illonis.eduras.gameclient.gui.game;

import java.awt.Point;

import de.illonis.eduras.items.Item;

/**
 * Handles tooltip triggerer.
 * 
 * @author illonis
 * 
 */
public interface TooltipHandler {

	/**
	 * Shows a tooltip that shows description of given item.
	 * 
	 * @param p
	 *            position of tooltip.
	 * @param item
	 *            item to describe.
	 */
	void showItemTooltip(Point p, Item item);

	/**
	 * Shows a tooltip at given position with given text.
	 * 
	 * @param p
	 *            position of tooltip.
	 * @param text
	 *            text.
	 */
	void showTooltip(Point p, String text);

	/**
	 * Hides any currently shown tooltip.
	 */
	void hideTooltip();

}
