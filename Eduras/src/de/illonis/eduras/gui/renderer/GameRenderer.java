package de.illonis.eduras.gui.renderer;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.LinkedList;

import de.illonis.eduras.GameObject;
import de.illonis.eduras.Player;
import de.illonis.eduras.gui.GameCamera;
import de.illonis.eduras.logicabstraction.InformationProvider;
import de.illonis.eduras.math.Vector2D;
import de.illonis.eduras.shapes.Circle;
import de.illonis.eduras.shapes.ObjectShape;
import de.illonis.eduras.shapes.Polygon;

/**
 * GameRenderer renders the buffered image for gamepanel.
 * 
 * @author illonis
 * 
 */
public class GameRenderer {
	private BufferedImage dbImage = null;
	private final GameCamera camera;
	private Graphics2D dbg = null;
	private final HashMap<Integer, GameObject> objs;
	private final Rectangle mapSize;

	/**
	 * Creates a new renderer.
	 * 
	 * @param camera
	 *            Game camera to use viewport.
	 * @param informationProvider
	 *            game-information that contains objects to render.
	 */
	public GameRenderer(GameCamera camera,
			InformationProvider informationProvider) {
		this.camera = camera;
		objs = informationProvider.getGameObjects();
		mapSize = informationProvider.getMapBounds();
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
		for (GameObject d : objs.values()) {

			// draw shape of gameObject if object has shape
			if (d.getShape() != null) {
				drawShapeOf(d);
			}

			if (hasImage(d)) {
				// TODO: draw image for gameobject.
			}
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
		// TODO: implement
		return false;
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
}
