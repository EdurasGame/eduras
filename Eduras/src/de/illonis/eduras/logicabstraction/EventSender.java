/**
 * 
 */
package de.illonis.eduras.logicabstraction;

import java.util.logging.Logger;

import de.eduras.eventingserver.ClientInterface;
import de.eduras.eventingserver.Event;
import de.eduras.eventingserver.exceptions.TooFewArgumentsExceptions;
import de.illonis.edulog.EduLog;
import de.illonis.eduras.events.GameEvent;
import de.illonis.eduras.events.GameEvent.GameEventNumber;
import de.illonis.eduras.exceptions.MessageNotSupportedException;
import de.illonis.eduras.exceptions.WrongEventTypeException;
import de.illonis.eduras.interfaces.GameLogicInterface;
import de.illonis.eduras.logic.ServerEventTriggerer;
import de.illonis.eduras.networking.EventParser;
import de.illonis.eduras.networking.InetPolizei;

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

	ClientInterface client;
	GameLogicInterface logic;

	private final static Logger L = EduLog
			.getLoggerFor(ServerEventTriggerer.class.getName());

	/**
	 * Creates an EventSender that uses the given client to forward events via
	 * network and to the client's game logic.
	 * 
	 * @param client
	 *            The client to use.
	 * @param logic
	 *            The logic to use.
	 */
	EventSender(ClientInterface client, GameLogicInterface logic) {

		this.client = client;
		client.setEventHandler(new EventParser(logic));

		// TODO: Replace inet polizei with something better!
		client.setNetworkPolicy(new InetPolizei());
		this.logic = logic;
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
	public void sendEvent(Event event) throws WrongEventTypeException,
			MessageNotSupportedException {

		GameEvent gameEvent = (GameEvent) event;

		if (!(gameEvent.getType().getNumber() < 100 || gameEvent.getType()
				.getNumber() > 200)) {
			throw new WrongEventTypeException(gameEvent);
		}

		try {
			client.sendEvent(gameEvent);
		} catch (IllegalArgumentException | TooFewArgumentsExceptions e) {
			L.warning("EventSender: " + e.getMessage());
		}
		if (gameEvent.getType() == GameEventNumber.SET_POS_UDP)
			logic.onGameEventAppeared(gameEvent);
	}
}
