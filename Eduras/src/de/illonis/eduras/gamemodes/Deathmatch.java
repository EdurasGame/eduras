package de.illonis.eduras.gamemodes;

import de.illonis.eduras.GameInformation;
import de.illonis.eduras.ObjectFactory.ObjectType;
import de.illonis.eduras.units.Player;
import de.illonis.eduras.units.Unit;

/**
 * The deathmatch mode.
 * 
 * @author Florian Mai <florian.ren.mai@googlemail.com>
 * 
 */
public class Deathmatch implements GameMode {

	private GameInformation gameInfo;

	/**
	 * Creates a new instance of deathmatch.
	 * 
	 * @param gameInfo
	 */
	public Deathmatch(GameInformation gameInfo) {
		this.gameInfo = gameInfo;
	}

	@Override
	public String getName() {
		return "Deathmatch";
	}

	@Override
	public void onDeath(Unit killedUnit, Unit killingUnit) {

		if (killingUnit instanceof Player) {

			gameInfo.getGameSettings().getStats()
					.addKillForPlayer((Player) killingUnit);
		}

		if (killedUnit instanceof Player) {
			gameInfo.getEventTriggerer().respawnPlayer((Player) killedUnit);
		}
	}

	@Override
	public void onTimeUp() {

		gameInfo.getEventTriggerer().onMatchEnd();

	}

	@Override
	public void onConnect(int ownerId) {

		// simply create the player
		gameInfo.getEventTriggerer().createObject(ObjectType.PLAYER, ownerId);

		// and add it to the statistic
		gameInfo.getGameSettings().getStats().addPlayerToStats(ownerId);

	}

}
