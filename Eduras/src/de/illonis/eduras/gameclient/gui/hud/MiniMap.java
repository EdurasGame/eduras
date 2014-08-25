package de.illonis.eduras.gameclient.gui.hud;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.lwjgl.opengl.Display;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.ObjectFactory.ObjectType;
import de.illonis.eduras.events.ObjectFactoryEvent;
import de.illonis.eduras.exceptions.ObjectNotFoundException;
import de.illonis.eduras.gameclient.gui.game.GameCamera;
import de.illonis.eduras.gameclient.gui.game.GameRenderer;
import de.illonis.eduras.gameclient.gui.hud.minimap.MiniMapBase;
import de.illonis.eduras.gameclient.gui.hud.minimap.MiniMapNeutralObject;
import de.illonis.eduras.gameclient.gui.hud.minimap.MiniMapPlayer;
import de.illonis.eduras.gameobjects.Base;
import de.illonis.eduras.gameobjects.DynamicPolygonObject;
import de.illonis.eduras.gameobjects.GameObject;
import de.illonis.eduras.items.weapons.Missile;
import de.illonis.eduras.items.weapons.Weapon;
import de.illonis.eduras.maps.NodeData;
import de.illonis.eduras.math.Geometry;
import de.illonis.eduras.units.PlayerMainFigure;

/**
 * A minimap.
 * 
 * @author illonis
 * 
 */
public class MiniMap extends ClickableGuiElement {

	private final static Logger L = EduLog
			.getLoggerFor(MiniMap.class.getName());

	private HashMap<Integer, MiniMapNeutralObject> neutralObjects;
	private HashMap<Integer, MiniMapBase> bases;
	private HashMap<Integer, MiniMapPlayer> players;
	private GameCamera viewPort;
	private float scale;
	private HashMap<Integer, NodeData> nodes;
	float windowScale;

	final static int SIZE = 150;
	private static final Color NEUTRAL_OBJECTS_FILL_COLOR = Color.gray;

	private final Rectangle bounds;

	protected MiniMap(UserInterface gui) {
		super(gui);
		windowScale = GameRenderer.getRenderScale();
		bounds = new Rectangle(0, 0, SIZE * windowScale, SIZE * windowScale);
		neutralObjects = new HashMap<Integer, MiniMapNeutralObject>();
		bases = new HashMap<Integer, MiniMapBase>();
		players = new HashMap<Integer, MiniMapPlayer>();
		scale = 1f;
	}

	private void renderNeutral(Graphics g) {
		g.setColor(NEUTRAL_OBJECTS_FILL_COLOR);
		g.setLineWidth(1f);
		LinkedList<MiniMapNeutralObject> objectsToRender = new LinkedList<MiniMapNeutralObject>(
				neutralObjects.values());
		for (MiniMapNeutralObject object : objectsToRender) {
			if (object.isDynamicShape()) {
				g.fill(new Polygon(
						Geometry.vectorsToFloat(object.getVertices())));
			} else {
				g.fillRect(object.getX(), object.getY(), object.getWidth(),
						object.getHeight());
			}
		}
	}

	float getSize() {
		return SIZE * windowScale;
	}

	private void renderBases(Graphics g) {
		g.setLineWidth(1f);
		for (MiniMapBase object : bases.values()) {
			g.setColor(object.getColor());
			g.fillRect(object.getX(), object.getY(), object.getWidth(),
					object.getHeight());
		}
	}

	private synchronized void renderNodeConnections(Graphics g) {
		checkIfNodesInitialized();

		if (nodes != null && !nodes.isEmpty()) {
			g.setLineWidth(1f);
			g.setColor(Color.yellow);
			for (NodeData someNode : nodes.values()) {

				for (NodeData adjacentOfSomeNode : someNode.getAdjacentNodes()) {

					// draw a line from some node to his adjacent
					Vector2f someNodePositionOnMinimap = gameToMinimapPosition(new Vector2f(
							someNode.getX(), someNode.getY()));
					Vector2f adjacentOfSomeNodePositionOnMinimap = gameToMinimapPosition(new Vector2f(
							adjacentOfSomeNode.getX(),
							adjacentOfSomeNode.getY()));

					g.drawLine(someNodePositionOnMinimap.getX(),
							someNodePositionOnMinimap.getY(),
							adjacentOfSomeNodePositionOnMinimap.getX(),
							adjacentOfSomeNodePositionOnMinimap.getY());
				}
			}
		}
	}

