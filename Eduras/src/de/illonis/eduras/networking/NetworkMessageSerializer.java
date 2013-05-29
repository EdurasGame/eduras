package de.illonis.eduras.networking;

import de.illonis.eduras.events.ClientRenameEvent;
import de.illonis.eduras.events.ConnectionAbortedEvent;
import de.illonis.eduras.events.ConnectionEstablishedEvent;
import de.illonis.eduras.events.DeathEvent;
import de.illonis.eduras.events.Event;
import de.illonis.eduras.events.GameEvent;
import de.illonis.eduras.events.GameInfoRequest;
import de.illonis.eduras.events.InitInformationEvent;
import de.illonis.eduras.events.ItemEvent;
import de.illonis.eduras.events.MatchEndEvent;
import de.illonis.eduras.events.MovementEvent;
import de.illonis.eduras.events.NetworkEvent;
import de.illonis.eduras.events.ObjectFactoryEvent;
import de.illonis.eduras.events.SetBooleanGameObjectAttributeEvent;
import de.illonis.eduras.events.SetGameModeEvent;
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
	 * @throws MessageNotSupportedException
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
					.getClient();
			break;
		case CONNECTION_ABORTED:
			serializedEvent += ((ConnectionAbortedEvent) networkEvent)
					.getClient();
			break;
		case NO_EVENT:
			break;
		case INIT_INFORMATION:
			InitInformationEvent initEvent = (InitInformationEvent) networkEvent;
			serializedEvent += concatenateWithDel("#", initEvent.getRole()
					.getTypeNum(), initEvent.getName(), initEvent.getClient());
			break;
		case GAME_READY:
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

		switch (gameEvent.getType()) {
		case DEATH:
			DeathEvent death = (DeathEvent) gameEvent;
			serializedEvent = buildEventString(death, death.getKilled(),
					death.getKillerOwner());
			break;
		case MOVE_DOWN_PRESSED:
		case MOVE_DOWN_RELEASED:
		case MOVE_LEFT_PRESSED:
		case MOVE_LEFT_RELEASED:
		case MOVE_RIGHT_PRESSED:
		case MOVE_RIGHT_RELEASED:
		case MOVE_UP_PRESSED:
		case MOVE_UP_RELEASED:
			serializedEvent = buildEventString(gameEvent,
					((UserMovementEvent) gameEvent).getOwner());
			break;
		case NO_EVENT:
			break;
		case OBJECT_CREATE:
			ObjectFactoryEvent createEvent = (ObjectFactoryEvent) gameEvent;
			serializedEvent = buildEventString(createEvent,
					createEvent.getId(), createEvent.getOwner(), createEvent
							.getObjectType().getNumber());
			break;
		case OBJECT_REMOVE:
			ObjectFactoryEvent removeEvent = (ObjectFactoryEvent) gameEvent;
			serializedEvent = buildEventString(removeEvent, removeEvent.getId());
			break;
		case SET_KILLS:
		case SET_DEATHS:
		case SETMAXHEALTH:
		case SETHEALTH:
			SetIntegerGameObjectAttributeEvent igo = (SetIntegerGameObjectAttributeEvent) gameEvent;
			serializedEvent = buildEventString(igo, igo.getObjectId(),
					igo.getNewValue());
			break;
		case SETSPEED:
		case SETSPEEDVECTOR:
		case SET_POS:
			MovementEvent moveEvent = (MovementEvent) gameEvent;
			serializedEvent = buildEventString(moveEvent,
					moveEvent.getObjectId(), moveEvent.getNewXPos(),
					moveEvent.getNewYPos());
			break;
		case SET_OWNER:
			SetOwnerEvent setOwnerEvent = (SetOwnerEvent) gameEvent;
			serializedEvent = buildEventString(setOwnerEvent,
					setOwnerEvent.getObjectId(), setOwnerEvent.getOwner());
			break;
		case SET_COLLIDABLE:
		case SET_VISIBLE:
			SetBooleanGameObjectAttributeEvent setAttributeEvent = (SetBooleanGameObjectAttributeEvent) gameEvent;
			serializedEvent = buildEventString(setAttributeEvent,
					setAttributeEvent.getObjectId(),
					setAttributeEvent.getNewValue());
			break;
		case ITEM_USE:
			ItemEvent itemEvent = (ItemEvent) gameEvent;
			Vector2D target = itemEvent.getTarget();
			serializedEvent = buildEventString(itemEvent,
					itemEvent.getSlotNum(), itemEvent.getOwner(),
					target.getX(), target.getY());
			break;
		case INFORMATION_REQUEST:
			serializedEvent = buildEventString(gameEvent,
					((GameInfoRequest) gameEvent).getRequester());
			break;
		case CLIENT_SETNAME:
			ClientRenameEvent e = (ClientRenameEvent) gameEvent;
			serializedEvent = buildEventString(e, e.getOwner(), e.getName());
			break;
		case SET_ITEM_SLOT:
			SetItemSlotEvent sis = (SetItemSlotEvent) gameEvent;
			serializedEvent = buildEventString(sis, sis.getOwner(),
					sis.getObjectId(), sis.getItemSlot());
			break;
		case MATCH_END:
			MatchEndEvent matchEndEvent = (MatchEndEvent) gameEvent;
			serializedEvent = buildEventString(matchEndEvent,
					matchEndEvent.getWinnerId());
			break;
		case SET_GAMEMODE:
			SetGameModeEvent setGameMode = (SetGameModeEvent) gameEvent;
			serializedEvent = buildEventString(setGameMode,
					setGameMode.getNewMode());
			break;
		default:
			throw new MessageNotSupportedException(gameEvent.getType(),
					"There does not exist a serialization for the given event yet!");
		}
		if (serializedEvent.endsWith("#"))
			throw new MessageNotSupportedException(gameEvent.getType(),
					"There does not exist a serialization for the given event yet!");
		return serializedEvent;

	}

	/**
	 * Creates an event string based on given event and parameters. The returned
	 * string is the serialized version of given parameters according to event
	 * string syntax (including ## and so on). You should pass arguments
	 * appropriate to event list in documentation.<br>
	 * <b>Note:</b> This method does not check for valid argument order or
	 * id/argument combination.
	 * 
	 * @param event
	 *            the event.
	 * @param args
	 *            arguments. Non-String arguments will converted using
	 *            {@link Object#toString()} method automatically.
	 * @return serialized eventstring.
	 */
	private static String buildEventString(GameEvent event, Object... args) {
		StringBuilder builder = new StringBuilder("##");
		builder.append(event.getType().toString());
		for (Object object : args) {
			builder.append("#");
			builder.append(object.toString());
		}
		return builder.toString();
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
	 * @return a string representing all strings delimited with given delimiter.
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
