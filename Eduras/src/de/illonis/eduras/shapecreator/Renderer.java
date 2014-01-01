package de.illonis.eduras.shapecreator;

import java.awt.Component;

/**
 * Renders the drawpanel.
 * 
 * @author illonis
 * 
 */
public class Renderer extends Thread {
	private final Component panel;

	protected Renderer(Component panel) {
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
