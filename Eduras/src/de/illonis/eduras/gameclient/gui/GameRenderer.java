package de.illonis.eduras.gameclient.gui;

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

import de.illonis.eduras.GameObject;
import de.illonis.eduras.Player;
import de.illonis.eduras.gameclient.GameCamera;
import de.illonis.eduras.gameclient.TooltipHandler;
import de.illonis.eduras.gui.guielements.ItemDisplay;
import de.illonis.eduras.gui.guielements.ItemTooltip;
import de.illonis.eduras.gui.guielements.RenderedGuiObject;
import de.illonis.eduras.gui.guielements.TooltipTriggerer;
import de.illonis.eduras.items.Item;
import de.illonis.eduras.logicabstraction.InformationProvider;
import de.illonis.eduras.math.Vector2D;
import de.illonis.eduras.shapes.Circle;
import de.illonis.eduras.shapes.ObjectShape;
import de.illonis.eduras.shapes.Polygon;
import de.illonis.eduras.units.Unit;

/**
 * GameRenderer renders the buffered image for gamepanel.
 * 
 * @author illonis
 * 
 */
public class GameRenderer implements TooltipHandler {
	private BufferedImage dbImage = null;
	private final GameCamera camera;
	private Graphics2D dbg = null;
	private final ConcurrentHashMap<Integer, GameObject> objs;
	private final Rectangle mapSize;
	private final ImageList imagelist;
	private ArrayList<RenderedGuiObject> uiObjects;
	private InformationProvider informationProvider;
	private GuiClickReactor gui;
	private ItemDisplay itemDisplay;
	private final static int HEALTHBAR_WIDTH = 50;
	private ItemTooltip tooltip;

	/**
	 * Creates a new renderer.
	 * 
	 * @param gui
	 *            reactor that handles gui events.
	 * @param camera
	 *            Game camera to use viewport.
	 * @param informationProvider
	 *            game-information that contains objects to render.
	 */
	public GameRenderer(GuiClickReactor gui, GameCamera camera,
			InformationProvider informationProvider) {
		this.informationProvider = informationProvider;
		imagelist = new ImageList();
		imagelist.load(); // TODO: asynchronously
		this.gui = gui;
		this.camera = camera;
		objs = informationProvider.getGameObjects();
		mapSize = informationProvider.getMapBounds();
		initGui();
	}

	/**
	 * initializes gui objects
	 */
	private void initGui() {
		uiObjects = new ArrayList<RenderedGuiObject>();
		itemDisplay = new ItemDisplay(gui, informationProvider, imagelist);
		uiObjects.add(itemDisplay);
		gui.registerTooltipTriggerer(itemDisplay);
		gui.addClickableGuiElement(itemDisplay);
	}

	ItemDisplay getItemDisplay() {
		return itemDisplay;
	}

	/**
	 * Renders buffered image with given size.
	 * 
	 * @param width
	 *            width of image.
	 * @param height
	 *            height of image.
	 */
	public void render(int width, int height) {

		// recreate image if it does not exist
		if (dbImage == null || dbg == null || width != dbImage.getWidth()) {
			dbImage = new BufferedImage(width, height,
					BufferedImage.TYPE_INT_RGB);

			dbg = (Graphics2D) dbImage.getGraphics();
			dbg.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);
		}
		// clear image
		dbg.setColor(Color.black);
		dbg.fillRect(0, 0, width, height);
		drawMap();
		drawObjects();
		drawGui();
	}

	/**
	 * Draw every gui element.
	 */
	private void drawGui() {
		for (int i = 0; i < uiObjects.size(); i++)
			uiObjects.get(i).render(dbg, imagelist);

		if (tooltip != null)
			tooltip.render(dbg, imagelist);
	}

	/**
	 * Actively renders the buffer images to given graphics.
	 * 
	 * @param graphics
	 *            images are painted here.
	 */
	public void paintGame(Graphics2D graphics) {
		if ((graphics != null) && (dbImage != null)) {
			graphics.drawImage(dbImage, 0, 0, null);
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
		dbg.draw(r);
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

				if (hasImage(d)) {
					// TODO: draw image for gameobject.
				} // draw shape of gameObject instead if object has shape
					// TODO: d
				if (d.getShape() != null) {
					drawShapeOf(d);
				}

				// if (d.isUnit()) {
				// drawHealthBarFor((Unit) d);
				// }

				// draws unit id next to unit for testing purpose
				dbg.drawString(d.getId() + "", d.getDrawX() - camera.x,
						d.getDrawY() - camera.y - 15);
			}
		}

	}

	/**
	 * Draws health bar that is assigned to given unit. It will be automatically
	 * be placed above the unit.
	 * 
	 * @param unit
	 *            assigned unit.
	 */
	private void drawHealthBarFor(Unit unit) {
		if (unit.isDead())
			return;

		int[] insets = new int[unit.getMaxHealth()];
		int segments = unit.getMaxHealth() + 1;
		for (int i = 0; i < unit.getMaxHealth(); i++) {
			insets[i] = i * (HEALTHBAR_WIDTH / segments)
					+ (HEALTHBAR_WIDTH / unit.getMaxHealth()) / 2;
		}
		int xOffset = (int) (unit.getBoundingBox().getWidth() - HEALTHBAR_WIDTH) / 2;
		dbg.setColor(Color.black);
		dbg.fillRect((int) unit.getBoundingBox().getX() + xOffset - camera.x,
				(int) unit.getBoundingBox().getY() - 10 - camera.y,
				HEALTHBAR_WIDTH, 10);
		dbg.setColor(Color.yellow);
		for (int i = 0; i < unit.getHealth(); i++) {
			dbg.fillRect((int) unit.getBoundingBox().getX() - camera.x
					+ xOffset + insets[i], (int) unit.getBoundingBox().getY()
					- 8 - camera.y, HEALTHBAR_WIDTH / unit.getMaxHealth(), 6);
		}

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
		return imagelist.hasImageFor(obj);
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

	/**
	 * Notifies all ui objects that gui size has changed.
	 * 
	 * @param width
	 *            new gamepanel width
	 * @param height
	 *            new gamepanel height
	 */
	void notifyGuiSizeChanged(int width, int height) {
		for (int i = 0; i < uiObjects.size(); i++) {
			uiObjects.get(i).onGuiSizeChanged(width, height);
		}
	}

	/**
	 * Notifies all ui objects that player data have been received.
	 */
	void notifyPlayerReceived() {
		for (int i = 0; i < uiObjects.size(); i++) {
			uiObjects.get(i).onPlayerInformationReceived();
		}
	}

	@Override
	public void registerTooltipTriggerer(TooltipTriggerer elem) {
	}

	@Override
	public void removeTooltipTriggerer(TooltipTriggerer elem) {
	}

	@Override
	public void showItemTooltip(Point p, Item item) {
		if (tooltip == null || !(tooltip instanceof ItemTooltip)
				|| !((ItemTooltip) tooltip).getItem().equals(item))
			tooltip = new ItemTooltip(informationProvider, item);

		tooltip.moveTo(p);
	}

	@Override
	public void showTooltip(Point p, String text) {
		// TODO: implement

	}

	@Override
	public void hideTooltip() {
		tooltip = null;
	}

	public void drawWin(int winnerId) {
		dbg.drawString("Player with id " + winnerId + " won the game!",
				(int) mapSize.getCenterX(), (int) mapSize.getCenterY());
	}
}
