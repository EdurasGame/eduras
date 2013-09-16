package de.illonis.eduras.gameclient;

import java.net.SocketAddress;

import de.illonis.eduras.events.NetworkEvent;

public class UDPHiEvent extends NetworkEvent {

	SocketAddress clientAddress;

	public UDPHiEvent(int client) {
		super(NetworkEventNumber.UDP_HI, client);
	}

}
