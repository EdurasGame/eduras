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

	public abstract void render(Graphics2D g2d);

	public abstract void onGuiSizeChanged(int newWidth, int newHeight);

}
