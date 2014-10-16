package de.illonis.eduras.gameclient.gui.game;

import org.newdawn.slick.geom.Vector2f;

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
	public void showItemTooltip(Vector2f p, Item data) {
		renderer.showItemTooltip(p, data);
	}

	@Override
	public void showTooltip(Vector2f p, String text) {
		renderer.showTooltip(p, text);
	}

	@Override
	public void hideTooltip() {
		renderer.hideTooltip();
	}

	@Override
	public void showTooltip(Vector2f p, String title, String description) {
		renderer.showTooltip(p, title, description);
	}

	@Override
	public void showTooltip(Vector2f p, String title, String description,
			int costs) {
		renderer.showTooltip(p, title, description, costs);
	}

}
