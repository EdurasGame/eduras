package de.illonis.eduras.gameclient.gui.game;

import java.awt.Polygon;
import java.awt.geom.Area;
import java.awt.image.BufferedImage;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.particles.ParticleSystem;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.Player;
import de.illonis.eduras.Team;
import de.illonis.eduras.exceptions.ObjectNotFoundException;
import de.illonis.eduras.exceptions.PlayerHasNoTeamException;
import de.illonis.eduras.gameclient.ClientData;
import de.illonis.eduras.gameclient.VisionInformation;
import de.illonis.eduras.gameclient.datacache.CacheException;
import de.illonis.eduras.gameclient.datacache.CacheInfo.ImageKey;
import de.illonis.eduras.gameclient.datacache.ImageCache;
import de.illonis.eduras.gameclient.gui.animation.EffectFactory;
import de.illonis.eduras.gameclient.gui.hud.HealthBar;
import de.illonis.eduras.gameclient.gui.hud.ItemTooltip;
import de.illonis.eduras.gameclient.gui.hud.RenderedGuiObject;
import de.illonis.eduras.gameclient.gui.hud.TextTooltip;
import de.illonis.eduras.gameclient.gui.hud.Tooltip;
import de.illonis.eduras.gameclient.gui.hud.UserInterface;
import de.illonis.eduras.gameobjects.Base;
import de.illonis.eduras.gameobjects.GameObject;
import de.illonis.eduras.items.Item;
import de.illonis.eduras.logicabstraction.InformationProvider;
import de.illonis.eduras.math.BasicMath;
import de.illonis.eduras.math.Geometry;
import de.illonis.eduras.math.Vector2df;
import de.illonis.eduras.settings.S;
import de.illonis.eduras.units.InteractMode;
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
	private final GameCamera viewPort;
	private final UserInterface gui;
	private final Map<Integer, GameObject> objs;
	private Tooltip tooltip;
	private float scale;
	private boolean tooltipShown = false;
	private final LinkedList<RenderedGuiObject> uiObjects;
	private final static int DEFAULT_WIDTH = 798;
	private final static int DEFAULT_HEIGHT = 571;
	private final InformationProvider info;
	private final ClientData data;
	private final static Color FOG_OF_WAR = new Color(0, 0, 0, 200);
	private static final float INTERACTMODE_OFFSET_Y = 25;

	private static final Color OUTLINE_COLOR = Color.black;
	private Font font;

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
		viewPort = new GameCamera();
		viewPort.setBounds(0, 0, DEFAULT_WIDTH, DEFAULT_HEIGHT);
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
	 * @return the current viewport.
	 */
	public GameCamera getViewport() {
		return viewPort;
	}

	/**
	 * Creates a screenshot.
	 * 
	 * @return a screenshot.
	 */
	public BufferedImage takeScreenshot() {
		BufferedImage image = new BufferedImage(DEFAULT_WIDTH, DEFAULT_HEIGHT,
				BufferedImage.TYPE_INT_RGB);
		return image;
		// Image target = new Image(gameContainer.getWidth(),
		// gameContainer.getHeight());
		// Graphics g = gameContainer.getGraphics();
		// g.copyArea(target, 0, 0);
		// return target;
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
		if (font == null)
			font = g.getFont();
		try {
			if (info.getPlayer().getPlayerMainFigure() == null) {
				// wait for player
				return;
			}
		} catch (ObjectNotFoundException e) {
			// Waiting for player
			return;
		}
		int width = container.getWidth();
		int height = container.getHeight();
		float newScale = calculateScale(width, height);

		clear(g, width, height);

		if (newScale != 1.0f) {
			g.scale(newScale, newScale);
		}

		if (scale != newScale) {
			viewPort.setSize(width, height);
			gui.onGuiSizeChanged(width, height);
			// camera.setSize(width, height); // maybe not?
		}

		scale = newScale;
		adjustCamera();
		g.translate(-viewPort.getX() / scale, -viewPort.getY() / scale);
		drawMap(g);
		drawObjects(g);
		drawAnimations(g);
		drawEffects(g);
		g.resetTransform();
		// g.translate(viewPort.getX() / scale, viewPort.getY() / scale);
		// g.scale(1 / scale, 1 / scale);
		drawGui(g);
	}

	private void drawEffects(Graphics g) {
		for (ParticleSystem effect : EffectFactory.getSystems().values()) {
			synchronized (effect) {
				effect.render();
			}
		}
	}

	private void drawAnimations(Graphics g) {
	}

	private void adjustCamera() {
		try {
			PlayerMainFigure p = getClientPlayer().getPlayerMainFigure();
			Vector2f c = p.getPositionVector();
			// get offset and increase offset by movement
			Vector2f offset = camera.getCameraOffset().add(
					camera.getCameraMovement());
			c.add(offset);
			camera.centerAt(c.x, c.y);
			viewPort.centerAt(c.x * scale, c.y * scale);
		} catch (ObjectNotFoundException | NullPointerException e) {
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
		InteractMode mode;
		try {
			mode = info.getPlayer().getCurrentMode();
		} catch (ObjectNotFoundException e) {
			L.log(Level.WARNING, "Player not found", e);
			return;
		}

		for (int i = 0; i < uiObjects.size(); i++) {
			RenderedGuiObject o = uiObjects.get(i);
			if (o.isEnabledInInteractMode(mode)
					&& o.isEnabledInGameMode(info.getGameMode())
					&& (!gui.isSpectator() || o.isVisibleForSpectator())) {
				o.render(g);
			}
		}

		if (tooltipShown) {
			tooltip.render(g);
		}
	}

	/**
	 * Draws a small red border where map bounds are.
	 * 
	 * @param g
	 *            the target graphics.
	 */
	private void drawMap(Graphics g) {
		Rectangle r = info.getMapBounds();
		g.setColor(Color.black);
		g.fill(r);
	}

	/**
	 * Draw every object of game-object list that is in camera viewport.
	 * 
	 * @param g
	 *            the graphics target.
	 */
	private void drawObjects(Graphics g) {
		PlayerMainFigure myPlayer;
		try {
			myPlayer = getClientPlayer().getPlayerMainFigure();
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

		LinkedList<GameObject> objectsInDrawOrder = new LinkedList<GameObject>(
				objs.values());
		Collections.sort(objectsInDrawOrder);

		for (Iterator<GameObject> iterator = objectsInDrawOrder.iterator(); iterator
				.hasNext();) {
			GameObject d = iterator.next();
			if (!d.isVisibleFor(myPlayer)) {
				continue;
			}
			float[] points = d.getShape().getPoints();
			Polygon p = new Polygon();
			for (int i = 0; i < points.length / 2; i++) {
				p.addPoint((int) points[2 * i], (int) points[2 * i + 1]);
			}
			Area a = new Area(p);
			a.intersect(visionArea);

			// draw only if in current view point
			if (Geometry.shapeCollides(camera, d.getShape())) {
				if (S.Server.vision_disabled
						|| (S.Server.vision_neutral_always
								&& d.getOwner() == -1 || d.equals(myPlayer) || !a
									.isEmpty())) {
					drawObject(d, g);
				}
			}

		}

		// if (!S.vision_disabled) {
		// g.setColor(FOG_OF_WAR);
		// g.fill(visionMask);
		// }

	}

	private void drawObject(GameObject d, Graphics g) {
		final float x = d.getXPosition();
		final float y = d.getYPosition();

		try {
			Image image = ImageCache.getObjectImage(d.getType());
			g.drawImage(image, x, y);
			if (S.Client.debug_render_shapes) {
				renderShape(d, g);
			}
		} catch (CacheException e) {
			renderShape(d, g);
		}
		if (S.Client.debug_render_boundingboxes) {
			float circleRadius = d.getShape().getBoundingCircleRadius();
			Circle c = new Circle(d.getShape().getCenterX(), d.getShape()
					.getCenterY(), circleRadius);
			g.setColor(Color.yellow);
			g.draw(c);
			g.setColor(Color.white);
			g.fillOval(x, y, 2, 2);
			g.setColor(Color.green);
			g.fillOval(d.getShape().getCenterX(), d.getShape().getCenterY(), 2,
					2);
		}

		if (d.isUnit()) {
			drawHealthBarFor((Unit) d, g);
		}

		if (d instanceof PlayerMainFigure) {
			drawPlayerSpecifics((PlayerMainFigure) d, g);
		}

		// draws unit id next to unit for testing purpose
		/*
		 * dbg.drawString(d.getId() + "", d.getDrawX() - camera.x, d.getDrawY()
		 * - camera.y - 15);
		 */
	}

	private void drawPlayerSpecifics(PlayerMainFigure d, Graphics g) {
		drawFace(d, (Circle) d.getShape(), g);
		drawInteractModeIndication(d, g);
	}

	private void drawInteractModeIndication(PlayerMainFigure d, Graphics g) {
		try {
			if (d.getPlayer().getCurrentMode()
					.equals(InteractMode.MODE_STRATEGY)
					&& d.getTeam().equals(info.getPlayer().getTeam())) {

				g.drawImage(
						ImageCache.getGuiImage(ImageKey.STRATEGY_MODE_ICON), d
								.getPositionVector().getX(), d
								.getPositionVector().getY()
								- INTERACTMODE_OFFSET_Y);
			}
		} catch (CacheException | PlayerHasNoTeamException
				| ObjectNotFoundException e) {
			L.log(Level.WARNING, "Cannot load image!", e);
		}

	}

	private void renderShape(GameObject d, Graphics g) {
		if (d.getShape() != null) {
			g.setColor(OUTLINE_COLOR);
			g.draw(d.getShape());

			Color colorOfObject = getColorForObject(d);
			switch (d.getVisibility()) {
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
			g.fill(d.getShape());
		}
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

	private void drawFace(GameObject obj, Circle shape, Graphics g) {
		int noseRadius = 3;
		int eyeRadius = 2;
		Vector2df circleCenter = new Vector2df(shape.getCenterX(),
				shape.getCenterY());
		Vector2df nose = new Vector2df(circleCenter);
		nose.x += shape.getWidth() / 1.5f;
		nose.rotate(obj.getRotation(), circleCenter);
		g.setColor(Color.yellow);
		g.fill(new Circle(nose.x, nose.y, noseRadius));

		Vector2df leftEye = nose.copy();
		leftEye.rotate(-30, circleCenter);
		Vector2f centerDist = leftEye.copy().sub(circleCenter);
		centerDist.scale(.5f);
		leftEye.sub(centerDist);
		g.fillOval((int) (leftEye.getX()) - eyeRadius, (int) (leftEye.getY())
				- eyeRadius, 2 * eyeRadius, 2 * eyeRadius);

		leftEye.rotate(60, circleCenter);
		g.fillOval((int) (leftEye.getX()) - eyeRadius, (int) (leftEye.getY())
				- eyeRadius, 2 * eyeRadius, 2 * eyeRadius);

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

	@Override
	public void showItemTooltip(Vector2f p, Item item) {
		if (tooltip == null || !(tooltip instanceof ItemTooltip)) {
			tooltip = new ItemTooltip(item);
		} else {
			((ItemTooltip) tooltip).setItem(item);
		}
		tooltip.moveTo(p);
		tooltipShown = true;
	}

	@Override
	public void showTooltip(Vector2f p, String text) {
		if (tooltip == null || !(tooltip instanceof TextTooltip)) {
			tooltip = new TextTooltip(text);
		} else {
			((TextTooltip) tooltip).setText(text);
		}
		tooltip.moveTo(p);
		tooltipShown = true;
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

	private Player getClientPlayer() throws ObjectNotFoundException {
		return info.getPlayer();
	}

	public GameCamera getCamera() {
		return camera;
	}

}
