package de.illonis.eduras.gamemodes;

import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.GameInformation;
import de.illonis.eduras.ObjectFactory.ObjectType;
import de.illonis.eduras.Player;
import de.illonis.eduras.Team;
import de.illonis.eduras.exceptions.ObjectNotFoundException;
import de.illonis.eduras.exceptions.PlayerHasNoTeamException;
import de.illonis.eduras.exceptions.WrongObjectTypeException;
import de.illonis.eduras.gameclient.userprefs.KeyBindings.KeyBinding;
import de.illonis.eduras.gameobjects.Base;
import de.illonis.eduras.gameobjects.GameObject;
import de.illonis.eduras.gameobjects.TimedEventHandler;
import de.illonis.eduras.gameobjects.TimingSource;
import de.illonis.eduras.logic.EventTriggerer;
import de.illonis.eduras.settings.S;
import de.illonis.eduras.units.InteractMode;
import de.illonis.eduras.units.PlayerMainFigure;
import de.illonis.eduras.units.Unit;

/**
 * This is intended to be Eduras? main game mode as described in the Eduras?
 * documentation.
 * 
 * @author Florian 'Ren' Mai <florian.ren.mai@googlemail.com>
 * 
 */
public class Edura extends TeamDeathmatch {

	private TimingSource timingSource;

	private RespawnTimer respawnTimer;

	private final static Logger L = EduLog.getLoggerFor(Edura.class.getName());

	/**
	 * Create the game mode.
	 * 
	 * @param gameInfo
	 */
	public Edura(GameInformation gameInfo) {
		super(gameInfo);
	}

	@Override
	public GameModeNumber getNumber() {
		return GameModeNumber.EDURA;
	}

	@Override
	public String getName() {
		return "Edura";
	}

	@Override
	public void onTimeUp() {
		gameInfo.getEventTriggerer().setRemainingTime(
				gameInfo.getGameSettings().getRoundTime());

		for (Base aNeutralBase : getAllBases()) {
			aNeutralBase.setResourceGenerateMultiplicator(aNeutralBase
					.getResourceGenerateMultiplicator() + 1);
		}
	}

	@Override
	public void onGameStart() {
		initTeams();
		onRoundStarts();
	}

	private void giveStartResources() {
		gameInfo.getEventTriggerer().changeResourcesOfTeamByAmount(getTeamA(),
				S.Server.gm_edura_startmoney);
		gameInfo.getEventTriggerer().changeResourcesOfTeamByAmount(getTeamB(),
				S.Server.gm_edura_startmoney);
	}

	@Override
	public void onDeath(Unit killedUnit, int killingPlayer) {
		try {
			changeStatsOnDeath(killedUnit, killingPlayer);
		} catch (ObjectNotFoundException e) {
			L.log(Level.WARNING, "Cannot update stats!", e);
			return;
		}

		EventTriggerer eventTriggerer = gameInfo.getEventTriggerer();
		eventTriggerer.removeObject(killedUnit.getId());

		if (killedUnit instanceof PlayerMainFigure) {
			PlayerMainFigure mainFigure = (PlayerMainFigure) killedUnit;
			Player deadPlayer = mainFigure.getPlayer();
			eventTriggerer.clearInventoryOfPlayer(deadPlayer);
			eventTriggerer.changeInteractMode(deadPlayer.getPlayerId(),
					InteractMode.MODE_DEAD);

			try {
				rewardForDeath(killedUnit,
						gameInfo.getPlayerByOwnerId(killingPlayer));
			} catch (ObjectNotFoundException e) {
				L.log(Level.WARNING, "Cannot find id of killing player.", e);
			}

			if (!S.Server.gm_edura_automatic_respawn) {
				checkAllPlayersOfTeamDead(deadPlayer);
			}
		}
	}

