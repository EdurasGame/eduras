package de.illonis.eduras.mapeditor.gui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

import de.illonis.eduras.mapeditor.MapInteractor;
import de.illonis.eduras.mapeditor.MapInteractor.InteractType;

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
	private JButton importShapeButton;
	private ObjectPlacingSelectionPanel listing;

	private final List<JToggleButton> buttons = new LinkedList<JToggleButton>();

	ToolMenuBar(MapInteractor interactor) {
		super();
		this.interactor = interactor;
		// setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		setPreferredSize(new Dimension(100, 50));
		addBaseButton = new JToggleButton("Add base");
		addBaseButton.addActionListener(this);
		addSpawnButton = new JToggleButton("Add spawnarea");
		addSpawnButton.addActionListener(this);
		importShapeButton = new JButton("Import shape");
		importShapeButton.addActionListener(this);
		buttons.add(addBaseButton);
		buttons.add(addSpawnButton);
		add(addBaseButton);
		add(addSpawnButton);
		add(importShapeButton);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		AbstractButton button = (AbstractButton) e.getSource();
		if (e.getSource() instanceof JToggleButton) {
			JToggleButton toggle = (JToggleButton) button;
			if (!toggle.isSelected()) {
				return;
			}
		}
		if (button == addBaseButton) {
			deselectOthers(addBaseButton);
			interactor.setInteractType(InteractType.PLACE_BASE);
		} else if (button == addSpawnButton) {
			deselectOthers(addSpawnButton);
			addBaseButton.setSelected(false);
			interactor.setInteractType(InteractType.PLACE_SPAWN);
		} else if (button == importShapeButton) {
			deselectAll();
			// TODO: Show import dialog.
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
