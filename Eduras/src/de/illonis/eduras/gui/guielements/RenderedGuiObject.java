package de.illonis.eduras.gui.guielements;

import java.awt.Graphics2D;

import de.illonis.eduras.logicabstraction.InformationProvider;

public abstract class RenderedGuiObject {

	private InformationProvider info;
	protected int screenX, screenY;

	public RenderedGuiObject(InformationProvider info) {
		this.info = info;
	}

	protected InformationProvider getInfo() {
		return info;
	}

	/**
	 * Renders this gui object on given graphics.
	 * 
	 * @param g2d
	 *            graphic target.
	 */
	public abstract void render(Graphics2D g2d);

	/**
	 * Indicates that gui has changed and interface positions have to be
	 * recalculated.
	 * 
	 * @param newWidth
	 *            new gui width.
	 * @param newHeight
	 *            new gui height.
	 */
	public abstract void onGuiSizeChanged(int newWidth, int newHeight);

}
