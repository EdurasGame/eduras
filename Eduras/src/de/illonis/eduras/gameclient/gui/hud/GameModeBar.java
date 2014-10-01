package de.illonis.eduras.gameclient.gui.hud;

import java.util.logging.Logger;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.gameclient.datacache.FontCache;
import de.illonis.eduras.gameclient.datacache.FontCache.FontKey;
import de.illonis.eduras.gamemodes.GameMode;

/**
 * A simple status bar that shows the current game mode at top of screen.
 * 
 * @author illonis
 * 
 */
public class GameModeBar extends RenderedGuiObject {
	private final static Logger L = EduLog.getLoggerFor(GameModeBar.class
			.getName());

	private String mode;

	/**
	 * Creates the game stat bar.
	 * 
	 * @param gui
	 *            associated gui.
	 */
	public GameModeBar(UserInterface gui) {
		super(gui);
		setVisibleForSpectator(true);
		mode = "unknown game mode";
		mode = gui.getInfos().getGameMode().getName();
		screenX = 30;
		screenY = 0;
	}

	@Override
	public void render(Graphics g) {
		g.setColor(Color.white);
		FontCache.getFont(FontKey.DEFAULT_FONT, g).drawString(screenX + 10,
				screenY + 10, mode);
	}

	@Override
	public void onGuiSizeChanged(int newWidth, int newHeight) {
		screenX = newWidth / 2;
	}

	@Override
	public void onGameModeChanged(GameMode newMode) {
		L.info("gamemode bar to: " + newMode);

		mode = newMode.getName();
	}

}
