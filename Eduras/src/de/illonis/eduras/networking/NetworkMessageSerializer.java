package de.illonis.eduras.networking;

import de.illonis.eduras.events.ClientRenameEvent;
import de.illonis.eduras.events.ConnectionAbortedEvent;
import de.illonis.eduras.events.ConnectionEstablishedEvent;
import de.illonis.eduras.events.Event;
import de.illonis.eduras.events.GameEvent;
import de.illonis.eduras.events.GameInfoRequest;
import de.illonis.eduras.events.ItemEvent;
import de.illonis.eduras.events.MatchEndEvent;
import de.illonis.eduras.events.MovementEvent;
import de.illonis.eduras.events.NetworkEvent;
import de.illonis.eduras.events.ObjectFactoryEvent;
import de.illonis.eduras.events.SetBooleanGameObjectAttributeEvent;
import de.illonis.eduras.events.SetIntegerGameObjectAttributeEvent;
import de.illonis.eduras.events.SetItemSlotEvent;
import de.illonis.eduras.events.SetOwnerEvent;
import de.illonis.eduras.events.UserMovementEvent;
import de.illonis.eduras.exceptions.MessageNotSupportedException;
import de.illonis.eduras.math.Vector2D;

/**
 * Serializes different NetworkMessages.
 * 
 * @author Florian Mai <florian.ren.mai@googlemail.com>
 * 
 */
public class NetworkMessageSerializer {

	/**
	 * Serializes a GameEvent into a String
	 * 
	 * @param event
	 *            The event to be serialized.
	 * @return The string.
	 * @throws UnsupportedOperationException
	 *             Occurs if there is no serialization for the given event.
	 */
	public static String serialize(Event event)
			throws MessageNotSupportedException {

		if (event instanceof GameEvent) {
			GameEvent gameEvent = (GameEvent) event;
			return serializeGameEvent(gameEvent);
		}

		if (event instanceof NetworkEvent) {
			NetworkEvent networkEvent = (NetworkEvent) event;
			return serializeNetworkEvent(networkEvent);
		}

		return "";
	}

	/**
	 * Serializes a NetworkEvent.
	 * 
	 * @param networkEvent
	 *            The NetworkEvent to serialize.
	 * @return The serialized NetworkEvent as a string.
	 */
	private static String serializeNetworkEvent(NetworkEvent networkEvent) {
		String serializedEvent = "";

		serializedEvent += networkEvent.getType().getNumber() + "#";

		switch (networkEvent.getType()) {
		case CONNECTION_ESTABLISHED:
			serializedEvent += ((ConnectionEstablishedEvent) networkEvent)
					.getClientId();
			break;
		case CONNECTION_ABORTED:
			serializedEvent += ((ConnectionAbortedEvent) networkEvent)
					.getClientId();
			break;
		case NO_EVENT:
			break;
		default:
			break;
		}
		return "##" + serializedEvent;
	}

