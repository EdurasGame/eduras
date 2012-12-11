package de.illonis.eduras.gui.guielements;

public interface ClickableElement {
	/**
	 * Calculates rectangle that represents clickable area relatively to gui.
	 * 
	 * @return clickable area.
	 */
	java.awt.Rectangle getClickableRect();
}
