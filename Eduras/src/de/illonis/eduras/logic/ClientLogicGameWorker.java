package de.illonis.eduras.logic;

import java.awt.geom.Area;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.GameInformation;
import de.illonis.eduras.Team;
import de.illonis.eduras.exceptions.ObjectNotFoundException;
import de.illonis.eduras.gameclient.VisionInformation;
import de.illonis.eduras.gameobjects.GameObject;
import de.illonis.eduras.gameobjects.MoveableGameObject;
import de.illonis.eduras.interfaces.GameEventListener;
import de.illonis.eduras.items.Usable;
import de.illonis.eduras.logicabstraction.EdurasInitializer;
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

	private final static Logger L = EduLog
			.getLoggerFor(ClientLogicGameWorker.class.getName());

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

		int playerOwner = EdurasInitializer.getInstance()
				.getInformationProvider().getOwnerID();
		PlayerMainFigure player;
		try {
			player = gameInformation.getPlayerByOwnerId(playerOwner);
		} catch (ObjectNotFoundException e) {
			L.log(Level.SEVERE, "Player has no mainfigure.", e);
			return;
		}

		Team team = player.getTeam();

		LinkedList<Integer> teamOwners = new LinkedList<Integer>();
		LinkedList<GameObject> teamObjects = new LinkedList<GameObject>();

		for (PlayerMainFigure teamPlayer : team.getPlayers()) {
			teamOwners.add(teamPlayer.getOwner());
		}

		for (GameObject o : gameInformation.getObjects().values()) {
			if (o.getOwner() == -1)
				continue;
			if (teamOwners.contains(o.getOwner())) {
				teamObjects.add(o);
			}
		}
		Area visionArea = new Area();

		LinkedList<GameObject> nearby = new LinkedList<GameObject>();

		for (GameObject gameObject : teamObjects) {
			visionArea.add(getVisionShapeFor(gameObject, nearby));
		}

		Area visionMask = new Area(visionArea);
		for (GameObject nearObject : nearby) {
			visionMask.add(new Area(nearObject.getBoundingBox()));
		}
		visionMask.add(new Area(player.getBoundingBox()));

		VisionInformation vinfo = gameInformation.getClientData()
				.getVisionInfo();
		synchronized (vinfo) {
			vinfo.clear();
			vinfo.setAreaForTeam(team, visionArea);
			vinfo.setMask(visionMask);
		}

	}

	private Area getVisionShapeFor(GameObject obj, LinkedList<GameObject> nearby) {
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
				LinkedList<GameObject> objs = gameInformation
						.findObjectsInDistance(p, ROTATION_DIST_RESOLUTION);
				if (objs.size() > 0) {
					if (objs.size() > 1 || !objs.getFirst().equals(obj)) {
						nearby.addAll(objs);
						break;
					}
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
