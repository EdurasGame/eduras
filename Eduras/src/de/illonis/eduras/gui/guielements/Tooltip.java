package de.illonis.eduras.gui.guielements;

import java.awt.Color;
import java.awt.Graphics2D;

import de.illonis.eduras.gameclient.gui.ImageList;
import de.illonis.eduras.logicabstraction.InformationProvider;

public abstract class Tooltip extends RenderedGuiObject {

	protected Tooltip(InformationProvider info) {
		super(info);
	}

	protected int width, height;

	@Override
	public void render(Graphics2D g2d, ImageList imageList) {
		g2d.setColor(Color.BLACK);
		g2d.fillRect(screenX, screenY, width, height);
		g2d.setColor(Color.GRAY);
		g2d.drawRect(screenX, screenY, width, height);
		g2d.setColor(Color.WHITE);
	}
}
