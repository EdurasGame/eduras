package de.illonis.eduras.gamemodes;

import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.newdawn.slick.Color;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.GameInformation;
import de.illonis.eduras.ObjectFactory.ObjectType;
import de.illonis.eduras.Player;
import de.illonis.eduras.Team;
import de.illonis.eduras.exceptions.ObjectNotFoundException;
import de.illonis.eduras.gameclient.userprefs.KeyBindings.KeyBinding;
import de.illonis.eduras.gameobjects.GameObject;
import de.illonis.eduras.gameobjects.GameObject.Relation;
import de.illonis.eduras.logic.EventTriggerer;
import de.illonis.eduras.maps.SpawnPosition.SpawnType;
import de.illonis.eduras.units.InteractMode;
import de.illonis.eduras.units.PlayerMainFigure;

/**
 * The team-deathmatch mode.
 * 
 * @author illonis
 * 
 */
public class TeamDeathmatch extends Deathmatch {

	private final static Logger L = EduLog.getLoggerFor(TeamDeathmatch.class
			.getName());
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
		EventTriggerer eventTriggerer = gameInfo.getEventTriggerer();

		teamA = new Team("Red Team", Team.getNextTeamId(), Color.red);
		teamB = new Team("Blue Team", Team.getNextTeamId(), Color.blue);
		LinkedList<Team> teams = new LinkedList<Team>();
		teams.add(teamA);
		teams.add(teamB);
		eventTriggerer.setTeams(teams);
		for (Player player : gameInfo.getPlayers()) {
			putPlayerInSmallestTeam(player);
		}

		for (Player player : gameInfo.getPlayers()) {
			eventTriggerer
					.createObject(ObjectType.PLAYER, player.getPlayerId());
			eventTriggerer.respawnPlayerAtRandomSpawnpoint(player);
		}
	}

	private void putPlayerInSmallestTeam(Player player) {
		Team targetTeam;
		if (teamA.getPlayers().size() > teamB.getPlayers().size()) {
			targetTeam = teamB;
		} else {
			targetTeam = teamA;
		}
		gameInfo.getEventTriggerer().addPlayerToTeam(player.getPlayerId(),
				targetTeam);
	}

	@Override
	public void onConnect(int ownerId) {

		Player newPlayer = handleNewPlayer(ownerId);

		// have to reimplement this unfortunately
		if (gameInfo.getPlayers().size() >= gameInfo.getGameSettings()
				.getMaxPlayers()) {
			gameInfo.getEventTriggerer().kickPlayer(ownerId);
		}

		// simply create the player and respawn it somewhere
		gameInfo.getEventTriggerer().createObject(ObjectType.PLAYER, ownerId);

		putPlayerInSmallestTeam(newPlayer);

		gameInfo.getEventTriggerer().respawnPlayerAtRandomSpawnpoint(newPlayer);

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
			playerA = gameInfo.getPlayerByOwnerId(a.getOwner())
					.getPlayerMainFigure();
			playerB = gameInfo.getPlayerByOwnerId(b.getOwner())
					.getPlayerMainFigure();
		} catch (ObjectNotFoundException e) {
			L.log(Level.SEVERE, "player not found", e);
			return Relation.UNKNOWN;
		}
		if (playerA.getTeam().equals(playerB.getTeam())) {
			return Relation.ALLIED;
		} else
			return Relation.HOSTILE;
	}

	@Override
	public void onDisconnect(int ownerId) {

		// and from the statistics and game
		gameInfo.getGameSettings().getStats().removePlayerFromStats(ownerId);
		gameInfo.getEventTriggerer().removePlayer(ownerId);

		// reset the teams such that they don't contain the player anymore
		gameInfo.getEventTriggerer().setTeams(gameInfo.getTeams());
	}

	@Override
	public boolean supportsKeyBinding(KeyBinding binding) {
		return true;
	}

	@Override
	public boolean canSwitchMode(Player player, InteractMode mode) {
		return true;
	}

	protected Team getTeamA() {
		return teamA;
	}

	protected Team getTeamB() {
		return teamB;
	}

}
