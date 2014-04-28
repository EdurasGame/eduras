package de.illonis.eduras.events;

import de.illonis.eduras.ObjectFactory.ObjectType;

public class CreateUnitEvent extends RTSActionEvent {

	private final ObjectType typeOfUnit;
	private final int idOfBaseToSpawnAt;

	public CreateUnitEvent(int executingPlayer, ObjectType type,
			int idOfBaseToSpawnAt) {
		super(GameEventNumber.CREATE_UNIT, executingPlayer);
		this.typeOfUnit = type;
		this.idOfBaseToSpawnAt = idOfBaseToSpawnAt;

		putArgument(type.getNumber());
		putArgument(idOfBaseToSpawnAt);
	}

	public ObjectType getTypeOfUnit() {
		return typeOfUnit;
	}

	public int getIdOfBaseToSpawnAt() {
		return idOfBaseToSpawnAt;
	}

}
