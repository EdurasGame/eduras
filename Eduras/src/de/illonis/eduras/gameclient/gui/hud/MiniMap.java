package de.illonis.eduras.gameclient.gui.hud;

import java.util.HashMap;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;

import de.illonis.eduras.events.ObjectFactoryEvent;
import de.illonis.eduras.gameclient.gui.hud.minimap.MiniMapBase;
import de.illonis.eduras.gameclient.gui.hud.minimap.MiniMapNeutralObject;
import de.illonis.eduras.gameclient.gui.hud.minimap.MiniMapPlayer;
import de.illonis.eduras.gameobjects.GameObject;
import de.illonis.eduras.gameobjects.NeutralBase;
import de.illonis.eduras.items.weapons.Missile;
import de.illonis.eduras.units.PlayerMainFigure;

/**
 * A minimap.
 * 
 * @author illonis
 * 
 */
public class MiniMap extends ClickableGuiElement {

	private HashMap<Integer, MiniMapNeutralObject> neutralObjects;
	private HashMap<Integer, MiniMapBase> bases;
	private HashMap<Integer, MiniMapPlayer> players;
	private float scale;

	final static int SIZE = 150;
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
		g.setColor(Color.gray);
		for (MiniMapNeutralObject object : neutralObjects.values()) {
			g.fillRect(object.getX(), object.getY(), object.getWidth(),
					object.getHeight());
		}
	}

	private void renderBases(Graphics g) {
		for (MiniMapBase object : bases.values()) {
			g.setColor(object.getColor());
			g.fillRect(object.getX(), object.getY(), object.getWidth(),
					object.getHeight());
		}
	}

	private void renderPlayers(Graphics g) {
		// System.out.println("rendering " + players.size() + " players");
		for (MiniMapPlayer object : players.values()) {
			g.setColor(object.getColor());
			// System.out.println("rendering player " +
			// object.getPlayer().getId() + " at " + object.getX() + ", "
			// + object.getY());
			// System.out.println("size: " + object.getWidth());
			g.fillOval(object.getX(), object.getY(), object.getWidth(),
					object.getHeight());
		}
	}

	@Override
	public boolean onClick(Vector2f p) {
		return false;
	}

	@Override
	public Rectangle getBounds() {
		return bounds;
	}

	private Vector2f gameToMinimapPosition(Vector2f pos) {
		Vector2f miniPos = new Vector2f(pos).scale(scale);
		miniPos.add(getBounds().getLocation());
		return miniPos;
	}

	private Vector2f minimapToGamePosition(Vector2f pos) {
		return null;
	}

	@Override
	public void render(Graphics g) {
		g.setColor(Color.black);
		g.fill(bounds);
		renderNeutral(g);
		renderBases(g);
		renderPlayers(g);
		// try {
		// Image i = ImageCache.getGuiImage(ImageKey.MINIMAP_DUMMY);
		// g.drawImage(i, screenX, screenY);
		// } catch (CacheException e) {
		// L.log(Level.WARNING, "Minimap dummy image not found.", e);
		// }
	}

	@Override
	public void onGuiSizeChanged(int newWidth, int newHeight) {
		screenY = newHeight - SIZE;
		bounds.setLocation(screenX, screenY);
	}

	@Override
	public void onObjectCreation(ObjectFactoryEvent event) {
		GameObject o = getInfo().findObjectById(event.getId());
		if (o == null || o instanceof Missile) {
			return;
		}
		Vector2f gamePos = new Vector2f(o.getXPosition(), o.getYPosition());
		Vector2f miniPos = gameToMinimapPosition(gamePos);
		float x = miniPos.x;
		float y = miniPos.y;

		if (o instanceof PlayerMainFigure) {
			float w = o.getShape().getWidth() * scale;
			float h = o.getShape().getWidth() * scale;
			players.put(o.getId(), new MiniMapPlayer((PlayerMainFigure) o, x,
					y, w, h));
		} else if (o instanceof NeutralBase) {
			bases.put(o.getId(), new MiniMapBase((NeutralBase) o, x, y));
		} else if (o.getOwner() == -1) {
			neutralObjects.put(o.getId(), new MiniMapNeutralObject(o, x, y));
		}
	}

	@Override
	public void onObjectRemove(ObjectFactoryEvent event) {
		GameObject o = getInfo().findObjectById(event.getId());
		if (o == null)
			return;
		if (o instanceof PlayerMainFigure)
			players.remove((PlayerMainFigure) o);
		else if (o instanceof NeutralBase) {
			bases.remove((NeutralBase) o);
		} else if (o.getOwner() == -1) {
			neutralObjects.remove(o);
		}
	}

	@Override
	public void onNewObjectPosition(GameObject object) {
		Vector2f gamePos = new Vector2f(object.getXPosition(),
				object.getYPosition());
		Vector2f miniPos = gameToMinimapPosition(gamePos);
		float x = miniPos.x;
		float y = miniPos.y;
		switch (object.getType()) {
		case PLAYER:
			players.get(object.getId()).setLocation(x, y);
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

		System.out.println("scale: " + scale);
		for (GameObject o : getInfo().getGameObjects().values()) {

			Vector2f gamePos = new Vector2f(o.getXPosition(), o.getYPosition());
			Vector2f miniPos = gameToMinimapPosition(gamePos);
			float x = miniPos.x;
			float y = miniPos.y;
			if (o instanceof PlayerMainFigure) {
				float w = o.getShape().getWidth() * scale;
				float h = o.getShape().getWidth() * scale;
				players.put(o.getId(), new MiniMapPlayer((PlayerMainFigure) o,
						x, y, w, h));
			} else if (o instanceof NeutralBase) {
				bases.put(o.getId(), new MiniMapBase((NeutralBase) o, x, y));
			} else if (o.getOwner() == -1) {
				neutralObjects
						.put(o.getId(), new MiniMapNeutralObject(o, x, y));
			}
		}
	}

}
