package de.illonis.eduras.mapeditor;

import java.util.LinkedList;
import java.util.List;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Line;
import org.newdawn.slick.geom.Point;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Vector2f;

import de.illonis.eduras.gameclient.datacache.CacheException;
import de.illonis.eduras.gameclient.datacache.ImageCache;
import de.illonis.eduras.gameclient.gui.game.GameCamera;
import de.illonis.eduras.gameobjects.Base;
import de.illonis.eduras.gameobjects.DynamicPolygonObject;
import de.illonis.eduras.gameobjects.GameObject;
import de.illonis.eduras.gameobjects.Portal;
import de.illonis.eduras.mapeditor.MapInteractor.InteractType;
import de.illonis.eduras.maps.NodeData;
import de.illonis.eduras.maps.SpawnPosition;
import de.illonis.eduras.math.Geometry;
import de.illonis.eduras.math.Vector2df;
import de.illonis.eduras.units.PlayerMainFigure;

/**
 * Renders the map in editor.
 * 
 * @author illonis
 * 
 */
public class MapRenderer {

	private static final Color OUTLINE_COLOR = Color.black;
	private final static Color SPAWN_COLOR = Color.green;
	private final static Color SHAPE_PLACE_COLOR = new Color(0f, 0f, 1f, 0.7f);

	private final MapData data = MapData.getInstance();
	private final MapInteractor interactor;
	private final GameCamera camera;

	MapRenderer(MapInteractor interactor) {
		this.interactor = interactor;
		camera = interactor.getViewPort();
	}

	/**
	 * Renders map graphics.
	 * 
	 * @param container
	 *            the gamecontainer.
	 * @param g
	 *            the target graphics.
	 */
	public void render(GameContainer container, Graphics g) {
		g.setColor(Color.gray);
		g.fillRect(0, 0, container.getWidth(), container.getHeight());

		g.translate(-camera.getX(), -camera.getY());
		g.scale(interactor.getZoom(), interactor.getZoom());

		List<EditorPlaceable> selected = interactor.getSelectedElements();

		// map bounds
		g.setColor(Color.black);
		g.fillRect(0, 0, data.getWidth(), data.getHeight());
		g.setColor(Color.red);
		g.drawRect(0, 0, data.getWidth(), data.getHeight());
		for (SpawnPosition spawn : data.getSpawnPoints()) {
			renderSpawn(spawn, g);
		}
		for (NodeData node : data.getBases()) {
			renderNode(node, g);
		}
		List<Portal> portals = new LinkedList<Portal>();
		for (GameObject o : data.getGameObjects()) {
			renderObject(o, g);
			if (o instanceof Portal) {
				portals.add((Portal) o);
			}
		}
		if (data.isShowNodeConnections()) {
			g.setLineWidth(1f);
			g.setColor(Color.yellow);
			for (NodeData node : data.getBases()) {
				for (NodeData linkedNode : node.getAdjacentNodes()) {
					g.drawLine(node.getXPosition() + node.getWidth() / 2,
							node.getYPosition() + node.getHeight() / 2,
							linkedNode.getXPosition() + linkedNode.getWidth()
									/ 2,
							linkedNode.getYPosition() + linkedNode.getHeight()
									/ 2);
				}
			}
		}
		if (data.isShowPortalLinks()) {
			g.setLineWidth(2f);
			g.setColor(Color.blue);
			for (Portal portal : portals) {
				Portal partner = portal.getPartnerPortal();
				if (partner != null) {
					Vector2f start = new Vector2f(portal.getShape()
							.getCenterX(), portal.getShape().getCenterY());
					Vector2df end = new Vector2df(partner.getShape()
							.getCenterX(), partner.getShape().getCenterY());
					Line l = new Line(start, end);
					g.draw(l);
					double angle = Math.atan2(l.getY2() - l.getY1(), l.getX2()
							- l.getX1());
					angle = angle - 4 * Math.PI / 3d;
					Vector2df right = new Vector2df(0, 5 * interactor.getZoom());
					right.rotate((float) Geometry.toDegree(angle));
					right.add(end);
					Vector2df left = right.copy();
					left.rotate(300, end);
					g.drawLine(end.x, end.y, right.x, right.y);
					g.drawLine(end.x, end.y, left.x, left.y);
				}
			}
		}
		for (EditorPlaceable element : selected) {
			renderSelection(element.getXPosition(), element.getYPosition(),
					element.getWidth(), element.getHeight(), g);
		}
		if (interactor.getInteractType() == InteractType.PLACE_SHAPE) {
			DynamicPolygonObject poly = data.getPlacingObject();
			if (poly != null) {
				Shape shape = poly.getShape();
				g.setColor(SHAPE_PLACE_COLOR);
				Point mouse = interactor.getMouseLocation();
				Vector2f mapPoint = interactor
						.computeGuiPointToGameCoordinate(new Vector2f(mouse
								.getX(), mouse.getY()));
				g.translate(mapPoint.getX() - shape.getWidth() / 2,
						mapPoint.getY() - shape.getHeight() / 2);
				g.fill(shape);
			}
		}
		g.resetTransform();
		if (interactor.getDragRect().getWidth() > 0) {
			g.setColor(Color.white);
			g.draw(interactor.getDragRect());
		}
	}

