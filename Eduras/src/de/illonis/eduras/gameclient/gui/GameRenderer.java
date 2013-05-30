package de.illonis.eduras.gameclient.gui;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;

import de.illonis.eduras.exceptions.ObjectNotFoundException;
import de.illonis.eduras.gameclient.TooltipHandler;
import de.illonis.eduras.gameclient.gui.guielements.ItemTooltip;
import de.illonis.eduras.gameclient.gui.guielements.RenderedGuiObject;
import de.illonis.eduras.gameobjects.GameObject;
import de.illonis.eduras.items.Item;
import de.illonis.eduras.logicabstraction.InformationProvider;
import de.illonis.eduras.math.BasicMath;
import de.illonis.eduras.math.Vector2D;
import de.illonis.eduras.shapes.Circle;
import de.illonis.eduras.shapes.ObjectShape;
import de.illonis.eduras.shapes.Polygon;
import de.illonis.eduras.units.Player;
import de.illonis.eduras.units.Unit;

/**
 * GameRenderer renders the buffered image for gamepanel.
 * 
 * @author illonis
 * 
 */
public class GameRenderer implements TooltipHandler {
	private BufferedImage mapImage = null;
	private BufferedImage guiImage = null;
	private BufferedImage displayImage = null;
	private final GameCamera camera;
	private UserInterface gui;
	private Graphics2D mapGraphics = null;
	private Graphics2D guiGraphics = null;
	private Graphics2D bothGraphics = null;
	private final ConcurrentHashMap<Integer, GameObject> objs;
	private RenderThread rendererThread;
	private GamePanel target;
	private final Rectangle mapSize;
	private ItemTooltip tooltip;
	private double scale;
	private boolean tooltipShown = false;
	private ArrayList<RenderedGuiObject> uiObjects = new ArrayList<RenderedGuiObject>();
	private final static int DEFAULT_WIDTH = 484;
	private final static int DEFAULT_HEIGHT = 462;

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
	 */
	public GameRenderer(GameCamera camera, UserInterface gui,
			InformationProvider info) {
		ImageList.load(); // TODO: asynchronously
		this.uiObjects = gui.getUiObjects();
		this.camera = camera;
		scale = 1;
		objs = info.getGameObjects();
		mapSize = info.getMapBounds();
		this.gui = gui;
		RendererTooltipHandler h = new RendererTooltipHandler(this);
		gui.setTooltipHandler(h);
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
		if (mapImage == null || mapGraphics == null || guiImage == null
				|| guiGraphics == null || width != mapImage.getWidth()) {
			createGraphics(width, height);
			camera.setSize(width, height);
			camera.setScale(scale);
		}
		// clear image
		clear(width, height);
		adjustCamera();
		drawMap();
		drawObjects();
		drawGui();
	}

