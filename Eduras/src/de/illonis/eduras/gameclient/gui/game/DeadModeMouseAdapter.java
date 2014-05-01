package de.illonis.eduras.gameclient.gui.game;

import java.util.logging.Logger;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.gameclient.GuiInternalEventListener;

public class DeadModeMouseAdapter extends GuiMouseAdapter {

	private final static Logger L = EduLog
			.getLoggerFor(DeadModeMouseAdapter.class.getName());

	DeadModeMouseAdapter(GamePanelLogic panelLogic,
			GuiInternalEventListener listener) {
		super(panelLogic, listener);
	}

	@Override
	public void itemClicked(int slot) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseClicked(int button, int x, int y, int clickCount) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseMoved(int oldx, int oldy, int newx, int newy) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseDragged(int oldx, int oldy, int newx, int newy) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(int button, int x, int y) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(int button, int x, int y) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseWheelMoved(int change) {
		// TODO Auto-generated method stub

	}
}
