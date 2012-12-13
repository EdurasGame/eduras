package de.illonis.eduras.gui.guielements;

import de.illonis.eduras.gameclient.gui.GuiClickReactor;
import de.illonis.eduras.logicabstraction.InformationProvider;

/**
 * A gui element that is clickable. All clickable elements can trigger events on
 * their {@link GuiClickReactor}.
 * 
 * @author illonis
 * 
 */
public abstract class ClickableGuiElement extends RenderedGuiObject implements
		ClickableGuiElementInterface {

	protected GuiClickReactor reactor;

	protected ClickableGuiElement(GuiClickReactor reactor,
			InformationProvider info) {
		super(info);
		this.reactor = reactor;
	}
}
