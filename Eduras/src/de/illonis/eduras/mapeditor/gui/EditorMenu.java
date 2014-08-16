package de.illonis.eduras.mapeditor.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.nio.MappedByteBuffer;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import de.illonis.eduras.mapeditor.gui.dialog.MapPropertiesDialog;

public class EditorMenu extends JMenuBar implements ActionListener {
	private JMenuItem mapProperties, newMap, saveMap, loadMap;

	public EditorMenu() {
		JMenu map = new JMenu("Map");
		map.setMnemonic(KeyEvent.VK_M);
		newMap = addItem("New...", KeyEvent.VK_N, KeyEvent.VK_N,
				ActionEvent.CTRL_MASK);
		loadMap = addItem("Load...", KeyEvent.VK_L, KeyEvent.VK_O,
				ActionEvent.CTRL_MASK);
		saveMap = addItem("Save...", KeyEvent.VK_S, KeyEvent.VK_S,
				ActionEvent.CTRL_MASK);
		mapProperties = addItem("Properties", KeyEvent.VK_P, KeyEvent.VK_E,
				ActionEvent.CTRL_MASK);
		map.add(newMap);
		map.add(loadMap);
		map.add(saveMap);
		map.addSeparator();
		map.add(mapProperties);
		add(map);
		JMenu edit = new JMenu("Edit");
		edit.setMnemonic(KeyEvent.VK_E);
		add(edit);
	}

	private JMenuItem addItem(String text, int key, int accKey, int accMask) {
		JMenuItem menuItem = new JMenuItem(text, key);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(accKey, accMask));
		menuItem.addActionListener(this);
		return menuItem;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == mapProperties) {
			MapPropertiesDialog dialog = new MapPropertiesDialog(null);
			dialog.setVisible(true);
		}
	}
}
