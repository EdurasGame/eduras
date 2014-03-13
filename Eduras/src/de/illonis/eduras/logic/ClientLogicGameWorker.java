package de.illonis.eduras.logic;

import java.awt.geom.Area;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map.Entry;

import de.illonis.eduras.GameInformation;
import de.illonis.eduras.Team;
import de.illonis.eduras.gameclient.VisionInformation;
import de.illonis.eduras.gameobjects.GameObject;
import de.illonis.eduras.gameobjects.MoveableGameObject;
import de.illonis.eduras.interfaces.GameEventListener;
import de.illonis.eduras.items.Usable;
import de.illonis.eduras.math.Line;
import de.illonis.eduras.math.Vector2D;
import de.illonis.eduras.settings.S;
import de.illonis.eduras.units.PlayerMainFigure;

/**
 * The {@link LogicGameWorker} on client side.
 * 
 * @author illonis
 * 
 */
public class ClientLogicGameWorker extends LogicGameWorker {

	private final static double ROTATION_STEP_RESOLUTION = 1;
	private final static double ROTATION_DIST_RESOLUTION = 10;

	protected ClientLogicGameWorker(GameInformation gameInfo,
			ListenerHolder<? extends GameEventListener> listenerHolder) {
		super(gameInfo, listenerHolder);
	}

	@Override
	protected void gameUpdate(long delta) {

		for (GameObject o : gameInformation.getObjects().values()) {
			if (o instanceof Usable) {
				((Usable) o).reduceCooldown(delta);
			}
			if (o instanceof MoveableGameObject) {
				if (!((MoveableGameObject) o).getSpeedVector().isNull()) {
					((MoveableGameObject) o).onMove(delta);
					if (listenerHolder.hasListener())
						listenerHolder.getListener().onNewObjectPosition(o);
					gameInformation.getEventTriggerer()
							.notifyNewObjectPosition(o);
				}

				if (hasRotated(o)) {
					gameInformation.getEventTriggerer().setRotation(o);
				}
			}
		}
		updateVision();
	}

	private void updateVision() {
		if (S.vision_disabled)
			return;

		// TODO: improve the following by storing objects for teams directly.

		// sort objects by their team to separate vision for each team.
		HashMap<Team, LinkedList<GameObject>> objects = new HashMap<Team, LinkedList<GameObject>>();
		HashMap<Integer, Team> teamOwners = new HashMap<Integer, Team>();

		for (Team team : gameInformation.getTeams()) {
			LinkedList<GameObject> teamobjects = new LinkedList<GameObject>();
			objects.put(team, teamobjects);
			for (PlayerMainFigure player : team.getPlayers()) {
				teamOwners.put(player.getOwner(), team);
			}
		}
		for (GameObject o : gameInformation.getObjects().values()) {
			if (o.getOwner() == -1)
				continue;
			Team t = teamOwners.get(o.getOwner());
			if (t != null)
				objects.get(t).add(o);
		}

		VisionInformation vinfo = gameInformation.getClientData()
				.getVisionInfo();

		HashMap<Team, Area> teamVision = new HashMap<Team, Area>();

		for (Entry<Team, LinkedList<GameObject>> objList : objects.entrySet()) {
			Area visionArea = new Area();
			for (GameObject obj : objList.getValue())
				visionArea.add(getVisionShapeFor(obj));
			teamVision.put(objList.getKey(), visionArea);
		}

		synchronized (vinfo) {
			vinfo.clear();
			for (Entry<Team, Area> vision : teamVision.entrySet()) {
				vinfo.setAreaForTeam(vision.getKey(), vision.getValue());
			}
		}

	}

	private Area getVisionShapeFor(GameObject obj) {
		LinkedList<Vector2D> polyEdges = new LinkedList<Vector2D>();

		final double angle = obj.getVisionAngle();
		final double radius = obj.getVisionRange();
		final double rotation = obj.getRotation();

		Vector2D orientation = new Vector2D(1, 0);
		orientation.rotate(rotation);
		Vector2D start = new Vector2D(orientation);
		start.rotate(-angle / 2);

		Vector2D work = start.copy();
		final Vector2D u = obj.getPositionVector();

		for (double i = 0; i < angle; i += ROTATION_STEP_RESOLUTION) {
			Vector2D v = u.copy();
			v.add(work);

			Line l = new Line(u, v);
			Vector2D r = l.getDirectionalVector().getUnitVector();
			r.mult(ROTATION_DIST_RESOLUTION);
			double distance = 0;
			int j = 1;
			Vector2D p = null;
			while (distance < radius) {
				p = l.getPointAt(ROTATION_DIST_RESOLUTION * j);
				if (gameInformation.isObjectAt(p, obj)) {
					break;
				}
				j++;
				distance = p.calculateDistance(u);
			}
			polyEdges.add(p);
			work.rotate(ROTATION_STEP_RESOLUTION);
		}

		java.awt.Polygon poly = new java.awt.Polygon();
		for (Vector2D vector2d : polyEdges) {
			poly.addPoint((int) Math.round(vector2d.getX()),
					(int) Math.round(vector2d.getY()));
		}
		poly.addPoint((int) Math.round(u.getX()), (int) Math.round(u.getY()));
		return new Area(poly);
	}

}
