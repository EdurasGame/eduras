package de.illonis.eduras.gameclient.gui.hud;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.Player;
import de.illonis.eduras.events.SetIntegerGameObjectAttributeEvent;
import de.illonis.eduras.exceptions.ObjectNotFoundException;
import de.illonis.eduras.units.InteractMode;
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
	private final static Color COLOR_BAR = Color.yellow;
	private final static Color COLOR_BG = Color.black;

	private int health;
	private int maxHealth;
	private int barWidth;
	private Player player;

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
		setActiveInteractModes(InteractMode.MODE_EGO);
	}

	@Override
	public void render(Graphics g2d) {
		g2d.setColor(COLOR_BG);
		g2d.fillRect(screenX, screenY, MAX_WIDTH, HEIGHT);
		g2d.setColor(COLOR_BAR);
		g2d.fillRect(screenX, screenY, barWidth, HEIGHT);
	}

	@Override
	public void onGuiSizeChanged(int newWidth, int newHeight) {
		screenY = newHeight - HEIGHT - ItemDisplay.HEIGHT;
	}

	@Override
	public void onHealthChanged(SetIntegerGameObjectAttributeEvent event) {
		PlayerMainFigure mainFigure = player.getPlayerMainFigure();
		if (mainFigure != null && event.getObjectId() == mainFigure.getId()) {

			health = event.getNewValue();
			maxHealth = mainFigure.getMaxHealth();
			recalculate();
		}
	}

	@Override
	public void onMaxHealthChanged(SetIntegerGameObjectAttributeEvent event) {
		PlayerMainFigure mainFigure = player.getPlayerMainFigure();
		if (event.getObjectId() == mainFigure.getId()) {
			maxHealth = event.getNewValue();
			recalculate();
		}
	}

	private void recalculate() {
		float percent = (float) health / maxHealth;
		barWidth = Math.round(percent * MAX_WIDTH);
	}

	@Override
	public void onGameReady() {
		try {
			player = getInfo().getPlayer();

		} catch (ObjectNotFoundException e) {
			L.log(Level.SEVERE, "Player received not found", e);
		}
	}

}
