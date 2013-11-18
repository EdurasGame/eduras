package de.illonis.eduras.gameclient.gui.hud;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.events.SetIntegerGameObjectAttributeEvent;
import de.illonis.eduras.exceptions.ObjectNotFoundException;
import de.illonis.eduras.units.PlayerMainFigure;

/**
 * Displays player details like stats, health, etc.
 * 
 * @author illonis
 * 
 */
public class PlayerStatBar extends RenderedGuiObject {
	private final static Logger L = EduLog.getLoggerFor(PlayerStatBar.class
			.getName());

	private final static int MAX_WIDTH = ItemDisplay.WIDTH;
	private final static int HEIGHT = 20;
	private final static Color COLOR_BAR = Color.YELLOW;
	private final static Color COLOR_BG = Color.BLACK;

	private int health;
	private int maxHealth;
	private int barWidth;
	private PlayerMainFigure player;

	/**
	 * Creates a new player stat bar.
	 * 
	 * @param gui
	 *            the gui.
	 */
	public PlayerStatBar(UserInterface gui) {
		super(gui);
		health = maxHealth = 10;
		screenX = 0;
		recalculate();
	}

	@Override
	public void render(Graphics2D g2d) {
		g2d.setColor(COLOR_BG);
		g2d.fillRect(screenX, screenY, MAX_WIDTH, HEIGHT);
		g2d.setColor(COLOR_BAR);
		g2d.fillRect(screenX, screenY, barWidth, HEIGHT);
		g2d.setColor(Color.BLACK);
	}

	@Override
	public void onGuiSizeChanged(int newWidth, int newHeight) {
		screenY = newHeight - HEIGHT - ItemDisplay.HEIGHT;
	}

	@Override
	public void onPlayerInformationReceived() {
		try {
			player = getInfo().getPlayer();
			maxHealth = player.getMaxHealth();
			health = player.getHealth();
			recalculate();
		} catch (ObjectNotFoundException e) {
			L.log(Level.SEVERE, "Player received not found", e);
		}
	}

	@Override
	public void onHealthChanged(SetIntegerGameObjectAttributeEvent event) {
		if (player != null && event.getObjectId() == player.getId()) {
			health = event.getNewValue();
			recalculate();
		}
	}

	@Override
	public void onMaxHealthChanged(SetIntegerGameObjectAttributeEvent event) {
		if (event.getObjectId() == player.getId()) {
			maxHealth = event.getNewValue();
			recalculate();
		}
	}

	private void recalculate() {
		double percent = (double) health / maxHealth;
		barWidth = (int) Math.round(percent * MAX_WIDTH);
	}

}