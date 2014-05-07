package de.illonis.eduras.events;

import de.illonis.eduras.ObjectFactory.ObjectType;

/**
 * Notifies the server about a client's wish to create a unit.
 * 
 * @author Florian 'Ren' Mai <florian.ren.mai@googlemail.com>
 * 
 */
public class CreateUnitEvent extends RTSActionEvent {

	private final ObjectType typeOfUnit;
	private final int idOfBaseToSpawnAt;

	/**
	 * Create a new instance of this event.
	 * 
	 * @param executingPlayer
	 *            The player who wants to perform the action.
	 * @param type
	 *            The type of object he wants to create.
	 * @param idOfBaseToSpawnAt
	 *            The id of the base to spawn the unit at.
	 */
	public CreateUnitEvent(int executingPlayer, ObjectType type,
			int idOfBaseToSpawnAt) {
		super(GameEventNumber.CREATE_UNIT, executingPlayer);
		this.typeOfUnit = type;
		this.idOfBaseToSpawnAt = idOfBaseToSpawnAt;

		putArgument(type.getNumber());
		putArgument(idOfBaseToSpawnAt);
	}

	/**
	 * Returns the type of unit the player wants to create.
	 * 
	 * @return type
	 */
	public ObjectType getTypeOfUnit() {
		return typeOfUnit;
	}

	/**
	 * Returns the id of the base to spawn the new unit at.
	 * 
	 * @return id of base
	 */
	public int getIdOfBaseToSpawnAt() {
		return idOfBaseToSpawnAt;
	}

}
