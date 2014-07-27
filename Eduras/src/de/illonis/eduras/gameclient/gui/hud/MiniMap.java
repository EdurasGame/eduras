package de.illonis.eduras.gameclient.gui.hud;

import java.util.HashMap;
import java.util.logging.Logger;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.ShapeFill;
import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Vector2f;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.ObjectFactory.ObjectType;
import de.illonis.eduras.events.ObjectFactoryEvent;
import de.illonis.eduras.gameclient.gui.game.GameCamera;
import de.illonis.eduras.gameclient.gui.hud.minimap.MiniMapBase;
import de.illonis.eduras.gameclient.gui.hud.minimap.MiniMapNeutralObject;
import de.illonis.eduras.gameclient.gui.hud.minimap.MiniMapPlayer;
import de.illonis.eduras.gamemodes.GameMode.GameModeNumber;
import de.illonis.eduras.gameobjects.DynamicPolygonObject;
import de.illonis.eduras.gameobjects.GameObject;
import de.illonis.eduras.gameobjects.NeutralBase;
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
	private int height;
	private HashMap<Integer, NodeData> nodes;

	final static int SIZE = 150;
	private static final Color NEUTRAL_OBJECTS_FILL_COLOR = Color.gray;

	private final Rectangle bounds;

	protected MiniMap(UserInterface gui) {
		super(gui);
		bounds = new Rectangle(0, 0, SIZE, SIZE);
		neutralObjects = new HashMap<Integer, MiniMapNeutralObject>();
		bases = new HashMap<Integer, MiniMapBase>();
		players = new HashMap<Integer, MiniMapPlayer>();
		scale = 1f;
	}

	private void renderNeutral(Graphics g) {
		g.setColor(NEUTRAL_OBJECTS_FILL_COLOR);
		for (MiniMapNeutralObject object : neutralObjects.values()) {
			if (object.isDynamicShape()) {
				g.draw(new Polygon(
						Geometry.vectorsToFloat(object.getVertices())),
						new ShapeFill() {

							@Override
							public Vector2f getOffsetAt(Shape shape, float x,
									float y) {
								return new Vector2f();
							}

							@Override
							public Color colorAt(Shape shape, float x, float y) {
								return NEUTRAL_OBJECTS_FILL_COLOR;
							}
						});
			} else {
				g.fillRect(object.getX(), object.getY(), object.getWidth(),
						object.getHeight());
			}
		}
	}

	private void renderBases(Graphics g) {
		for (MiniMapBase object : bases.values()) {
			g.setColor(object.getColor());
			g.fillRect(object.getX(), object.getY(), object.getWidth(),
					object.getHeight());
		}
	}

	private void renderNodeConnections(Graphics g) {
		checkIfNodesInitialized();

		// if it IS null, some game mode other than Edura is running
		if (nodes != null) {
			g.setLineWidth(1f);
			g.setColor(Color.yellow);
			for (Integer nodeId : nodes.keySet()) {
				NodeData someNode = nodes.get(nodeId);

				for (Integer adjacentOfSomeNodeId : someNode.getAdjacentNodes()) {
					NodeData adjacentOfSomeNode = nodes
							.get(adjacentOfSomeNodeId);

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
			if (getInfo().getGameMode().getNumber() == GameModeNumber.EDURA) {

				try {
					nodes = NodeData.nodeDataToVertices(getInfo().getNodes());
				} catch (IllegalArgumentException e) {
					L.warning("This map is running Edura! game mode although it's not an Edura! map!");
					return;
				}
			}
		}
	}

	private void renderPlayers(Graphics g) {
		for (MiniMapPlayer object : players.values()) {
			g.setColor(object.getColor());
			g.fillOval(object.getX(), object.getY(), object.getWidth(),
					object.getHeight());
		}
	}

	@Override
	public boolean onClick(Vector2f p) {
		Vector2f gamePos = minimapToGamePosition(p);
		getMouseHandler().mapClicked(gamePos);
		return true;
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
		renderNeutral(g);
		renderBases(g);
		renderNodeConnections(g);
		renderPlayers(g);
		renderViewPort(g);
	}

	private void renderViewPort(Graphics g) {
		g.setLineWidth(1f);
		g.setColor(Color.white);
		Vector2f pos = gameToMinimapPosition(new Vector2f(viewPort.getX(),
				viewPort.getY()));
		g.drawRect(pos.x, pos.y, viewPort.getWidth() * scale,
				viewPort.getHeight() * scale);
//		g.pushTransform();
//		g.translate(-scale, (height - SIZE) - scale);
//		g.scale(scale, scale);
//		g.drawRect(0, 0, viewPort.getWidth(), viewPort.getHeight());
//		g.popTransform();
	}

	@Override
	public void onGuiSizeChanged(int newWidth, int newHeight) {
		screenY = newHeight - SIZE;
		this.height = newHeight;
		bounds.setLocation(screenX, screenY);
		relocateObjects();
	}

	private void relocateObjects() {
		for (MiniMapPlayer player : players.values()) {
			Vector2f miniPos = gameToMinimapPosition(player.getObjectLocation());
			player.setLocation(miniPos.x, miniPos.y);
		}
		for (MiniMapBase base : bases.values()) {
			Vector2f miniPos = gameToMinimapPosition(base.getObjectLocation());
			base.setLocation(miniPos.x, miniPos.y);
		}
		for (MiniMapNeutralObject object : neutralObjects.values()) {
			Vector2f miniPos = gameToMinimapPosition(object.getObjectLocation());
			object.setLocation(miniPos.x, miniPos.y);
		}
	}

	@Override
	public void onObjectCreation(ObjectFactoryEvent event) {
		GameObject o = getInfo().findObjectById(event.getId());
		maybeAddObject(o);
	}

	@Override
	public void onObjectRemove(ObjectFactoryEvent event) {
		GameObject o = getInfo().findObjectById(event.getId());
		if (o == null)
			return;

		if (!isTracked(o)) {
			return;
		}

		if (o instanceof PlayerMainFigure)
			players.remove(o.getId());
		else if (o instanceof NeutralBase) {
			bases.remove(o.getId());
			resetNodeData();
		} else if (o.getOwner() == -1) {
			neutralObjects.remove(o.getId());
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
			players.put(o.getId(), new MiniMapPlayer((PlayerMainFigure) o, x,
					y, w, h));
		} else if (o instanceof NeutralBase) {
			float w = o.getShape().getWidth() * scale;
			float h = o.getShape().getHeight() * scale;
			bases.put(o.getId(), new MiniMapBase((NeutralBase) o, x, y, w, h));
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
			neutralObjects.put(o.getId(), newNeutralObject);
		}
	}

	private void resetNodeData() {
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
		scale = SIZE / size;

		for (GameObject o : getInfo().getGameObjects().values()) {
			maybeAddObject(o);
		}
	}

	public void setCamera(GameCamera viewport) {
		this.viewPort = viewport;
	}

}
