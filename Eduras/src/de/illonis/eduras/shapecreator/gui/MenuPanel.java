package de.illonis.eduras.shapecreator.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;
import javax.swing.KeyStroke;

public class MenuPanel extends JMenuBar implements ActionListener {

	private JMenuItem importItem, exportItem, newEmptyItem, newTemplateItem,
			undoItem, redoItem, exitItem, rotateItem, mirrorItem,
			resetViewItem, zoomDefaultItem, zoomTwoItem, zoomThreeItem,
			zoomFourItem, zoomFiveItem, zoomHalfItem, zoomCustomItem;

	public MenuPanel() {
		super();

		buildGui();
	}

	private void buildGui() {
		JMenu fileMenu = new JMenu("File");
		add(fileMenu);
		JMenu editMenu = new JMenu("Edit");
		add(editMenu);
		JMenu transformMenu = new JMenu("Transform");
		add(transformMenu);
		JMenu viewMenu = new JMenu("View");
		add(viewMenu);

		JMenu newItem = new JMenu("New");
		fileMenu.add(newItem);
		newEmptyItem = addItemToMenu("Empty shape", KeyEvent.VK_N,
				ActionEvent.CTRL_MASK, newItem);
		fileMenu.add(new JSeparator());
		newTemplateItem = addItemToMenu("from template...", KeyEvent.VK_N,
				ActionEvent.CTRL_MASK + ActionEvent.SHIFT_MASK, newItem);
		importItem = addItemToMenu("Import", KeyEvent.VK_I,
				ActionEvent.CTRL_MASK, fileMenu);

		exportItem = addItemToMenu("export", KeyEvent.VK_E,
				ActionEvent.CTRL_MASK, fileMenu);

		fileMenu.add(new JSeparator());

		exitItem = addItemToMenu("exit", KeyEvent.VK_Q, ActionEvent.CTRL_MASK,
				fileMenu);

		undoItem = addItemToMenu("undo", KeyEvent.VK_Z, ActionEvent.CTRL_MASK,
				editMenu);
		undoItem = addItemToMenu("redo", KeyEvent.VK_Z, ActionEvent.CTRL_MASK
				+ ActionEvent.SHIFT_MASK, editMenu);

		rotateItem = addItemToMenu("rotate shape...", KeyEvent.VK_R,
				ActionEvent.CTRL_MASK, transformMenu);
		mirrorItem = addItemToMenu("mirror shape...", KeyEvent.VK_M,
				ActionEvent.CTRL_MASK, transformMenu);

		JMenu zoomMenu = new JMenu("zoom");
		viewMenu.add(zoomMenu);

		viewMenu.add(new JSeparator());

		resetViewItem = addItemToMenu("reset view", viewMenu);
		zoomHalfItem = addItemToMenu("0.5x", zoomMenu);
		zoomDefaultItem = addItemToMenu("1x (default)", KeyEvent.VK_1,
				ActionEvent.SHIFT_MASK, zoomMenu);
		zoomTwoItem = addItemToMenu("2x", KeyEvent.VK_2,
				ActionEvent.SHIFT_MASK, zoomMenu);
		zoomThreeItem = addItemToMenu("3x", KeyEvent.VK_3,
				ActionEvent.SHIFT_MASK, zoomMenu);
		zoomFourItem = addItemToMenu("4x", KeyEvent.VK_4,
				ActionEvent.SHIFT_MASK, zoomMenu);
		zoomFiveItem = addItemToMenu("5x", KeyEvent.VK_5,
				ActionEvent.SHIFT_MASK, zoomMenu);
		zoomMenu.add(new JSeparator());
		zoomCustomItem = addItemToMenu("custom...", KeyEvent.VK_Z, 0, zoomMenu);
	}

	private JMenuItem addItemToMenu(String label, JMenu menu) {
		JMenuItem menuItem = new JMenuItem(label);
		menu.add(menuItem);
		menuItem.addActionListener(this);
		return menuItem;
	}

	private JMenuItem addItemToMenu(String label, int key, int mask, JMenu menu) {
		JMenuItem menuItem = addItemToMenu(label, menu);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(key, mask));
		return menuItem;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JMenuItem source = (JMenuItem) e.getSource();
		System.out.println("Menu item pressed: " + source.getText());
	}

}
