package de.illonis.eduras;

import de.illonis.eduras.gui.GameRenderer;
import de.illonis.eduras.gui.GameWorldPanel;
import de.illonis.eduras.networking.Client;

public class GameWorker implements Runnable {

	private boolean running = false;
	private Client client;
	private GameWorldPanel gameWorldPanel;
	private GameRenderer renderer;

	public GameWorker(Client client, GameWorldPanel gameWorldPanel) {
		this.client = client;
		this.renderer = new GameRenderer(this, gameWorldPanel);
		this.gameWorldPanel = gameWorldPanel;
	}

	public Client getClient() {
		return client;
	}

	@Override
	public void run() {
		running = true;
		while (running) {
			gameUpdate();
			gameRender();
			gameWorldPanel.repaint();
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {

			}
		}
		// exit
	}

	public void stop() {
		running = false;
	}

	private void gameRender() {
		renderer.render(gameWorldPanel.getWidth(), gameWorldPanel.getHeight());
	}

	private void gameUpdate() {
	}

}
