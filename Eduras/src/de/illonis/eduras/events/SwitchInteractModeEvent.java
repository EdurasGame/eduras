package de.illonis.eduras.events;

import de.illonis.eduras.units.InteractMode;

/**
 * Indicates a mode switching request by a player.
 * 
 * @author illonis
 * 
 */
public class SwitchInteractModeEvent extends OwnerGameEvent {

	private final InteractMode requestedMode;

	/**
	 * @param playerOwnerId
	 *            the ownerid of requesting player.
	 * @param requestedMode
	 *            the requested mode.
	 */
	public SwitchInteractModeEvent(int playerOwnerId, InteractMode requestedMode) {
		super(GameEventNumber.SWITCH_INTERACTMODE, playerOwnerId);
		this.requestedMode = requestedMode;
		putArgument(requestedMode.toString());
	}

	/**
	 * @return the requested interaction mode.
	 */
	public InteractMode getRequestedMode() {
		return requestedMode;
	}

}
