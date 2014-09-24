package de.illonis.eduras.events;

import java.util.logging.Logger;

import org.newdawn.slick.geom.Vector2f;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.ObjectFactory;

public class AoEDamageEvent extends GameEvent {
	private final static Logger L = EduLog.getLoggerFor(AoEDamageEvent.class
			.getName());

	private ObjectFactory.ObjectType type;
	private Vector2f position;

	/**
	 * Creates a AoEDamageEvent.
	 * 
	 * @param type
	 *            the type of the object that dealt the damage
	 * @param position
	 */
	public AoEDamageEvent(ObjectFactory.ObjectType type, Vector2f position) {
		super(GameEventNumber.AOE_DAMAGE);

		this.type = type;
		this.position = position;
		putArgument(type.ordinal());
		putArgument(position.x);
		putArgument(position.y);
	}

	public ObjectFactory.ObjectType getObjectType() {
		return type;
	}

	public Vector2f getPosition() {
		return position;
	}

}
