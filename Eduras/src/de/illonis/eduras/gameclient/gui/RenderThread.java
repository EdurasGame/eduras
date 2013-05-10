package de.illonis.eduras.gameclient.gui;

import de.illonis.eduras.logger.EduLog;

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

	/**
	 * Creates a new {@link RenderThread} that calls given {@link GameRenderer}.
	 * 
	 * @param renderer
	 *            renderer to repeatedly call.
	 */
	RenderThread(GameRenderer renderer) {
		this.renderer = renderer;
	}

	@Override
	public void run() {
		running = true;
		while (running) {
			// BufferStrategy bs = panel.getBufferStrategy();
			// if (bs == null) {
			// panel.createBufferStrategy(3);
			// }
			renderer.render();
			renderer.paintGame();
			try {
				Thread.sleep(DRAW_INTERVAL);
			} catch (InterruptedException e) {
				EduLog.passException(e);
			}
		}
		EduLog.info("RenderThread stopped.");
	}

	/**
	 * Stops this renderer. A stopped {@link RenderThread} cannot be started
	 * again.
	 */
	public void stop() {
		running = false;
	}
}