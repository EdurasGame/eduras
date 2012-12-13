package de.illonis.eduras.gui.guielements;

import de.illonis.eduras.gameclient.gui.GuiClickReactor;
import de.illonis.eduras.logicabstraction.InformationProvider;

public abstract class ClickableGuiElement extends RenderedGuiObject implements
		ClickableGuiElementInterface {
	
	protected GuiClickReactor reactor;

	public ClickableGuiElement(GuiClickReactor reactor, InformationProvider info) {
		super(info);
		this.reactor = reactor;
	}
}
