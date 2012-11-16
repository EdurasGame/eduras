/**
 * 
 */
package de.illonis.eduras.interfaces;

import de.illonis.eduras.events.NetworkEvent;

/**
 * This interface serves as a listener for NetworkEvents. You as a GUI developer
 * should implement this interface to handle networkevents as you wish.
 * 
 * @author Florian Mai <florian.ren.mai@googlemail.com>
 * 
 */
public interface NetworkEventListener {

	/**
	 * This function gets called when a network event appears.
	 * 
	 * @param event
	 *            The appearing event.
	 */
	public void onNetworkEventAppeared(NetworkEvent event);

}
