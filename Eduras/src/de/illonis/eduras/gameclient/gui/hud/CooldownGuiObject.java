package de.illonis.eduras.gameclient.gui.hud;

import java.util.logging.Logger;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import de.illonis.edulog.EduLog;

public abstract class CooldownGuiObject extends RenderedGuiObject {

	private final static Logger L = EduLog.getLoggerFor(CooldownGuiObject.class
			.getName());

	private final static Color COLOR_SEMITRANSPARENT = new Color(0, 0, 0, 120);

	protected CooldownGuiObject(UserInterface gui) {
		super(gui);
	}

	/**
	 * Uses the Graphics object to render the cooldown at the given position
	 * with the given height and width.
	 * 
	 * @param g
	 * @param xPos
	 * @param yPos
	 * @param width
	 * @param height
	 */
	public void renderCooldown(Graphics g, float xPos, float yPos, float width,
			float height) {
		long cd = getCooldown();
		if (cd > 0) {
			g.setColor(COLOR_SEMITRANSPARENT);
			float a = getCooldownPercent();
			g.fillArc(xPos, yPos, width, height, -90 - a * 360, -90);
		}
	}

	abstract long getCooldown();

	private float getCooldownPercent() {
		return getCooldownTime() == 0 ? 0 : (float) getCooldown()
				/ getCooldownTime();
	}

	abstract long getCooldownTime();
}
