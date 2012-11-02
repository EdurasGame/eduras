package de.illonis.eduras.gui;

public class RenderThread implements Runnable {

	private GameRenderer renderer;
	private boolean running;

	public RenderThread(GameRenderer renderer) {
		this.renderer = renderer;
	}

	@Override
	public void run() {
		running = true;
		while (running) {
			renderer.render();
			renderer.paintGame();
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public void stop() {
		running = false;
	}

}
