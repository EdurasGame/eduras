package de.illonis.eduras.gameclient.gui.hud;

import de.illonis.eduras.gameclient.gui.game.FPSListener;

/**
 * Similar to {@link FPSListener}, this interface shall be implemented by a
 * class that can process ping information.
 * 
 * @author Florian Mai <florian.ren.mai@googlemail.com>
 * 
 */
public interface PingListener {

	/**
	 * Called when new ping information has arrived.
	 * 
	 * @param latency
	 *            The new ping.
	 */
	public void setPING(long latency);
}
