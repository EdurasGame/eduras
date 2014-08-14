package de.illonis.eduras.mapeditor;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import de.illonis.eduras.gameclient.datacache.CacheException;
import de.illonis.eduras.gameclient.datacache.ImageCache;
import de.illonis.eduras.gameclient.gui.game.GameCamera;
import de.illonis.eduras.gameobjects.Base;
import de.illonis.eduras.gameobjects.GameObject;
import de.illonis.eduras.math.Geometry;
import de.illonis.eduras.units.PlayerMainFigure;

public class MapRenderer {

	private static final Color OUTLINE_COLOR = Color.black;

	private final MapData data = MapData.getInstance();
	private final MapInteractor interactor;
	private final GameCamera camera;

	public MapRenderer(MapInteractor interactor) {
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
		g.setColor(Color.black);
		g.fillRect(-camera.getX(), -camera.getY(), data.getWidth(),
				data.getHeight());
		g.translate(-camera.getX(), -camera.getY());

		// map bounds
		g.setColor(Color.red);
		g.drawRect(0, 0, data.getWidth(), data.getHeight());
		for (GameObject o : data.getGameObjects()) {
			renderObject(o, g);
		}
	}

	private void renderObject(GameObject o, Graphics g) {
		if (Geometry.shapeCollides(o.getShape(), camera)) {
			final float x = o.getXPosition();
			final float y = o.getYPosition();
			try {
				Image image = ImageCache.getObjectImage(o.getType());
				g.drawImage(image, x, y);
			} catch (CacheException e) {
				renderShape(o, g);
			}
		}
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
		case DYNAMIC_POLYGON_BLOCK:
			return Color.pink;
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
