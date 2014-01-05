package de.illonis.eduras.networking;

import de.eduras.eventingserver.Event;
import de.eduras.eventingserver.Event.PacketType;
import de.eduras.eventingserver.NetworkPolicy;
import de.illonis.eduras.events.GameEvent.GameEventNumber;

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
		GameEventNumber number = GameEventNumber.getByNumber(event
				.getEventNumber());
		if (number == GameEventNumber.SET_POS_UDP
				|| number == GameEventNumber.SET_ROTATION) {
			return PacketType.UDP;
		} else {
			return PacketType.TCP;
		}
	}
}
