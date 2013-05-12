package de.illonis.eduras.networking;

import java.util.LinkedList;

import de.illonis.eduras.events.ConnectionEstablishedEvent;
import de.illonis.eduras.events.Event;
import de.illonis.eduras.events.GameEvent;
import de.illonis.eduras.events.NetworkEvent;
import de.illonis.eduras.interfaces.GameLogicInterface;
import de.illonis.eduras.interfaces.NetworkEventListener;
import de.illonis.eduras.logger.EduLog;

/**
 * Processes messages that arrive at the client.
 * 
 * @author Florian Mai <florian.ren.mai@googlemail.com>
 * 
 *         More detailed, the incoming message is deserialized and forwarded to
 *         the gamelogic or to the network listener.
 */
public class ClientParser extends Thread {

	Client client;
	NetworkEventListener networkEventListener;
	GameLogicInterface logic;
	private final Buffer inputBuffer;

	/**
	 * Creates a new ClientLogic that deserializes the given message into an
	 * event and forwards it to the given GameLogic or to the given
	 * NetworkEventListener.
	 * 
	 * @param logic
	 *            The GameLogic the event is forwarded to.
	 * @param inputBuffer
	 *            The input buffer that this parser will read from.
	 * @param networkEventListener
	 *            The network event listener to forward NetworkEvents to.
	 * @param client
	 *            The client which belongs to the clientLogic.
	 */
	public ClientParser(GameLogicInterface logic, Buffer inputBuffer,
			NetworkEventListener networkEventListener, Client client) {
		this.client = client;
		this.logic = logic;
		this.inputBuffer = inputBuffer;
		this.networkEventListener = networkEventListener;
	}

	@Override
	public void run() {
		EduLog.info("[ClientParser] Started.");
		readFromInputBuffer();
	}

	/**
	 * Reads repeatedly from input buffer and decodes those messages.<br>
	 * This does not need a wait implementation because reading from input
	 * buffer is blocking.
	 */
	private void readFromInputBuffer() {
		while (true) {
			try {
				String s = inputBuffer.getNext();
				decodeMessage(s);
			} catch (InterruptedException e) {
				EduLog.passException(e);
			}
		}
	}

	/**
	 * Decodes given messages into one or more events and notices logic.
	 * 
	 * @param message
	 *            message to decode.
	 */
	private void decodeMessage(String message) {
		if (message.isEmpty())
			return;
		LinkedList<Event> deserializedMessages = NetworkMessageDeserializer
				.deserialize(message);
		EduLog.info("[ServerDecoder] Decoded " + deserializedMessages.size()
				+ " messages from: " + message);
		for (Event event : deserializedMessages)
			if (event instanceof GameEvent) {
				logic.onGameEventAppeared((GameEvent) event);
			} else {
				if (event instanceof ConnectionEstablishedEvent) {
					ConnectionEstablishedEvent connectionEvent = (ConnectionEstablishedEvent) event;
					EduLog.info("Received ConnectionEstablished event.");
					client.setOwnerId(connectionEvent.getClient());
				}
				networkEventListener
						.onNetworkEventAppeared((NetworkEvent) event);
			}
	}
}