package de.illonis.eduras.gameclient.gui.hud;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Stroke;

import de.illonis.eduras.units.PlayerMainFigure.InteractMode;

/**
 * Displays the rectangle the user is dragging when selecting units in
 * buildmode.
 * 
 * @author illonis
 * 
 */
public class DragSelectionRectangle extends RenderedGuiObject {
	private int width, height;
	private boolean draw;
	private final Stroke stroke = new BasicStroke(1f);

	protected DragSelectionRectangle(UserInterface gui) {
		super(gui);
		setActiveInteractModes(InteractMode.MODE_STRATEGY);
		setVisibleForSpectator(false);
		draw = false;
	}

	/**
	 * Sets the current selection rectangle.
	 * 
	 * @param rect
	 *            the rectangle in gui coordinates.
	 */
	public void setRectangle(Rectangle rect) {
		screenX = rect.x;
		screenY = rect.y;
		width = rect.width;
		height = rect.height;
		draw = true;
	}

	/**
	 * Clears selection rectangle.
	 */
	public void clear() {
		draw = false;
	}

	@Override
	public void render(Graphics2D g2d) {
		if (!draw)
			return;
		g2d.setColor(Color.WHITE);
		g2d.setStroke(stroke);
		g2d.drawRect(screenX, screenY, width, height);
	}

	@Override
	public void onGuiSizeChanged(int newWidth, int newHeight) {
	}

	@Override
	public void onPlayerInformationReceived() {
	}

}
