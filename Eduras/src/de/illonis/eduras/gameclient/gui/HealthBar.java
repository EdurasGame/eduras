package de.illonis.eduras.gameclient.gui;

import java.awt.Color;
import java.awt.Graphics2D;

import de.illonis.eduras.gameclient.GameCamera;
import de.illonis.eduras.units.Unit;

/**
 * Represents a unit health bar manager.
 * 
 * @author illonis
 * 
 */
public class HealthBar {

	private final static HealthBar instance = new HealthBar();
	private final static int HEALTHBAR_WIDTH = 50;
	private final static int HEALTHBAR_HEIGHT = 5;
	private final static int UNIT_GAP = 15;

	private int x, y, w, h;

	private HealthBar() {
		x = y = 0;
		w = HEALTHBAR_WIDTH;
		h = HEALTHBAR_HEIGHT;
	}

	protected static void createFor(Unit unit, GameCamera camera) {
		int maxHealth = unit.getMaxHealth();
		int health = unit.getHealth();

		instance.w = (int) Math.floor((double) ((double) health / maxHealth)
				* HEALTHBAR_WIDTH);

		double overlength = (HEALTHBAR_WIDTH - unit.getBoundingBox().getWidth()) / 2;
		System.out.println("over: " + overlength);
		System.out.println("w: " + instance.w);
		instance.x = (int) Math.round(unit.getDrawX() - overlength);
		// fix because topleft is middle in triangles.
		instance.x -= (int) unit.getBoundingBox().getWidth() / 2;
		System.out.println(instance.x);
		System.out.println("d: " + unit.getDrawX());
		System.out.println("x: " + instance.x);
		instance.y = (int) (unit.getBoundingBox().getY() - HEALTHBAR_HEIGHT - UNIT_GAP);

		instance.x -= camera.x;

		instance.y -= camera.y;

	}

	protected static void draw(Graphics2D g2d) {
		g2d.setColor(Color.black);
		g2d.fillRect(instance.x, instance.y, HEALTHBAR_WIDTH, HEALTHBAR_HEIGHT);
		g2d.setColor(Color.yellow);
		g2d.fillRect(instance.x, instance.y, instance.w, instance.h);
	}
}
