package de.illonis.eduras.gameclient.gui.hud;

import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.events.ObjectFactoryEvent;
import de.illonis.eduras.exceptions.ObjectNotFoundException;
import de.illonis.eduras.gameclient.gui.game.GameCamera;
import de.illonis.eduras.gameclient.gui.hud.minimap.MiniMapBase;
import de.illonis.eduras.gameclient.gui.hud.minimap.MiniMapNeutralObject;
import de.illonis.eduras.gameclient.gui.hud.minimap.MiniMapPlayer;
import de.illonis.eduras.gameobjects.GameObject;
import de.illonis.eduras.gameobjects.NeutralBase;
import de.illonis.eduras.items.weapons.Missile;
import de.illonis.eduras.items.weapons.Weapon;
import de.illonis.eduras.units.InteractMode;
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
		for (MiniMapPlayer object : players.values()) {
			g.setColor(object.getColor());
			g.fillOval(object.getX(), object.getY(), object.getWidth(),
					object.getHeight());
		}
	}

	@Override
	public boolean onClick(Vector2f p) {
		try {
			if (getInfo().getPlayer().getCurrentMode() == InteractMode.MODE_STRATEGY) {
				Vector2f gamePos = minimapToGamePosition(p);
				getMouseHandler().mapClicked(gamePos);
				return true;
			}
		} catch (ObjectNotFoundException e) {
			L.log(Level.WARNING, "Could not determine interaction mode.", e);
		}
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
		Vector2f gamePos = new Vector2f(pos).sub(getBounds().getLocation());
		gamePos.scale(1 / scale);
		return gamePos;
	}

	@Override
	public void render(Graphics g) {
		g.setColor(Color.black);
		g.fill(bounds);
		renderNeutral(g);
		renderBases(g);
		renderPlayers(g);
		renderViewPort(g);
	}

	private void renderViewPort(Graphics g) {
		g.setLineWidth(1f);
		g.setColor(Color.white);
		Vector2f pos = gameToMinimapPosition(viewPort.getLocation());
		g.drawRect(pos.x, pos.y, viewPort.getWidth() * scale,
				viewPort.getHeight() * scale);
	}

	@Override
	public void onGuiSizeChanged(int newWidth, int newHeight) {
		screenY = newHeight - SIZE;
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
		if (o instanceof PlayerMainFigure)
			players.remove(o.getId());
		else if (o instanceof NeutralBase) {
			bases.remove(o.getId());
		} else if (o.getOwner() == -1) {
			neutralObjects.remove(o.getId());
		}
	}

	private void maybeAddObject(GameObject o) {
		if (o == null || o instanceof Missile) {
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
		} else if (o.getOwner() == -1 && !(o instanceof Weapon)) {
			float w = o.getShape().getWidth() * scale;
			float h = o.getShape().getHeight() * scale;
			neutralObjects.put(o.getId(), new MiniMapNeutralObject(o, x, y, w,
					h));
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

		System.out.println("scale: " + scale);
		for (GameObject o : getInfo().getGameObjects().values()) {
			maybeAddObject(o);
		}
	}

	public void setCamera(GameCamera viewport) {
		this.viewPort = viewport;
	}

}
