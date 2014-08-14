package de.illonis.eduras.mapeditor.gui;

import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class EditorMenu extends JMenuBar {

	public EditorMenu() {
		JMenuItem main = new JMenuItem("Map");
		add(main);
		JMenuItem edit = new JMenuItem("Edit");
		add(edit);
	}
}
