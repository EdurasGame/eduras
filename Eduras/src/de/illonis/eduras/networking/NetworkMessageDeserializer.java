package de.illonis.eduras.networking;

import java.util.LinkedList;

import de.illonis.eduras.ObjectFactory.ObjectType;
import de.illonis.eduras.events.ClientRenameEvent;
import de.illonis.eduras.events.ConnectionAbortedEvent;
import de.illonis.eduras.events.ConnectionEstablishedEvent;
import de.illonis.eduras.events.Event;
import de.illonis.eduras.events.GameEvent;
import de.illonis.eduras.events.GameEvent.GameEventNumber;
import de.illonis.eduras.events.GameInfoRequest;
import de.illonis.eduras.events.GameReadyEvent;
import de.illonis.eduras.events.InitInformationEvent;
import de.illonis.eduras.events.ItemEvent;
import de.illonis.eduras.events.MatchEndEvent;
import de.illonis.eduras.events.MovementEvent;
import de.illonis.eduras.events.NetworkEvent;
import de.illonis.eduras.events.NetworkEvent.NetworkEventNumber;
import de.illonis.eduras.events.NoEvent;
import de.illonis.eduras.events.ObjectFactoryEvent;
import de.illonis.eduras.events.SetBooleanGameObjectAttributeEvent;
import de.illonis.eduras.events.SetIntegerGameObjectAttributeEvent;
import de.illonis.eduras.events.SetItemSlotEvent;
import de.illonis.eduras.events.SetOwnerEvent;
import de.illonis.eduras.events.UserMovementEvent;
import de.illonis.eduras.exceptions.GivenParametersDoNotFitToEventException;
import de.illonis.eduras.exceptions.InvalidMessageFormatException;
import de.illonis.eduras.exceptions.MessageNotSupportedException;
import de.illonis.eduras.logger.EduLog;
import de.illonis.eduras.networking.ServerClient.ClientRole;

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
		EduLog.info("[DESERIALIZE] orig: " + eventString);
		// FIXME: Fix StringIndexOutOfBoundsException when eventString equals ""
		String[] messages;
		try {
			messages = eventString.substring(2).split("##");
		} catch (StringIndexOutOfBoundsException e) {
			EduLog.error("Fix: Stringindexoutofbounds.");
			return events;
		}

		for (String msg : messages) {

			if (msg == null)
				continue;

			EduLog.info("message: " + msg);
			try {
				Event ge = deserializeMessage(msg);
				events.add(ge);
			} catch (InvalidMessageFormatException e) {
				EduLog.passException(e);
			} catch (GivenParametersDoNotFitToEventException e) {
				EduLog.passException(e);
			} catch (MessageNotSupportedException e) {
				EduLog.passException(e);
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
	private static Event deserializeMessage(String msg)
			throws InvalidMessageFormatException,
			GivenParametersDoNotFitToEventException,
			MessageNotSupportedException {
		if (msg.isEmpty())
			throw new InvalidMessageFormatException(
					"Message is empty (length 0)", msg);
		String[] args = msg.split("#");
		if (args.length < 2)
			throw new InvalidMessageFormatException(
					"Message has not enough arguments (less than two).", msg);

		// try to extract event type
		int typeInt;
		try {
			typeInt = parseInt(args[0]);
		} catch (NumberFormatException e) {
			throw new InvalidMessageFormatException(
					"Event id of message is no valid integer value: " + args[0],
					msg);
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
	private static Event handleNetworkEvent(String msg, String[] args,
			int typeInt) {

		NetworkEventNumber typeNumber = NetworkEvent
				.toNetworkEventNumber(typeInt);
		NetworkEvent networkEvent = null;

		switch (typeNumber) {
		case CONNECTION_ABORTED:
			int disconnectClientId = parseInt(args[1]);
			networkEvent = new ConnectionAbortedEvent(disconnectClientId);
			break;
		case CONNECTION_ESTABLISHED:
			int clientId = parseInt(args[1]);
			networkEvent = new ConnectionEstablishedEvent(clientId);
			break;
		case NO_EVENT:
			break;
		case INIT_INFORMATION:
			int clientRoleNum = parseInt(args[1]);
			ClientRole clientRole = ClientRole.getValueOf(clientRoleNum);
			String name = args[2];
			networkEvent = new InitInformationEvent(clientRole, name);
			break;
		case GAME_READY:
			networkEvent = new GameReadyEvent();
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
	private static Event handleGameEvent(String msg, String[] args, int typeInt)
			throws InvalidMessageFormatException,
			GivenParametersDoNotFitToEventException,
			MessageNotSupportedException {
		GameEventNumber typeNumber = GameEvent.toGameEventNumber(typeInt);
		GameEvent gameEvent = null;

		switch (typeNumber) {
		case SETSPEED:
		case SETSPEEDVECTOR:
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
		case ITEM_USE:
			gameEvent = handleItemEvent(msg, args, typeNumber);
			break;
		case INFORMATION_REQUEST:
			gameEvent = new GameInfoRequest(parseInt(args[1]));
			break;
		case SET_OWNER:
			gameEvent = new SetOwnerEvent(parseInt(args[2]), parseInt(args[1]));
			break;
		case SET_VISIBLE:
		case SET_COLLIDABLE:
			gameEvent = new SetBooleanGameObjectAttributeEvent(typeNumber,
					parseInt(args[1]), parseBool(args[2]));
			break;
		case OBJECT_CREATE:
			int objectTypeNum = parseInt(args[3]);
			ObjectType objectType = ObjectType
					.getObjectTypeByNumber(objectTypeNum);
			ObjectFactoryEvent objectFactoryEventCreate = new ObjectFactoryEvent(
					GameEventNumber.OBJECT_CREATE, objectType);
			objectFactoryEventCreate.setId(parseInt(args[1]));
			objectFactoryEventCreate.setOwner(parseInt(args[2]));
			gameEvent = objectFactoryEventCreate;
			break;
		case OBJECT_REMOVE:
			ObjectFactoryEvent objectFactoryEventRemove = new ObjectFactoryEvent(
					GameEventNumber.OBJECT_REMOVE, null);
			objectFactoryEventRemove.setId(parseInt(args[1]));
			gameEvent = objectFactoryEventRemove;
			break;
		case CLIENT_SETNAME:
			try {
				gameEvent = new ClientRenameEvent(parseInt(args[1]), args[2]);
			} catch (Exception e) {
				gameEvent = new NoEvent();
			}

			break;
		case SET_ITEM_SLOT:
			gameEvent = new SetItemSlotEvent(parseInt(args[2]),
					parseInt(args[1]), parseInt(args[3]));
			break;
		case NO_EVENT:
			gameEvent = new NoEvent();
			break;
		case SETHEALTH:
			gameEvent = new SetIntegerGameObjectAttributeEvent(
					GameEventNumber.SETHEALTH, parseInt(args[1]),
					parseInt(args[2]));
			break;
		case MATCH_END:
			gameEvent = new MatchEndEvent(parseInt(args[1]));
			break;
		default:
			throw new MessageNotSupportedException(typeNumber, msg);
		}

		return gameEvent;
	}

	/**
	 * Parses a string into a boolean.
	 * 
	 * @param string
	 *            The string to parse.
	 * @return The parsed boolean.
	 */
	private static boolean parseBool(String string) {
		return Boolean.parseBoolean(string);
	}

	/**
	 * Handles an item event.
	 * 
	 * @param msg
	 *            The original message.
	 * @param args
	 *            The arguments of the message.
	 * @param typeNumber
	 *            The type number of the event.
	 * @return Returns the deserialized item event.
	 */
	private static GameEvent handleItemEvent(String msg, String[] args,
			GameEventNumber typeNumber) {
		ItemEvent itemEvent = new ItemEvent(typeNumber, parseInt(args[2]));
		itemEvent.setSlotNum(parseInt(args[1]));

		if (typeNumber == GameEventNumber.ITEM_USE) {
			itemEvent.setTargetX(parseDouble(args[3]));
			itemEvent.setTargetY(parseDouble(args[4]));
		}

		return itemEvent;
	}

	private static GameEvent handleStartMovementEvent(String msg,
			String[] args, GameEventNumber typeNumber)
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
	private static GameEvent handleMovementPositionEvent(String fullMessage,
			String[] splittedMessage, GameEventNumber typeNumber)
			throws InvalidMessageFormatException,
			GivenParametersDoNotFitToEventException {
		if (splittedMessage.length != 4)
			throw new InvalidMessageFormatException(
					"Invalid number of arguments: " + splittedMessage.length
							+ " instead of 4.", fullMessage);

		try {
			int id = parseInt(splittedMessage[1]);
			double newXPos = parseDouble(splittedMessage[2]);
			double newYPos = parseDouble(splittedMessage[3]);
			MovementEvent moveEvent = new MovementEvent(typeNumber, id);
			moveEvent.setNewXPos(newXPos);
			moveEvent.setNewYPos(newYPos);
			return moveEvent;
		} catch (NumberFormatException e) {
			throw new GivenParametersDoNotFitToEventException(typeNumber,
					splittedMessage[1], splittedMessage[2], splittedMessage[3]);
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

	/**
	 * Uses Double.parseDouble to parse a string into a double.
	 * 
	 * @param str
	 *            The string to parse.
	 * @return The parsed double value.
	 */
	private static double parseDouble(String str) {
		return Double.parseDouble(str);
	}

	/**
	 * Returns a specific argument from given serialized message.<br>
	 * Note that this is not very fast. Use other methods if you want to get
	 * multiple arguments. This method does not check whether serialized message
	 * is correct.
	 * 
	 * @param message
	 *            serialized message to parse.
	 * @param argument
	 *            argument to look at. Note that first argument is
	 *            GameEventNumber.
	 * @return selected argument of given message.
	 * @throws There
	 *             is a nullpointerexception thrown if something goes wrong with
	 *             the string.
	 */
	public static String getArgumentFromMessage(String message, int argument) {

		// FIXME: Very important: try to find out why a message can be
		// errornous.
		try {
			String[] parts = message.substring(2).split("#");
			return parts[argument];
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Extracts a {@link GameEventNumber} from given serialized message.<br>
	 * Note that this is not very fast. Use other methods if you want to get
	 * multiple arguments. This method does not check whether serialized message
	 * is correct.
	 * 
	 * @see #getArgumentFromMessage(String, int)
	 * @param msg
	 *            Message to parse.
	 * @return A {@link GameEventNumber} used in given message.
	 */
	public static GameEventNumber extractGameEventNumber(String msg) {
		return GameEvent.toGameEventNumber(parseInt(getArgumentFromMessage(msg,
				0)));
	}
}