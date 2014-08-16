package de.illonis.eduras.mapeditor.gui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

import de.illonis.eduras.mapeditor.MapInteractor;
import de.illonis.eduras.mapeditor.MapInteractor.InteractType;

public class QuickMenuBar extends JPanel implements ActionListener {
	private final MapInteractor interactor;
	private JButton addBaseButton, addSpawnButton, importShapeButton;

	public QuickMenuBar(MapInteractor interactor) {
		super();
		this.interactor = interactor;
		// setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		setPreferredSize(new Dimension(100, 50));
		addBaseButton = new JButton("Add base");
		addBaseButton.addActionListener(this);
		addSpawnButton = new JButton("Add spawnarea");
		addSpawnButton.addActionListener(this);
		importShapeButton = new JButton("Import shape");
		importShapeButton.addActionListener(this);
		add(addBaseButton);
		add(addSpawnButton);
		add(importShapeButton);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JButton button = (JButton) e.getSource();
		if (button == addBaseButton) {
			interactor.setInteractType(InteractType.PLACE_BASE);
		} else if (button == addSpawnButton) {
			interactor.setInteractType(InteractType.PLACE_SPAWN);
		} else if (button == importShapeButton) {
			// TODO: Show import dialog.
		}
	}

}
