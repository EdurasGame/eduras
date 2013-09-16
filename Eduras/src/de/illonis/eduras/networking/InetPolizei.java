package de.illonis.eduras.networking;

import de.illonis.eduras.events.Event;
import de.illonis.eduras.events.GameEvent;
import de.illonis.eduras.events.GameEvent.GameEventNumber;
import de.illonis.eduras.events.NetworkEvent;
import de.illonis.eduras.events.NetworkEvent.NetworkEventNumber;
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
		if (event instanceof GameEvent) {
			GameEvent gameEvent = (GameEvent) event;
			if (gameEvent.getType() == GameEventNumber.SET_POS_UDP) {
				return PacketType.UDP;
			}
		}
		if (event instanceof NetworkEvent) {
			NetworkEvent networkEvent = (NetworkEvent) event;
			if (networkEvent.getType() == NetworkEventNumber.UDP_HI) {
				return PacketType.UDP;
			}
		}
		return PacketType.TCP;
	}

}
