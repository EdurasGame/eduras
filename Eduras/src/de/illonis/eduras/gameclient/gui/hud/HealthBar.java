package de.illonis.eduras.gameclient.gui.hud;

import java.awt.Color;
import java.awt.Graphics2D;

import de.illonis.eduras.gameclient.gui.GameCamera;
import de.illonis.eduras.units.Unit;

/**
 * Represents a unit health bar manager. There are no specific healthbar objects
 * stored. Instead, an internal object's values are changed.
 * 
 * @author illonis
 * 
 */
public class HealthBar {

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
	 */
	public static void calculateFor(Unit unit) {
		int maxHealth = unit.getMaxHealth();
		int health = unit.getHealth();

		instance.w = (int) Math.round((double) ((double) health / maxHealth)
				* HEALTHBAR_WIDTH);

		double overlength = (HEALTHBAR_WIDTH - unit.getBoundingBox().getWidth()) / 2;

		instance.x = (int) Math.round(unit.getDrawX() - overlength);

		// TODO: make position of objects either topleft or center, but make it
		// unitary.
		// fix because topleft is middle in triangles.
		instance.x -= (int) unit.getBoundingBox().getWidth() / 2;

		instance.y = (int) (unit.getBoundingBox().getY() - HEALTHBAR_HEIGHT - UNIT_GAP);

	}

	/**
	 * Draws current healthbar settings to given graphic object.
	 * 
	 * @param g2d
	 *            target graphics.
	 * @param camera
	 *            camera offset.
	 */
	public static void draw(Graphics2D g2d, GameCamera camera) {
		g2d.setColor(Color.black);
		g2d.fillRect(instance.x - camera.x, instance.y - camera.y,
				HEALTHBAR_WIDTH, HEALTHBAR_HEIGHT);
		g2d.setColor(Color.yellow);
		g2d.fillRect(instance.x - camera.x, instance.y - camera.y, instance.w,
				instance.h);
	}
}
