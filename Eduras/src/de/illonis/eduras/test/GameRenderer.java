package de.illonis.eduras.test;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import de.illonis.eduras.GameInformation;
import de.illonis.eduras.GameObject;

/**
 * GameRenderer renders the buffered image for gameworldpanel.
 * 
 * @author illonis
 * 
 */
public class GameRenderer {
	private GameInformation game;
	private GameWorldPanel gameWorldPanel;
	private BufferedImage dbImage = null;
	private Graphics2D dbg = null;

	/**
	 * Creates a new gamerenderer.
	 * 
	 * @param game
	 * @param gameWorldPanel
	 */
	public GameRenderer(GameInformation game, GameWorldPanel gameWorldPanel) {
		this.game = game;
		this.gameWorldPanel = gameWorldPanel;
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
		if (dbImage == null || dbg == null
				|| width != dbImage.getWidth(gameWorldPanel)) {
			dbImage = new BufferedImage(width, height,
					BufferedImage.TYPE_INT_RGB);

			dbg = (Graphics2D) dbImage.getGraphics();
			dbg.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);
		}
		// clear image
		dbg.setColor(Color.black);
		dbg.fillRect(0, 0, width, height);

		drawObjects();
	}

	/**
	 * actively renders the buffer images to screen.
	 */
	public void paintGame() {
		Graphics g;
		try {
			g = gameWorldPanel.getGraphics();
			if ((g != null) && (dbImage != null))
				g.drawImage(dbImage, 0, 0, null);
			g.dispose();
		} catch (Exception e) {
			System.out.println("Graphics context error: " + e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * Draw every object of game-object list.
	 */
	private void drawObjects() {
		ArrayList<GameObject> objs = game.getObjects();

		dbg.setColor(Color.yellow);
		for (int i = 0; i < objs.size(); i++) {
			GameObject d = objs.get(i);
	//		d.draw(dbg);
		}
	}
}
