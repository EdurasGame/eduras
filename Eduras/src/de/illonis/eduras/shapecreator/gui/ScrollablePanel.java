package de.illonis.eduras.shapecreator.gui;

import java.awt.Dimension;
import java.awt.Rectangle;

import javax.swing.JPanel;
import javax.swing.Scrollable;
import javax.swing.SwingConstants;

/**
 * A scrollable panel that does allow to expand its width.
 * 
 * @author illonis
 * 
 */
public class ScrollablePanel extends JPanel implements Scrollable {

	private static final long serialVersionUID = 1L;

	@Override
	public Dimension getPreferredScrollableViewportSize() {
		return getPreferredSize();
	}

	@Override
	public int getScrollableUnitIncrement(Rectangle visibleRect,
			int orientation, int direction) {
		return 10;
	}

	@Override
	public int getScrollableBlockIncrement(Rectangle visibleRect,
			int orientation, int direction) {
		return ((orientation == SwingConstants.VERTICAL) ? visibleRect.height
				: visibleRect.width) - 10;
	}

	@Override
	public boolean getScrollableTracksViewportWidth() {
		return true;
	}

	@Override
	public boolean getScrollableTracksViewportHeight() {
		return false;
	}
}
