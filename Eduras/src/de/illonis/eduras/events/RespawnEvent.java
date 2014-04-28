package de.illonis.eduras.events;

/**
 * Indicates a player has been revived.
 * 
 * @author illonis
 * 
 */
public class RespawnEvent extends OwnerGameEvent {

	/**
	 * Creates a new death event.
	 * 
	 * @param revived
	 *            owner-id of revived player.
	 */
	public RespawnEvent(int revived) {
		super(GameEventNumber.INFO_RESPAWN, revived);
	}

}
