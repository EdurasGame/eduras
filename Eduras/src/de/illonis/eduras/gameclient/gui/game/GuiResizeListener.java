package de.illonis.eduras.gameclient.gui.game;

/**
 * Does need notification when size of gui has changed.
 * 
 * @author illonis
 * 
 */
public interface GuiResizeListener {

	/**
	 * Notifies gui objects that game panel size has changed.
	 * 
	 * @param width
	 *            new width.
	 * @param height
	 *            new height.
	 */
	void onGuiSizeChanged(int width, int height);
}
