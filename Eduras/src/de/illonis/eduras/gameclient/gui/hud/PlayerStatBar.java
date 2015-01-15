package de.illonis.eduras.gameclient.gui.hud;

import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.newdawn.slick.Font;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Vector2f;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.Player;
import de.illonis.eduras.events.SetInteractModeEvent;
import de.illonis.eduras.exceptions.ActionFailedException;
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

	}

	class GUIPlayerBar extends PlayerDisplay {

		private final int index;

		public GUIPlayerBar(Player player, int index) {
			super(userInterface, player);
			this.index = index;
		}

		@Override
		public void render(Graphics g2d) {
			if (player == null) {
				L.severe("Dont have a player when printing its statbar");
				return;
			}
			Font font = FontCache.getFont(FontKey.DEFAULT_FONT, g2d);
			screenY = 50
					+ index
					* (font.getLineHeight() * 2 + GAP_BETWEEN_PLAYERS
							* GameRenderer.getRenderScale());
			super.render(g2d);
		}

		@Override
		public boolean init(Graphics g, int windowWidth, int windowHeight) {
			screenY = windowHeight - statBarSize;
			return true;
		}

		@Override
		public boolean mouseMoved(int oldx, int oldy, int newx, int newy) {
			return true;
		}

		@Override
		public boolean mousePressed(int button, int x, int y) {
			return true;
		}

		@Override
		public boolean mouseReleased(int button, int x, int y) {
			return true;
		}

		@Override
		public boolean mouseClicked(int button, int x, int y, int count) {
			GamePanelLogic logic = userInterface.getLogic();
			if (!player.isDead()) {
				PlayerMainFigure mainFigure = player.getPlayerMainFigure();
				switch (logic.getClickState()) {
				case SELECT_TARGET_FOR_SPELL:
					// select player for a spell
					if (button == Input.MOUSE_LEFT_BUTTON) {
						try {
							userInterface.getListener().onUnitSpell(mainFigure);
							actionDone();
							logic.setClickState(ClickState.DEFAULT);
						} catch (ActionFailedException e) {
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
				if (logic.getClickState() == ClickState.SELECT_PLAYER_FOR_REZZ) {
					if (button == Input.MOUSE_LEFT_BUTTON) {
						EdurasInitializer.getInstance()
								.getInformationProvider().getClientData()
								.setCurrentResurrectTarget(player);
						logic.setClickState(ClickState.SELECT_BASE_FOR_REZZ);
						return true;
					}
				}
				return false;
			}
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
	public boolean init(Graphics g, int windowWidth, int windowHeight) {
		return true;
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
