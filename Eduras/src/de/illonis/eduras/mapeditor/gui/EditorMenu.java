package de.illonis.eduras.mapeditor.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.filechooser.FileNameExtensionFilter;

import de.illonis.eduras.mapeditor.EditorException;
import de.illonis.eduras.mapeditor.MapData;
import de.illonis.eduras.mapeditor.MapInteractor;
import de.illonis.eduras.mapeditor.MapInteractor.InteractType;
import de.illonis.eduras.mapeditor.MapSaver;
import de.illonis.eduras.mapeditor.gui.dialog.AboutDialog;
import de.illonis.eduras.mapeditor.gui.dialog.ControlsInfo;
import de.illonis.eduras.mapeditor.gui.dialog.ManageNodesDialog;
import de.illonis.eduras.mapeditor.gui.dialog.MapPropertiesDialog;
import de.illonis.eduras.mapeditor.gui.dialog.ValidateDialog;
import de.illonis.eduras.maps.Map;
import de.illonis.eduras.maps.persistence.InvalidDataException;
import de.illonis.eduras.maps.persistence.MapParser;
import de.illonis.eduras.shapecreator.ShapeCreator;

/**
 * The menu bar for Eduras? map editor.
 * 
 * @author illonis
 * 
 */
public class EditorMenu extends JMenuBar implements ActionListener {

	private static final long serialVersionUID = 1L;
	private final MapInteractor interactor;
	private final JFileChooser mapChooser;
	private final EditorWindow window;

	private JMenuItem mapProperties, newMap, saveMap, loadMap, shapeCreator,
			validate, controls, showNodeConnections, showPortalLinks, exit,
			manageNodeConnections, undo, redo, about;

	EditorMenu(MapInteractor interactor, EditorWindow editorWindow) {
		this.window = editorWindow;
		this.interactor = interactor;
		mapChooser = new JFileChooser();
		mapChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		mapChooser.setMultiSelectionEnabled(false);
		FileNameExtensionFilter filter = new FileNameExtensionFilter(
				"Eduras? map file (*" + MapParser.FILE_EXTENSION + ")",
				MapParser.FILE_EXTENSION.substring(1));
		mapChooser.setFileFilter(filter);
		mapChooser.setAcceptAllFileFilterUsed(false);
		JMenu file = new JMenu("File");
		file.setMnemonic(KeyEvent.VK_F);
		newMap = addItem("New...", KeyEvent.VK_N, KeyEvent.VK_N,
				ActionEvent.CTRL_MASK);
		loadMap = addItem("Load...", KeyEvent.VK_L, KeyEvent.VK_O,
				ActionEvent.CTRL_MASK);
		saveMap = addItem("Save as...", KeyEvent.VK_S, KeyEvent.VK_S,
				ActionEvent.CTRL_MASK);
		exit = addItem("Exit", KeyEvent.VK_X, KeyEvent.VK_F4,
				ActionEvent.ALT_MASK);
		file.add(newMap);
		file.add(loadMap);
		file.addSeparator();
		file.add(saveMap);
		file.addSeparator();
		file.add(exit);
		add(file);
		JMenu map = new JMenu("Map");
		map.setMnemonic(KeyEvent.VK_M);
		mapProperties = addItem("Properties", KeyEvent.VK_P, KeyEvent.VK_M,
				ActionEvent.CTRL_MASK);
		map.add(mapProperties);
		validate = addItem("Validate", KeyEvent.VK_V, KeyEvent.VK_F5, 0);
		map.addSeparator();
		map.add(validate);
		add(map);
		JMenu edit = new JMenu("Edit");
		edit.setMnemonic(KeyEvent.VK_E);
		manageNodeConnections = addItem("Node connections...", KeyEvent.VK_N,
				KeyEvent.VK_F3, 0);

		undo = addItem("Undo", KeyEvent.VK_U, KeyEvent.VK_Z,
				ActionEvent.CTRL_MASK);

		redo = addItem("Redo", KeyEvent.VK_R, KeyEvent.VK_Z,
				ActionEvent.CTRL_MASK | ActionEvent.SHIFT_MASK);

		edit.add(undo);
		edit.add(redo);
		undo.setEnabled(false);
		redo.setEnabled(false);
		edit.addSeparator();
		edit.add(manageNodeConnections);
		add(edit);

		JMenu view = new JMenu("View");
		showNodeConnections = new JCheckBoxMenuItem("Show node connections");
		showNodeConnections.setMnemonic(KeyEvent.VK_N);
		showNodeConnections.setAccelerator(KeyStroke.getKeyStroke(
				KeyEvent.VK_F8, 0));
		showNodeConnections.addActionListener(this);
		view.add(showNodeConnections);
		showPortalLinks = new JCheckBoxMenuItem("Show portal links");
		showPortalLinks.setMnemonic(KeyEvent.VK_P);
		showPortalLinks.setAccelerator(KeyStroke
				.getKeyStroke(KeyEvent.VK_F9, 0));
		showPortalLinks.addActionListener(this);
		view.add(showPortalLinks);
		add(view);

		JMenu tools = new JMenu("Tools");
		tools.setMnemonic(KeyEvent.VK_T);
		shapeCreator = addItem("ShapeCreator", KeyEvent.VK_S, KeyEvent.VK_E,
				ActionEvent.CTRL_MASK);
		tools.add(shapeCreator);
		add(tools);
		JMenu help = new JMenu("Help");
		help.setMnemonic(KeyEvent.VK_H);
		controls = addItem("Controls", KeyEvent.VK_C, KeyEvent.VK_F1, 0);
		help.add(controls);
		help.addSeparator();
		about = addItem("About", KeyEvent.VK_A, 0, 0);
		help.add(about);
		add(help);
	}

