package de.illonis.eduras.gameclient.gui.hud;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Vector2f;

import de.illonis.eduras.gameclient.gui.game.GameRenderer;

/**
 * Shows detailed information depending on mouse position. Only one tooltip will
 * be rendered by {@link GameRenderer} at any time.
 * 
 * @author illonis
 * 
 */
public abstract class Tooltip {
	float screenX, screenY;

	protected int width, height;

	/**
	 * Moves this tooltip to given mouse position. Tooltip will be automatically
	 * arranged so it is within screen.
	 * 
	 * @param p
	 *            new position to anchor tooltip to.
	 */
	public void moveTo(Vector2f p) {
		this.screenX = p.x;
		this.screenY = p.y - height;
		// TODO: make mouse-relative position dependend on screen position to
		// prevent hidden parts.
	}

	/**
	 * Renders this tooltip
	 * 
	 * @param g2d
	 *            target graphics.
	 */
	public void render(Graphics g2d) {
		g2d.setLineWidth(1f);
		g2d.setColor(Color.black);
		g2d.fillRect(screenX, screenY, width, height);
		g2d.setColor(Color.gray);
		g2d.drawRect(screenX, screenY, width, height);
	}
}
