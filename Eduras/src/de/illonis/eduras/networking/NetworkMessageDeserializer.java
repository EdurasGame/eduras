package de.illonis.eduras.networking;

import java.util.LinkedList;

import de.illonis.eduras.ObjectFactory.ObjectType;
import de.illonis.eduras.events.ConnectionEstablishedEvent;
import de.illonis.eduras.events.Event;
import de.illonis.eduras.events.GameEvent;
import de.illonis.eduras.events.GameEvent.GameEventNumber;
import de.illonis.eduras.events.GameInfoRequest;
import de.illonis.eduras.events.MovementEvent;
import de.illonis.eduras.events.NetworkEvent;
import de.illonis.eduras.events.NetworkEvent.NetworkEventNumber;
import de.illonis.eduras.events.ObjectFactoryEvent;
import de.illonis.eduras.events.UserMovementEvent;
import de.illonis.eduras.exceptions.GivenParametersDoNotFitToEventException;
import de.illonis.eduras.exceptions.InvalidMessageFormatException;
import de.illonis.eduras.exceptions.MessageNotSupportedException;

/**
 * Deserializes different NetworkMessages.
 * 
 * @author Florian Mai <florian.ren.mai@googlemail.com>
 * 
 */
public class NetworkMessageDeserializer {

	/**
	 * (jme) Deserializes a string into a list of GameEvents.
	 * 
	 * @param eventString
	 *            The string to be deserialized.
	 * @return A list of deserialized GameEvents.<br>
	 *         <b>Note:</b> This function is more or less able to identify
	 *         invalid strings. If a string is rated to be invalid, the function
	 *         proceeds deserializing from the next hashtag and omits all
	 *         characters up to the hash.
	 */
	public static LinkedList<Event> deserialize(String eventString) {
		LinkedList<Event> events = new LinkedList<Event>();
		System.out.println("[DESERIALIZE] orig: " + eventString);
		String[] messages = eventString.substring(2).split("##");

		for (String msg : messages) {
			System.out.println("message: " + msg);
			try {
				Event ge = deserializeMessage(msg);
				events.add(ge);
			} catch (InvalidMessageFormatException e) {
				e.printStackTrace();
			} catch (GivenParametersDoNotFitToEventException e) {
				e.printStackTrace();
			} catch (MessageNotSupportedException e) {
				e.printStackTrace();
			}
		}

		return events;
	}

	/**
	 * (jme) Deserializes given single message and returns a {@link Event}
	 * representing it.<br>
	 * See {@link #deserialize(String)}.
	 * 
	 * @param msg
	 *            Message to deserialized.
	 * @return An Event describing serialized message.
	 * @throws InvalidMessageFormatException
	 *             Thrown if message has an invalid format. Especially when it
	 *             has no or too less arguments.
	 * @throws GivenParametersDoNotFitToEventException
	 *             Thrown if generation of gameevent failed.
	 * @throws MessageNotSupportedException
	 *             Thrown if retrieved message is not supported.
	 * 
	 */
	private static Event deserializeMessage(String msg) throws InvalidMessageFormatException, GivenParametersDoNotFitToEventException,
			MessageNotSupportedException {
		if (msg.isEmpty())
			throw new InvalidMessageFormatException("Message is empty (length 0)", msg);
		String[] args = msg.split("#");
		if (args.length < 2)
			throw new InvalidMessageFormatException("Message has not enough arguments (less than two).", msg);

		// try to extract event type
		int typeInt;
		try {
			typeInt = Integer.parseInt(args[0]);
		} catch (NumberFormatException e) {
			throw new InvalidMessageFormatException("Event id of message is no valid integer value: " + args[0], msg);
		}

		Event result = null;

		// is it a game event or a network event?
		if (isGameEvent(typeInt)) {
			result = handleGameEvent(msg, args, typeInt);
		} else {
			result = handleNetworkEvent(msg, args, typeInt);
		}
		return result;
	}

	/**
	 * Deserializes a NetworkEvent message.
	 * 
	 * @param msg
	 *            The full message.
	 * @param args
	 *            The arguments of the message.
	 * @param typeInt
	 *            The number representing the type of the message.
	 * @return Returns the deserialized event.
	 */
	private static Event handleNetworkEvent(String msg, String[] args, int typeInt) {

		NetworkEventNumber typeNumber = NetworkEvent.toNetworkEventNumber(typeInt);
		NetworkEvent networkEvent = null;

		switch (typeNumber) {
		case CONNECTION_ABORTED:
			break;
		case CONNECTION_ESTABLISHED:
			int clientId = Integer.parseInt(args[0]);
			System.out.println("deser:");
			System.out.println(args);
			networkEvent = new ConnectionEstablishedEvent(clientId);
			break;
		case NO_EVENT:
			break;
		default:
			// TODO: Maybe we should generalize NetworkEventNumber and
			// GameEventNumber by a super-enum to be able to throw an exception
			// here easily.
			break;

		}
		return networkEvent;
	}

