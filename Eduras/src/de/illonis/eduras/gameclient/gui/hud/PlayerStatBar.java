package de.illonis.eduras.gameclient.gui.hud;

import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.Player;
import de.illonis.eduras.events.SetIntegerGameObjectAttributeEvent;
import de.illonis.eduras.events.SetInteractModeEvent;
import de.illonis.eduras.exceptions.InsufficientResourceException;
import de.illonis.eduras.exceptions.ObjectNotFoundException;
import de.illonis.eduras.exceptions.PlayerHasNoTeamException;
import de.illonis.eduras.gameclient.gui.game.GamePanelLogic;
import de.illonis.eduras.gameclient.gui.game.GamePanelLogic.ClickState;
import de.illonis.eduras.gameclient.gui.game.GameRenderer;
import de.illonis.eduras.logicabstraction.EdurasInitializer;
import de.illonis.eduras.units.InteractMode;
import de.illonis.eduras.units.PlayerMainFigure;
import de.illonis.eduras.units.Unit;

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
	private float statBarSize;

	private final static int PLAYER_BAR_SIZE = 30;
	private final static Color COLOR_BAR = Color.yellow;
	private final static Color COLOR_BG = Color.black;
	private static final Color COLOR_TEXT = Color.black;

	private static final int GAP_BETWEEN_PLAYERS = 10;

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
		statBarSize = PLAYER_BAR_SIZE * GameRenderer.getRenderScale();
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

			Rectangle playerStatBox = new Rectangle(screenX, yPosition,
					statBarSize, statBarSize);
			// fill black
			g2d.setColor(Color.black);
			g2d.fill(playerStatBox);
			// draw frame
			g2d.setColor(Color.white);
			g2d.draw(playerStatBox);

			g2d.setColor(Color.white);
			g2d.drawString(player.getName(), screenX, yPosition);
			g2d.setColor(COLOR_BG);
			g2d.fillRect(screenX, yPosition + statBarSize / 3, statBarSize,
					statBarSize - statBarSize / 3);
			g2d.setColor(COLOR_BAR);
			g2d.fillRect(screenX, yPosition + statBarSize / 3, barWidth,
					statBarSize / 3 * 2);
			g2d.setColor(COLOR_TEXT);
			g2d.drawString(health + " / " + maxHealth, screenX, yPosition
					+ statBarSize / 3);
		}

		@Override
		public void onGuiSizeChanged(int newWidth, int newHeight) {
			screenY = newHeight - statBarSize;
		}

		@Override
		public void onHealthChanged(Unit unit, int oldValue, int newValue) {
			PlayerMainFigure mainFigure = player.getPlayerMainFigure();
			if (mainFigure != null && unit.equals(mainFigure)) {

				health = newValue;
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
			barWidth = Math.round(percent * statBarSize);
		}

		@Override
		public boolean mouseClicked(int button, int x, int y, int clickCount) {

			if (!player.isDead()) {
				PlayerMainFigure mainFigure = player.getPlayerMainFigure();

				GamePanelLogic logic = userInterface.getLogic();
				switch (logic.getClickState()) {
				case SELECT_TARGET_FOR_SPELL:
					// select player for a spell
					if (button == Input.MOUSE_LEFT_BUTTON) {
						try {
							userInterface.getListener().onUnitSpell(mainFigure);
							actionDone();
							logic.setClickState(ClickState.DEFAULT);
						} catch (InsufficientResourceException e) {
							logic.onActionFailed(e);
						}
					}
					break;
				default:
					// center the camera on the clicked player
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
			return new Rectangle(screenX, screenY + offset, statBarSize,
					statBarSize);
		}
	}

	private void actionDone() {
		EdurasInitializer.getInstance().getInformationProvider()
				.getClientData().setCurrentActionSelected(-1);
	}

	@Override
	public void render(Graphics g) {
	}

	@Override
	public void onGuiSizeChanged(int newWidth, int newHeight) {
		statBarSize = PLAYER_BAR_SIZE * GameRenderer.getRenderScale();
	}

	@Override
	public void onInteractModeChanged(SetInteractModeEvent setModeEvent) {
		super.onInteractModeChanged(setModeEvent);

		for (GUIPlayerBar guiPlayerBar : players) {
			userInterface.removeGuiElement(guiPlayerBar);
		}

		if (setModeEvent.getNewMode().equals(InteractMode.MODE_STRATEGY)) {
			try {
				int offset = (int) (30 * GameRenderer.getRenderScale());
				for (Player teamMate : getInfo().getPlayer().getTeam()
						.getPlayers()) {
					new GUIPlayerBar(teamMate, offset);
					offset += statBarSize + GAP_BETWEEN_PLAYERS
							* GameRenderer.getRenderScale();
				}
			} catch (PlayerHasNoTeamException | ObjectNotFoundException e) {
				L.log(Level.WARNING, "Cannot find player or his team!", e);
				return;
			}
		}
	}
}
