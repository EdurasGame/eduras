package de.illonis.eduras.mapeditor.gui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.LinkedList;
import java.util.List;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.newdawn.slick.geom.Vector2f;

import de.illonis.eduras.ObjectFactory.ObjectType;
import de.illonis.eduras.gameobjects.DynamicPolygonObject;
import de.illonis.eduras.mapeditor.MapData;
import de.illonis.eduras.mapeditor.MapInteractor;
import de.illonis.eduras.mapeditor.MapInteractor.InteractType;
import de.illonis.eduras.shapecreator.ShapeFiler;
import de.illonis.eduras.shapecreator.templates.RectangleTemplate;

/**
 * Toolbar with buttons for special actions.
 * 
 * @author illonis
 * 
 */
public class ToolMenuBar extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1L;
	private final MapInteractor interactor;
	private JToggleButton addBaseButton, addSpawnButton;
	private JButton importShapeButton, addShapeButton;
	private ObjectPlacingSelectionPanel listing;
	private final JFileChooser fileChooser;

	private final List<JToggleButton> buttons = new LinkedList<JToggleButton>();

	ToolMenuBar(MapInteractor interactor) {
		super();
		fileChooser = new JFileChooser();
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fileChooser.setMultiSelectionEnabled(false);
		FileNameExtensionFilter filter = new FileNameExtensionFilter(
				"Eduras? shape file (*." + ShapeFiler.FILE_EXT + ")",
				ShapeFiler.FILE_EXT);
		fileChooser.setFileFilter(filter);
		fileChooser.setAcceptAllFileFilterUsed(false);
		this.interactor = interactor;
		// setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		setPreferredSize(new Dimension(100, 50));
		addBaseButton = new JToggleButton("Add base");
		addBaseButton.addActionListener(this);
		addSpawnButton = new JToggleButton("Add spawnarea");
		addSpawnButton.addActionListener(this);
		addShapeButton = new JButton("Add DynamicPolygon");
		addShapeButton.addActionListener(this);
		importShapeButton = new JButton("Import shape");
		importShapeButton.addActionListener(this);
		buttons.add(addBaseButton);
		buttons.add(addSpawnButton);
		add(addBaseButton);
		add(addSpawnButton);
		add(addShapeButton);
		add(importShapeButton);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		AbstractButton button = (AbstractButton) e.getSource();
		if (e.getSource() instanceof JToggleButton) {
			JToggleButton toggle = (JToggleButton) button;
			if (!toggle.isSelected()) {
				interactor.setInteractType(InteractType.DEFAULT);
				return;
			}
		}
		if (button == addShapeButton) {
			deselectAll();
			List<Vector2f> vertices = new RectangleTemplate()
					.getDefaultVector2dfs();
			Vector2f[] verts = new Vector2f[vertices.size()];
			for (int i = 0; i < verts.length; i++) {
				verts[i] = vertices.get(i);
			}

			MapData.getInstance().setPlacingObject(
					new DynamicPolygonObject(ObjectType.DYNAMIC_POLYGON_BLOCK,
							null, null, -1, verts));
			interactor.setInteractType(InteractType.PLACE_SHAPE);
		} else if (button == addBaseButton) {
			deselectOthers(addBaseButton);
			interactor.setInteractType(InteractType.PLACE_BASE);
		} else if (button == addSpawnButton) {
			deselectOthers(addSpawnButton);
			addBaseButton.setSelected(false);
			interactor.setInteractType(InteractType.PLACE_SPAWN);
		} else if (button == importShapeButton) {
			deselectAll();
			int result = fileChooser.showDialog(this, "import");
			if (result == JFileChooser.APPROVE_OPTION) {
				File file = fileChooser.getSelectedFile();
				if (!file.getAbsolutePath().endsWith("." + ShapeFiler.FILE_EXT))
					file = new File(file.getAbsolutePath() + "."
							+ ShapeFiler.FILE_EXT);
				if (file.exists())
					interactor.importShape(file);
				else
					JOptionPane.showMessageDialog(this, "File not found: "
							+ file.getAbsolutePath());
			}
		}
	}

	void deselectOthers(JToggleButton me) {
		if (me != null)
			listing.deselect();
		for (JToggleButton button : buttons) {
			if (button == me)
				continue;
			button.setSelected(false);
		}
	}

	void deselectAll() {
		deselectOthers(null);
	}

	void setOther(ObjectPlacingSelectionPanel objectListing) {
		this.listing = objectListing;
	}

}
