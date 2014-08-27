package de.illonis.eduras.gameclient.gui.hud;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.exceptions.ObjectNotFoundException;
import de.illonis.eduras.units.InteractMode;

public class BlinkDisplay extends CooldownGuiObject {

	private final static Logger L = EduLog.getLoggerFor(BlinkDisplay.class
			.getName());

	public BlinkDisplay(UserInterface gui) {
		super(gui);

		setActiveInteractModes(InteractMode.MODE_EGO);
	}

	@Override
	public void render(Graphics g) {
		try {
			g.setColor(Color.white);
			g.drawString(getInfo().getPlayer().getBlinksAvailable() + "",
					screenX, screenY);
		} catch (ObjectNotFoundException e) {
			L.log(Level.WARNING, "Cannot find player!", e);
		}
	}

	@Override
	public void onGuiSizeChanged(int newWidth, int newHeight) {
		screenX = 0;
		screenY = newHeight / 2;
	}

	@Override
	long getCooldown() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	long getCooldownTime() {
		// TODO Auto-generated method stub
		return 0;
	}
}