	private void rewardForDeath(Unit killedUnit, Player killingPlayer) {
		try {
			if (killedUnit.getTeam() != null) {
				if (killedUnit.getTeam().equals(killingPlayer.getTeam())) {
					// team/self killers get punished
					gameInfo.getEventTriggerer().changeResourcesOfTeamByAmount(
							killedUnit.getTeam(),
							S.Server.gm_edura_money_per_selfkill);
				} else {
					// get reward for killing someone
					gameInfo.getEventTriggerer().changeResourcesOfTeamByAmount(
							killingPlayer.getTeam(),
							S.Server.gm_edura_money_per_kill);
				}
			}
		} catch (PlayerHasNoTeamException e) {
			L.log(Level.WARNING, "Cannot find team of player " + killingPlayer,
					e);
		}
	}

	private void setUpRespawnTimer() {
		Base node = getMainbaseOfTeam(getTeamA());
		if (node == null)
			return;
		timingSource = node.getTimingSource();
		if (timingSource != null) {
			respawnTimer = new RespawnTimer();
			timingSource.addTimedEventHandler(respawnTimer);
			gameInfo.getEventTriggerer().notififyRespawnTime(
					respawnTimer.getTimeTillRespawn());
		}
	}

	class RespawnTimer implements TimedEventHandler {

		private long timeTillRespawn = S.Server.gm_edura_respawn_time;

		@Override
		public long getInterval() {
			return 1000;
		}

		@Override
		public void onIntervalElapsed(long delta) {
			timeTillRespawn = Math.max(0, timeTillRespawn - delta);

			if (timeTillRespawn == 0) {
				for (Player player : gameInfo.getPlayers()) {
					if (player.isDead()) {
						gameInfo.getEventTriggerer()
								.respawnPlayerAtRandomSpawnpoint(player);
					}
				}

				timeTillRespawn = S.Server.gm_edura_respawn_time;
				gameInfo.getEventTriggerer().notififyRespawnTime(
						timeTillRespawn);
			}
		}

		public long getTimeTillRespawn() {
			return timeTillRespawn;
		}
	}

	private void checkAllPlayersOfTeamDead(Player player) {
		boolean allPlayersDead = true;
		Team teamOfDeadPlayer = null;
		try {
			teamOfDeadPlayer = player.getTeam();
		} catch (PlayerHasNoTeamException e) {
			L.log(Level.SEVERE, "Every player MUST have a team in Edura!", e);
		}

		for (Player aPlayerOfSameTeam : teamOfDeadPlayer.getPlayers()) {
			// if there is a player who is alive, the team can still win
			if (aPlayerOfSameTeam.getCurrentMode() != InteractMode.MODE_DEAD) {
				allPlayersDead = false;
				break;
			}
		}

		if (allPlayersDead) {
			Team playersTeam;
			try {
				playersTeam = player.getTeam();
			} catch (PlayerHasNoTeamException e) {
				L.log(Level.SEVERE,
						"When checking if all players are dead, a player doesn't have a team!",
						e);
				return;
			}
			if (playersTeam.equals(getTeamA())) {
				endRound(getTeamB());
			} else {
				endRound(getTeamA());
			}

		}
	}

	private void endRound(Team winnerTeam) {
		gameInfo.getEventTriggerer().onMatchEnd(winnerTeam.getTeamId());
	}

	@Override
	public void onBaseOccupied(Base base, Team occupyingTeam) {
		boolean matchEnd = false;

		setTeamOfNode(base, occupyingTeam);
		if (S.Server.gm_edura_conquer_all_bases) {
			Collection<GameObject> allBases = gameInfo
					.findObjectsByType(ObjectType.NEUTRAL_BASE);

			boolean allBasesConquered = true;
			for (GameObject object : allBases) {
				if ((object instanceof Base)) {
					Base aBase = (Base) object;
					allBasesConquered = allBasesConquered
							&& (aBase.getCurrentOwnerTeam() == null || aBase
									.getCurrentOwnerTeam()
									.equals(occupyingTeam));
				}
			}
			matchEnd = allBasesConquered;
		} else {
			for (Base aMainBase : getMainBases()) {
				if (base.equals(aMainBase)) {
					// TODO: we're assuming that a main base cannot be conquered
					// by
					// the own team for any reason. put a check if the conquered
					// main base belongs to the opponent.
					matchEnd = true;
				}
			}

		}

		if (matchEnd) {
			endRound(occupyingTeam);
			return;
		}

		startGeneratingResourcesInBaseForTeam(base, occupyingTeam);
	}

