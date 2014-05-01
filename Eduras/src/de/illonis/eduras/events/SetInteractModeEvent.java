package de.illonis.eduras.events;

import de.illonis.eduras.units.InteractMode;

/**
 * Indicates the setting of a interact mode by server for a specific player.
 * 
 * @author illonis
 * 
 */
public class SetInteractModeEvent extends OwnerGameEvent {

	private final InteractMode newMode;

	/**
	 * Creates a new event.
	 * 
	 * @param playerOwnerId
	 *            the ownerid of the player.
	 * @param newMode
	 *            the new mode.
	 */
	public SetInteractModeEvent(int playerOwnerId, InteractMode newMode) {
		super(GameEventNumber.SET_INTERACTMODE, playerOwnerId);
		this.newMode = newMode;
		putArgument(newMode.toString());
	}

	/**
	 * @return the new mode.
	 */
	public InteractMode getNewMode() {
		return newMode;
	}

}
