package de.illonis.eduras.events;

import java.net.SocketAddress;

/**
 * This event is used as an initial UDP event from the client to tell the server
 * the port.
 * 
 * @author Florian Mai <florian.ren.mai@googlemail.com>
 * 
 */
public class UDPHiEvent extends NetworkEvent {

	SocketAddress clientAddress;

	/**
	 * Create a new UDPHiEvent.
	 * 
	 * @param client
	 *            The client's id, who sends this event.
	 */
	public UDPHiEvent(int client) {
		super(NetworkEventNumber.UDP_HI, client);
	}

}
