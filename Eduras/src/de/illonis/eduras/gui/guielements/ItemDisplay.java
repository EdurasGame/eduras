package de.illonis.eduras.gui.guielements;

import java.awt.Color;
import java.awt.Graphics2D;

import de.illonis.eduras.logicabstraction.InformationProvider;

public class ItemDisplay extends RenderedGuiObject {
	private int height, width, blocksize;

	public ItemDisplay(InformationProvider info) {
		super(info);
		height = 110;
		width = 150;
		blocksize = 30;

	}

	@Override
	public void render(Graphics2D g2d) {
		g2d.setColor(Color.white);
		g2d.fillRect(screenX, screenY, width, height);
		g2d.setColor(Color.black);
		g2d.drawRect(screenX + 20, screenY + 20, blocksize, blocksize);
		g2d.drawRect(screenX + 60, screenY + 20, blocksize, blocksize);
		g2d.drawRect(screenX + 100, screenY + 20, blocksize, blocksize);
	}

	@Override
	public void onGuiSizeChanged(int newWidth, int newHeight) {
		screenX = 0;
		screenY = newHeight - height;
	}
}
