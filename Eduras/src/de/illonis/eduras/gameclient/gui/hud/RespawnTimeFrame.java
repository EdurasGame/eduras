package de.illonis.eduras.gameclient.gui.hud;

import java.util.logging.Logger;

import org.newdawn.slick.Color;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.gameclient.datacache.CacheException;
import de.illonis.eduras.gameclient.datacache.FontCache;
import de.illonis.eduras.gameclient.datacache.FontCache.FontKey;
import de.illonis.eduras.gamemodes.GameMode;
import de.illonis.eduras.gamemodes.GameMode.GameModeNumber;
import de.illonis.eduras.settings.S;

public class RespawnTimeFrame extends TimeFrame {

	private final static Logger L = EduLog.getLoggerFor(RespawnTimeFrame.class
			.getName());

	public RespawnTimeFrame(UserInterface gui) {
		super(gui, Color.yellow, 5f);
	}

	@Override
	protected long getTimeToDisplay() {
		return getInfo().getRespawnTime();
	}

	@Override
	public void onGameReady() {
		super.onGameReady();
		try {
			screenY = FontCache.getFont(FontKey.DEFAULT_FONT).getLineHeight();
		} catch (CacheException e) {
		}
	}

	@Override
	public boolean isEnabledInGameMode(GameMode gameMode) {
		if (gameMode.getNumber() == GameModeNumber.EDURA
				&& S.Server.gm_edura_automatic_respawn) {
			return true;
		} else {
			return false;
		}
	}
}
