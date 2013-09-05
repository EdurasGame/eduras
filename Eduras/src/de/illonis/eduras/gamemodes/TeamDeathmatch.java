package de.illonis.eduras.gamemodes;

import de.illonis.eduras.GameInformation;
import de.illonis.eduras.ObjectFactory.ObjectType;
import de.illonis.eduras.Team;
import de.illonis.eduras.Team.TeamColor;
import de.illonis.eduras.exceptions.ObjectNotFoundException;
import de.illonis.eduras.gameobjects.GameObject;
import de.illonis.eduras.gameobjects.GameObject.Relation;
import de.illonis.eduras.logger.EduLog;
import de.illonis.eduras.maps.SpawnPosition.SpawnType;
import de.illonis.eduras.units.PlayerMainFigure;

/**
 * The team-deathmatch mode.
 * 
 * @author illonis
 * 
 */
public class TeamDeathmatch extends Deathmatch {

	private Team teamA;
	private Team teamB;

	/**
	 * Creates a new team-deathmatch instance.
	 * 
	 * @param gameInfo
	 */
	public TeamDeathmatch(GameInformation gameInfo) {
		super(gameInfo);
	}

	@Override
	public String getName() {
		return "Team-Deathmatch";
	}

	@Override
	public void onGameStart() {
		teamA = new Team("Red Team", TeamColor.RED);
		teamB = new Team("Blue Team", TeamColor.BLUE);
		gameInfo.getEventTriggerer().setTeams(teamA, teamB);
		for (PlayerMainFigure player : gameInfo.getPlayers()) {
			putPlayerInSmallestTeam(player);
		}
	}

	private void putPlayerInSmallestTeam(PlayerMainFigure player) {
		Team targetTeam;
		if (teamA.getPlayers().size() > teamB.getPlayers().size()) {
			targetTeam = teamB;
		} else {
			targetTeam = teamA;
		}
		gameInfo.getEventTriggerer().addPlayerToTeam(player.getOwner(),
				targetTeam);
	}

	@Override
	public void onConnect(int ownerId) {

		// simply create the player and respawn it somewhere
		gameInfo.getEventTriggerer().createObject(ObjectType.PLAYER, ownerId);

		PlayerMainFigure newPlayer;
		try {
			newPlayer = gameInfo.getPlayerByOwnerId(ownerId);
		} catch (ObjectNotFoundException e) {
			EduLog.passException(e);
			return;
		}

		putPlayerInSmallestTeam(newPlayer);

		gameInfo.getEventTriggerer().respawnPlayer(newPlayer);

		// and add it to the statistic
		gameInfo.getGameSettings().getStats().addPlayerToStats(ownerId);
	}

	@Override
	public SpawnType getSpawnTypeForTeam(Team team) {
		if (team.equals(teamA)) {
			return SpawnType.TEAM_A;
		} else {
			return SpawnType.TEAM_B;
		}
	}

	@Override
	public GameModeNumber getNumber() {
		return GameModeNumber.TEAM_DEATHMATCH;
	}

	@Override
	public Relation getRelation(GameObject a, GameObject b) {
		PlayerMainFigure playerA, playerB;
		int ownerA = a.getOwner();
		int ownerB = b.getOwner();
		if (ownerA == -1 || ownerB == -1)
			return Relation.ENVIRONMENT;
		try {
			playerA = gameInfo.getPlayerByOwnerId(a.getOwner());
			playerB = gameInfo.getPlayerByOwnerId(b.getOwner());
		} catch (ObjectNotFoundException e) {
			EduLog.passException(e);
			return Relation.UNKNOWN;
		}
		if (playerA.getTeam().equals(playerB.getTeam())) {
			return Relation.ALLIED;
		} else
			return Relation.HOSTILE;
	}
}
