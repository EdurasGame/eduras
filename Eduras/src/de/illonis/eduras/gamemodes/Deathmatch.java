package de.illonis.eduras.gamemodes;

import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.GameInformation;
import de.illonis.eduras.ObjectFactory.ObjectType;
import de.illonis.eduras.Player;
import de.illonis.eduras.Statistic.StatsProperty;
import de.illonis.eduras.Team;
import de.illonis.eduras.exceptions.ObjectNotFoundException;
import de.illonis.eduras.exceptions.PlayerHasNoTeamException;
import de.illonis.eduras.exceptions.WrongObjectTypeException;
import de.illonis.eduras.gameclient.userprefs.KeyBindings.KeyBinding;
import de.illonis.eduras.gameobjects.Base;
import de.illonis.eduras.gameobjects.GameObject;
import de.illonis.eduras.gameobjects.GameObject.Relation;
import de.illonis.eduras.gameobjects.TimedEventHandler;
import de.illonis.eduras.logic.EventTriggerer;
import de.illonis.eduras.maps.SpawnPosition.SpawnType;
import de.illonis.eduras.settings.S;
import de.illonis.eduras.units.InteractMode;
import de.illonis.eduras.units.PlayerMainFigure;
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
	private BlinkTimer blinkTimer;

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
			changeStatsOnDeath(killedUnit, killingPlayer);

			EventTriggerer et = gameInfo.getEventTriggerer();
			if (et == null) {
				L.severe("EventTriggerer is null!");
				System.exit(-1);
			}
			if (killedUnit instanceof PlayerMainFigure) {

				// need to check here because client has no event triggerer.
				// TODO: find a solution for client-workaraound.
				et.clearInventoryOfPlayer(((PlayerMainFigure) killedUnit)
						.getPlayer());
				et.respawnPlayerAtRandomSpawnpoint(((PlayerMainFigure) killedUnit)
						.getPlayer());
				// TODO: give player items here if game mode should do.

			}
		} catch (ObjectNotFoundException e) {
			L.log(Level.SEVERE, "player not found", e);
		}

	}

	protected void changeStatsOnDeath(Unit killedUnit, int killingPlayer)
			throws ObjectNotFoundException {

		EventTriggerer et = gameInfo.getEventTriggerer();
		if (killedUnit instanceof PlayerMainFigure) {
			Player killed = gameInfo.getPlayerByOwnerId(killedUnit.getOwner());
			et.changeStatOfPlayerByAmount(StatsProperty.DEATHS, killed, 1);
		}

		Player killer = gameInfo.getPlayerByOwnerId(killingPlayer);
		Team killersTeam;
		Team killedUnitsTeam = killedUnit.getTeam();
		try {
			killersTeam = killer.getTeam();
		} catch (PlayerHasNoTeamException e) {
			L.log(Level.WARNING, "Killer doesn't have a team", e);
			return;
		}

		if (killedUnitsTeam == null || !killedUnitsTeam.equals(killersTeam)) {
			et.changeStatOfPlayerByAmount(StatsProperty.KILLS, killer, 1);
		} else {
			et.changeStatOfPlayerByAmount(StatsProperty.KILLS, killer, -1);
		}
	}

	@Override
	public void onTimeUp() {

		int winnerId = -1;

		LinkedList<Player> playersWithMostFrags = new LinkedList<Player>(
				gameInfo.getGameSettings().getStats()
						.findPlayersWithMostFrags(gameInfo.getPlayers()));

		if (!playersWithMostFrags.isEmpty()) {
			if (playersWithMostFrags.size() > 1) {
				// TODO: determine players with least deaths
			} else {
				winnerId = playersWithMostFrags.getFirst().getPlayerId();
			}
		}

		try {
			gameInfo.getEventTriggerer().onMatchEnd(winnerId);

		} catch (NullPointerException e) {
			// FIXME: Client should never trigger this.
		}
	}

	@Override
	public void onConnect(int ownerId) {
		super.onConnect(ownerId);

		Player newPlayer = handleNewPlayer(ownerId);

		// simply create the player and respawn it somewhere
		gameInfo.getEventTriggerer().createObject(ObjectType.PLAYER, ownerId);

		int teamId = Team.getNextTeamId();
		Team t = new Team("Team " + teamId, teamId);
		gameInfo.addTeam(t);
		gameInfo.getEventTriggerer().setTeams(gameInfo.getTeams());
		gameInfo.getEventTriggerer()
				.addPlayerToTeam(newPlayer.getPlayerId(), t);
		gameInfo.getEventTriggerer().respawnPlayerAtRandomSpawnpoint(newPlayer);

		// and add it to the statistic
		Player p;
		try {
			p = gameInfo.getPlayerByOwnerId(ownerId);
			gameInfo.getGameSettings().getStats().addPlayerToStats(p);
		} catch (ObjectNotFoundException e) {
			L.log(Level.SEVERE, "Could not find currently connected player.", e);
		}
	}

	protected Player handleNewPlayer(int ownerId) {
		EventTriggerer eventTriggerer = gameInfo.getEventTriggerer();

		Player newPlayer = new Player(ownerId, "unknown");
		gameInfo.addPlayer(newPlayer);

		eventTriggerer.onPlayerJoined(newPlayer);
		return newPlayer;
	}

	@Override
	public void onGameStart() {
		EventTriggerer eventTriggerer = gameInfo.getEventTriggerer();

		LinkedList<Team> teams = new LinkedList<Team>();
		for (Player player : gameInfo.getPlayers()) {
			eventTriggerer.changeInteractMode(player.getPlayerId(),
					InteractMode.MODE_EGO);
			Team t = new Team(player.getName(), Team.getNextTeamId());
			teams.add(t);
			eventTriggerer.addPlayerToTeam(player.getPlayerId(), t);
		}
		eventTriggerer.setTeams(teams);

	}

	private void setUpBlinkTimer() {
		blinkTimer = new BlinkTimer();
		gameInfo.getTimingSource().addTimedEventHandler(blinkTimer);
	}

	class BlinkTimer implements TimedEventHandler {

		private long timeTillNewBlink = S.Server.gm_deathmatch_blink_timer;

		@Override
		public long getInterval() {
			return 1000;
		}

		@Override
		public void onIntervalElapsed(long delta) {
			timeTillNewBlink = Math.max(0, timeTillNewBlink - delta);

			if (timeTillNewBlink == 0) {
				for (Player player : gameInfo.getPlayers()) {
					gameInfo.getEventTriggerer()
							.changeBlinkChargesBy(player, 1);
				}

				timeTillNewBlink = S.Server.gm_deathmatch_blink_timer;
			}
		}

		public long getTimeTillRespawn() {
			return timeTillNewBlink;
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
		if (a.getId() == b.getId())
			return Relation.ALLIED;
		return Relation.HOSTILE;
	}

	@Override
	public void onDisconnect(int ownerId) {
		Player gonePlayer;
		try {
			gonePlayer = gameInfo.getPlayerByOwnerId(ownerId);
		} catch (ObjectNotFoundException e) {
			L.log(Level.SEVERE, "player not found", e);
			return;
		}

		// remove it to the statistic
		gameInfo.getGameSettings().getStats()
				.removePlayerFromStats(gonePlayer.getPlayerId());

		Team playersTeam = null;
		try {
			playersTeam = gonePlayer.getTeam();
		} catch (PlayerHasNoTeamException e) {
			L.log(Level.SEVERE, "At this point the player should have a team!",
					e);
		}
		// remove the actual player (also removes the player from its team)
		gameInfo.getEventTriggerer().removePlayer(ownerId);

		// .. and remove it's team
		gameInfo.removeTeam(playersTeam);
		gameInfo.getEventTriggerer().setTeams(gameInfo.getTeams());
	}

	@Override
	public void onBaseOccupied(Base base, Team occupyingTeam) {
		L.info("Team " + occupyingTeam.getName()
				+ " occupied the base with id " + base.getId() + "!");
	}

	@Override
	public void onBaseLost(Base base, Team losingTeam) {
		L.info("Team " + losingTeam.getName() + " lost the base with id "
				+ base.getId() + "!");
	}

	@Override
	public boolean supportsKeyBinding(KeyBinding binding) {
		if (binding == KeyBinding.SWITCH_MODE
				|| binding == KeyBinding.SELECT_TEAM)
			return false;
		return true;
	}

	@Override
	public boolean canSwitchMode(Player player, InteractMode mode) {
		return false;
	}

	@Override
	public void onGameEnd() {

		for (Team team : gameInfo.getTeams()) {
			gameInfo.removeTeam(team);
		}

		// tell clients that all teams have been removed
		gameInfo.getEventTriggerer().setTeams(new LinkedList<Team>());
	}

	@Override
	public boolean doItemsRespawn() {
		return true;
	}

	@Override
	public void onPlayerSpawn(Player player) {
		for (String objTypeString : S.Server.sv_startweapons) {
			try {
				ObjectType objType = ObjectType.valueOf(objTypeString);
				gameInfo.getEventTriggerer().giveNewItem(player, objType);
			} catch (WrongObjectTypeException | IllegalArgumentException e) {
				L.log(Level.SEVERE, "Wrong item type!", e);
				continue;
			}
		}
	}

	@Override
	public void onRoundStarts() {
		EventTriggerer eventTriggerer = gameInfo.getEventTriggerer();

		for (Player player : gameInfo.getPlayers()) {
			eventTriggerer
					.createObject(ObjectType.PLAYER, player.getPlayerId());
			eventTriggerer.respawnPlayerAtRandomSpawnpoint(player);
		}

		if (S.Server.gm_deathmatch_blink_available) {
			setUpBlinkTimer();
		}
	}

	@Override
	public boolean onRoundEnds() {
		for (Player player : gameInfo.getPlayers()) {
			gameInfo.getEventTriggerer().clearInventoryOfPlayer(player);
		}

		if (S.Server.gm_deathmatch_blink_available) {
			try {
				gameInfo.getTimingSource().removeTimedEventHandler(blinkTimer);
			} catch (NoSuchElementException e) {
				// do nothing
			}
			blinkTimer = null;

			// reset blink count for every player
			for (Player player : gameInfo.getPlayers()) {
				gameInfo.getEventTriggerer().changeBlinkChargesBy(player,
						-player.getBlinksAvailable());
			}
		}

		return false;
	}
}
