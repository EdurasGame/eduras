package de.illonis.eduras.gameclient;

/**
 * Handles triggers from the connectin panel.
 * 
 * @author illonis
 * 
 */
public interface ProgressPanelReactor {

	/**
	 * Indicates that user aborted connection establishing.
	 */
	void abort();
}