	private void adjustCamera() {
		try {
			Player p = getClientPlayer();
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
		// mapImage = new BufferedImage(width, height,
		// BufferedImage.TYPE_INT_RGB);

		GraphicsEnvironment env = GraphicsEnvironment
				.getLocalGraphicsEnvironment();
		GraphicsDevice device = env.getDefaultScreenDevice();
		GraphicsConfiguration config = device.getDefaultConfiguration();

		mapImage = config.createCompatibleImage(width, height,
				Transparency.OPAQUE);
		guiImage = config.createCompatibleImage(width, height,
				Transparency.TRANSLUCENT);
		displayImage = config.createCompatibleImage(width, height,
				Transparency.OPAQUE);

		scale = calculateScale(width, height);

		bothGraphics = (Graphics2D) displayImage.getGraphics();
		bothGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		mapGraphics = (Graphics2D) mapImage.getGraphics();
		mapGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		mapGraphics.scale(scale, scale);
		guiGraphics = (Graphics2D) guiImage.getGraphics();
		guiGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
	}

	private synchronized void clear(int width, int height) {
		mapGraphics.setColor(Color.black);
		mapGraphics.fillRect(0, 0, width, height);
		bothGraphics.setColor(Color.black);
		bothGraphics.fillRect(0, 0, width, height);

		// clears ui image with alpha value to let map shine through
		guiGraphics.setComposite(AlphaComposite
				.getInstance(AlphaComposite.CLEAR));

		guiGraphics.fillRect(0, 0, width, height);

		// reset composite
		guiGraphics.setComposite(AlphaComposite
				.getInstance(AlphaComposite.SRC_OVER));
	}

	/**
	 * Draw every gui element.
	 */
	private void drawGui() {
		for (int i = 0; i < uiObjects.size(); i++) {
			RenderedGuiObject o = uiObjects.get(i);
			if (!gui.isSpectator() || o.isVisibleForSpectator())
				o.render(guiGraphics);
		}
		if (tooltipShown) {
			tooltip.render(guiGraphics);
		}
	}

	/**
	 * Actively renders the buffered image on draw target.
	 * 
	 */
	public void paintGame() {
		if ((target != null) && (mapImage != null) && guiImage != null
				&& (displayImage != null)) {

			Graphics2D g2d = (Graphics2D) target.getGraphics();
			bothGraphics.drawImage(mapImage, 0, 0, null);
			bothGraphics.drawImage(guiImage, 0, 0, null);
			g2d.drawImage(displayImage, 0, 0, null);
		}
	}

	/**
	 * Draws a small red border where map bounds are.
	 */
	private void drawMap() {
		Rectangle r = new Rectangle(mapSize.getBounds());
		r.x -= camera.x;
		r.y -= camera.y;
		mapGraphics.setColor(Color.BLUE);
		mapGraphics.fill(r);
	}

	/**
	 * Draw every object of game-object list that is in camera viewport.
	 */
	private synchronized void drawObjects() {

		mapGraphics.setColor(Color.yellow);

		for (Iterator<GameObject> iterator = objs.values().iterator(); iterator
				.hasNext();) {
			GameObject d = iterator.next();
			if (!d.isVisible()) {
				continue;
			}

			// draw only if in current view point
			if (d.getBoundingBox().intersects(camera)) {
				// TODO: distinguish between object images and icon images
				if (hasImage(d)) {
					drawImageOf(d);
				} // draw shape of gameObject instead if object has shape

				if (d.getShape() != null) {
					drawShapeOf(d);
				}

				if (d.isUnit()) {
					drawHealthBarFor((Unit) d);
				}

				if (d instanceof Player) {
					Player player = (Player) d;
					mapGraphics.drawString(player.getName(), player.getDrawX()
							- camera.x, player.getDrawY() - camera.y);
				}

				// draws unit id next to unit for testing purpose
				/*
				 * dbg.drawString(d.getId() + "", d.getDrawX() - camera.x,
				 * d.getDrawY() - camera.y - 15);
				 */
			}
		}

	}

	/**
	 * Draws image for given object.
	 * 
	 * @param obj
	 *            object to draw image for.
	 */
	private void drawImageOf(GameObject obj) {
		// TODO: implement (use scale!)
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
		// TODO: use scale
		HealthBar.calculateFor(unit);
		HealthBar.draw(mapGraphics, camera);
	}

	/**
	 * Checks if renderer has an image that is associated to given
	 * {@link GameObject}, so it can be drawn.
	 * 
	 * @param obj
	 *            game object to test.
	 * @return true if gameobject has an image, false otherwise.
	 */
	private boolean hasImage(GameObject obj) {
		return ImageList.hasImageFor(obj);
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
		}
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

		mapGraphics.drawOval(d.getDrawX() - radius - camera.x, d.getDrawY()
				- radius - camera.y, 2 * radius, 2 * radius);
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
			xPositions[j] = (int) vertices.get(j).getX();
			yPositions[j] = (int) vertices.get(j).getY();
		}

		for (int j = 0; j < vCount; j++) {
			mapGraphics.drawLine(xPositions[j] - camera.x, yPositions[j]
					- camera.y, xPositions[(j + 1) % vCount] - camera.x,
					yPositions[(j + 1) % vCount] - camera.y);
		}
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
		rendererThread = new RenderThread(this);
		Thread t = new Thread(rendererThread);
		t.setName("RendererThread");
		t.start();
	}

	/**
	 * Sets drawing target of renderer.
	 * 
	 * @param gamePanel
	 *            target game panel.
	 */
	void setTarget(GamePanel gamePanel) {
		this.target = gamePanel;
	}

	/**
	 * Returns current rendering scale.
	 * 
	 * @return scale factor.
	 */
	double getCurrentScale() {
		return scale;
	}

	private Player getClientPlayer() throws ObjectNotFoundException {
		return gui.getInfos().getPlayer();
	}

}
