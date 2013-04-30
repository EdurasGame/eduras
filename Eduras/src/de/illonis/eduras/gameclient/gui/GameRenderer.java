package de.illonis.eduras.gameclient.gui;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;

import de.illonis.eduras.gameclient.TooltipHandler;
import de.illonis.eduras.gameclient.gui.guielements.ItemTooltip;
import de.illonis.eduras.gameclient.gui.guielements.RenderedGuiObject;
import de.illonis.eduras.gameobjects.GameObject;
import de.illonis.eduras.items.Item;
import de.illonis.eduras.logicabstraction.InformationProvider;
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
	private BufferedImage dbImage = null;
	private BufferedImage dbUiImage = null;
	private BufferedImage displayImage = null;
	private final GameCamera camera;
	private UserInterface gui;
	private Graphics2D dbg = null;
	private Graphics2D dbuig = null;
	private Graphics2D displayg = null;
	private final ConcurrentHashMap<Integer, GameObject> objs;
	private RenderThread rendererThread;
	private GamePanel target;
	private final Rectangle mapSize;
	private ItemTooltip tooltip;
	private double scale;
	private boolean tooltipShown = false;
	private ArrayList<RenderedGuiObject> uiObjects = new ArrayList<RenderedGuiObject>();

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
		objs = info.getGameObjects();
		scale = 2.0;
		mapSize = info.getMapBounds();
		this.gui = gui;
		RendererTooltipHandler h = new RendererTooltipHandler(this);
		gui.setTooltipHandler(h);
	}

	/**
	 * Renders buffered image with size of current target.
	 */
	public void render() {
		int width = target.getWidth();
		int height = target.getHeight();

		// recreate image if it does not exist
		if (dbImage == null || dbg == null || dbUiImage == null
				|| dbuig == null || width != dbImage.getWidth()) {
			dbImage = new BufferedImage(width, height,
					BufferedImage.TYPE_INT_RGB);
			dbUiImage = new BufferedImage(width, height,
					BufferedImage.TYPE_INT_ARGB);
			displayImage = new BufferedImage(width, height,
					BufferedImage.TYPE_INT_RGB);

			displayg = (Graphics2D) displayImage.getGraphics();
			displayg.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);

			dbg = (Graphics2D) dbImage.getGraphics();
			dbg.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);

			dbg.scale(scale, scale);
			dbuig = (Graphics2D) dbUiImage.getGraphics();
			dbuig.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);
		}
		// clear image
		clear(width, height);
		drawMap();
		drawObjects();
		drawGui();
	}

	private synchronized void clear(int width, int height) {
		dbg.setColor(Color.black);
		dbg.fillRect(0, 0, width, height);
		displayg.setColor(Color.black);
		displayg.fillRect(0, 0, width, height);

		// clears ui image with alpha value to let map shine through
		dbuig.setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR));

		dbuig.fillRect(0, 0, width, height);

		// reset composite
		dbuig.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
	}

	/**
	 * Draw every gui element.
	 */
	private void drawGui() {
		for (int i = 0; i < uiObjects.size(); i++) {
			uiObjects.get(i).render(dbuig);
		}
		if (tooltipShown) {
			tooltip.render(dbuig);
		}
	}

	/**
	 * Actively renders the buffered image on draw target.
	 * 
	 */
	public void paintGame() {
		if ((target != null) && (dbImage != null) && dbUiImage != null
				&& (displayImage != null)) {

			Graphics2D g2d = (Graphics2D) target.getGraphics();
			displayg.drawImage(dbImage, 0, 0, null);
			displayg.drawImage(dbUiImage, 0, 0, null);
			g2d.drawImage(displayImage, 0, 0, null);
		}
	}

	/**
	 * Draws a small red border where map bounds are.
	 */
	private void drawMap() {
		dbg.setColor(Color.red);
		Rectangle r = mapSize.getBounds();
		r.x -= camera.x;
		r.y -= camera.y;
		dbg.setColor(Color.BLUE);
		dbg.fill(r);
	}

	/**
	 * Draw every object of game-object list that is in camera viewport.
	 */
	private synchronized void drawObjects() {

		dbg.setColor(Color.yellow);

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
		HealthBar.draw(dbg, camera);
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

			if (obj instanceof Player) {
				Player player = (Player) obj;
				dbg.drawString(player.getName(), player.getDrawX() - camera.x,
						player.getDrawY() - camera.y);
			}
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

		dbg.drawOval(d.getDrawX() - radius - camera.x, d.getDrawY() - radius
				- camera.y, 2 * radius, 2 * radius);
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
			dbg.drawLine(xPositions[j] - camera.x, yPositions[j] - camera.y,
					xPositions[(j + 1) % vCount] - camera.x, yPositions[(j + 1)
							% vCount]
							- camera.y);
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
	 * Stopps rendering process.
	 */
	void stopRendering() {
		if (rendererThread != null)
			rendererThread.stop();
	}

	void startRendering() {
		rendererThread = new RenderThread(this);
		Thread t = new Thread(rendererThread);
		t.start();
	}

	public void drawWin(int winnerId) {
		// TODO: make working
		dbg.drawString("Player with id " + winnerId + " won the game!",
				(int) mapSize.getCenterX(), (int) mapSize.getCenterY());
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

}
