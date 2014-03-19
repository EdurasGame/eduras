package de.illonis.eduras.shapecreator.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;
import javax.swing.KeyStroke;
import javax.swing.filechooser.FileNameExtensionFilter;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.shapecreator.MenuActionReactor.Axis;
import de.illonis.eduras.shapecreator.MenuTriggerer;
import de.illonis.eduras.shapecreator.PanelInteractor.InteractMode;
import de.illonis.eduras.shapecreator.ShapeFiler;
import de.illonis.eduras.shapecreator.templates.TemplateNotFoundException;

/**
 * Represents the top menu and handles direct actions on that.
 * 
 * @author illonis
 * 
 */
public class MenuPanel extends JMenuBar implements ActionListener {

	private final static Logger L = EduLog.getLoggerFor(MenuPanel.class
			.getName());

	private static final long serialVersionUID = 1L;
	private final MenuTriggerer triggerer;
	private final TemplateSelector selector;
	private final RotationSelector rotationSelector;
	private final JFileChooser fileChooser;
	private final JFrame frame;

	private JMenuItem openItem, saveItem, newEmptyItem, newTemplateItem,
			undoItem, redoItem, exitItem, rotateItem, mirrorXItem, mirrorYItem,
			resetViewItem, zoomDefaultItem, zoomTwoItem, zoomThreeItem,
			zoomFourItem, zoomFiveItem, zoomHalfItem, zoomCustomItem,
			zoomIncreaseItem, zoomDecreaseItem, preferencesItem,
			modeSelectItem, modeAddItem, modeDeleteItem, modeDragItem,
			modeScaleItem, collisionCheckItem, validateItem;

