package de.illonis.eduras.gameclient.gui.game;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.Transparency;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

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
import de.illonis.eduras.math.Geometry;
import de.illonis.eduras.math.Vector2D;
import de.illonis.eduras.settings.S;
import de.illonis.eduras.shapes.Circle;
import de.illonis.eduras.shapes.ObjectShape;
import de.illonis.eduras.shapes.Polygon;
import de.illonis.eduras.units.PlayerMainFigure;
import de.illonis.eduras.units.Unit;
import de.illonis.eduras.utils.ImageTools;

/**
 * Renders the game graphics onto the gamepanel.
 * 
 * @author illonis
 * 
 */
public class GameRenderer implements TooltipHandler {

	private final Logger L = EduLog.getLoggerFor(GameRenderer.class.getName());

	private BufferedImage mapImage = null;
	private BufferedImage displayImage = null;
	private final GameCamera camera;
	private final UserInterface gui;
	private Graphics2D mapGraphics = null;
	private Graphics2D bothGraphics = null;
	private final Map<Integer, GameObject> objs;
	private RenderThread rendererThread;
	private Component target;
	private BufferStrategy buffer;
	private ItemTooltip tooltip;
	private double scale;
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
		return ImageTools.deepCopy(displayImage);
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
	private double calculateScale(int currentWidth, int currentHeight) {
		if (currentHeight == DEFAULT_HEIGHT && currentWidth == DEFAULT_WIDTH)
			return 1;

		double diffW = (double) currentWidth / DEFAULT_WIDTH;
		double diffH = (double) currentHeight / DEFAULT_HEIGHT;

		double newScale = BasicMath.avg(diffW, diffH);
		return newScale;
	}

	/**
	 * Renders buffered image with size of current target.
	 */
	public void render() {
		int width = target.getWidth();
		int height = target.getHeight();

		// recreate image if it does not exist
		if (mapImage == null || mapGraphics == null
				|| width != mapImage.getWidth()) {
			createGraphics(width, height);
			camera.setSize(width, height);
			camera.setScale(scale);
		}
		// clear image
		clear(width, height);
		adjustCamera();
		drawMap(mapGraphics);
		drawObjects(mapGraphics);
		drawAnimations(mapGraphics);
		bothGraphics.drawImage(mapImage, 0, 0, null);
		drawGui(bothGraphics);

		Graphics2D g2d = (Graphics2D) buffer.getDrawGraphics();
		g2d.drawImage(displayImage, 0, 0, null);
		g2d.dispose();
		if (!buffer.contentsLost()) {
			buffer.show();
			Toolkit.getDefaultToolkit().sync();
		}
	}

	private void drawAnimations(Graphics2D g2d) {
		for (int i = 0; i < data.getAnimations().size(); i++) {
			Animation animation = data.getAnimations().get(i);
			animation.draw(g2d, -camera.x, -camera.y);
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

	/**
	 * Rebuilds buffer graphics.
	 * 
	 * @param width
	 *            graphics width.
	 * @param height
	 *            graphics height.
	 */
	private void createGraphics(int width, int height) {
		GraphicsConfiguration config = GraphicsEnvironment
				.getLocalGraphicsEnvironment().getDefaultScreenDevice()
				.getDefaultConfiguration();

		mapImage = config.createCompatibleImage(width, height,
				Transparency.OPAQUE);
		displayImage = config.createCompatibleImage(width, height,
				Transparency.OPAQUE);

		scale = calculateScale(width, height);

		bothGraphics = (Graphics2D) displayImage.getGraphics();
		bothGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_OFF);
		mapGraphics = (Graphics2D) mapImage.getGraphics();
		mapGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		mapGraphics.scale(scale, scale);
	}

	private synchronized void clear(int width, int height) {
		mapGraphics.setColor(Color.GRAY);
		mapGraphics.fillRect(0, 0, width, height);
	}

	/**
	 * Draw every gui element.
	 */
	private void drawGui(Graphics2D g2d) {
		for (int i = 0; i < uiObjects.size(); i++) {
			RenderedGuiObject o = uiObjects.get(i);
			if (!gui.isSpectator() || o.isVisibleForSpectator())
				o.render(g2d);
		}
		if (tooltipShown) {
			tooltip.render(g2d);
		}
	}

	/**
	 * Draws a small red border where map bounds are.
	 * 
	 * @param mapGraphics2
	 */
	private void drawMap(Graphics2D g2d) {
		Rectangle r = new Rectangle(info.getMapBounds());
		r.translate(-camera.x, -camera.y);
		g2d.setColor(Color.BLACK);
		g2d.fill(r);
	}

	/**
	 * Draw every object of game-object list that is in camera viewport.
	 * 
	 * @param g2d
	 *            the graphics target.
	 */
	private void drawObjects(Graphics2D g2d) {
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
			if (d.getBoundingBox().intersects(camera)) {
				if (S.vision_disabled
						|| (S.vision_neutral_always && d.getOwner() == -1)
						|| visionArea.intersects(d.getBoundingBox())) {
					drawObject(d, g2d);
					if (S.debug_render_boundingboxes) {
						mapGraphics.setColor(Color.YELLOW);
						Rectangle2D.Double r = d.getBoundingBox();
						r.x -= camera.x;
						r.y -= camera.y;
						mapGraphics.draw(r);
					}
				}
			}
		}

		if (!S.vision_disabled) {
			AffineTransform af = new AffineTransform();
			af.translate(-camera.x, -camera.y);

			Area map = new Area(camera);
			map.subtract(visionMask);

			map.transform(af);

			mapGraphics.setStroke(new BasicStroke(1f));
			mapGraphics.setColor(new Color(0, 0, 0, 0.6f));
			mapGraphics.fill(map);
			mapGraphics.setColor(Color.WHITE);
			mapGraphics.draw(map);
		}
	}

