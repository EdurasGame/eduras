package de.illonis.eduras.shapecreator.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;
import javax.swing.KeyStroke;
import javax.swing.filechooser.FileNameExtensionFilter;

import de.illonis.eduras.shapecreator.DataHolder;
import de.illonis.eduras.shapecreator.MenuActionReactor.Axis;
import de.illonis.eduras.shapecreator.MenuTriggerer;
import de.illonis.eduras.shapecreator.ShapeFiler;
import de.illonis.eduras.shapecreator.templates.TemplateNotFoundException;

public class MenuPanel extends JMenuBar implements ActionListener {

	private static final long serialVersionUID = 1L;
	private final MenuTriggerer triggerer;
	private final TemplateSelector selector;
	private final JFileChooser fileChooser;

	private JMenuItem importItem, exportItem, newEmptyItem, newTemplateItem,
			undoItem, redoItem, exitItem, rotateItem, mirrorItem,
			resetViewItem, zoomDefaultItem, zoomTwoItem, zoomThreeItem,
			zoomFourItem, zoomFiveItem, zoomHalfItem, zoomCustomItem,
			zoomIncreaseItem, zoomDecreaseItem;

	public MenuPanel(MenuTriggerer triggerer, JFrame frame) {
		super();
		fileChooser = new JFileChooser();
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fileChooser.setMultiSelectionEnabled(false);
		FileNameExtensionFilter filter = new FileNameExtensionFilter(
				"Eduras? shape file (*." + ShapeFiler.FILE_EXT + ")",
				ShapeFiler.FILE_EXT);
		fileChooser.setFileFilter(filter);
		fileChooser.setAcceptAllFileFilterUsed(false);
		this.triggerer = triggerer;
		selector = new TemplateSelector(frame);
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
		importItem = addItemToMenu("Import...", KeyEvent.VK_I,
				ActionEvent.CTRL_MASK, fileMenu);

		exportItem = addItemToMenu("Export...", KeyEvent.VK_E,
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
				ActionEvent.CTRL_MASK, zoomMenu);
		zoomTwoItem = addItemToMenu("2x", KeyEvent.VK_2, ActionEvent.CTRL_MASK,
				zoomMenu);
		zoomThreeItem = addItemToMenu("3x", KeyEvent.VK_3,
				ActionEvent.CTRL_MASK, zoomMenu);
		zoomFourItem = addItemToMenu("4x", KeyEvent.VK_4,
				ActionEvent.CTRL_MASK, zoomMenu);
		zoomFiveItem = addItemToMenu("5x", KeyEvent.VK_5,
				ActionEvent.CTRL_MASK, zoomMenu);
		zoomMenu.add(new JSeparator());
		zoomIncreaseItem = addItemToMenu("increase (+0.5)", KeyEvent.VK_PLUS,
				ActionEvent.CTRL_MASK, zoomMenu);
		zoomDecreaseItem = addItemToMenu("decrease (-0.5)", KeyEvent.VK_MINUS,
				ActionEvent.CTRL_MASK, zoomMenu);
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

	private void importShape() {
		int result = fileChooser.showDialog(this, "Import");
		if (result == JFileChooser.APPROVE_OPTION) {
			File file = fileChooser.getSelectedFile();
			if (file.exists())
				triggerer.importShape(file);
			else
				JOptionPane.showMessageDialog(this,
						"File not found: " + file.getAbsolutePath());
		}
	}

	private void exportShape() {
		fileChooser.setApproveButtonText("Export");
		int result = fileChooser.showDialog(this, "Export");
		if (result == JFileChooser.APPROVE_OPTION) {
			File file = fileChooser.getSelectedFile();
			try {
				if (file.createNewFile() || file.canWrite())
					triggerer.exportShape(file);
			} catch (IOException e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(
						this,
						"Could not open file for writing: "
								+ file.getAbsolutePath());
			}
		}
	}

	private void openTemplate() {

		selector.showFrame();
		if (selector.getAnswer()) {
			try {
				triggerer.newShape(selector.getSelectedTemplate());
			} catch (TemplateNotFoundException e) {
				JOptionPane.showMessageDialog(this,
						"Template not found: " + e.getMessage());
				e.printStackTrace();
			}
		}
		selector.dispose();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JMenuItem source = (JMenuItem) e.getSource();

		if (source == exitItem)
			triggerer.exit();
		else if (source == newEmptyItem)
			triggerer.newShape();
		else if (source == undoItem)
			triggerer.undo();
		else if (source == redoItem)
			triggerer.redo();
		else if (source == importItem)
			importShape();
		else if (source == exportItem)
			exportShape();
		else if (source == newTemplateItem)
			openTemplate();
		else if (source == resetViewItem)
			triggerer.resetPanel();
		else if (source == rotateItem)
			triggerer.rotateShape(90);
		else if (source == mirrorItem)
			triggerer.mirrorShape(Axis.HORIZONTAL);
		else if (source == zoomCustomItem)
			customZoom();
		else if (source == zoomDefaultItem)
			triggerer.setZoom(1.0f);
		else if (source == zoomDefaultItem)
			triggerer.setZoom(1.0f);
		else if (source == zoomTwoItem)
			triggerer.setZoom(2.0f);
		else if (source == zoomThreeItem)
			triggerer.setZoom(3.0f);
		else if (source == zoomFourItem)
			triggerer.setZoom(4.0f);
		else if (source == zoomFiveItem)
			triggerer.setZoom(5.0f);
		else if (source == zoomHalfItem)
			triggerer.setZoom(.5f);
		else if (source == zoomDecreaseItem)
			triggerer.setZoom(DataHolder.getInstance().getZoom() - 0.5f);
		else if (source == zoomIncreaseItem)
			triggerer.setZoom(DataHolder.getInstance().getZoom() + 0.5f);
	}

	private void customZoom() {
		String value = JOptionPane.showInputDialog(this,
				"<html>Please enter a new zoom level.<br>"
						+ "You can enter any value greater 0.1.<br> "
						+ "Examples: 1, 3.5, 5, 10.03</html>",
				"Change zoom level", JOptionPane.QUESTION_MESSAGE);
		if (value == null)
			return;
		try {
			float newZoom = Float.parseFloat(value);
			triggerer.setZoom(newZoom);
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(this, "Invalid zoom value: " + value);
		}
	}
}
