package de.illonis.eduras.gameclient.gui.game;

import java.util.logging.Logger;

import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Vector2f;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.gameclient.GuiInternalEventListener;
import de.illonis.eduras.units.InteractMode;

/**
 * The {@link GuiMouseAdapter} for a player in {@link InteractMode}_DEAD_MODE.
 * 
 * @author Florian 'Ren' Mai <florian.ren.mai@googlemail.com>
 * 
 */
public class SpectatorMouseAdapter extends ScrollModeMouseAdapter {

	private final static Logger L = EduLog
			.getLoggerFor(SpectatorMouseAdapter.class.getName());

	SpectatorMouseAdapter(GamePanelLogic panelLogic,
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
		if (button == Input.MOUSE_LEFT_BUTTON) {
			Vector2f clickGamePoint = getPanelLogic()
					.computeGuiPointToGameCoordinate(new Vector2f(x, y));
			getListener().selectOrDeselectAt(clickGamePoint,
					getPanelLogic().isKeyDown(Input.KEY_LSHIFT),
					getPanelLogic().isKeyDown(Input.KEY_LCONTROL));
		}
	}

	@Override
	public void mouseWheelMoved(int change) {
		// do nothing
	}
}