	/**
	 * Creates a new menu panel.
	 * 
	 * @param triggerer
	 *            the triggerer that handles menu actions.
	 * @param frame
	 *            the parent frame.
	 */
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
		rotationSelector = new RotationSelector(frame);
		this.frame = frame;
		buildGui();
	}

	private void buildGui() {
		JMenu fileMenu = new JMenu("File");
		fileMenu.setMnemonic(KeyEvent.VK_F);
		add(fileMenu);
		JMenu editMenu = new JMenu("Edit");
		editMenu.setMnemonic(KeyEvent.VK_E);
		add(editMenu);
		JMenu transformMenu = new JMenu("Transform");
		transformMenu.setMnemonic(KeyEvent.VK_S);
		add(transformMenu);
		JMenu modeMenu = new JMenu("Mode");
		modeMenu.setMnemonic(KeyEvent.VK_M);
		add(modeMenu);
		JMenu testMenu = new JMenu("Test");
		testMenu.setMnemonic(KeyEvent.VK_T);
		add(testMenu);
		JMenu viewMenu = new JMenu("View");
		viewMenu.setMnemonic(KeyEvent.VK_V);
		add(viewMenu);

		JMenu newItem = new JMenu("New");
		fileMenu.add(newItem);
		newEmptyItem = addItemToMenu("Empty shape", KeyEvent.VK_N,
				ActionEvent.CTRL_MASK, newItem);
		fileMenu.add(new JSeparator());
		newTemplateItem = addItemToMenu("From template...", KeyEvent.VK_N,
				ActionEvent.CTRL_MASK + ActionEvent.SHIFT_MASK, newItem);
		openItem = addItemToMenu("Open...", KeyEvent.VK_O,
				ActionEvent.CTRL_MASK, fileMenu);

		saveItem = addItemToMenu("Save...", KeyEvent.VK_S,
				ActionEvent.CTRL_MASK, fileMenu);

		fileMenu.add(new JSeparator());

		exitItem = addItemToMenu("Exit", KeyEvent.VK_Q, ActionEvent.CTRL_MASK,
				fileMenu);

		undoItem = addItemToMenu("Undo", KeyEvent.VK_Z, ActionEvent.CTRL_MASK,
				editMenu);
		undoItem.setEnabled(false);
		redoItem = addItemToMenu("Redo", KeyEvent.VK_Z, ActionEvent.CTRL_MASK
				+ ActionEvent.SHIFT_MASK, editMenu);
		redoItem.setEnabled(false);

		editMenu.add(new JSeparator());
		preferencesItem = addItemToMenu("Preferences...", editMenu);
		preferencesItem.setEnabled(false);

		rotateItem = addItemToMenu("Rotate shape...", transformMenu);
		rotateItem.setEnabled(false);
		JMenu mirrorMenu = new JMenu("Mirror shape");
		transformMenu.add(mirrorMenu);

		modeSelectItem = addItemToMenu("Select and Drag", KeyEvent.VK_S, 0,
				modeMenu);
		modeDragItem = addItemToMenu("Drag shape", modeMenu);
		modeScaleItem = addItemToMenu("Scale shape", modeMenu);
		modeAddItem = addItemToMenu("Add vertices", KeyEvent.VK_A, 0, modeMenu);
		modeDeleteItem = addItemToMenu("Delete vertices", KeyEvent.VK_D, 0,
				modeMenu);

		mirrorXItem = addItemToMenu("X-axis (vertical)", mirrorMenu);
		mirrorYItem = addItemToMenu("Y-axis (horizontal)", mirrorMenu);

		JMenu zoomMenu = new JMenu("Zoom");
		viewMenu.add(zoomMenu);

		viewMenu.add(new JSeparator());

		resetViewItem = addItemToMenu("Reset view", viewMenu);
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
		zoomIncreaseItem = addItemToMenu("Increase (+0.5)", KeyEvent.VK_PLUS,
				ActionEvent.CTRL_MASK, zoomMenu);
		zoomDecreaseItem = addItemToMenu("Decrease (-0.5)", KeyEvent.VK_MINUS,
				ActionEvent.CTRL_MASK, zoomMenu);
		zoomMenu.add(new JSeparator());
		zoomCustomItem = addItemToMenu("Custom...", KeyEvent.VK_Z, 0, zoomMenu);

		collisionCheckItem = addItemToMenu("Collision", KeyEvent.VK_C, 0,
				testMenu);
		collisionCheckItem.setEnabled(false);
		testMenu.add(new JSeparator());
		validateItem = addItemToMenu("Validate", KeyEvent.VK_V, 0, testMenu);
		validateItem.setEnabled(false);
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

	private void openShape() {
		int result = fileChooser.showDialog(frame, "Open");
		if (result == JFileChooser.APPROVE_OPTION) {
			File file = fileChooser.getSelectedFile();
			if (!file.getAbsolutePath().endsWith("." + ShapeFiler.FILE_EXT))
				file = new File(file.getAbsolutePath() + "."
						+ ShapeFiler.FILE_EXT);
			if (file.exists())
				triggerer.openShape(file);
			else
				JOptionPane.showMessageDialog(frame,
						"File not found: " + file.getAbsolutePath());
		}
	}

	private void saveShape() {
		int result = fileChooser.showDialog(frame, "Save");
		if (result == JFileChooser.APPROVE_OPTION) {
			File file = fileChooser.getSelectedFile();
			if (!file.getAbsolutePath().endsWith("." + ShapeFiler.FILE_EXT))
				file = new File(file.getAbsolutePath() + "."
						+ ShapeFiler.FILE_EXT);
			if (file.exists()) {
				int owResult = JOptionPane.showConfirmDialog(frame, "The file "
						+ file.getAbsolutePath()
						+ " already exists. Do you want to overwrite?",
						"File exists", JOptionPane.YES_NO_OPTION);
				if (owResult == JOptionPane.NO_OPTION)
					return;
			}
			triggerer.saveShape(file);
		}
	}

	private void openTemplate() {

		selector.showFrame();
		if (selector.getAnswer()) {
			try {
				triggerer.newShape(selector.getSelectedTemplate());
			} catch (TemplateNotFoundException e) {
				JOptionPane.showMessageDialog(frame,
						"Template not found: " + e.getMessage());
				L.log(Level.SEVERE, "Cannot find template.", e);
			}
		}
		selector.dispose();
	}

	private void showPreferences() {
		// TODO implement
	}

	private void rotateShape() {
		rotationSelector.showFrame();
		if (rotationSelector.isAccepted()) {
			int value = rotationSelector.getValue();
			triggerer.rotateShape(value);
		}
		rotationSelector.dispose();
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
		else if (source == openItem)
			openShape();
		else if (source == saveItem)
			saveShape();
		else if (source == newTemplateItem)
			openTemplate();
		else if (source == resetViewItem)
			triggerer.resetPanel();
		else if (source == rotateItem)
			rotateShape();
		else if (source == mirrorXItem)
			triggerer.mirrorShape(Axis.HORIZONTAL);
		else if (source == mirrorYItem)
			triggerer.mirrorShape(Axis.VERTICAL);
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

			triggerer.modZoom(-0.5f);
		else if (source == zoomIncreaseItem)
			triggerer.modZoom(+0.5f);
		else if (source == preferencesItem)
			showPreferences();
		else if (source == modeAddItem)
			triggerer.setMode(InteractMode.ADD_VERT);
		else if (source == modeSelectItem)
			triggerer.setMode(InteractMode.NONE);
		else if (source == modeDeleteItem)
			triggerer.setMode(InteractMode.REM_VERT);
		else if (source == modeDragItem)
			triggerer.setMode(InteractMode.DRAG_SHAPE);
		else if (source == modeScaleItem)
			triggerer.setMode(InteractMode.SCALE_SHAPE);
		else if (source == validateItem)
			System.out.println("validate");
		else if (source == collisionCheckItem)
			System.out.println("collision");
	}

	private void customZoom() {
		String value = JOptionPane.showInputDialog(frame,
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
			JOptionPane
					.showMessageDialog(frame, "Invalid zoom value: " + value);
		}
	}
}
