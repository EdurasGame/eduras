package de.illonis.eduras.mapeditor.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JPanel;

import de.illonis.eduras.mapeditor.StatusListener;

/**
 * Displays a line of text at the bottom.
 * 
 * @author illonis
 * 
 */
public class StatusBar extends JPanel implements StatusListener {

	private static final long serialVersionUID = 1L;
	private final JLabel label;

	StatusBar() {
		super(new BorderLayout());
		setPreferredSize(new Dimension(10, 30));
		label = new JLabel();
		add(label);
	}

	@Override
	public void setStatus(String text) {
		label.setText(text);
	}

	@Override
	public void setCoordinate(float x, float y) {
		label.setText(String.format("Mouse at %.0f, %.0f", x, y));
	}
}
