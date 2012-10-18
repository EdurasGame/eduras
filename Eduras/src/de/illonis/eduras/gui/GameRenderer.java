package de.illonis.eduras.gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.util.ArrayList;

import de.illonis.eduras.GameObject;
import de.illonis.eduras.GameWorker;

public class GameRenderer {
	private GameWorker gameWorker;
	private GameWorldPanel gameWorldPanel;
	private Image dbImage = null;
	private Graphics2D dbg;

	public GameRenderer(GameWorker gw, GameWorldPanel gameWorldPanel) {
		gameWorker = gw;
		this.gameWorldPanel = gameWorldPanel;
		gameWorldPanel.setImage(dbImage);
	}

	public void render(int width, int height) {

		if (dbImage == null) {
			dbImage = gameWorldPanel.createImage(width, height);
		}
		dbg = (Graphics2D) dbImage.getGraphics();

		// clear image
		dbg.setColor(Color.black);
		dbg.fillRect(0, 0, width, height);

		drawObjects();

	}

	private void drawObjects() {
		ArrayList<GameObject> objs = gameWorker.getClient().getGame()
				.getObjects();

		for (int i = 0; i < objs.size(); i++) {
			GameObject o = objs.get(i);
			dbg.drawOval(o.getXPosition() - 10, o.getYPosition() - 10, 20, 20);
		}
	}
}