	/**
	 * Returns if the given number represents a GameEvent or not.
	 * 
	 * @param typeInt
	 *            The number to check.
	 * @return Returns true if the number represents a GameEvent and false
	 *         otherwise.
	 */
	private static boolean isGameEvent(int typeInt) {

		return typeInt < 200;
	}

	/**
	 * Deserializes a GameEvent
	 * 
	 * @param msg
	 *            The full message.
	 * @param args
	 *            The arguments of the message beginning with the first argument
	 *            args[1].
	 * @return Returns the deserialized event.
	 * @throws GivenParametersDoNotFitToEventException
	 *             Thrown if generation of gameevent failed.
	 * @throws InvalidMessageFormatException
	 *             Thrown if message has an invalid format. Especially when it
	 *             has no or too less arguments.
	 * @throws MessageNotSupportedException
	 *             Thrown if the given message is not yet supported by
	 *             deserialization.
	 */
	private static Event handleGameEvent(String msg, String[] args, int typeInt) throws InvalidMessageFormatException,
			GivenParametersDoNotFitToEventException, MessageNotSupportedException {
		GameEventNumber typeNumber = GameEvent.toGameEventNumber(typeInt);
		GameEvent gameEvent = null;

		switch (typeNumber) {
		case SET_POS:
			gameEvent = handleMovementPositionEvent(msg, args, typeNumber);
			break;
		case MOVE_DOWN_RELEASED:
		case MOVE_LEFT_RELEASED:
		case MOVE_RIGHT_RELEASED:
		case MOVE_UP_RELEASED:	
		case MOVE_DOWN_PRESSED:
		case MOVE_RIGHT_PRESSED:
		case MOVE_LEFT_PRESSED:
		case MOVE_UP_PRESSED:
			gameEvent = handleStartMovementEvent(msg, args, typeNumber);
			break;
		case INFORMATION_REQUEST:
			gameEvent = new GameInfoRequest(parseInt(args[1]));
			break;
		case OBJECT_CREATE:
			int objectTypeNum = parseInt(args[3]);
			ObjectType objectType = ObjectType.getObjectTypeByNumber(objectTypeNum);
			ObjectFactoryEvent objectFactoryEvent = new ObjectFactoryEvent(GameEventNumber.OBJECT_CREATE, objectType);
			objectFactoryEvent.setId(parseInt(args[1]));
			objectFactoryEvent.setOwnerId(parseInt(args[2]));
			gameEvent = objectFactoryEvent;
			break;
		case OBJECT_REMOVE:

		default:
			throw new MessageNotSupportedException(typeNumber, msg);
		}

		return gameEvent;
	}

	private static GameEvent handleStartMovementEvent(String msg, String[] args, GameEventNumber typeNumber)
			throws GivenParametersDoNotFitToEventException {

		int owner = parseInt(args[1]);
		if (owner < 0)
			throw new GivenParametersDoNotFitToEventException(typeNumber, args);
		return new UserMovementEvent(typeNumber, owner);
	}

	// TODO: fix and enhance javadoc
	/**
	 * (jme) Handles a position event.
	 * 
	 * @param fullMessage
	 *            Full message
	 * @param splittedMessage
	 *            Message split at #.
	 * @param typeNumber
	 *            typenumber of gameevent.
	 * @throws InvalidMessageFormatException
	 *             Thrown if message has an invalid format. Especially when it
	 *             has no or too less arguments.
	 * @throws GivenParametersDoNotFitToEventException
	 *             Thrown if generation of gameevent failed.
	 */
	private static GameEvent handleMovementPositionEvent(String fullMessage, String[] splittedMessage, GameEventNumber typeNumber)
			throws InvalidMessageFormatException, GivenParametersDoNotFitToEventException {
		if (splittedMessage.length != 4)
			throw new InvalidMessageFormatException("Invalid number of arguments: " + splittedMessage.length + " instead of 4.", fullMessage);

		try {
			int id = Integer.parseInt(splittedMessage[1]);
			int newXPos = Integer.parseInt(splittedMessage[2]);
			int newYPos = Integer.parseInt(splittedMessage[3]);
			MovementEvent moveEvent = new MovementEvent(typeNumber, id);
			moveEvent.setNewXPos(newXPos);
			moveEvent.setNewYPos(newYPos);
			return moveEvent;
		} catch (NumberFormatException e) {
			throw new GivenParametersDoNotFitToEventException(typeNumber, splittedMessage[1], splittedMessage[2], splittedMessage[3]);
		}
	}

	/**
	 * Uses Integer.parseInt to parse a string into an integer.
	 * 
	 * @param str
	 *            The string to parse.
	 * @return The parsed integer.
	 */
	private static int parseInt(String str) {
		return Integer.parseInt(str);
	}
}
