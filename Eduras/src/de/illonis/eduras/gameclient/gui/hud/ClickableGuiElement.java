package de.illonis.eduras.gameclient.gui.hud;

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
}
