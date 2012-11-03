package de.illonis.eduras.gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.HashMap;

import de.illonis.eduras.GameObject;
import de.illonis.eduras.Player;
import de.illonis.eduras.logicabstraction.InformationProvider;

/**
 * GameRenderer renders the buffered image for gamepanel.
 * 
 * @author illonis
 * 
 */
public class GameRenderer {
	private BufferedImage dbImage = null;
	private Graphics2D dbg = null;
	private HashMap<Integer, GameObject> objs;

	/**
	 * Creates a new renderer.
	 * 
	 * @param game
	 *            game-information that contains objects to render.
	 */
	public GameRenderer(InformationProvider informationProvider) {
		objs = informationProvider.getGameObjects();
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
			dbImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

			dbg = (Graphics2D) dbImage.getGraphics();
			dbg.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		}
		// clear image
		dbg.setColor(Color.black);
		dbg.fillRect(0, 0, width, height);

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
	 * Draw every object of game-object list.
	 */
	private synchronized void drawObjects() {

		dbg.setColor(Color.yellow);
		for (int i = 0; i < objs.size(); i++) {
			GameObject d = objs.get(i);
			if (d instanceof Player) {
				dbg.fillRect(d.getDrawX(), d.getDrawY(), 30, 30);
			}
		}
	}
}
