package de.illonis.eduras.gameclient.gui;

import java.awt.Point;

import de.illonis.eduras.items.Item;

/**
 * Provides a bridge between user interface and game renderer.
 * 
 * @author illonis
 * 
 */
public class RendererTooltipHandler implements TooltipHandler {
	private GameRenderer renderer;

	/**
	 * Creates a new tooltip handler attached to given renderer.
	 * 
	 * @param renderer
	 *            attached renderer.
	 */
	public RendererTooltipHandler(GameRenderer renderer) {
		this.renderer = renderer;
	}

	@Override
	public void showItemTooltip(Point p, Item data) {
		renderer.showItemTooltip(p, data);
	}

	@Override
	public void showTooltip(Point p, String text) {
		renderer.showTooltip(p, text);
	}

	@Override
	public void hideTooltip() {
		renderer.hideTooltip();
	}

}
