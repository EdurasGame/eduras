package de.illonis.eduras.networking;

import de.eduras.eventingserver.Event;
import de.eduras.eventingserver.Event.PacketType;
import de.eduras.eventingserver.NetworkPolicy;

public class InetPolizei extends NetworkPolicy {

	@Override
	public PacketType determinePacketType(Event event) {
		if (event.getEventNumber() == EventParser.SET_POS_UDP) {
			return PacketType.UDP;
		} else {
			return PacketType.TCP;
		}
	}
}
