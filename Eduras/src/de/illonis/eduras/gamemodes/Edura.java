package de.illonis.eduras.gamemodes;

import java.util.Collection;
import java.util.HashMap;
import java.util.Set;
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
import de.illonis.eduras.gameobjects.Base.BaseType;
import de.illonis.eduras.gameobjects.GameObject;
import de.illonis.eduras.gameobjects.TimedEventHandler;
import de.illonis.eduras.gameobjects.TimingSource;
import de.illonis.eduras.logic.EventTriggerer;
import de.illonis.eduras.maps.EduraMap;
import de.illonis.eduras.maps.NodeData;
import de.illonis.eduras.math.Vector2df;
import de.illonis.eduras.math.graphs.Vertex;
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

	private HashMap<Base, Vertex> baseToVertex;
	private HashMap<Team, Base> mainBaseOfTeam;
	private HashMap<Base, ResourceGenerator> baseToResourceGenerator;

	private RespawnTimer respawnTimer;

	private final static Logger L = EduLog.getLoggerFor(Edura.class.getName());

	/**
	 * Create the game mode.
	 * 
	 * @param gameInfo
	 */
	public Edura(GameInformation gameInfo) {
		super(gameInfo);

		baseToVertex = new HashMap<Base, Vertex>();
		mainBaseOfTeam = new HashMap<Team, Base>();
		baseToResourceGenerator = new HashMap<Base, ResourceGenerator>();
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

		for (Base aNeutralBase : baseToVertex.keySet()) {
			aNeutralBase.setResourceGenerateMultiplicator(aNeutralBase
					.getResourceGenerateMultiplicator() + 1);
		}
	}

	@Override
	public void onGameStart() {
		super.onGameStart();

		// make sure all players are in EGO_MODE
		for (Player player : gameInfo.getPlayers()) {
			gameInfo.getEventTriggerer().changeInteractMode(
					player.getPlayerId(), InteractMode.MODE_EGO);
		}

		loadNodes();
		giveStartResources();
		if (S.Server.gm_edura_automatic_respawn) {
			setUpRespawnTimer();
		}
	}

	private void giveStartResources() {
		gameInfo.getEventTriggerer().changeResourcesOfTeamByAmount(getTeamA(),
				S.Server.gm_edura_startmoney);
		gameInfo.getEventTriggerer().changeResourcesOfTeamByAmount(getTeamB(),
				S.Server.gm_edura_startmoney);
	}

	private void loadNodes() {
		HashMap<Integer, Base> nodeIdToBase = new HashMap<Integer, Base>();
		EduraMap eduraMap = (EduraMap) gameInfo.getMap();

		clear();

		for (NodeData nodeData : eduraMap.getNodes()) {
			int nodeid = nodeData.getId();
			int objectId = gameInfo.getEventTriggerer().createObjectAt(
					ObjectType.NEUTRAL_BASE,
					new Vector2df(nodeData.getX(), nodeData.getY()), -1);

			Base base = (Base) gameInfo.findObjectById(objectId);
			nodeIdToBase.put(nodeid, base);
			baseToVertex.put(base, new Vertex());
		}

		// TODO: At the moment we rely on the assumption that there are always
		// two main nodes in the map and that there is only two teams.
		Team teamA = getTeamA();
		Team teamB = getTeamB();

		for (NodeData nodeData : eduraMap.getNodes()) {
			int nodeid = nodeData.getId();
			Vertex vertexForNode = nodeIdToVertex(nodeIdToBase, nodeid);

			for (Integer adjacentNode : nodeData.getAdjacentNodes()) {
				Vertex vertexForAdjacent = nodeIdToVertex(nodeIdToBase,
						adjacentNode);

				vertexForNode.addAdjacentVertex(vertexForAdjacent);
			}

			if (nodeData.isMainNode() != BaseType.NEUTRAL) {
				Team teamOfMainNode;
				switch (nodeData.isMainNode()) {
				case TEAM_B:
					teamOfMainNode = teamB;
					break;
				case TEAM_A:
					teamOfMainNode = teamA;
					break;
				default:
					return;
				}

				vertexForNode.setColor(teamOfMainNode.getTeamId());
				Base mainBase = nodeIdToBase.get(nodeid);
				mainBaseOfTeam.put(teamOfMainNode, mainBase);
				mainBase.setCurrentOwnerTeam(teamOfMainNode);
				gameInfo.getEventTriggerer().notifyAreaConquered(mainBase,
						teamOfMainNode);

				startGeneratingResourcesInBaseForTeam(mainBase, teamOfMainNode);
			}
		}

	}

	private void clear() {
		for (GameObject neutralBase : baseToVertex.keySet()) {
			gameInfo.getEventTriggerer().removeObject(neutralBase.getId());
		}
	}

	private Vertex nodeIdToVertex(HashMap<Integer, Base> nodeIdToBase,
			int nodeid) {
		return baseToVertex.get(nodeIdToBase.get(nodeid));
	}

	@Override
	public Team determineProgressingTeam(Base base, GameObject object,
			boolean objectEntered, Set<GameObject> presentObjects) {
		Team team = determineOnlyContainingTeam(presentObjects);

		if (team == null) {
			return team;
		}
		if (baseToVertex.get(base).hasAdjacentNodeOfColor(team.getTeamId())) {
			return team;
		} else {
			return null;
		}

	}

	private Team determineOnlyContainingTeam(Set<GameObject> presentObjects) {

		if (presentObjects.size() == 0) {
			return null;
		}

		for (Team team : gameInfo.getTeams()) {
			boolean ofThisTeam = true;
			for (GameObject go : presentObjects) {
				Unit unit = (Unit) go;
				if (unit.getTeam() == null) {
					ofThisTeam = false;
					break;
				}
				if (!unit.getTeam().equals(team)) {
					ofThisTeam = false;
					break;
				}
			}

			if (ofThisTeam) {
				return team;
			} else {
				continue;
			}
		}
		return null;
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

			checkAllPlayersOfTeamDead(deadPlayer);
		}
	}

	private void setUpRespawnTimer() {
		// TODO: assuming that there is a main base at this point and that its
		// timing source is valid is very bad design. change this some day :D
		timingSource = mainBaseOfTeam.get(getTeamA()).getTimingSource();

		respawnTimer = new RespawnTimer();
		timingSource.addTimedEventHandler(respawnTimer);
	}

	class RespawnTimer implements TimedEventHandler {

		@Override
		public long getInterval() {
			return S.Server.gm_edura_respawn_time;
		}

		@Override
		public void onIntervalElapsed(long delta) {
			for (Player player : gameInfo.getPlayers()) {
				if (player.isDead()) {
					gameInfo.getEventTriggerer()
							.respawnPlayerAtRandomSpawnpoint(player);
				}
			}
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
		for (Base aMainBase : mainBaseOfTeam.values()) {
			if (base.equals(aMainBase)) {
				// TODO: we're assuming that a main base cannot be conquered by
				// the own team for any reason. put a check if the conquered
				// main base belongs to the opponent.
				matchEnd = true;
			}
		}

		if (matchEnd) {
			endRound(occupyingTeam);
		} else {
			baseToVertex.get(base).setColor(occupyingTeam.getTeamId());
		}

		startGeneratingResourcesInBaseForTeam(base, occupyingTeam);
	}

	private void startGeneratingResourcesInBaseForTeam(Base base, Team team) {
		// generate resources
		if (baseToResourceGenerator.containsKey(base)) {
			L.severe("Base got occupied although it's already generating resources.");
		} else {
			ResourceGenerator newGenerator = new ResourceGenerator(base, team);
			base.getTimingSource().addTimedEventHandler(newGenerator);
			baseToResourceGenerator.put(base, newGenerator);
		}

	}

	class ResourceGenerator implements TimedEventHandler {

		private Base base;
		private Team team;

		public ResourceGenerator(Base base, Team team) {
			this.base = base;
			this.team = team;
		}

		@Override
		public long getInterval() {
			return base.getResourceGenerateTimeInterval();
		}

		@Override
		public void onIntervalElapsed(long delta) {
			gameInfo.getEventTriggerer().changeResourcesOfTeamByAmount(team,
					base.getResourceGenerateAmountPerTimeInterval());
		}
	}

	@Override
	public void onBaseLost(Base base, Team losingTeam) {
		// stop generating resources for the team
		base.getTimingSource().removeTimedEventHandler(
				baseToResourceGenerator.get(base));
		baseToResourceGenerator.put(base, null);
	}

	@Override
	public boolean canSwitchMode(Player player, InteractMode mode) {
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

		if (S.Server.gm_edura_automatic_respawn && timingSource != null) {
			timingSource.removeTimedEventHandler(respawnTimer);
			respawnTimer = null;
		}
	}
}
