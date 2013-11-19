package de.illonis.eduras.gameclient.gui.game;

import java.awt.event.MouseAdapter;

import de.illonis.eduras.gameclient.GuiInternalEventListener;
import de.illonis.eduras.gameclient.gui.hud.ClickableGuiElementInterface;

public abstract class GuiMouseAdapter extends MouseAdapter implements
		GuiClickReactor {
	private final GamePanelLogic panelLogic;
	private final GuiInternalEventListener listener;

	public GuiMouseAdapter(GamePanelLogic panelLogic,
			GuiInternalEventListener listener) {
		this.panelLogic = panelLogic;
		this.listener = listener;
		// TODO Auto-generated constructor stub
	}

	GuiInternalEventListener getListener() {
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

}
