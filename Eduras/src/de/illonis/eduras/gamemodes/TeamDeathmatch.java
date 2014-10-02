package de.illonis.eduras.gamemodes;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.newdawn.slick.Color;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.GameInformation;
import de.illonis.eduras.ObjectFactory.ObjectType;
import de.illonis.eduras.Player;
import de.illonis.eduras.Team;
import de.illonis.eduras.exceptions.ObjectNotFoundException;
import de.illonis.eduras.exceptions.PlayerHasNoTeamException;
import de.illonis.eduras.gameclient.userprefs.KeyBindings.KeyBinding;
import de.illonis.eduras.gameobjects.GameObject;
import de.illonis.eduras.gameobjects.GameObject.Relation;
import de.illonis.eduras.logic.EventTriggerer;
import de.illonis.eduras.maps.SpawnPosition.SpawnType;
import de.illonis.eduras.settings.S;
import de.illonis.eduras.units.InteractMode;

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

		initTeams();
		onRoundStarts();
	}

	protected void initTeams() {
		EventTriggerer eventTriggerer = gameInfo.getEventTriggerer();

		teamA = new Team("Red Team", Team.getNextTeamId(), Color.red);
		teamB = new Team("Blue Team", Team.getNextTeamId(), Color.blue);
		teams.clear();
		teams.add(teamA);
		teams.add(teamB);
		eventTriggerer.setTeams(teams);
		for (Player player : gameInfo.getPlayers()) {
			putPlayerInSmallestTeam(player);
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

		// simply create the player and respawn it somewhere
		gameInfo.getEventTriggerer().createObject(ObjectType.PLAYER, ownerId);

		putPlayerInSmallestTeam(newPlayer);

		gameInfo.getEventTriggerer().respawnPlayerAtRandomSpawnpoint(newPlayer);

		// and add it to the statistic
		gameInfo.getGameSettings().getStats().addPlayerToStats(newPlayer);
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
		Player playerA, playerB;
		int ownerA = a.getOwner();
		int ownerB = b.getOwner();
		if (ownerA == -1 || ownerB == -1)
			return Relation.ENVIRONMENT;
		try {
			playerA = gameInfo.getPlayerByOwnerId(a.getOwner());
			playerB = gameInfo.getPlayerByOwnerId(b.getOwner());
		} catch (ObjectNotFoundException e) {
			L.log(Level.SEVERE, "player not found", e);
			return Relation.UNKNOWN;
		}

		Team teamOfPlayerA;
		Team teamOfPlayerB;
		try {
			teamOfPlayerA = playerA.getTeam();
			teamOfPlayerB = playerB.getTeam();
		} catch (PlayerHasNoTeamException e) {
			L.log(Level.WARNING, "Cannot find team of player.", e);
			return Relation.UNKNOWN;
		}

		if (teamOfPlayerA.equals(teamOfPlayerB)) {
			return Relation.ALLIED;
		} else
			return Relation.HOSTILE;
	}

	@Override
	public void onDisconnect(int ownerId) {

		// and from the statistics and game
		Player player;
		try {
			player = gameInfo.getPlayerByOwnerId(ownerId);
			gameInfo.getGameSettings().getStats()
					.removePlayerFromStats(player.getPlayerId());
		} catch (ObjectNotFoundException e) {
			L.log(Level.WARNING, "Player already vanished onDisconnect", e);
		}
		gameInfo.getEventTriggerer().removePlayer(ownerId);

		// reset the teams such that they don't contain the player anymore
		gameInfo.getEventTriggerer().setTeams(gameInfo.getTeams());
	}

	@Override
	public boolean supportsKeyBinding(KeyBinding binding) {
		if (binding == KeyBinding.SELECT_TEAM && !S.Server.sv_switchteams) {
			return false;
		}

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

	@Override
	public void onTimeUp() {
		int killsOfTeamA = gameInfo.getGameSettings().getStats()
				.getKillsByTeam(teamA);
		int killsOfTeamB = gameInfo.getGameSettings().getStats()
				.getKillsByTeam(teamB);

		int winnerId = -1;
		if (killsOfTeamA > killsOfTeamB) {
			winnerId = teamA.getTeamId();
		}
		if (killsOfTeamB > killsOfTeamA) {
			winnerId = teamB.getTeamId();
		}

		gameInfo.getEventTriggerer().onMatchEnd(winnerId);
	}
}
