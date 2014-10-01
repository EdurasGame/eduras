package de.illonis.eduras.gameclient.gui.hud;

import java.util.logging.Level;
import java.util.logging.Logger;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.exceptions.ObjectNotFoundException;
import de.illonis.eduras.gameclient.gui.game.GuiClickReactor;

/**
 * A gui element that is clickable. All clickable elements can trigger events on
 * the userinterface's {@link GuiClickReactor}.
 * 
 * @author illonis
 * 
 */
public abstract class ClickableGuiElement extends RenderedGuiObject implements
		ClickableGuiElementInterface {

	private final static Logger L = EduLog
			.getLoggerFor(ClickableGuiElement.class.getName());

	/**
	 * Creates a clickable gui element that triggers clicks on given gui.
	 * 
	 * @param gui
	 *            user interface.
	 */
	protected ClickableGuiElement(UserInterface gui) {
		super(gui);
		getMouseHandler().addClickableGuiElement(this);
	}

	@Override
	public boolean mousePressed(int button, int x, int y) {
		return false;
	}

	@Override
	public boolean mouseReleased(int button, int x, int y) {
		return false;
	}

	@Override
	public boolean mouseMoved(int oldx, int oldy, int newx, int newy) {
		return false;
	}

	@Override
	public boolean mouseClicked(int button, int x, int y, int clickCount) {
		return false;
	}

	@Override
	public boolean mouseWheelMoved(int change) {
		return false;
	}

	@Override
	public boolean mouseDragged(int oldx, int oldy, int newx, int newy) {
		return false;
	}

	@Override
	public final boolean isActive() {
		try {
			return isEnabledInInteractMode(getInfo().getPlayer()
					.getCurrentMode()) && isVisible();
		} catch (ObjectNotFoundException e) {
			L.log(Level.SEVERE, "Player not found here!!", e);
		}
		return false;
	}
}
