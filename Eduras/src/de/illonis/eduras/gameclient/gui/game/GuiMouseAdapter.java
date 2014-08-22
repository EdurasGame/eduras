package de.illonis.eduras.gameclient.gui.game;

import org.newdawn.slick.Input;
import org.newdawn.slick.MouseListener;

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
public abstract class GuiMouseAdapter implements GuiClickReactor, MouseListener {
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

	@Override
	public void exitRequested() {
		panelLogic.onGameQuit();
	}

	@Override
	public final void inputEnded() {
	}

	@Override
	public final void inputStarted() {
	}

	@Override
	public final boolean isAcceptingInput() {
		return true;
	}

	@Override
	public final void setInput(Input input) {
	}
}