	private void drawObject(GameObject d, Graphics2D g2d) {
		final int x = d.getDrawX() - camera.x;
		final int y = d.getDrawY() - camera.y;

		try {
			mapGraphics.drawImage(ImageCache.getObjectImage(d.getType()), x, y,
					null);
		} catch (CacheException e) {
			if (d.getShape() != null) {
				if (isSelected(d)) {
					g2d.setColor(Color.RED);
				} else {
					g2d.setColor(Color.YELLOW);
				}
				drawShapeOf(d);
			}
		}

		if (d.isUnit()) {
			drawHealthBarFor((Unit) d);
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
	private void drawHealthBarFor(Unit unit) {
		if (unit.isDead())
			return;
		HealthBar.calculateAndDrawFor(unit, mapGraphics, camera);
	}

	/**
	 * Draws shape of a {@link GameObject}.
	 * 
	 * @param obj
	 *            gameobject.
	 */
	private void drawShapeOf(GameObject obj) {
		ObjectShape objectShape = obj.getShape();

		if (objectShape instanceof Polygon) {

			drawPolygon((Polygon) objectShape, obj);

		} else if (objectShape instanceof Circle) {
			drawCircle((Circle) objectShape, obj);

			if (obj instanceof PlayerMainFigure) {
				drawFace(obj, (Circle) objectShape);
			}
		}
	}

	private void drawFace(GameObject obj, Circle shape) {
		int noseRadius = 3;
		int eyeRadius = 2;
		Vector2D nose = Geometry.getRelativePointAtAngleOnCircle(shape,
				obj.getRotation());
		Vector2D radiusAdder = new Vector2D(shape.getRadius(),
				shape.getRadius());
		Vector2D circleCenter = obj.getPositionVector().copy();
		circleCenter.add(radiusAdder);
		nose.add(obj.getPositionVector());
		nose.add(radiusAdder);
		mapGraphics.setColor(Color.YELLOW);
		mapGraphics.fillOval((int) nose.getX() - noseRadius - camera.x,
				(int) nose.getY() - noseRadius - camera.y, 2 * noseRadius,
				2 * noseRadius);

		Vector2D leftEye = nose.copy();
		leftEye.rotate(-35, circleCenter);
		Vector2D centerDist = leftEye.getDistanceVectorTo(circleCenter);
		centerDist.mult(.5);
		leftEye.add(centerDist);
		mapGraphics.fillOval((int) (leftEye.getX()) - eyeRadius - camera.x,
				(int) (leftEye.getY()) - eyeRadius - camera.y, 2 * eyeRadius,
				2 * eyeRadius);

		leftEye.rotate(70, circleCenter);
		mapGraphics.fillOval((int) (leftEye.getX()) - eyeRadius - camera.x,
				(int) (leftEye.getY()) - eyeRadius - camera.y, 2 * eyeRadius,
				2 * eyeRadius);

	}

	/**
	 * Draws a circle which belongs to the given object.
	 * 
	 * @param objectShape
	 *            The circle.
	 * @param d
	 *            The object.
	 */
	private void drawCircle(Circle objectShape, GameObject d) {
		int radius = (int) objectShape.getRadius();
		int xPos = d.getDrawX() - camera.x;
		int yPos = d.getDrawY() - camera.y;
		mapGraphics.setColor(getColorForObject(d));
		mapGraphics.fillOval(xPos, yPos, 2 * radius, 2 * radius);
	}

	private Color getColorForObject(GameObject d) {
		switch (d.getType()) {
		case BIGBLOCK:
		case BIGGERBLOCK:
		case BUILDING:
		case SMALLCIRCLEDBLOCK:
		case DYNAMIC_POLYGON_BLOCK:
			return Color.GRAY;
		case PLAYER:
			PlayerMainFigure p = (PlayerMainFigure) d;
			if (p.getTeam() == null) {
				return Color.BLUE;
			} else {
				return p.getTeam().getColor();
			}
		case BIRD:
			return new Color(0.4231f, 0.6361f, 0.995f, 0.4f);
		case ITEM_WEAPON_SIMPLE:
			return Color.ORANGE;
		case ITEM_WEAPON_SNIPER:
			return Color.MAGENTA;
		case ITEM_WEAPON_SPLASH:
			return Color.GREEN;
		case ITEM_WEAPON_SWORD:
			return Color.CYAN;
		case MISSILE_SPLASH:
			return Color.GREEN;
		case MISSILE_SPLASHED:
			return Color.GREEN;
		case SIMPLEMISSILE:
			return Color.ORANGE;
		case SNIPERMISSILE:
			return Color.MAGENTA;
		case SWORDMISSILE:
			return Color.CYAN;
		case MINELAUNCHER:
		case MINE_MISSILE:
			return Color.YELLOW;
		case ASSAULT_MISSILE:
		case ASSAULTRIFLE:
			return Color.PINK;
		case MAPBOUNDS:
			return new Color(0, 0, 0, 0);
		default:
			return Color.WHITE;
		}
	}

	/**
	 * Draws a polygon which belongs to the given object.
	 * 
	 * @param polygon
	 *            The polygon to draw.
	 * @param object
	 *            The gameobject the polygon belongs to.
	 */
	private void drawPolygon(Polygon polygon, GameObject object) {
		LinkedList<Vector2D> vertices = polygon.getAbsoluteVertices(object);

		int vCount = vertices.size();
		int[] xPositions = new int[vCount];
		int[] yPositions = new int[vCount];

		for (int j = 0; j < vCount; j++) {
			xPositions[j] = (int) vertices.get(j).getX() - camera.x;
			yPositions[j] = (int) vertices.get(j).getY() - camera.y;
		}

		// for (int j = 0; j < vCount; j++) {
		// mapGraphics.drawLine(xPositions[j] - camera.x, yPositions[j]
		// - camera.y, xPositions[(j + 1) % vCount] - camera.x,
		// yPositions[(j + 1) % vCount] - camera.y);
		// }

		mapGraphics.setColor(getColorForObject(object));
		mapGraphics.fillPolygon(xPositions, yPositions, vCount);
	}

	@Override
	public void showItemTooltip(Point p, Item item) {
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
	public void showTooltip(Point p, String text) {
		// TODO: implement

	}

	@Override
	public void hideTooltip() {
		tooltipShown = false;
	}

	/**
	 * Stops rendering process.
	 */
	void stopRendering() {
		if (rendererThread != null)
			rendererThread.stop();
	}

	/**
	 * Starts rendering process by creating a new renderer thread. *
	 * 
	 * @author illonis
	 */
	void startRendering() {
		rendererThread = new RenderThread(this, gui.getFPSListener());
		Thread t = new Thread(rendererThread);
		t.setName("RendererThread");
		t.start();
	}

	/**
	 * Sets drawing target of renderer.
	 * 
	 * @param guiPanel
	 *            target game panel.
	 */
	void setTarget(GamePanel guiPanel) {
		guiPanel.createBufferStrategy(2);
		target = guiPanel;
		buffer = guiPanel.getBufferStrategy();
	}

	/**
	 * Returns current rendering scale.
	 * 
	 * @return scale factor.
	 */
	double getCurrentScale() {
		return scale;
	}

	private PlayerMainFigure getClientPlayer() throws ObjectNotFoundException {
		return info.getPlayer();
	}

}
