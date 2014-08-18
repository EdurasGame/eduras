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

import de.illonis.eduras.mapeditor.MapData;
import de.illonis.eduras.mapeditor.MapInteractor;
import de.illonis.eduras.mapeditor.MapInteractor.InteractType;
import de.illonis.eduras.mapeditor.MapSaver;
import de.illonis.eduras.mapeditor.gui.dialog.ControlsInfo;
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

	private JMenuItem mapProperties, newMap, saveMap, loadMap, shapeCreator,
			validate, controls, showNodeConnections;

	EditorMenu(MapInteractor interactor) {
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
		validate = addItem("Validate", KeyEvent.VK_V, KeyEvent.VK_F5, 0);
		map.addSeparator();
		map.add(validate);
		add(map);
		JMenu edit = new JMenu("Edit");
		edit.setMnemonic(KeyEvent.VK_E);
		add(edit);

		JMenu view = new JMenu("View");
		showNodeConnections = new JCheckBoxMenuItem("Show node connections");
		showNodeConnections.setMnemonic(KeyEvent.VK_N);
		showNodeConnections.setAccelerator(KeyStroke.getKeyStroke(
				KeyEvent.VK_F8, 0));
		showNodeConnections.addActionListener(this);

		view.add(showNodeConnections);
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
		add(help);
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
		} else if (item == controls) {
			ControlsInfo info = new ControlsInfo();
			info.setVisible(true);
		} else if (item == validate) {
			ValidateDialog dialog = new ValidateDialog();
			dialog.setVisible(true);
		} else if (item == showNodeConnections) {
			JCheckBoxMenuItem checkItem = (JCheckBoxMenuItem) item;
			MapData.getInstance()
					.setShowNodeConnections(checkItem.isSelected());
		} else if (item == loadMap) {
			int result = mapChooser.showDialog(this, "Load");
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
					} catch (InvalidDataException | IOException ex) {
						JOptionPane.showMessageDialog(this,
								"Could not load mapfile: " + ex.getMessage());
						ex.printStackTrace();
					}
				}
			}
		} else if (item == saveMap) {
			int result = mapChooser.showDialog(this, "Save");
			if (result == JFileChooser.APPROVE_OPTION) {
				File file = mapChooser.getSelectedFile();
				if (!file.getAbsolutePath().endsWith(MapParser.FILE_EXTENSION))
					file = new File(file.getAbsolutePath()
							+ MapParser.FILE_EXTENSION);
				if (file.exists()) {

				}

				MapSaver saver;
				try {
					saver = new MapSaver(file);
					saver.save();
				} catch (IOException ex) {
					JOptionPane.showMessageDialog(this, "Could not save map: "
							+ ex.getMessage());
					ex.printStackTrace();
				}
			}
		}
	}
}
