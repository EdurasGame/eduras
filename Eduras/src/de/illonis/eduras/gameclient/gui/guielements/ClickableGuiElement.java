package de.illonis.eduras.gameclient.gui.guielements;

import de.illonis.eduras.gameclient.gui.GuiClickReactor;
import de.illonis.eduras.gameclient.gui.UserInterface;

/**
 * A gui element that is clickable. All clickable elements can trigger events on
 * the userinterface's {@link GuiClickReactor}.
 * 
 * @author illonis
 * 
 */
public abstract class ClickableGuiElement extends RenderedGuiObject implements
		ClickableGuiElementInterface {

	/**
	 * Creates a clickable gui element that triggers clicks on given gui.
	 * 
	 * @param gui
	 *            user interface.
	 */
	protected ClickableGuiElement(UserInterface gui) {
		super(gui);
		getClickReactor().addClickableGuiElement(this);
	}
}