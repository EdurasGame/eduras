package de.illonis.eduras.gameclient;

import java.awt.Point;

import de.illonis.eduras.gui.guielements.TooltipTriggerer;
import de.illonis.eduras.items.Item;

/**
 * Handles tooltip triggerer.
 * 
 * @author illonis
 * 
 */
public interface TooltipHandler {

	/**
	 * Adds a tooltip triggerer.
	 * 
	 * @param elem
	 *            triggerer to add.
	 */
	void registerTooltipTriggerer(TooltipTriggerer elem);

	/**
	 * Removes a tooltip triggerer.
	 * 
	 * @param elem
	 *            triggerer to remove.
	 */
	void removeTooltipTriggerer(TooltipTriggerer elem);

	void showItemTooltip(Point p, Item item);

	void showTooltip(Point p, String text);

	void hideTooltip();

}
