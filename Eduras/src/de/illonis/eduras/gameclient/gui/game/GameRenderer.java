package de.illonis.eduras.gameclient.gui.game;

import java.awt.geom.Area;
import java.awt.image.BufferedImage;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.Team;
import de.illonis.eduras.exceptions.ObjectNotFoundException;
import de.illonis.eduras.gameclient.ClientData;
import de.illonis.eduras.gameclient.VisionInformation;
import de.illonis.eduras.gameclient.datacache.CacheException;
import de.illonis.eduras.gameclient.datacache.ImageCache;
import de.illonis.eduras.gameclient.gui.animation.Animation;
import de.illonis.eduras.gameclient.gui.hud.HealthBar;
import de.illonis.eduras.gameclient.gui.hud.ItemTooltip;
import de.illonis.eduras.gameclient.gui.hud.RenderedGuiObject;
import de.illonis.eduras.gameclient.gui.hud.UserInterface;
import de.illonis.eduras.gameobjects.GameObject;
import de.illonis.eduras.items.Item;
import de.illonis.eduras.logicabstraction.InformationProvider;
import de.illonis.eduras.math.BasicMath;
import de.illonis.eduras.settings.S;
import de.illonis.eduras.units.PlayerMainFigure;
import de.illonis.eduras.units.Unit;

/**
 * Renders the game graphics onto the gamepanel.
 * 
 * @author illonis
 * 
 */
public class GameRenderer implements TooltipHandler {

	private final Logger L = EduLog.getLoggerFor(GameRenderer.class.getName());

	private final GameCamera camera;
	private final UserInterface gui;
	private final Map<Integer, GameObject> objs;
	private ItemTooltip tooltip;
	private float scale;
	private boolean tooltipShown = false;
	private final LinkedList<RenderedGuiObject> uiObjects;
	private final static int DEFAULT_WIDTH = 484;
	private final static int DEFAULT_HEIGHT = 462;
	private final InformationProvider info;
	private final ClientData data;

	/**
	 * Creates a new renderer.
	 * 
	 * @param camera
	 *            Game camera to use viewport.
	 * 
	 * @param gui
	 *            reactor that handles gui events.
	 * @param info
	 *            game-information that contains objects to render.
	 * @param data
	 *            client data object.
	 */
	public GameRenderer(GameCamera camera, UserInterface gui,
			InformationProvider info, ClientData data) {
		this.uiObjects = gui.getUiObjects();
		this.data = data;
		this.camera = camera;
		scale = 1;
		objs = info.getGameObjects();
		this.info = info;
		this.gui = gui;
		RendererTooltipHandler h = new RendererTooltipHandler(this);
		gui.setTooltipHandler(h);
	}

	/**
	 * Creates a screenshot.
	 * 
	 * @return a screenshot.
	 */
	public BufferedImage getScreenshot() {
		// FIXME: adapt for slick
		return null;
	}

	/**
	 * Calculates gui scale based on given ui dimensions.
	 * 
	 * @param currentWidth
	 *            current ui width.
	 * @param currentHeight
	 *            current ui height.
	 * @return new scale factor.
	 */
	private float calculateScale(int currentWidth, int currentHeight) {
		if (currentHeight == DEFAULT_HEIGHT && currentWidth == DEFAULT_WIDTH)
			return 1;

		float diffW = (float) currentWidth / DEFAULT_WIDTH;
		float diffH = (float) currentHeight / DEFAULT_HEIGHT;

		float newScale = BasicMath.avg(diffW, diffH);
		return newScale;
	}

	/**
	 * Renders buffered image with size of current target.
	 * 
	 * @param container
	 *            the gamecontainer.
	 * @param g
	 *            the target graphics.
	 */
	public void render(GameContainer container, Graphics g) {
		int width = container.getWidth();
		int height = container.getHeight();
		clear(g, width, height);
		adjustCamera();
		g.translate(-camera.getX(), -camera.getY());
		drawMap(g);
		drawObjects(g);
		drawAnimations(g);
		g.translate(camera.getX(), camera.getY());

		drawGui(g);
	}

	private void drawAnimations(Graphics g) {
		for (int i = 0; i < data.getAnimations().size(); i++) {
			Animation animation = data.getAnimations().get(i);
			animation.draw(g, -camera.getX(), -camera.getY());
		}
	}

	private void adjustCamera() {
		try {
			PlayerMainFigure p = getClientPlayer();
			camera.centerAt(p.getDrawX(), p.getDrawY());
		} catch (ObjectNotFoundException e) {
			// EduLog.passException(e);
		}
	}

	private synchronized void clear(Graphics g, int width, int height) {
		g.setColor(Color.gray);
		g.fillRect(0, 0, width, height);
	}

	/**
	 * Draw every gui element.
	 */
	private void drawGui(Graphics g) {
		for (int i = 0; i < uiObjects.size(); i++) {
			RenderedGuiObject o = uiObjects.get(i);
			if (!gui.isSpectator() || o.isVisibleForSpectator())
				o.render(g);
		}
		if (tooltipShown) {
			tooltip.render(g);
		}
	}

	/**
	 * Draws a small red border where map bounds are.
	 * 
	 * @param mapGraphics2
	 */
	private void drawMap(Graphics g) {
		Rectangle r = info.getMapBounds();
		//r.setLocation(-camera.getX(), -camera.getY());
		g.setColor(Color.black);
		g.fill(r);
	}

