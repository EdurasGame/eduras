package de.illonis.eduras.gameclient.gui.game;

import java.util.logging.Logger;

import de.illonis.edulog.EduLog;

/**
 * This thread does repeatedly call render methods of its renderer.
 * 
 * @author illonis
 * 
 */
public class RenderThread implements Runnable {

	private final static Logger L = EduLog.getLoggerFor(RenderThread.class
			.getName());

	private final static int DRAW_INTERVAL = 20;
	private final GameRenderer renderer;
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
				break;
			}
		}
		L.info("RenderThread stopped.");
	}

	/**
	 * Stops this renderer. A stopped {@link RenderThread} cannot be started
	 * again.
	 */
	public void stop() {
		running = false;
	}
}