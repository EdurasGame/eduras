package de.illonis.eduras.gameclient.gui.guielements;

import java.awt.Graphics2D;

import de.illonis.eduras.gameclient.gui.ImageList;
import de.illonis.eduras.logicabstraction.InformationProvider;

/**
 * An element that is part of the gui and is shown to end user (that means it is
 * rendered).
 * 
 * @author illonis
 * 
 */
public abstract class RenderedGuiObject {

	private InformationProvider info;
	protected int screenX, screenY;

	/**
	 * Creates a new {@link RenderedGuiObject} using given game information.
	 * 
	 * @param info
	 *            game information to use.
	 */
	protected RenderedGuiObject(InformationProvider info) {
		this.info = info;
		screenX = screenY = 0;
	}

	/**
	 * Retrieves current game information.
	 * 
	 * @return game information.
	 */
	protected InformationProvider getInfo() {
		return info;
	}

	/**
	 * Renders this gui object on given graphics.
	 * 
	 * @param g2d
	 *            graphic target.
	 * @param imageList
	 *            image list, that can be accessed to.
	 */
	public abstract void render(Graphics2D g2d, ImageList imageList);

	/**
	 * Indicates that gui has changed and interface positions have to be
	 * recalculated.
	 * 
	 * @param newWidth
	 *            new gui width.
	 * @param newHeight
	 *            new gui height.
	 */
	public abstract void onGuiSizeChanged(int newWidth, int newHeight);

	public abstract void onPlayerInformationReceived();

}
