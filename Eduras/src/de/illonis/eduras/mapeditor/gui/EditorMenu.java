package de.illonis.eduras.mapeditor.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import de.illonis.eduras.mapeditor.gui.dialog.MapPropertiesDialog;
import de.illonis.eduras.shapecreator.ShapeCreator;

/**
 * The menu bar for Eduras? map editor.
 * 
 * @author illonis
 * 
 */
public class EditorMenu extends JMenuBar implements ActionListener {
	
	private static final long serialVersionUID = 1L;
	
	private JMenuItem mapProperties, newMap, saveMap, loadMap, shapeCreator;

	EditorMenu() {
		JMenu file = new JMenu("File");
		file.setMnemonic(KeyEvent.VK_F);
		newMap = addItem("New...", KeyEvent.VK_N, KeyEvent.VK_N,
				ActionEvent.CTRL_MASK);
		loadMap = addItem("Load...", KeyEvent.VK_L, KeyEvent.VK_O,
				ActionEvent.CTRL_MASK);
		saveMap = addItem("Save...", KeyEvent.VK_S, KeyEvent.VK_S,
				ActionEvent.CTRL_MASK);
		file.add(newMap);
		file.add(loadMap);
		file.addSeparator();
		file.add(saveMap);
		add(file);
		JMenu map = new JMenu("Map");
		map.setMnemonic(KeyEvent.VK_M);
		mapProperties = addItem("Properties", KeyEvent.VK_P, KeyEvent.VK_M,
				ActionEvent.CTRL_MASK);
		map.add(mapProperties);
		add(map);
		JMenu edit = new JMenu("Edit");
		edit.setMnemonic(KeyEvent.VK_E);
		add(edit);
		JMenu tools = new JMenu("Tools");
		tools.setMnemonic(KeyEvent.VK_T);
		shapeCreator = addItem("ShapeCreator", KeyEvent.VK_S, KeyEvent.VK_E,
				ActionEvent.CTRL_MASK);
		tools.add(shapeCreator);
		add(tools);
	}

	private JMenuItem addItem(String text, int key, int accKey, int accMask) {
		JMenuItem menuItem = new JMenuItem(text, key);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(accKey, accMask));
		menuItem.addActionListener(this);
		return menuItem;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JMenuItem item = (JMenuItem) e.getSource();
		if (item == mapProperties) {
			MapPropertiesDialog dialog = new MapPropertiesDialog(null);
			dialog.setVisible(true);
		} else if (item == shapeCreator) {
			ShapeCreator.main(new String[] {});
		}
	}
}
