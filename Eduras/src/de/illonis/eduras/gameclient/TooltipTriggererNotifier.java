package de.illonis.eduras.gameclient;

import de.illonis.eduras.gameclient.gui.guielements.TooltipTriggerer;

/**
 * Manages and notifies tooltip triggerers.
 * 
 * @author illonis
 * 
 */
public interface TooltipTriggererNotifier {

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

}