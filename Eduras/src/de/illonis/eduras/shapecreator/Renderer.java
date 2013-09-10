package de.illonis.eduras.shapecreator;

import de.illonis.eduras.shapecreator.gui.DrawPanel;

/**
 * Renders the drawpanel.
 * 
 * @author illonis
 * 
 */
public class Renderer extends Thread {
	private final DrawPanel panel;

	protected Renderer(DrawPanel panel) {
		super("Renderer");
		this.panel = panel;
	}

	@Override
	public void run() {
		while (true) {
			panel.repaint();
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				return;
			}
		}
	}
}
