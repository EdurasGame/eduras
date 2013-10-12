package de.illonis.eduras.gamemodes;

import de.illonis.eduras.GameInformation;
import de.illonis.eduras.ObjectFactory.ObjectType;
import de.illonis.eduras.Team;
import de.illonis.eduras.Team.TeamColor;
import de.illonis.eduras.exceptions.ObjectNotFoundException;
import de.illonis.eduras.gameobjects.GameObject;
import de.illonis.eduras.gameobjects.GameObject.Relation;
import de.illonis.eduras.logger.EduLog;
import de.illonis.eduras.logic.EventTriggerer;
import de.illonis.eduras.maps.SpawnPosition.SpawnType;
import de.illonis.eduras.units.PlayerMainFigure;
import de.illonis.eduras.units.Unit;

/**
 * The deathmatch mode.
 * 
 * @author Florian Mai <florian.ren.mai@googlemail.com>
 * 
 */
public class Deathmatch extends BasicGameMode {

	/**
	 * Creates a new instance of deathmatch.
	 * 
	 * @param gameInfo
	 */
	public Deathmatch(GameInformation gameInfo) {
		super(gameInfo);
	}

	@Override
	public String getName() {
		return "Deathmatch";
	}

	@Override
	public void onDeath(Unit killedUnit, int killingPlayer) {

		try {
			// TODO: should not track npc kills this way.
			PlayerMainFigure killer = gameInfo
					.getPlayerByOwnerId(killingPlayer);
			if (killedUnit instanceof PlayerMainFigure) {
				EventTriggerer et = gameInfo.getEventTriggerer();
				// need to check here because client has no event triggerer.
				// TODO: find a solution for client-workaraound.
				if (et != null)
					gameInfo.getEventTriggerer().respawnPlayer(
							(PlayerMainFigure) killedUnit);
				// TODO: give player items here if game mode should do.
				gameInfo.getGameSettings().getStats()
						.addDeathForPlayer((PlayerMainFigure) killedUnit);
			}
			if (killer.equals(killedUnit))
				return;
			gameInfo.getGameSettings().getStats().addKillForPlayer(killer);
		} catch (ObjectNotFoundException e) {
			EduLog.passException(e);
		}

	}

	@Override
	public void onTimeUp() {
		try {
			gameInfo.getEventTriggerer().onMatchEnd();
		} catch (NullPointerException e) {
			// FIXME: Client should never trigger this.
		}
	}

	@Override
	public void onConnect(int ownerId) {
		super.onConnect(ownerId);

		// simply create the player and respawn it somewhere
		gameInfo.getEventTriggerer().createObject(ObjectType.PLAYER, ownerId);

		PlayerMainFigure newPlayer;
		try {
			newPlayer = gameInfo.getPlayerByOwnerId(ownerId);
		} catch (ObjectNotFoundException e) {
			EduLog.passException(e);
			return;
		}

		gameInfo.getEventTriggerer().addPlayerToTeam(ownerId,
				gameInfo.getTeams().getFirst());
		gameInfo.getEventTriggerer().respawnPlayer(newPlayer);

		// and add it to the statistic
		gameInfo.getGameSettings().getStats().addPlayerToStats(ownerId);
	}

	@Override
	public void onGameStart() {
		Team team = new Team("All Players", TeamColor.NEUTRAL);
		gameInfo.getEventTriggerer().setTeams(team);
		for (PlayerMainFigure player : gameInfo.getPlayers()) {
			gameInfo.getEventTriggerer().addPlayerToTeam(player.getOwner(),
					team);
		}

	}

	@Override
	public SpawnType getSpawnTypeForTeam(Team team) {
		return SpawnType.ANY;
	}

	@Override
	public GameModeNumber getNumber() {
		return GameModeNumber.DEATHMATCH;
	}

	@Override
	public Relation getRelation(GameObject a, GameObject b) {
		return Relation.HOSTILE;
	}
}
