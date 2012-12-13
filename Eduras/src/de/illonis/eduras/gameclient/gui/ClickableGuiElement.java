package de.illonis.eduras.gameclient.gui;

import de.illonis.eduras.gui.guielements.ClickableGuiElementInterface;
import de.illonis.eduras.gui.guielements.RenderedGuiObject;
import de.illonis.eduras.logicabstraction.InformationProvider;

public abstract class ClickableGuiElement extends RenderedGuiObject implements
		ClickableGuiElementInterface {
	
	protected GuiClickReactor reactor;

	public ClickableGuiElement(GuiClickReactor reactor, InformationProvider info) {
		super(info);
		this.reactor = reactor;
	}
}