	private void renderSelection(float xPosition, float yPosition, float width,
			float height, Graphics g) {
		g.setColor(Color.yellow);
		g.setLineWidth(2f);
		// top left
		g.drawLine(xPosition - 5, yPosition - 5, xPosition + 5, yPosition - 5);
		g.drawLine(xPosition - 5, yPosition - 5, xPosition - 5, yPosition + 5);

		// bottom left
		g.drawLine(xPosition - 5, yPosition + height + 5, xPosition - 5,
				yPosition + height - 5);
		g.drawLine(xPosition - 5, yPosition + height + 5, xPosition + 5,
				yPosition + height + 5);

		// bottom right
		g.drawLine(xPosition + width - 5, yPosition + height + 5, xPosition
				+ width + 5, yPosition + height + 5);
		g.drawLine(xPosition + width + 5, yPosition + height - 5, xPosition
				+ width + 5, yPosition + height + 5);

		// top right
		g.drawLine(xPosition + width - 5, yPosition - 5, xPosition + width + 5,
				yPosition - 5);
		g.drawLine(xPosition + width + 5, yPosition - 5, xPosition + width + 5,
				yPosition + 5);

		g.setLineWidth(1f);
	}

	private void renderSpawn(SpawnPosition spawn, Graphics g) {
		g.setColor(SPAWN_COLOR);
		g.fill(spawn.getArea());
	}

	private void renderNode(NodeData node, Graphics g) {
		Color color;
		switch (node.isMainNode()) {
		case TEAM_A:
			color = Color.red;
			break;
		case TEAM_B:
			color = Color.blue;
			break;
		default:
			color = Color.white;
		}
		g.setColor(color);
		g.fillRect(node.getXPosition(), node.getYPosition(), node.getWidth(),
				node.getHeight());
	}

	private void renderObject(GameObject o, Graphics g) {
		g.setLineWidth(1f);
		final float x = o.getXPosition();
		final float y = o.getYPosition();
		if (interactor.getInteractType() == InteractType.EDIT_SHAPE) {
			if (o.equals(data.getEditObject())) {
				drawEditShape(g);
				return;
			}
		}
		try {
			Image image = ImageCache.getObjectImage(o.getType());
			g.drawImage(image, x, y);
		} catch (CacheException e) {
			renderShape(o, g);
		}
	}

	private void drawEditShape(Graphics g) {
		boolean first = true;
		Vector2f firstVector = null;
		Vector2f last = null;
		g.setColor(Color.white);
		g.setLineWidth(1.5f);
		for (Vector2f v : data.getEditShape().getVector2dfs()) {
			g.setColor(Color.white);
			g.fillRect(v.x - 2, v.y - 2, 4, 4);
			if (first) {
				firstVector = v;
				first = false;
			} else {
				if (isRemovingLine(last, v))
					g.setColor(Color.gray);
				g.drawLine(last.x, last.y, v.x, v.y);
			}
			last = v;
		}
		if (data.getEditShape().getVector2dfs().size() > 1) {
			// connect last point with first one
			g.drawLine(last.x, last.y, firstVector.x, firstVector.y);
		}
		if (data.getTempLineB() != null) {
			g.setColor(Color.green);
			g.draw(data.getTempLineA());
			g.draw(data.getTempLineB());
		}
	}

	private boolean isRemovingLine(Vector2f last, Vector2f v) {
		if (data.getTempLineB() == null)
			return false;
		return (data.getRemovedLine().getStart().equals(last) && data
				.getRemovedLine().getEnd().equals(v))
				|| (data.getRemovedLine().getStart().equals(v) && data
						.getRemovedLine().getEnd().equals(last));

	}

	private void renderShape(GameObject o, Graphics g) {
		if (o.getShape() != null) {
			g.setColor(OUTLINE_COLOR);
			g.draw(o.getShape());

			Color colorOfObject = getColorForObject(o);
			switch (o.getVisibility()) {
			case ALL:
				break;
			case OWNER:
			case OWNER_ALLIED:
			case OWNER_TEAM:
			case TEAM:
				colorOfObject = new Color(colorOfObject.r, colorOfObject.g,
						colorOfObject.b, 0.33f);
				break;
			default:
				break;
			}
			g.setColor(colorOfObject);
			g.fill(o.getShape());
		}
	}

	private Color getColorForObject(GameObject d) {
		switch (d.getType()) {
		case BIGBLOCK:
		case BIGGERBLOCK:
		case BUILDING:
		case SMALLCIRCLEDBLOCK:
			return Color.pink;
		case DYNAMIC_POLYGON_BLOCK:
			return ((DynamicPolygonObject) d).getColor();
		case PLAYER:
			PlayerMainFigure p = (PlayerMainFigure) d;
			if (p.getTeam() == null) {
				return Color.blue;
			} else {
				return p.getTeam().getColor();
			}
		case BIRD:
			return new Color(0.4231f, 0.6361f, 0.995f, 0.4f);
		case ITEM_WEAPON_SIMPLE:
			return Color.orange;
		case ITEM_WEAPON_SNIPER:
			return Color.magenta;
		case ITEM_WEAPON_SPLASH:
			return Color.green;
		case ITEM_WEAPON_SWORD:
			return Color.cyan;
		case MISSILE_SPLASH:
			return Color.green;
		case MISSILE_SPLASHED:
			return Color.green;
		case SIMPLEMISSILE:
			return Color.orange;
		case SNIPERMISSILE:
			return Color.magenta;
		case SWORDMISSILE:
			return Color.cyan;
		case MINELAUNCHER:
		case MINE_MISSILE:
			return Color.yellow;
		case ASSAULT_MISSILE:
		case ASSAULTRIFLE:
			return Color.pink;
		case HEALING_POTION:
		case SPEED_POWERUP:
		case INVISIBILITY_POWERUP:
			return Color.darkGray;
		case MAPBOUNDS:
			return new Color(0, 0, 0, 0);
		case NEUTRAL_BASE:
			Base base = (Base) d;
			if (base.getCurrentOwnerTeam() == null) {
				return Color.white;
			} else {
				return base.getCurrentOwnerTeam().getColor();
			}
		case PORTAL:
			return Color.orange;
		default:
			return Color.white;
		}
	}

}
