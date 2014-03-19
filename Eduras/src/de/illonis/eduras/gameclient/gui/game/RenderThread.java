package de.illonis.eduras.gameclient.gui.game;

import java.util.logging.Logger;

import de.illonis.edulog.EduLog;

/**
 * This thread does repeatedly call render methods of its renderer.
 * 
 * @author illonis
 * 
 */
public final class RenderThread implements Runnable {

	private final static Logger L = EduLog.getLoggerFor(RenderThread.class
			.getName());

	private final static int DRAW_INTERVAL = 1;
	private final GameRenderer renderer;
	private final FPSListener listener;
	private boolean running;
	private int fps;

	/**
	 * Creates a new {@link RenderThread} that calls given {@link GameRenderer}.
	 * 
	 * @param renderer
	 *            renderer to repeatedly call.
	 */
	RenderThread(GameRenderer renderer, FPSListener listener) {
		this.renderer = renderer;
		this.listener = listener;
		fps = 0;
	}

	/**
	 * @return the current fps.
	 */
	public int getFps() {
		return fps;
	}

	@Override
	public void run() {
		running = true;
		int frames = 0;
		long renderTime;
		long lastTime = System.currentTimeMillis();
		while (running) {
			// BufferStrategy bs = panel.getBufferStrategy();
			// if (bs == null) {
			// panel.createBufferStrategy(3);
			// }
			frames++;

			renderer.render();
			renderTime = System.currentTimeMillis();

			if (renderTime > lastTime + 1000) {
				fps = ((int) (renderTime - lastTime)) / 1000 * frames;
				listener.setFPS(fps);
				lastTime = renderTime;
				frames = 0;
			}
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