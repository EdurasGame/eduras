package de.illonis.eduras.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

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
	private InformationProvider informationProvider;
	private GamePanel gamePanel;
	private BufferedImage dbImage = null;
	private Graphics2D dbg = null;

	/**
	 * Creates a new gamerenderer.
	 * 
	 * @param game
	 * @param gameWorldPanel
	 */
	public GameRenderer(InformationProvider informationProvider, GamePanel gameWorldPanel) {
		this.informationProvider = informationProvider;
		this.gamePanel = gameWorldPanel;
	}

	/**
	 * Renders buffered image with panel size.
	 */
	public void render() {
		render(gamePanel.getWidth(), gamePanel.getHeight());
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
		if (dbImage == null || dbg == null || width != dbImage.getWidth(gamePanel)) {
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
	 * actively renders the buffer images to screen.
	 */
	public void paintGame() {
		Graphics g;
		try {
			g = gamePanel.getGraphics();
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
		ArrayList<GameObject> objs = informationProvider.getGameObjects();

		dbg.setColor(Color.yellow);
		for (int i = 0; i < objs.size(); i++) {
			GameObject d = objs.get(i);
			if (d instanceof Player) {
				dbg.fillRect(d.getDrawX(), d.getDrawY(), 30, 30);
			}

		}
	}
}
