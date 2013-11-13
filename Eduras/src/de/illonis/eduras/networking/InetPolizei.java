package de.illonis.eduras.networking;

import de.eduras.eventingserver.Event;
import de.eduras.eventingserver.Event.PacketType;
import de.eduras.eventingserver.NetworkPolicy;

/**
 * This is a first attempt to improve network performance by introducing UDP
 * messages. One day, when there is more or less final decisions on what to send
 * in which way, we might want to rename this (maybe).
 * 
 * @author Florian Mai <florian.ren.mai@googlemail.com>
 * 
 */
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
