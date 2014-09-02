package de.illonis.eduras.gameclient.gui.hud;

import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
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
import de.illonis.eduras.gameclient.datacache.FontCache;
import de.illonis.eduras.gameclient.datacache.FontCache.FontKey;
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

	private final static int PLAYER_BAR_WIDTH = 60;
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
		statBarSize = PLAYER_BAR_WIDTH * GameRenderer.getRenderScale();
	}

	class GUIPlayerBar extends ClickableGuiElement {

		private int health;
		private int maxHealth;
		private int barWidth;
		private int index;
		private Player player;
		private float yPosition;
		private Rectangle bounds;

		public GUIPlayerBar(Player player, int index) {
			super(userInterface);
			this.player = player;
			health = maxHealth = 10;
			screenX = 5;
			this.index = index;
			bounds = new Rectangle(0, 0, 10, 10);
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
			Font font = FontCache.getFont(FontKey.DEFAULT_FONT, g2d);
			g2d.setLineWidth(1f);
			yPosition = screenY
					+ 50
					+ index
					* (font.getLineHeight() * 2 + GAP_BETWEEN_PLAYERS
							* GameRenderer.getRenderScale());
			bounds = new Rectangle(screenX, yPosition, statBarSize,
					font.getLineHeight() * 2);
			// fill black
			g2d.setColor(COLOR_BG);
			g2d.fill(bounds);
			// draw frame
			g2d.setColor(Color.white);
			g2d.draw(bounds);

			font.drawString(screenX + 5, yPosition, player.getName(),
					Color.white);
			// g2d.setColor(COLOR_BG);
			// g2d.fillRect(screenX, yPosition + statBarSize / 3, statBarSize,
			// statBarSize - statBarSize / 3);
			g2d.setColor(COLOR_BAR);
			g2d.fillRect(screenX, yPosition + font.getLineHeight(), barWidth,
					font.getLineHeight());
			font.drawString(screenX + 5, yPosition + font.getLineHeight(),
					health + " / " + maxHealth, COLOR_TEXT);
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
		public boolean mouseMoved(int oldx, int oldy, int newx, int newy) {
			return true;
		}

		@Override
		public boolean mouseReleased(int button, int x, int y) {
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
						L.log(Level.WARNING,
								"Could not find player selected in group frame.",
								e);
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
			return bounds;
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
		statBarSize = PLAYER_BAR_WIDTH * GameRenderer.getRenderScale();
	}

	@Override
	public void onInteractModeChanged(SetInteractModeEvent setModeEvent) {
		super.onInteractModeChanged(setModeEvent);

		if (setModeEvent.getOwner() != getInfo().getOwnerID()) {
			return;
		}

		for (GUIPlayerBar guiPlayerBar : players) {
			userInterface.removeGuiElement(guiPlayerBar);
			guiPlayerBar.getMouseHandler().removeClickableGuiElement(
					guiPlayerBar);
		}

		if (setModeEvent.getNewMode().equals(InteractMode.MODE_STRATEGY)) {
			try {
				int index = 0;
				for (Player teamMate : getInfo().getPlayer().getTeam()
						.getPlayers()) {

					players.add(new GUIPlayerBar(teamMate, index));
					index++;
				}
			} catch (PlayerHasNoTeamException | ObjectNotFoundException e) {
				L.log(Level.WARNING, "Cannot find player or his team!", e);
				return;
			}
		}
	}
}