	private JMenuItem addItem(String text, int key, int accKey, int accMask) {
		JMenuItem menuItem = new JMenuItem(text, key);
		if (accKey != KeyEvent.VK_UNDEFINED) {
			menuItem.setAccelerator(KeyStroke.getKeyStroke(accKey, accMask));
		}
		menuItem.addActionListener(this);
		return menuItem;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JMenuItem item = (JMenuItem) e.getSource();
		if (item == newMap) {
			int result = JOptionPane.showConfirmDialog(window,
					"This will discard any unsaved changes to current map.",
					"New map", JOptionPane.OK_CANCEL_OPTION,
					JOptionPane.WARNING_MESSAGE);
			if (result == JOptionPane.OK_OPTION) {
				MapData.getInstance().reset();
				window.refreshTitle();
				interactor.setInteractType(InteractType.DEFAULT);
			}
		} else if (item == mapProperties) {
			MapPropertiesDialog dialog = new MapPropertiesDialog(window);
			dialog.setVisible(true);
		} else if (item == shapeCreator) {
			ShapeCreator.main(new String[] {});
		} else if (item == controls) {
			ControlsInfo info = new ControlsInfo(window);
			info.setVisible(true);
		} else if (item == validate) {
			ValidateDialog dialog = new ValidateDialog(window);
			dialog.setVisible(true);
		} else if (item == showNodeConnections) {
			JCheckBoxMenuItem checkItem = (JCheckBoxMenuItem) item;
			MapData.getInstance()
					.setShowNodeConnections(checkItem.isSelected());
		} else if (item == showPortalLinks) {
			JCheckBoxMenuItem checkItem = (JCheckBoxMenuItem) item;
			MapData.getInstance().setShowPortalLinks(checkItem.isSelected());
		} else if (item == loadMap) {
			if (!MapData.getInstance().getGameObjects().isEmpty()) {
				int result = JOptionPane
						.showConfirmDialog(
								window,
								"This will discard any unsaved changes to current map.",
								"Load map", JOptionPane.OK_CANCEL_OPTION,
								JOptionPane.WARNING_MESSAGE);
				if (result != JOptionPane.OK_OPTION)
					return;
			}
			int result = mapChooser.showDialog(window, "Load");
			if (result == JFileChooser.APPROVE_OPTION) {
				File file = mapChooser.getSelectedFile();
				if (!file.getAbsolutePath().endsWith(MapParser.FILE_EXTENSION))
					file = new File(file.getAbsolutePath()
							+ MapParser.FILE_EXTENSION);
				if (file.exists()) {
					Map map;
					try {
						map = MapParser.readMap(file.toURI().toURL());
						interactor.setInteractType(InteractType.DEFAULT);
						MapData.getInstance().importMap(map);
						interactor.onMapLoaded();
						window.refreshTitle();
					} catch (InvalidDataException | IOException ex) {
						JOptionPane.showMessageDialog(window,
								"Could not load mapfile: " + ex.getMessage());
						ex.printStackTrace();
					}
				}
			}
		} else if (item == saveMap) {
			int result = mapChooser.showDialog(window, "Save");
			if (result == JFileChooser.APPROVE_OPTION) {
				File file = mapChooser.getSelectedFile();
				if (!file.getAbsolutePath().endsWith(MapParser.FILE_EXTENSION))
					file = new File(file.getAbsolutePath()
							+ MapParser.FILE_EXTENSION);
				if (file.exists()) {
					int overwriteResult = JOptionPane.showConfirmDialog(window,
							"This will overwrite existing file.", "Save map",
							JOptionPane.OK_CANCEL_OPTION,
							JOptionPane.WARNING_MESSAGE);
					if (overwriteResult != JOptionPane.OK_OPTION)
						return;
				}

				MapSaver saver;
				try {
					saver = new MapSaver(file);
					saver.save();
				} catch (IOException ex) {
					JOptionPane.showMessageDialog(window,
							"Could not save map: " + ex.getMessage());
					ex.printStackTrace();
				}
			}
		} else if (item == manageNodeConnections) {
			try {
				new ManageNodesDialog(window).setVisible(true);
			} catch (EditorException ex) {
				JOptionPane.showMessageDialog(window, ex.getMessage(),
						"Node error", JOptionPane.ERROR_MESSAGE);
			}
		} else if (item == about) {
			new AboutDialog(window).setVisible(true);
		} else if (item == exit) {
			window.tryExit();
		}
	}
}
