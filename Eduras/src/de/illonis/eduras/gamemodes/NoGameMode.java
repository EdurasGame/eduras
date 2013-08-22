package de.illonis.eduras.gamemodes;

import de.illonis.eduras.GameInformation;
import de.illonis.eduras.ObjectFactory.ObjectType;
import de.illonis.eduras.logic.EventTriggerer;
import de.illonis.eduras.units.PlayerMainFigure;
import de.illonis.eduras.units.Unit;

/**
 * This game mode is one that has got no winning conditions, does not care for
 * statistics and respawns dead players immediately.
 * 
 * @author Florian Mai <florian.ren.mai@googlemail.com>
 * 
 */
public class NoGameMode implements GameMode {

	private GameInformation gameInfo;

	/**
	 * Creates a new instance of this gamemode.
	 * 
	 * @param gameInfo
	 */
	public NoGameMode(GameInformation gameInfo) {
		this.gameInfo = gameInfo;
	}

	@Override
	public String getName() {
		return "NONE";
	}

	@Override
	public void onDeath(Unit killedUnit, int killingUnit) {
		// The player's health is set to max and he is respawned somewhere.
		// Other units are simply removed.

		EventTriggerer triggerer = gameInfo.getEventTriggerer();

		if (killedUnit instanceof PlayerMainFigure) {
			PlayerMainFigure killedPlayer = (PlayerMainFigure) killedUnit;

			triggerer.respawnPlayer(killedPlayer);
		} else {
			triggerer.removeObject(killedUnit.getId());
		}
	}

	@Override
	public void onTimeUp() {

		// do nothing

	}

	@Override
	public void onConnect(int ownerId) {

		// simply create the player
		gameInfo.getEventTriggerer().createObject(ObjectType.PLAYER, ownerId);

	}

	@Override
	public void onGameStart() {

	}

}
