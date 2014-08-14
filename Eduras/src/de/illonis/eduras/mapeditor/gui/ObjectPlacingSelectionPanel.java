package de.illonis.eduras.mapeditor.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JPanel;

public class ObjectPlacingSelectionPanel extends JPanel {
	public ObjectPlacingSelectionPanel() {
		super(new BorderLayout());
		Dimension dimension = new Dimension(200, 100);
		setPreferredSize(dimension);
		setSize(200, 100);
	}

}
