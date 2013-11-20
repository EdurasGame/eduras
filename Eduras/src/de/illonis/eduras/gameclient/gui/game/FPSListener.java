package de.illonis.eduras.gameclient.gui.game;

/**
 * Processes fps information.
 * 
 * @author illonis
 * 
 */
public interface FPSListener {

	/**
	 * Indicates that fps value has changed.
	 * 
	 * @param value
	 *            new fps value.
	 */
	void setFPS(int value);

}