	/**
	 * Serializes a GameEvent.
	 * 
	 * @param gameEvent
	 *            The GameEvent to serialize.
	 * @return The serialized GameEvent as a string.
	 * @throws MessageNotSupportedException
	 */
	private static String serializeGameEvent(GameEvent gameEvent)
			throws MessageNotSupportedException {
		String serializedEvent = "";

		serializedEvent += gameEvent.getType().getNumber() + "#";

		switch (gameEvent.getType()) {
		case DEATH:
			break;
		case MOVE_DOWN_PRESSED:
		case MOVE_DOWN_RELEASED:
		case MOVE_LEFT_PRESSED:
		case MOVE_LEFT_RELEASED:
		case MOVE_RIGHT_PRESSED:
		case MOVE_RIGHT_RELEASED:
		case MOVE_UP_PRESSED:
		case MOVE_UP_RELEASED:
			serializedEvent += ((UserMovementEvent) gameEvent).getOwner() + "";
			break;
		case NO_EVENT:
			break;
		case OBJECT_CREATE:
			ObjectFactoryEvent createEvent = (ObjectFactoryEvent) gameEvent;
			serializedEvent += concatenateWithDel("#", createEvent.getId(),
					createEvent.getOwner(), createEvent.getObjectType()
							.getNumber());
			break;
		case OBJECT_REMOVE:
			ObjectFactoryEvent removeEvent = (ObjectFactoryEvent) gameEvent;
			serializedEvent += removeEvent.getId();
			break;
		case SETHEALTH:
			SetIntegerGameObjectAttributeEvent igo = (SetIntegerGameObjectAttributeEvent) gameEvent;
			serializedEvent += concatenateWithDel("#", igo.getObjectId(),
					igo.getNewValue());
			break;
		case SETSPEED:
		case SETSPEEDVECTOR:
		case SET_POS:
			MovementEvent moveEvent = (MovementEvent) gameEvent;
			serializedEvent += concatenateWithDel("#", moveEvent.getObjectId(),
					moveEvent.getNewXPos(), moveEvent.getNewYPos());
			break;
		case SET_OWNER:
			SetOwnerEvent setOwnerEvent = (SetOwnerEvent) gameEvent;
			serializedEvent += concatenateWithDel("#",
					setOwnerEvent.getObjectId(), setOwnerEvent.getOwner());
			break;
		case SET_COLLIDABLE:
		case SET_VISIBLE:
			SetBooleanGameObjectAttributeEvent setAttributeEvent = (SetBooleanGameObjectAttributeEvent) gameEvent;
			serializedEvent += concatenateWithDel("#",
					setAttributeEvent.getObjectId(),
					setAttributeEvent.getNewValue());
			break;
		case ITEM_USE:
			ItemEvent itemEvent = (ItemEvent) gameEvent;
			Vector2D target = itemEvent.getTarget();
			serializedEvent += concatenateWithDel("#", itemEvent.getSlotNum(),
					itemEvent.getOwner(), target.getX(), target.getY());
			break;
		case INFORMATION_REQUEST:
			serializedEvent += ((GameInfoRequest) gameEvent).getRequester();
			break;
		case CLIENT_SETNAME:
			ClientRenameEvent e = (ClientRenameEvent) gameEvent;
			serializedEvent += concatenateWithDel("#", e.getOwner(),
					e.getName());
			break;
		case SET_ITEM_SLOT:
			SetItemSlotEvent sis = (SetItemSlotEvent) gameEvent;
			serializedEvent += concatenateWithDel("#", sis.getOwner(),
					sis.getObjectId(), sis.getItemSlot());
			break;
		case MATCH_END:
			MatchEndEvent matchEndEvent = (MatchEndEvent) gameEvent;
			serializedEvent += concatenateWithDel("#",
					matchEndEvent.getWinnerId());
			break;
		default:
			break;
		}
		if (serializedEvent.endsWith("#"))
			throw new MessageNotSupportedException(gameEvent.getType(),
					"There does not exist a serialization for the given event yet!");
		return "##" + serializedEvent;

	}

	/**
	 * Concatenates given strings.
	 * 
	 * @param messages
	 *            Messages to concatenate
	 * @return concatenated messages
	 */

	public static String concatenate(String... messages) {
		StringBuilder b = new StringBuilder();
		for (String s : messages) {
			b.append(s);
		}
		return b.toString();
	}

	/**
	 * Concatenates given strings with specified delimiter.
	 * 
	 * @param delimiter
	 *            delimiter between strings.
	 * @param strings
	 *            strings to concatenate.
	 */
	public static String concatenateWithDel(String delimiter, Object... strings) {
		if (strings.length <= 0)
			return "";
		StringBuilder builder = new StringBuilder();
		for (Object string : strings) {
			builder.append(string.toString()).append(delimiter);
		}
		builder.delete(builder.lastIndexOf(delimiter), builder.length());
		return builder.toString();
	}

}
