package de.illonis.eduras.gameclient.gui.hud;

import java.util.logging.Logger;

import org.newdawn.slick.Color;

import de.illonis.edulog.EduLog;

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
}
