package de.illonis.eduras.gameclient.gui.hud;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

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
	 * @param g
	 *            the target graphics
	 * @param camera
	 *            the current camera.
	 */
	public static void calculateAndDrawFor(Unit unit, Graphics g,
			GameCamera camera) {
		int maxHealth = unit.getMaxHealth();
		int health = unit.getHealth();

		instance.w = (int) Math.round((float) health / maxHealth
				* HEALTHBAR_WIDTH);
		double unitHalfWidth = unit.getShape().getWidth() / 2;
		instance.x = (int) Math.round(unit.getDrawX() + unitHalfWidth
				- HEALTHBAR_WIDTH / 2);
		instance.y = unit.getDrawY() - HEALTHBAR_HEIGHT - UNIT_GAP;
		g.setColor(Color.yellow);
		g.fillRect(instance.x, instance.y ,
				instance.w, instance.h);
		if (unit instanceof PlayerMainFigure) {
			PlayerMainFigure player = (PlayerMainFigure) unit;
			g.drawString(player.getName(), instance.x,
					instance.y - HEALTHBAR_HEIGHT);
		}
	}
}
