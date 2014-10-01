package de.illonis.eduras.gameclient.gui.game;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.newdawn.slick.geom.Vector2f;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.exceptions.ObjectNotFoundException;
import de.illonis.eduras.gameclient.GuiInternalEventListener;
import de.illonis.eduras.logicabstraction.EdurasInitializer;

/**
 * Abstract mouse adapter that enables the user to scroll over the map.
 * 
 * @author Florian 'Ren' Mai <florian.ren.mai@googlemail.com>
 * 
 */
public abstract class ScrollModeMouseAdapter extends GuiMouseAdapter {

	private final static Logger L = EduLog
			.getLoggerFor(ScrollModeMouseAdapter.class.getName());

	private final static int SCROLL_MOUSE_PADDING = 30;

	ScrollModeMouseAdapter(GamePanelLogic panelLogic,
			GuiInternalEventListener listener) {
		super(panelLogic, listener);
	}

	@Override
	public abstract void itemClicked(int slot);

	@Override
	public abstract void mouseClicked(int button, int x, int y, int clickCount);

	@Override
	public void mouseMoved(int oldx, int oldy, int newx, int newy) {
		Vector2f cameraMovement = getPanelLogic().getCamera()
				.getCameraMovement();
		// TODO: Make camera movement speed a user setting.

		// scroll camera when mouse is near window border
		// horizontal movement
		if (newx < SCROLL_MOUSE_PADDING)
			cameraMovement.x = -5;
		else if (newx > getPanelLogic().getGui().getWidth()
				- SCROLL_MOUSE_PADDING)
			cameraMovement.x = 5;
		else
			cameraMovement.x = 0;

		// vertical movement
		if (newy < SCROLL_MOUSE_PADDING)
			cameraMovement.y = -5;
		else if (newy > getPanelLogic().getGui().getHeight()
				- SCROLL_MOUSE_PADDING)
			cameraMovement.y = 5;
		else
			cameraMovement.y = 0;
	}

	@Override
	public void mouseLost() {
		Vector2f cameraMovement = getPanelLogic().getCamera()
				.getCameraMovement();
		cameraMovement.x = 0;
		cameraMovement.y = 0;
	}

	@Override
	public void mapClicked(Vector2f gamePos) {
		try {
			Vector2f newPos = EdurasInitializer.getInstance()
					.getInformationProvider().getPlayer().getPlayerMainFigure()
					.getPositionVector().copy();
			gamePos.sub(newPos);

			getPanelLogic().getCamera().getCameraOffset()
					.set(gamePos.x, gamePos.y);
		} catch (ObjectNotFoundException e) {
			L.log(Level.WARNING, "Could not retrieve player location.", e);
		}
	}
}
