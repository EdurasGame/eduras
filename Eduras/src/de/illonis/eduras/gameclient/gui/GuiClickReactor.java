package de.illonis.eduras.gameclient.gui;

import de.illonis.eduras.gui.guielements.ClickableGuiElementInterface;

/**
 * Represents an object that reacts on clicks on gui elements. It provides
 * functionality to add and remove elements to listen on.
 * 
 * @author illonis
 * 
 */
public interface GuiClickReactor {

	/**
	 * Indicates that a specific item has been clicked.
	 * 
	 * @param slot
	 *            slot that was clicked.
	 */
	void itemClicked(int slot);

	/**
	 * Adds a clickable gui element to listener list.
	 * 
	 * @param elem
	 *            element to add.
	 */
	void addClickableGuiElement(ClickableGuiElementInterface elem);

	/**
	 * Removes a clickable gui element from listener list.
	 * 
	 * @param elem
	 *            element to remove.
	 */
	void removeClickableGuiElement(ClickableGuiElementInterface elem);
}
