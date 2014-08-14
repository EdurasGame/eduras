package de.illonis.eduras.mapeditor.gui;

import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

public class QuickMenuBar extends JPanel {

	public QuickMenuBar() {
		super();
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		setPreferredSize(new Dimension(100, 50));
	}

}
