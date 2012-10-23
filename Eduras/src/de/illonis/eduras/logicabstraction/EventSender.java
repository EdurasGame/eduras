/**
 * 
 */
package de.illonis.eduras.logicabstraction;

import de.illonis.eduras.events.GameEvent;
import de.illonis.eduras.exceptions.MessageNotSupportedException;
import de.illonis.eduras.exceptions.WrongEventTypeException;
import de.illonis.eduras.interfaces.GameLogicInterface;
import de.illonis.eduras.networking.Client;
import de.illonis.eduras.networking.NetworkMessageSerializer;

/**
 * This class provides a connection between GUI and logic for sending events, on
 * client side as well as on server side. GUI developers can use the methods
 * provided here to send events. Note that you can only send events that are
 * user-events.
 * 
 * @author Florian Mai <florian.ren.mai@googlemail.com>
 * 
 */
public class EventSender {

	Client client;
	GameLogicInterface logic;

	/**
	 * Creates an EventSender that uses the given client to forward events via
	 * network and to the client's game logic.
	 * 
	 * @param client The client to use.
	 */
	EventSender(Client client) {

		this.client = client;
		this.logic = client.getLogic();
	}

	/**
	 * You shall use this method to mark that the user triggered some event.
	 * 
	 * @param event
	 *            The event that occured
	 * 
	 *            Note: You can only send events that are marked as to be a
	 *            user-event.
	 * @throws WrongEventTypeException
	 *             Occurs if you want to send an event that is no user-event.
	 * @throws MessageNotSupportedException
	 *             Occurs if the given event is not yet supported by the
	 *             serializer.
	 */
	public void sendEvent(GameEvent event) throws WrongEventTypeException,
			MessageNotSupportedException {

		// TODO: Think of a rearragement of the event to make it fit better to a
		// distinction of events.
		if (!(event.getType().getNumber() < 100)) {
			throw new WrongEventTypeException(event);
		}

		String msg = NetworkMessageSerializer.serialize(event);

		client.sendMessage(msg);
		logic.onGameEventAppeared(event);
	}
}
