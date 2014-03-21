package de.illonis.eduras.gameclient.gui.hud;

import java.awt.Color;
import java.awt.Graphics2D;

import de.illonis.eduras.gameclient.gui.game.GameCamera;
import de.illonis.eduras.units.PlayerMainFigure;
import de.illonis.eduras.units.Unit;

/**
 * Represents a unit health bar manager. There are no specific healthbar objects
 * stored. Instead, an internal object's values are changed.
 * 
 * @author illonis
 * 
 */
public class HealthBar {

	private static final Color TRANSLUCENT = new Color(0, 0, 0, 0);

	private final static HealthBar instance = new HealthBar();
	private final static int HEALTHBAR_WIDTH = 50;
	private final static int HEALTHBAR_HEIGHT = 5;
	/**
	 * Gap between unit and health bar.
	 */
	private final static int UNIT_GAP = 5;

	private int x, y, w, h;

	private HealthBar() {
		x = y = 0;
		w = HEALTHBAR_WIDTH;
		h = HEALTHBAR_HEIGHT;
	}

	/**
	 * Calculates healthbar data for given unit.
	 * 
	 * @param unit
	 *            unit that's health should be shown.
	 * @param g2d
	 *            the target graphics
	 * @param camera
	 *            the current camera.
	 */
	public static void calculateAndDrawFor(Unit unit, Graphics2D g2d,
			GameCamera camera) {
		int maxHealth = unit.getMaxHealth();
		int health = unit.getHealth();

		instance.w = (int) Math.round((double) health / maxHealth
				* HEALTHBAR_WIDTH);
		double unitHalfWidth = unit.getBoundingBox().getWidth() / 2;
		instance.x = (int) Math.round(unit.getDrawX() + unitHalfWidth
				- HEALTHBAR_WIDTH / 2);
		instance.y = unit.getDrawY() - HEALTHBAR_HEIGHT - UNIT_GAP;

		// g2d.setColor(Color.black);
		g2d.setColor(TRANSLUCENT); // translucent
		g2d.fillRect(instance.x - camera.x, instance.y - camera.y,
				HEALTHBAR_WIDTH, HEALTHBAR_HEIGHT);
		g2d.setColor(Color.YELLOW);
		g2d.fillRect(instance.x - camera.x, instance.y - camera.y, instance.w,
				instance.h);
		if (unit instanceof PlayerMainFigure) {
			PlayerMainFigure player = (PlayerMainFigure) unit;
			g2d.drawString(player.getName(), instance.x - camera.x, instance.y - camera.y - 1);
		}
	}
}
