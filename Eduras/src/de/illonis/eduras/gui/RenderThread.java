package de.illonis.eduras.gui;

import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;

/**
 * This thread does repeatedly call render methods of its renderer.
 * 
 * @author illonis
 * 
 */
public class RenderThread implements Runnable {

	private final static int DRAW_INTERVAL = 20;
	private GameRenderer renderer;
	private boolean running;
	private GamePanel panel;

	/**
	 * Creates a new {@link RenderThread} that calls given {@link GameRenderer}.
	 * 
	 * @param renderer
	 *            renderer to repeatedly call.
	 * @param panel
	 *            panel to draw onto
	 */
	public RenderThread(GameRenderer renderer, GamePanel panel) {
		this.renderer = renderer;
		this.panel = panel;
	}

	@Override
	public void run() {
		running = true;
		while (running) {
			BufferStrategy bs = panel.getBufferStrategy();
			if (bs == null) {
				panel.createBufferStrategy(3);
			}
			renderer.render(panel.getWidth(), panel.getHeight());
			renderer.paintGame((Graphics2D) panel.getGraphics());
			try {
				Thread.sleep(DRAW_INTERVAL);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Stops this renderer. A stopped {@link RenderThread} cannot be started
	 * again.
	 */
	public void stop() {
		running = false;
	}
}