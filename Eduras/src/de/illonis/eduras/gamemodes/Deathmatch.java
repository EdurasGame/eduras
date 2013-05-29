package de.illonis.eduras.gamemodes;

import de.illonis.eduras.GameInformation;
import de.illonis.eduras.ObjectFactory.ObjectType;
import de.illonis.eduras.exceptions.ObjectNotFoundException;
import de.illonis.eduras.logger.EduLog;
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
	public void onDeath(Unit killedUnit, int killingPlayer) {

		try {
			// TODO: should not track npc kills this way.
			Player killer = gameInfo.getPlayerByOwnerId(killingPlayer);
			gameInfo.getGameSettings().getStats().addKillForPlayer(killer);
		} catch (ObjectNotFoundException e) {
			EduLog.passException(e);
		}

		if (killedUnit instanceof Player) {
			gameInfo.getEventTriggerer().respawnPlayer((Player) killedUnit);
			gameInfo.getGameSettings().getStats()
					.addDeathForPlayer((Player) killedUnit);
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
