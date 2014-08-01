package de.illonis.eduras.gamemodes;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.GameInformation;
import de.illonis.eduras.ObjectFactory.ObjectType;
import de.illonis.eduras.Player;
import de.illonis.eduras.Team;
import de.illonis.eduras.exceptions.ObjectNotFoundException;
import de.illonis.eduras.exceptions.WrongObjectTypeException;
import de.illonis.eduras.gameclient.userprefs.KeyBindings.KeyBinding;
import de.illonis.eduras.gameobjects.GameObject;
import de.illonis.eduras.gameobjects.NeutralBase;
import de.illonis.eduras.gameobjects.TimedEventHandler;
import de.illonis.eduras.logic.EventTriggerer;
import de.illonis.eduras.maps.EduraMap;
import de.illonis.eduras.maps.NodeData;
import de.illonis.eduras.math.Vector2df;
import de.illonis.eduras.math.graphs.Vertex;
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

	private HashMap<NeutralBase, Vertex> baseToVertex;
	private HashMap<Team, NeutralBase> mainBaseOfTeam;
	private HashMap<NeutralBase, ResourceGenerator> baseToResourceGenerator;
	private Team teamA;
	private Team teamB;

	private final static Logger L = EduLog.getLoggerFor(Edura.class.getName());

	/**
	 * Create the game mode.
	 * 
	 * @param gameInfo
	 */
	public Edura(GameInformation gameInfo) {
		super(gameInfo);

		baseToVertex = new HashMap<NeutralBase, Vertex>();
		mainBaseOfTeam = new HashMap<Team, NeutralBase>();
		baseToResourceGenerator = new HashMap<NeutralBase, ResourceGenerator>();
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

		for (NeutralBase aNeutralBase : baseToVertex.keySet()) {
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
	}

	private void loadNodes() {
		HashMap<Integer, NeutralBase> nodeIdToBase = new HashMap<Integer, NeutralBase>();
		EduraMap eduraMap = (EduraMap) gameInfo.getMap();

		clear();

		for (NodeData nodeData : eduraMap.getNodes()) {
			int nodeid = nodeData.getId();
			int objectId = gameInfo.getEventTriggerer().createObjectAt(
					ObjectType.NEUTRAL_BASE,
					new Vector2df(nodeData.getX(), nodeData.getY()), -1);

			NeutralBase base = (NeutralBase) gameInfo.findObjectById(objectId);
			nodeIdToBase.put(nodeid, base);
			baseToVertex.put(base, new Vertex());
		}

		// TODO: At the moment we rely on the assumption that there are always
		// two main nodes in the map and that there is only two teams.
		Iterator<Team> iterator = gameInfo.getTeams().iterator();
		teamA = iterator.next();
		teamB = iterator.next();
		boolean teamAHasMainNode = false;

		for (NodeData nodeData : eduraMap.getNodes()) {
			int nodeid = nodeData.getId();
			Vertex vertexForNode = nodeIdToVertex(nodeIdToBase, nodeid);

			for (Integer adjacentNode : nodeData.getAdjacentNodes()) {
				Vertex vertexForAdjacent = nodeIdToVertex(nodeIdToBase,
						adjacentNode);

				vertexForNode.addAdjacentVertex(vertexForAdjacent);
			}

			if (nodeData.isMainNode()) {
				if (teamAHasMainNode) {
					vertexForNode.setColor(teamB.getTeamId());
					NeutralBase mainBase = nodeIdToBase.get(nodeid);
					mainBaseOfTeam.put(teamB, mainBase);
					mainBase.setCurrentOwnerTeam(teamB);
					gameInfo.getEventTriggerer().notifyAreaConquered(mainBase,
							teamB);

					startGeneratingResourcesInBaseForTeam(mainBase, teamB);
				} else {
					vertexForNode.setColor(teamA.getTeamId());
					NeutralBase mainBase = nodeIdToBase.get(nodeid);
					mainBaseOfTeam.put(teamA, mainBase);
					mainBase.setCurrentOwnerTeam(teamA);
					teamAHasMainNode = true;
					gameInfo.getEventTriggerer().notifyAreaConquered(mainBase,
							teamA);

					startGeneratingResourcesInBaseForTeam(mainBase, teamA);
				}
			}
		}

	}

	private void clear() {
		for (GameObject neutralBase : baseToVertex.keySet()) {
			gameInfo.getEventTriggerer().removeObject(neutralBase.getId());
		}
	}

	private Vertex nodeIdToVertex(HashMap<Integer, NeutralBase> nodeIdToBase,
			int nodeid) {
		return baseToVertex.get(nodeIdToBase.get(nodeid));
	}

	@Override
	public Team determineProgressingTeam(NeutralBase base, GameObject object,
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

	private void checkAllPlayersOfTeamDead(Player player) {
		boolean allPlayersDead = true;
		for (Player aPlayerOfSameTeam : player.getTeam().getPlayers()) {
			// if there is a player who is alive, the team can still win
			if (aPlayerOfSameTeam.getCurrentMode() != InteractMode.MODE_DEAD) {
				allPlayersDead = false;
				break;
			}
		}

		if (allPlayersDead) {
			if (player.getTeam().equals(teamA)) {
				endRound(teamB);
			} else {
				endRound(teamA);
			}
			
		}
	}

	private void endRound(Team winnerTeam) {
		gameInfo.getEventTriggerer().onMatchEnd(winnerTeam.getTeamId());
	}

	@Override
	public void onBaseOccupied(NeutralBase base, Team occupyingTeam) {
		boolean matchEnd = false;
		for (NeutralBase aMainBase : mainBaseOfTeam.values()) {
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

	private void startGeneratingResourcesInBaseForTeam(NeutralBase base,
			Team team) {
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

		private NeutralBase base;
		private Team team;

		public ResourceGenerator(NeutralBase base, Team team) {
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
	public void onBaseLost(NeutralBase base, Team losingTeam) {
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

			for (GameObject intersectingGameObject : intersectingObjects) {
				NeutralBase intersectingBase = (NeutralBase) intersectingGameObject;
				if (intersectingBase.getCurrentOwnerTeam() != null
						&& intersectingBase.getCurrentOwnerTeam().equals(
								player.getTeam())) {
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
