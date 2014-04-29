package de.illonis.eduras.gamemodes;

import java.util.LinkedList;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.GameInformation;
import de.illonis.eduras.ObjectFactory.ObjectType;
import de.illonis.eduras.Statistic.StatsProperty;
import de.illonis.eduras.Team;
import de.illonis.eduras.exceptions.ObjectNotFoundException;
import de.illonis.eduras.gameclient.userprefs.KeyBindings.KeyBinding;
import de.illonis.eduras.gameobjects.GameObject;
import de.illonis.eduras.gameobjects.GameObject.Relation;
import de.illonis.eduras.gameobjects.NeutralBase;
import de.illonis.eduras.logic.EventTriggerer;
import de.illonis.eduras.maps.SpawnPosition.SpawnType;
import de.illonis.eduras.units.PlayerMainFigure;
import de.illonis.eduras.units.PlayerMainFigure.InteractMode;
import de.illonis.eduras.units.Unit;

/**
 * The deathmatch mode.
 * 
 * @author Florian Mai <florian.ren.mai@googlemail.com>
 * 
 */
public class Deathmatch extends BasicGameMode {

	private final static Logger L = EduLog.getLoggerFor(Deathmatch.class
			.getName());

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

			EventTriggerer et = gameInfo.getEventTriggerer();
			if (et == null) {
				L.severe("EventTriggerer is null!");
				System.exit(-1);
			}
			if (killedUnit instanceof PlayerMainFigure) {

				// need to check here because client has no event triggerer.
				// TODO: find a solution for client-workaraound.
				et.respawnPlayerAtRandomSpawnpoint((PlayerMainFigure) killedUnit);
				et.changeStatOfPlayerByAmount(StatsProperty.DEATHS,
						(PlayerMainFigure) killedUnit, 1);
				// TODO: give player items here if game mode should do.

			}
			if (killer.equals(killedUnit))
				return;
			et.changeStatOfPlayerByAmount(StatsProperty.KILLS, killer, 1);
		} catch (ObjectNotFoundException e) {
			L.log(Level.SEVERE, "player not found", e);
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
			L.log(Level.SEVERE, "player not found", e);
			return;
		}

		int teamId = Team.getNextTeamId();
		Team t = new Team("Team " + teamId, teamId);
		gameInfo.addTeam(t);
		gameInfo.getEventTriggerer().setTeams(gameInfo.getTeams());
		gameInfo.getEventTriggerer().addPlayerToTeam(newPlayer.getOwner(), t);
		gameInfo.getEventTriggerer().respawnPlayerAtRandomSpawnpoint(newPlayer);

		// and add it to the statistic
		gameInfo.getGameSettings().getStats().addPlayerToStats(ownerId);
	}

	@Override
	public void onGameStart() {
		LinkedList<Team> teams = new LinkedList<Team>();
		for (PlayerMainFigure player : gameInfo.getPlayers()) {
			gameInfo.getEventTriggerer().changeInteractMode(player.getOwner(),
					InteractMode.MODE_EGO);
			Team t = new Team(player.getName(), Team.getNextTeamId());
			teams.add(t);
			gameInfo.getEventTriggerer().addPlayerToTeam(player.getOwner(), t);
		}
		gameInfo.getEventTriggerer().setTeams(teams);

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
		if (a.getId() == b.getId())
			return Relation.ALLIED;
		return Relation.HOSTILE;
	}

	@Override
	public void onDisconnect(int ownerId) {
		PlayerMainFigure gonePlayer;
		try {
			gonePlayer = gameInfo.getPlayerByOwnerId(ownerId);
		} catch (ObjectNotFoundException e) {
			L.log(Level.SEVERE, "player not found", e);
			return;
		}

		// remove it to the statistic
		gameInfo.getGameSettings().getStats().removePlayerFromStats(ownerId);

		Team playersTeam = gonePlayer.getTeam();
		// remove the actual player (also removes the player from its team)
		gameInfo.getEventTriggerer().removePlayer(ownerId);

		// .. and remove it's team
		gameInfo.removeTeam(playersTeam);
		gameInfo.getEventTriggerer().setTeams(gameInfo.getTeams());
	}

	@Override
	public Team determineProgressingTeam(NeutralBase base, GameObject object,
			boolean objectEntered, Set<GameObject> presentObjects) {
		if (presentObjects.size() == 1) {
			Unit onlyUnitInside = (Unit) presentObjects.iterator().next();
			return onlyUnitInside.getTeam();
		}
		return null;
	}

	@Override
	public void onBaseOccupied(NeutralBase base, Team occupyingTeam) {
		L.info("Team " + occupyingTeam.getName()
				+ " occupied the base with id " + base.getId() + "!");
	}

	@Override
	public void onBaseLost(NeutralBase base, Team losingTeam) {
		L.info("Team " + losingTeam.getName() + " lost the base with id "
				+ base.getId() + "!");
	}

	@Override
	public boolean supportsKeyBinding(KeyBinding binding) {
		// if (binding == KeyBinding.SWITCH_MODE)
		// retburn false;
		return true;
	}

	@Override
	public boolean canSwitchMode(PlayerMainFigure player, InteractMode mode) {
		return true;
	}
}
