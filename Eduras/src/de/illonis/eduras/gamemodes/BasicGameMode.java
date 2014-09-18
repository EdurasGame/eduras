package de.illonis.eduras.gamemodes;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.newdawn.slick.geom.Vector2f;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.GameInformation;
import de.illonis.eduras.ObjectFactory.ObjectType;
import de.illonis.eduras.Team;
import de.illonis.eduras.exceptions.NoSuchGameModeException;
import de.illonis.eduras.exceptions.ObjectNotFoundException;
import de.illonis.eduras.gameobjects.Base;
import de.illonis.eduras.gameobjects.Base.BaseType;
import de.illonis.eduras.gameobjects.GameObject;
import de.illonis.eduras.gameobjects.GameObject.Relation;
import de.illonis.eduras.gameobjects.TimedEventHandler;
import de.illonis.eduras.maps.Map;
import de.illonis.eduras.maps.NodeData;
import de.illonis.eduras.maps.SpawnPosition.SpawnType;
import de.illonis.eduras.math.graphs.Vertex;
import de.illonis.eduras.units.Unit;

/**
 * The BasicGameMode defines behavior that applies to all game modes.
 * 
 * @author Florian Mai <florian.ren.mai@googlemail.com>
 * 
 */
public abstract class BasicGameMode implements GameMode {

	private final static Logger L = EduLog.getLoggerFor(BasicGameMode.class
			.getName());
	protected GameInformation gameInfo;
	private HashMap<Base, ResourceGenerator> baseToResourceGenerator;
	private HashMap<Team, Base> mainBaseOfTeam;
	private HashMap<Base, Vertex> baseToVertex;
	protected LinkedList<Team> teams;

	/**
	 * Creates a GameMode with the given game information.
	 * 
	 * @param gameInfo
	 */
	public BasicGameMode(GameInformation gameInfo) {
		this.gameInfo = gameInfo;
		baseToResourceGenerator = new HashMap<Base, ResourceGenerator>();
		mainBaseOfTeam = new HashMap<Team, Base>();
		teams = new LinkedList<Team>();
		baseToVertex = new HashMap<Base, Vertex>();
	}

	@Override
	public abstract GameModeNumber getNumber();

	@Override
	public abstract String getName();

	@Override
	public abstract Relation getRelation(GameObject a, GameObject b);

	@Override
	public abstract void onDeath(Unit killedUnit, int killingPlayer);

	@Override
	public abstract void onTimeUp();

	@Override
	public void onConnect(int ownerId) {
		if (gameInfo.getPlayers().size() >= gameInfo.getGameSettings()
				.getMaxPlayers()) {
			gameInfo.getEventTriggerer().kickPlayer(ownerId);
		}
	}

	@Override
	public abstract void onGameStart();

	@Override
	public abstract SpawnType getSpawnTypeForTeam(Team team);

	/**
	 * 
	 * @param gameModeName
	 * @param gameInfo
	 * @return The gamemode, if existing.
	 * @throws NoSuchGameModeException
	 *             Thrown if there is no such GameMode of the given name.
	 */
	public static GameMode getGameModeByName(String gameModeName,
			GameInformation gameInfo) throws NoSuchGameModeException {
		switch (gameModeName.toLowerCase()) {
		case "deathmatch":
			return new Deathmatch(gameInfo);
		case "teamdeathmatch":
			return new TeamDeathmatch(gameInfo);
		case "kingofthehill":
			return new KingOfTheHill(gameInfo);
		case "edura":
			return new Edura(gameInfo);
		default:
			throw new NoSuchGameModeException(gameModeName);
		}
	}

	protected void stopGeneratingResourcesInBaseForTeam(Base base, Team team) {
		// stop generating resources for the team
		base.getTimingSource().removeTimedEventHandler(
				baseToResourceGenerator.get(base));
		baseToResourceGenerator.put(base, null);
	}

	protected void startGeneratingResourcesInBaseForTeam(Base base, Team team) {
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

	private void clearNodes() {
		for (Base neutralBase : baseToVertex.keySet()) {
			gameInfo.getEventTriggerer().removeObject(neutralBase.getId());
		}
		baseToVertex.clear();
		baseToResourceGenerator.clear();
	}

	/**
	 * Make sure this is loaded after teams set.
	 */
	protected void loadNodes() {
		HashMap<Integer, Base> nodeIdToBase = new HashMap<Integer, Base>();
		Map eduraMap = gameInfo.getMap();

		clearNodes();

		for (NodeData nodeData : eduraMap.getNodes()) {
			int nodeid = nodeData.getId();
			int objectId = gameInfo.getEventTriggerer().createObjectAt(
					ObjectType.NEUTRAL_BASE,
					new Vector2f(nodeData.getXPosition(), nodeData
							.getYPosition()), -1);

			Base base;
			try {
				base = (Base) gameInfo.findObjectById(objectId);
				base.setResourceGenerateMultiplicator(nodeData
						.getResourceMultiplicator());
				gameInfo.getEventTriggerer().setTriggerAreaSize(objectId,
						nodeData.getWidth(), nodeData.getHeight());
			} catch (ObjectNotFoundException e) {
				L.log(Level.WARNING, "Cannot find object!", e);
				return;
			}
			nodeIdToBase.put(nodeid, base);
			baseToVertex.put(base, new Vertex());
		}

		// TODO: At the moment we rely on the assumption that there are always
		// two main nodes in the map and that there is only two teams.
		Team teamA = teams.get(0);
		Team teamB = teams.get(1);

		for (NodeData nodeData : eduraMap.getNodes()) {
			int nodeid = nodeData.getId();
			Vertex vertexForNode = nodeIdToVertex(nodeIdToBase, nodeid);

			for (NodeData adjacentNode : nodeData.getAdjacentNodes()) {
				Vertex vertexForAdjacent = nodeIdToVertex(nodeIdToBase,
						adjacentNode.getId());

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

	@Override
	public Team determineProgressingTeam(Base base, GameObject object,
			boolean objectEntered, Set<GameObject> presentObjects) {
		Team team = determineOnlyContainingTeam(presentObjects);

		if (team == null) {
			return team;
		}
		Vertex vertex = baseToVertex.get(base);
		if (vertex == null) {
			return null;
		}
		if (vertex.getAdjacentVertices().isEmpty()
				|| vertex.hasAdjacentNodeOfColor(team.getTeamId())) {
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

	protected Collection<Base> getAllBases() {
		return baseToVertex.keySet();
	}

	protected Collection<Base> getMainBases() {
		return mainBaseOfTeam.values();
	}

	protected Base getMainbaseOfTeam(Team team) {
		return mainBaseOfTeam.get(team);
	}

	protected final void setTeamOfNode(Base base, Team team) {
		baseToVertex.get(base).setColor(team.getTeamId());
	}

	private Vertex nodeIdToVertex(HashMap<Integer, Base> nodeIdToBase,
			int nodeid) {
		return baseToVertex.get(nodeIdToBase.get(nodeid));
	}

}