	private void checkIfNodesInitialized() {
		if (nodes == null) {
			nodes = NodeData.nodeDataToVertices(getInfo().getNodes());
		}
	}

	private void renderPlayers(Graphics g) {
		List<MiniMapPlayer> ps = new LinkedList<MiniMapPlayer>(players.values());
		for (int i = 0; i < ps.size(); i++) {
			MiniMapPlayer object = ps.get(i);
			g.setColor(object.getColor());
			g.fillOval(object.getX(), object.getY(), object.getWidth(),
					object.getHeight());
		}
	}

	@Override
	public boolean mouseReleased(int button, int x, int y) {
		centerAtMouse(x, y);
		return true;
	}

	@Override
	public boolean mouseDragged(int oldx, int oldy, int newx, int newy) {
		centerAtMouse(newx, newy);
		return true;
	}

	private void centerAtMouse(int x, int y) {
		Vector2f gamePos = minimapToGamePosition(new Vector2f(x, y));
		getMouseHandler().mapClicked(gamePos);
	}

	@Override
	public Rectangle getBounds() {
		return bounds;
	}

	private Vector2f gameToMinimapPosition(Vector2f pos) {
		Vector2f miniPos = new Vector2f(pos).scale(scale);
		miniPos.add(new Vector2f(getBounds().getX(), getBounds().getY()));
		return miniPos;
	}

	private Vector2f minimapToGamePosition(Vector2f pos) {
		Vector2f gamePos = new Vector2f(pos).sub(new Vector2f(getBounds()
				.getX(), getBounds().getY()));
		gamePos.scale(1 / scale);
		return gamePos;
	}

	@Override
	public void render(Graphics g) {
		g.setColor(Color.black);
		g.fill(bounds);

		synchronized (neutralObjects) {
			renderNeutral(g);
		}

		synchronized (bases) {
			renderBases(g);
		}

		renderNodeConnections(g);

		synchronized (players) {
			renderPlayers(g);
		}

		renderViewPort(g);
	}

	private void renderViewPort(Graphics g) {
		g.setLineWidth(1f);
		g.setColor(Color.white);
		float minimapScale = getSize() / getInfo().getMapBounds().getHeight();
		float rectWidth = 800f;
		float ratio = (float) Display.getHeight() / Display.getWidth();
		float rectHeight = rectWidth * ratio;
		float yDiff = (600 - rectHeight) / 2;
		g.drawRect(viewPort.getX() * minimapScale, (viewPort.getY() + yDiff)
				* minimapScale + screenY, rectWidth * minimapScale, rectHeight
				* minimapScale);
	}

	@Override
	public void onGuiSizeChanged(int newWidth, int newHeight) {
		screenY = newHeight - getSize();
		bounds.setLocation(screenX, screenY);
		relocateObjects();
	}

	private void relocateObjects() {
		synchronized (players) {
			for (MiniMapPlayer player : players.values()) {
				Vector2f miniPos = gameToMinimapPosition(player
						.getObjectLocation());
				player.setLocation(miniPos.x, miniPos.y);
			}
		}

		synchronized (bases) {
			for (MiniMapBase base : bases.values()) {
				Vector2f miniPos = gameToMinimapPosition(base
						.getObjectLocation());
				base.setLocation(miniPos.x, miniPos.y);
			}
		}

		synchronized (neutralObjects) {
			for (MiniMapNeutralObject object : neutralObjects.values()) {
				Vector2f miniPos = gameToMinimapPosition(object
						.getObjectLocation());
				object.setLocation(miniPos.x, miniPos.y);
			}
		}
	}

	@Override
	public void onObjectCreation(ObjectFactoryEvent event) {
		GameObject o;
		try {
			o = getInfo().findObjectById(event.getId());
		} catch (ObjectNotFoundException e) {
			L.log(Level.WARNING, "Cannot find object that was just created!", e);
			return;
		}
		maybeAddObject(o);
	}

