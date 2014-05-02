package de.illonis.eduras.gameclient.gui.game;

import de.illonis.eduras.gameclient.GamePanelReactor;
import de.illonis.eduras.gameclient.GuiInternalEventListener;
import de.illonis.eduras.gameclient.gui.hud.ClickableGuiElementInterface;

/**
 * A mouse adapter that can listen to mouse events and performs the appropriate
 * action.
 * 
 * @author illonis
 * 
 */
public abstract class GuiMouseAdapter implements GuiClickReactor {
	private final GamePanelLogic panelLogic;
	private final GuiInternalEventListener listener;

	GuiMouseAdapter(GamePanelLogic panelLogic, GuiInternalEventListener listener) {
		this.panelLogic = panelLogic;
		this.listener = listener;
	}

	GamePanelReactor getListener() {
		return listener;
	}

	GamePanelLogic getPanelLogic() {
		return panelLogic;
	}

	@Override
	public void addClickableGuiElement(ClickableGuiElementInterface elem) {
	}

	@Override
	public void removeClickableGuiElement(ClickableGuiElementInterface elem) {
	}

	public abstract void mouseClicked(int button, int x, int y, int clickCount);

	public abstract void mouseMoved(int oldx, int oldy, int newx, int newy);

	public abstract void mouseDragged(int oldx, int oldy, int newx, int newy);

	public abstract void mousePressed(int button, int x, int y);

	public abstract void mouseReleased(int button, int x, int y);

	public abstract void mouseWheelMoved(int change);

}
