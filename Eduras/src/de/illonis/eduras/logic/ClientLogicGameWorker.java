package de.illonis.eduras.logic;

import java.awt.geom.Area;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.newdawn.slick.geom.Vector2f;

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
import de.illonis.eduras.math.Vector2df;
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

	private final static float ROTATION_STEP_RESOLUTION = 1;
	private final static float ROTATION_DIST_RESOLUTION = 10;

	protected ClientLogicGameWorker(GameInformation gameInfo,
			ListenerHolder<? extends GameEventListener> listenerHolder) {
		super(gameInfo, listenerHolder);
	}

	@Override
	public void gameUpdate(long delta) {

		for (GameObject o : gameInformation.getObjects().values()) {
			if (o instanceof Usable) {
				((Usable) o).reduceCooldown(delta);
			}
			if (o instanceof MoveableGameObject) {
				MoveableGameObject mgo = (MoveableGameObject) o;
				mgo.onMove(delta);
				if (listenerHolder.hasListener())
					listenerHolder.getListener().onNewObjectPosition(o);
				gameInformation.getEventTriggerer().notifyNewObjectPosition(o);
			}

			if (hasRotated(o)) {
				gameInformation.getEventTriggerer().setRotation(o);
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
		if (team == null)
			return;

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
		// for (GameObject nearObject : nearby) {
		// visionMask.add(new Area(nearObject.getBoundingBox()));
		// }
		// visionMask.add(new Area(player.getBoundingBox()));

		VisionInformation vinfo = gameInformation.getClientData()
				.getVisionInfo();
		synchronized (vinfo) {
			vinfo.clear();
			vinfo.setAreaForTeam(team, visionArea);
			vinfo.setMask(visionMask);
		}

	}

	private Area getVisionShapeFor(GameObject obj, LinkedList<GameObject> nearby) {
		LinkedList<Vector2df> polyEdges = new LinkedList<Vector2df>();

		final float angle = obj.getVisionAngle();
		final float radius = obj.getVisionRange();
		final float rotation = obj.getRotation();

		Vector2df orientation = new Vector2df(1, 0);
		orientation.rotate(rotation);
		Vector2df start = new Vector2df(orientation);
		start.rotate(-angle / 2);

		Vector2df work = start.copy();
		final Vector2df u = obj.getPositionVector();

		for (double i = 0; i < angle; i += ROTATION_STEP_RESOLUTION) {
			Vector2df v = u.copy();
			v.add(work);

			Line l = new Line(u, v);
			Vector2f r = l.getDirectionalVector().normalise();
			r.scale(ROTATION_DIST_RESOLUTION);
			double distance = 0;
			int j = 1;
			Vector2df p = null;
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
				distance = p.distance(u);
			}
			polyEdges.add(p);
			work.rotate(ROTATION_STEP_RESOLUTION);
		}

		java.awt.Polygon poly = new java.awt.Polygon();
		for (Vector2df vector2d : polyEdges) {
			poly.addPoint((int) Math.round(vector2d.getX()),
					(int) Math.round(vector2d.getY()));
		}
		poly.addPoint((int) Math.round(u.getX()), (int) Math.round(u.getY()));
		return new Area(poly);
	}
}
