package de.illonis.eduras.gameclient.gui.hud;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;

import de.illonis.eduras.units.InteractMode;

/**
 * Displays the rectangle the user is dragging when selecting units in
 * buildmode.
 * 
 * @author illonis
 * 
 */
public class DragSelectionRectangle extends RenderedGuiObject {
	private float width, height;
	private boolean draw;
	private final float stroke = 1f;

	protected DragSelectionRectangle(UserInterface gui) {
		super(gui);
		setActiveInteractModes(InteractMode.MODE_STRATEGY,
				InteractMode.MODE_DEAD);
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
		screenX = rect.getX();
		screenY = rect.getY();
		width = rect.getWidth();
		height = rect.getHeight();
		draw = true;
	}

	/**
	 * Clears selection rectangle.
	 */
	public void clear() {
		draw = false;
	}

	@Override
	public void render(Graphics g) {
		if (!draw)
			return;
		g.setColor(Color.white);
		g.setLineWidth(stroke);
		g.drawRect(screenX, screenY, width, height);
	}

	@Override
	public void onGuiSizeChanged(int newWidth, int newHeight) {
	}
}
