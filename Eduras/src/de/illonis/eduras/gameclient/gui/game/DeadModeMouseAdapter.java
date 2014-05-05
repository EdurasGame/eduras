package de.illonis.eduras.gameclient.gui.game;

import java.util.logging.Logger;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.gameclient.GuiInternalEventListener;

public class DeadModeMouseAdapter extends ScrollModeMouseAdapter {

	private final static Logger L = EduLog
			.getLoggerFor(DeadModeMouseAdapter.class.getName());

	DeadModeMouseAdapter(GamePanelLogic panelLogic,
			GuiInternalEventListener listener) {
		super(panelLogic, listener);
	}

	@Override
	public void itemClicked(int slot) {
		// do nothing
	}

	@Override
	public void mouseClicked(int button, int x, int y, int clickCount) {
		// do nothing
	}

	@Override
	public void mouseDragged(int oldx, int oldy, int newx, int newy) {
		// do nothing
	}

	@Override
	public void mousePressed(int button, int x, int y) {
		// do nothing
	}

	@Override
	public void mouseReleased(int button, int x, int y) {
		// do nothing
	}

	@Override
	public void mouseWheelMoved(int change) {
		// do nothing
	}
}
