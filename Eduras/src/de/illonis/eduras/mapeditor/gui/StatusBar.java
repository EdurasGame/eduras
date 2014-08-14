package de.illonis.eduras.mapeditor.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class StatusBar extends JPanel {

	private final JLabel label;

	public StatusBar() {
		super(new BorderLayout());
		setPreferredSize(new Dimension(10, 30));
		label = new JLabel();
		add(label);
	}

	public void setText(String text) {
		label.setText(text);
	}
}
