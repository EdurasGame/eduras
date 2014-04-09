package de.illonis.eduras.gamemodes;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Logger;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.GameInformation;
import de.illonis.eduras.ObjectFactory.ObjectType;
import de.illonis.eduras.Team;
import de.illonis.eduras.gameobjects.GameObject;
import de.illonis.eduras.gameobjects.NeutralBase;
import de.illonis.eduras.maps.EduraMap;
import de.illonis.eduras.maps.NodeData;
import de.illonis.eduras.math.Vector2df;
import de.illonis.eduras.math.graphs.Vertex;
import de.illonis.eduras.units.Unit;

public class Edura extends TeamDeathmatch {

	private HashMap<NeutralBase, Vertex> baseToVertex;
	private HashMap<Team, NeutralBase> mainBaseOfTeam;

	private final static Logger L = EduLog.getLoggerFor(Edura.class.getName());

	public Edura(GameInformation gameInfo) {
		super(gameInfo);

		baseToVertex = new HashMap<NeutralBase, Vertex>();
		mainBaseOfTeam = new HashMap<Team, NeutralBase>();
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
		// TODO: do something when time is up
	}

	@Override
	public void onGameStart() {
		super.onGameStart();

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
		Team teamA = iterator.next();
		Team teamB = iterator.next();
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
				} else {
					vertexForNode.setColor(teamA.getTeamId());
					NeutralBase mainBase = nodeIdToBase.get(nodeid);
					mainBaseOfTeam.put(teamA, mainBase);
					mainBase.setCurrentOwnerTeam(teamA);
					teamAHasMainNode = true;
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
			gameInfo.getEventTriggerer().onMatchEnd();
		} else {
			baseToVertex.get(base).setColor(occupyingTeam.getTeamId());
		}
	}

	@Override
	public void onBaseLost(NeutralBase base, Team losingTeam) {
		// don't do anything
	}
}
