package de.illonis.eduras.networking;

import de.illonis.eduras.events.Event;
import de.illonis.eduras.networking.ClientSender.PacketType;

/**
 * First dummy implementation of a networkPolicy
 * 
 * @author Florian Mai <florian.ren.mai@googlemail.com>
 * 
 */
public class InetPolizei extends NetworkPolicy {

	@Override
	public PacketType determinePacketType(Event event) {
		return PacketType.TCP;
	}

}
