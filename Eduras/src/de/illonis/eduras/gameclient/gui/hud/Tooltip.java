package de.illonis.eduras.gameclient.gui.hud;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;

import de.illonis.eduras.gameclient.gui.GameRenderer;

/**
 * Shows detailed information depending on mouse position. Only one tooltip will
 * be rendered by {@link GameRenderer} at any time.
 * 
 * @author illonis
 * 
 */
public abstract class Tooltip extends RenderedGuiObject {

	/**
	 * Creates a new tooltip.
	 * 
	 * @param gui
	 *            the associated user interface.
	 */
	protected Tooltip(UserInterface gui) {
		super(gui);
	}

	protected int width, height;

	/**
	 * Moves this tooltip to given mouse position. Tooltip will be automatically
	 * arranged so it is within screen.
	 * 
	 * @param p
	 *            new position to anchor tooltip to.
	 */
	public void moveTo(Point p) {
		this.screenX = p.x;
		this.screenY = p.y - height;
		// TODO: make mouse-relative position dependend on screen position to
		// prevent hidden parts.
	}

	@Override
	public void render(Graphics2D g2d) {
		g2d.setColor(Color.BLACK);
		g2d.fillRect(screenX, screenY, width, height);
		g2d.setColor(Color.GRAY);
		g2d.drawRect(screenX, screenY, width, height);
		g2d.setColor(Color.WHITE);
	}
}
