package de.illonis.eduras.gameclient.gui.hud;

import java.util.logging.Logger;

import org.newdawn.slick.Color;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.gamemodes.GameMode;
import de.illonis.eduras.gamemodes.GameMode.GameModeNumber;

public class RespawnTimeFrame extends TimeFrame {

	private final static Logger L = EduLog.getLoggerFor(RespawnTimeFrame.class
			.getName());

	public RespawnTimeFrame(UserInterface gui) {
		super(gui, Color.yellow, 20f);
	}

	@Override
	protected long getTimeToDisplay() {
		return getInfo().getRespawnTime();
	}

	@Override
	public boolean isEnabledInGameMode(GameMode gameMode) {
		if (gameMode.getNumber() == GameModeNumber.EDURA) {
			return true;
		} else {
			return false;
		}
	}
}
