package de.illonis.eduras;

import de.illonis.eduras.gamemodes.GameMode;
import de.illonis.eduras.units.Unit;

/**
 * This is a dummy class for the client which does nothing, because all the
 * action needed here shall be performed on the server.
 * 
 * @author Florian Mai <florian.ren.mai@googlemail.com>
 * 
 */
public class ClientGameMode implements GameMode {

	@Override
	public String getName() {
		return "";
	}

	@Override
	public void onDeath(Unit killedUnit, Unit killingUnit) {

		// do nothing

	}

	@Override
	public void onTimeUp() {
		// do nothing

	}

	@Override
	public void onConnect(int ownerId) {
		// do nothing

	}

}