	@Override
	public void onBaseLost(Base base, Team losingTeam) {
		stopGeneratingResourcesInBaseForTeam(base, losingTeam);
	}

	@Override
	public boolean canSwitchMode(Player player, InteractMode mode) {
		if (!(player.getModeSwitchCooldown() <= 0)) {
			return false;
		}

		switch (mode) {
		case MODE_EGO:
			return true;
		case MODE_STRATEGY:
			if (player.getPlayerMainFigure().isDead()) {
				return false;
			}

			Collection<GameObject> neutralBases = gameInfo
					.findObjectsByType(ObjectType.NEUTRAL_BASE);

			Collection<GameObject> intersectingObjects = gameInfo
					.doesAnyOfOtherObjectsIntersect(
							player.getPlayerMainFigure(), neutralBases);

			Team teamOfSwitchingPlayer = null;
			try {
				teamOfSwitchingPlayer = player.getTeam();
			} catch (PlayerHasNoTeamException e) {
				L.log(Level.SEVERE,
						"At this point the player should belong to a team!", e);
			}
			for (GameObject intersectingGameObject : intersectingObjects) {
				Base intersectingBase = (Base) intersectingGameObject;
				if (intersectingBase.getCurrentOwnerTeam() != null
						&& intersectingBase.getCurrentOwnerTeam().equals(
								teamOfSwitchingPlayer)) {
					return true;
				}
			}
			return false;
		default:
			return false;
		}
	}

	@Override
	public boolean supportsKeyBinding(KeyBinding binding) {
		return true;
	}

	@Override
	public void onConnect(int ownerId) {
		super.onConnect(ownerId);

		if (S.Server.gm_edura_automatic_respawn && respawnTimer != null) {
			gameInfo.getEventTriggerer().notififyRespawnTime(
					respawnTimer.getTimeTillRespawn());
		}
	}

	@Override
	public boolean doItemsRespawn() {
		return false;
	}

	@Override
	public void onPlayerSpawn(Player player) {
		gameInfo.getEventTriggerer().changeInteractMode(player.getPlayerId(),
				InteractMode.MODE_EGO);

		if (S.Server.gm_edura_startweapons) {
			try {
				gameInfo.getEventTriggerer().giveNewItem(player,
						ObjectType.ASSAULTRIFLE);
				gameInfo.getEventTriggerer().giveNewItem(player,
						ObjectType.ITEM_WEAPON_SWORD);
			} catch (WrongObjectTypeException e) {
				L.log(Level.SEVERE, "Wrong item type!", e);
			}
		}

	}

	@Override
	public void onGameEnd() {
		super.onGameEnd();
	}

	@Override
	public void onRoundStarts() {
		EventTriggerer eventTriggerer = gameInfo.getEventTriggerer();

		// make sure all players are in EGO_MODE
		for (Player player : gameInfo.getPlayers()) {
			gameInfo.getEventTriggerer().changeInteractMode(
					player.getPlayerId(), InteractMode.MODE_EGO);
		}

		loadNodes();

		for (Player player : gameInfo.getPlayers()) {
			eventTriggerer
					.createObject(ObjectType.PLAYER, player.getPlayerId());
			eventTriggerer.respawnPlayerAtRandomSpawnpoint(player);
		}

		giveStartResources();
		if (S.Server.gm_edura_automatic_respawn) {
			setUpRespawnTimer();
		}
	}

	@Override
	public void onRoundEnds() {
		super.onRoundEnds();

		if (S.Server.gm_edura_automatic_respawn && timingSource != null) {
			timingSource.removeTimedEventHandler(respawnTimer);
			respawnTimer = null;
		}
	}
}
