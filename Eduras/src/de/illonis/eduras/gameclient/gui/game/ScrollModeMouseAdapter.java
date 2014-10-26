package de.illonis.eduras.gameclient.gui.game;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.newdawn.slick.geom.Vector2f;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.exceptions.ObjectNotFoundException;
import de.illonis.eduras.gameclient.GuiInternalEventListener;
import de.illonis.eduras.gameobjects.MoveableGameObject.Direction;

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

	ScrollModeMouseAdapter(UserInputListener panelLogic,
			GuiInternalEventListener listener) {
		super(panelLogic, listener);
	}

	@Override
	public abstract void itemClicked(int slot);

	@Override
	public abstract void mouseClicked(int button, int x, int y, int clickCount);

	@Override
	public void mouseMoved(int oldx, int oldy, int newx, int newy) {
		// scroll camera when mouse is near window border
		// horizontal movement
		if (newx < SCROLL_MOUSE_PADDING)
			getPanelLogic().startCameraMovement(Direction.LEFT, 0);
		else if (newx > getPanelLogic().getContainerWidth()
				- SCROLL_MOUSE_PADDING)
			getPanelLogic().startCameraMovement(Direction.RIGHT, 0);
		else {
			getPanelLogic().stopCameraMovement(Direction.RIGHT, 0);
			getPanelLogic().stopCameraMovement(Direction.LEFT, 0);
		}

		// vertical movement
		if (newy < SCROLL_MOUSE_PADDING)
			getPanelLogic().startCameraMovement(Direction.TOP, 0);
		else if (newy > getPanelLogic().getContainerHeight()
				- SCROLL_MOUSE_PADDING)
			getPanelLogic().startCameraMovement(Direction.BOTTOM, 0);
		else {
			getPanelLogic().stopCameraMovement(Direction.TOP, 0);
			getPanelLogic().stopCameraMovement(Direction.BOTTOM, 0);
		}
	}

	@Override
	public void mouseLost() {
		getPanelLogic().stopCameraMovement();
	}

	@Override
	public void mapClicked(Vector2f gamePos) {
		try {
			getPanelLogic().setCameraPosition(gamePos);
		} catch (ObjectNotFoundException e) {
			L.log(Level.WARNING,
					"Could not retrieve player location while clicking on map.",
					e);
		}
	}
}
