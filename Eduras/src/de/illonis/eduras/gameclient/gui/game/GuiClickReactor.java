package de.illonis.eduras.gameclient.gui.game;

import org.newdawn.slick.geom.Vector2f;

import de.illonis.eduras.Team;
import de.illonis.eduras.gameclient.gui.hud.ClickableGuiElementInterface;

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

	/**
	 * Indicates a click on the minimap that correspondates a position ingame.
	 * 
	 * @param gamePos
	 *            the target location on map.
	 */
	void mapClicked(Vector2f gamePos);

	void exitRequested();

	void teamSelected(Team team);
}
