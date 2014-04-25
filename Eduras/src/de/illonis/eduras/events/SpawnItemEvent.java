package de.illonis.eduras.events;

import java.util.logging.Logger;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.ObjectFactory.ObjectType;
import de.illonis.eduras.math.Vector2df;

public class SpawnItemEvent extends RTSActionEvent {

	private final static Logger L = EduLog.getLoggerFor(SpawnItemEvent.class
			.getName());

	private ObjectType objectType;
	private Vector2df position;

	public SpawnItemEvent(int executingPlayer, ObjectType type,
			Vector2df position) {
		super(GameEventNumber.SPAWN_ITEM, executingPlayer);

		putArgument(type.getNumber());
		putArgument(position.x);
		putArgument(position.y);

		this.objectType = type;
		this.position = position;
	}

	public ObjectType getObjectType() {
		return objectType;
	}

	public Vector2df getPosition() {
		return position;
	}
}