	/**
	 * Draw every object of game-object list that is in camera viewport.
	 * 
	 * @param g2d
	 *            the graphics target.
	 */
	private void drawObjects(Graphics g) {
		PlayerMainFigure myPlayer;
		try {
			myPlayer = getClientPlayer();
		} catch (ObjectNotFoundException e) {
			L.log(Level.SEVERE,
					"Could not find playerMainFigure while rendering.", e);
			return;
		}
		Team playerTeam = myPlayer.getTeam();
		VisionInformation vinfo = info.getClientData().getVisionInfo();
		Area visionArea;
		Area visionMask;
		synchronized (vinfo) {
			visionArea = vinfo.getVisionForTeam(playerTeam);
			visionMask = vinfo.getVisionMask();
		}

		for (Iterator<GameObject> iterator = objs.values().iterator(); iterator
				.hasNext();) {
			GameObject d = iterator.next();
			if (!d.isVisible()) {
				continue;
			}

			// draw only if in current view point
			if (d.getShape().intersects(camera)) {
				if (S.vision_disabled
						|| (S.vision_neutral_always && d.getOwner() == -1)) {
					drawObject(d, g);

				}
			}
		}

	}

	private void drawObject(GameObject d, Graphics g) {
		final float x = d.getDrawX() - camera.getX();
		final float y = d.getDrawY() - camera.getY();

		try {
			Image image = ImageCache.getObjectImage(d.getType());
			g.drawImage(image, x, y);
		} catch (CacheException e) {
			if (d.getShape() != null) {
				if (isSelected(d)) {
					g.setColor(Color.red);
				} else {
					g.setColor(Color.yellow);
				}
				g.fill(d.getShape());
			}
		}

		if (d.isUnit()) {
			drawHealthBarFor((Unit) d, g);
		}

		// draws unit id next to unit for testing purpose
		/*
		 * dbg.drawString(d.getId() + "", d.getDrawX() - camera.x, d.getDrawY()
		 * - camera.y - 15);
		 */
	}

	private boolean isSelected(GameObject object) {
		return data.getSelectedUnits().contains(object.getId());
	}

	/**
	 * Draws health bar that is assigned to given unit. It will be automatically
	 * be placed centered above the unit.
	 * 
	 * @param unit
	 *            assigned unit.
	 */
	private void drawHealthBarFor(Unit unit, Graphics g) {
		if (unit.isDead())
			return;
		HealthBar.calculateAndDrawFor(unit, g, camera);
	}

	// private void drawFace(GameObject obj, Circle shape) {
	// int noseRadius = 3;
	// int eyeRadius = 2;
	// Vector2df nose = Geometry.getRelativePointAtAngleOnCircle(shape,
	// obj.getRotation());
	// Vector2df radiusAdder = new Vector2df(shape.getRadius(),
	// shape.getRadius());
	// Vector2df circleCenter = obj.getPositionVector().copy();
	// circleCenter.add(radiusAdder);
	// nose.add(obj.getPositionVector());
	// nose.add(radiusAdder);
	// mapGraphics.setColor(Color.YELLOW);
	// mapGraphics.fillOval((int) nose.getX() - noseRadius - camera.x,
	// (int) nose.getY() - noseRadius - camera.y, 2 * noseRadius,
	// 2 * noseRadius);
	//
	// Vector2df leftEye = nose.copy();
	// leftEye.rotate(-35, circleCenter);
	// Vector2df centerDist = leftEye.getDistanceVectorTo(circleCenter);
	// centerDist.mult(.5);
	// leftEye.add(centerDist);
	// mapGraphics.fillOval((int) (leftEye.getX()) - eyeRadius - camera.getx,
	// (int) (leftEye.getY()) - eyeRadius - camera.getY(), 2 * eyeRadius,
	// 2 * eyeRadius);
	//
	// leftEye.rotate(70, circleCenter);
	// mapGraphics.fillOval((int) (leftEye.getX()) - eyeRadius - camera.x,
	// (int) (leftEye.getY()) - eyeRadius - camera.y, 2 * eyeRadius,
	// 2 * eyeRadius);
	//
	// }

	private Color getColorForObject(GameObject d) {
		switch (d.getType()) {
		case BIGBLOCK:
		case BIGGERBLOCK:
		case BUILDING:
		case SMALLCIRCLEDBLOCK:
		case DYNAMIC_POLYGON_BLOCK:
			return Color.gray;
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
		case MAPBOUNDS:
			return new Color(0, 0, 0, 0);
		default:
			return Color.white;
		}
	}

	@Override
	public void showItemTooltip(Vector2f p, Item item) {
		if (tooltip == null) {
			tooltip = new ItemTooltip(gui, item);
			tooltip.removeFromGui();
		} else {
			tooltip.setItem(item);
		}
		tooltip.moveTo(p);
		tooltipShown = true;
	}

	@Override
	public void showTooltip(Vector2f p, String text) {
		// TODO: implement
	}

	@Override
	public void hideTooltip() {
		tooltipShown = false;
	}

	/**
	 * Returns current rendering scale.
	 * 
	 * @return scale factor.
	 */
	float getCurrentScale() {
		return scale;
	}

	private PlayerMainFigure getClientPlayer() throws ObjectNotFoundException {
		return info.getPlayer();
	}

}
