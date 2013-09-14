package de.illonis.eduras.unittests;

import static org.junit.Assert.assertEquals;

import java.util.LinkedList;

import org.junit.Before;
import org.junit.Test;

import de.illonis.eduras.ObjectFactory.ObjectType;
import de.illonis.eduras.events.Event;
import de.illonis.eduras.events.GameEvent.GameEventNumber;
import de.illonis.eduras.events.MovementEvent;
import de.illonis.eduras.events.ObjectFactoryEvent;
import de.illonis.eduras.exceptions.MessageNotSupportedException;
import de.illonis.eduras.networking.NetworkMessageDeserializer;
import de.illonis.eduras.networking.NetworkMessageSerializer;

/**
 * Tests {@link NetworkMessageSerializer} and {@link NetworkMessageDeserializer}
 * 
 * @author illonis
 * 
 */
public class NetworkMessageTests {

	private ObjectFactoryEvent objectEvent;
	private MovementEvent moveEvent;
	private double d;

	@Before
	public void init() {
		d = 13.314;
		moveEvent = new MovementEvent(GameEventNumber.SET_POS_UDP, 23);
		moveEvent.setNewXPos(d);
		moveEvent.setNewYPos(2d);
		objectEvent = new ObjectFactoryEvent(GameEventNumber.OBJECT_CREATE,
				ObjectType.PLAYER);
	}

	@Test
	public void backAndForth() {
		try {
			MovementEvent me = (MovementEvent) NetworkMessageDeserializer
					.deserialize(NetworkMessageSerializer.serialize(moveEvent))
					.getFirst();

			assertEquals(moveEvent.getNewXPos(), me.getNewXPos(), .001);
			assertEquals(moveEvent.getNewYPos(), me.getNewYPos(), .001);
			assertEquals(moveEvent.getObjectId(), me.getObjectId(), .001);
			assertEquals(moveEvent.getType(), me.getType());
		} catch (MessageNotSupportedException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void serializeTwo() {
		String s;
		try {
			s = NetworkMessageSerializer.serialize(moveEvent);
			s += NetworkMessageSerializer.serialize(objectEvent);
		} catch (MessageNotSupportedException e) {
			e.printStackTrace();
			return;
		}

		LinkedList<Event> events = NetworkMessageDeserializer.deserialize(s);

		MovementEvent me = (MovementEvent) events.getFirst();
		ObjectFactoryEvent oe = (ObjectFactoryEvent) events.getLast();

		assertEquals(moveEvent.getNewXPos(), me.getNewXPos(), .001);
		assertEquals(moveEvent.getNewYPos(), me.getNewYPos(), .001);
		assertEquals(moveEvent.getObjectId(), me.getObjectId(), .001);
		assertEquals(moveEvent.getType(), me.getType());

		assertEquals(oe.getObjectType(), objectEvent.getObjectType());
		assertEquals(oe.getId(), objectEvent.getId());
		assertEquals(oe.getType(), objectEvent.getType());
		assertEquals(oe.getOwner(), objectEvent.getOwner());

	}
}