	@Override
	public void onObjectRemove(ObjectFactoryEvent event) {
		GameObject o;
		try {
			o = getInfo().findObjectById(event.getId());
		} catch (ObjectNotFoundException e) {
			L.log(Level.WARNING, "Cannot find object to remove!", e);
			return;
		}

		if (!isTracked(o)) {
			return;
		}

		if (o instanceof PlayerMainFigure) {
			synchronized (players) {
				players.remove(o.getId());
			}
		} else if (o instanceof Base) {
			synchronized (bases) {
				bases.remove(o.getId());
			}
			resetNodeData();
		} else if (o.getOwner() == -1) {
			synchronized (neutralObjects) {
				neutralObjects.remove(o.getId());
			}
		}
	}

	private void maybeAddObject(GameObject o) {
		if (!isTracked(o)) {
			return;
		}
		Vector2f gamePos = new Vector2f(o.getXPosition(), o.getYPosition());
		Vector2f miniPos = gameToMinimapPosition(gamePos);
		float x = miniPos.x;
		float y = miniPos.y;

		if (o instanceof PlayerMainFigure) {
			float w = o.getShape().getWidth() * scale;
			float h = o.getShape().getHeight() * scale;

			synchronized (players) {
				players.put(o.getId(), new MiniMapPlayer((PlayerMainFigure) o,
						x, y, w, h));
			}
		} else if (o instanceof Base) {
			float w = o.getShape().getWidth() * scale;
			float h = o.getShape().getHeight() * scale;

			synchronized (bases) {
				bases.put(o.getId(), new MiniMapBase((Base) o, x, y, w, h));
			}

			resetNodeData();
		} else if (o.getOwner() == -1 && !(o instanceof Weapon)) {

			float w = o.getShape().getWidth() * scale;
			float h = o.getShape().getHeight() * scale;

			MiniMapNeutralObject newNeutralObject;

			if (o.getType() == ObjectType.DYNAMIC_POLYGON_BLOCK) {
				DynamicPolygonObject dynamicObject = (DynamicPolygonObject) o;

				Vector2f[] originalVertices = dynamicObject
						.getPolygonVertices();
				Vector2f[] verticesOnMinimap = new Vector2f[originalVertices.length];
				for (int i = 0; i < originalVertices.length; i++) {

					verticesOnMinimap[i] = gameToMinimapPosition(originalVertices[i]);
				}
				newNeutralObject = new MiniMapNeutralObject(o, x, y, w, h,
						verticesOnMinimap);
			} else {
				newNeutralObject = new MiniMapNeutralObject(o, x, y, w, h);
			}

			synchronized (neutralObjects) {
				neutralObjects.put(o.getId(), newNeutralObject);
			}
		}
	}

	private synchronized void resetNodeData() {
		nodes = null;
	}

	private boolean isTracked(GameObject o) {
		if (o == null || o instanceof Missile || o instanceof Weapon) {
			return false;
		}
		return true;
	}

	@Override
	public void onNewObjectPosition(GameObject object) {
		if (!isTracked(object)) {
			return;
		}

		Vector2f gamePos = new Vector2f(object.getXPosition(),
				object.getYPosition());
		Vector2f miniPos = gameToMinimapPosition(gamePos);
		float x = miniPos.x;
		float y = miniPos.y;
		switch (object.getType()) {
		case PLAYER:
			MiniMapPlayer p = players.get(object.getId());
			if (p != null)
				p.setLocation(x, y);
			break;
		case NEUTRAL_BASE:
			bases.get(object.getId()).setLocation(x, y);
			break;
		default:
			if (object.getOwner() == -1) {
				neutralObjects.get(object.getId()).setLocation(x, y);
			}
			break;
		}
	}

	@Override
	public void onGameReady() {
		Rectangle r = getInfo().getMapBounds();
		float size = Math.min(r.getWidth(), r.getHeight());

		scale = (SIZE * windowScale) / size;
		bounds.setSize(SIZE * windowScale, SIZE * windowScale);
		for (GameObject o : getInfo().getGameObjects().values()) {
			maybeAddObject(o);
		}
	}

	void setCamera(GameCamera viewport) {
		this.viewPort = viewport;
	}

}
