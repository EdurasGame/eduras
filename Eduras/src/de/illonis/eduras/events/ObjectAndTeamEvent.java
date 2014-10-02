package de.illonis.eduras.events;

public class ObjectAndTeamEvent extends GameEvent {

	private int objectId;
	private int teamId;

	/**
	 * Creates this event
	 * 
	 * @param type
	 * @param objectId
	 * @param teamId
	 */
	public ObjectAndTeamEvent(GameEventNumber type, int objectId, int teamId) {
		super(type);

		this.objectId = objectId;
		this.teamId = teamId;

		putArgument(objectId);
		putArgument(teamId);
	}

	public int getObjectId() {
		return objectId;
	}

	public int getTeamId() {
		return teamId;
	}
}
