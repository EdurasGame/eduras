package de.illonis.eduras.chat;

import de.eduras.eventingserver.Event;
import de.eduras.eventingserver.Event.PacketType;
import de.eduras.eventingserver.NetworkPolicy;

/**
 * Defines the {@link NetworkPolicy} for the {@link Chat}.
 * 
 * @author Florian Mai <florian.ren.mai@googlemail.com>
 * 
 */
class ChatPolicy extends NetworkPolicy {

	@Override
	public PacketType determinePacketType(Event event) {
		return PacketType.TCP;
	}
}
