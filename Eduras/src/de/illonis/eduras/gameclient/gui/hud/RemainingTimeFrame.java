package de.illonis.eduras.gameclient.gui.hud;

import java.util.logging.Logger;

import org.newdawn.slick.Color;

import de.illonis.edulog.EduLog;

public class RemainingTimeFrame extends TimeFrame {

	private final static Logger L = EduLog
			.getLoggerFor(RemainingTimeFrame.class.getName());

	public RemainingTimeFrame(UserInterface gui) {
		super(gui, Color.white, 5f);
	}

	@Override
	protected long getTimeToDisplay() {
		return getInfo().getRemainingTime();
	}
}
