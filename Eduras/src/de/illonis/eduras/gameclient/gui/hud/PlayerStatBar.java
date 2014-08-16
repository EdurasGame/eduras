package de.illonis.eduras.gameclient.gui.hud;

import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.Player;
import de.illonis.eduras.events.SetIntegerGameObjectAttributeEvent;
import de.illonis.eduras.events.SetInteractModeEvent;
import de.illonis.eduras.exceptions.ObjectNotFoundException;
import de.illonis.eduras.exceptions.PlayerHasNoTeamException;
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

	private UserInterface userInterface;
	private LinkedList<GUIPlayerBar> players;

	private final static int WIDTH = 50;
	private final static int HEIGHT = 50;
	private final static Color COLOR_BAR = Color.yellow;
	private final static Color COLOR_BG = Color.black;
	private static final Color COLOR_TEXT = Color.black;

	private static final int GAP_BETWEEN_PLAYERS = 15;

	/**
	 * Creates a new player stat bar.
	 * 
	 * @param gui
	 *            the gui.
	 */
	public PlayerStatBar(UserInterface gui) {
		super(gui);
		userInterface = gui;
		players = new LinkedList<GUIPlayerBar>();
	}

	class GUIPlayerBar extends ClickableGuiElement {

		private int health;
		private int maxHealth;
		private int barWidth;
		private float offset;
		private Player player;

		public GUIPlayerBar(Player player, float offset) {
			super(userInterface);
			this.player = player;
			health = maxHealth = 10;
			screenX = 5;
			this.offset = offset;
			recalculate();
			setActiveInteractModes(InteractMode.MODE_STRATEGY);

			PlayerMainFigure mainFigure = player.getPlayerMainFigure();
			if (mainFigure != null) {
				maxHealth = mainFigure.getMaxHealth();
				health = mainFigure.getHealth();
			} else {
				health = 0;
			}
			recalculate();
		}

		@Override
		public void render(Graphics g2d) {
			float yPosition = screenY + offset;

			// draw frame
			g2d.setColor(Color.white);
			g2d.draw(new Rectangle(screenX, yPosition, WIDTH, HEIGHT));

			g2d.setColor(Color.white);
			g2d.drawString(player.getName(), screenX, yPosition);
			g2d.setColor(COLOR_BG);
			g2d.fillRect(screenX, yPosition + 20, WIDTH, HEIGHT - 20);
			g2d.setColor(COLOR_BAR);
			g2d.fillRect(screenX, yPosition + 20, barWidth, HEIGHT - 20);
			g2d.setColor(COLOR_TEXT);
			g2d.drawString(health + " / " + maxHealth, screenX, yPosition + 20);
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
			barWidth = Math.round(percent * WIDTH);
		}

		@Override
		public boolean onClick(Vector2f p) {

			if (!player.isDead()) {
				PlayerMainFigure mainFigure = player.getPlayerMainFigure();
				if (mainFigure != null) {
					Vector2f gamePos = new Vector2f(mainFigure.getShape()
							.getX(), mainFigure.getShape().getY());
					Vector2f newPos;
					try {
						newPos = getInfo().getPlayer().getPlayerMainFigure()
								.getPositionVector().copy();
					} catch (ObjectNotFoundException e) {
						L.log(Level.WARNING, "TODO: message", e);
						return false;
					}
					gamePos.sub(newPos);

					userInterface.getGameCamera().getCameraOffset()
							.set(gamePos.x, gamePos.y);
				}
				return true;
			} else {
				return false;
			}
		}

		@Override
		public Rectangle getBounds() {
			return new Rectangle(screenX, screenY + offset, WIDTH, HEIGHT);
		}
	}

	@Override
	public void render(Graphics g) {
	}

	@Override
	public void onGuiSizeChanged(int newWidth, int newHeight) {
	}

	@Override
	public void onInteractModeChanged(SetInteractModeEvent setModeEvent) {
		super.onInteractModeChanged(setModeEvent);

		for (GUIPlayerBar guiPlayerBar : players) {
			userInterface.removeGuiElement(guiPlayerBar);
		}

		if (setModeEvent.getNewMode().equals(InteractMode.MODE_STRATEGY)) {
			try {
				int offset = 30;
				for (Player teamMate : getInfo().getPlayer().getTeam()
						.getPlayers()) {
					new GUIPlayerBar(teamMate, offset);
					offset += HEIGHT + GAP_BETWEEN_PLAYERS;
				}
			} catch (PlayerHasNoTeamException | ObjectNotFoundException e) {
				L.log(Level.WARNING, "Cannot find player or his team!", e);
				return;
			}
		}
	}
}
