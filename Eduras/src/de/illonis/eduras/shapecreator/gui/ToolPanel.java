package de.illonis.eduras.shapecreator.gui;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;

import de.illonis.eduras.images.ImageFiler;
import de.illonis.eduras.shapecreator.DataHolder;
import de.illonis.eduras.shapecreator.PanelInteractor;

public class ToolPanel extends JPanel {

	private final DataHolder data;
	private final PanelInteractor interactor;
	private JToolBar toolBar;
	private JToggleButton modeButtonDrag, modeButtonAdd, modeButtonRemove,
			modeButtonNone;
	private ButtonGroup bgroup;

	public ToolPanel(PanelInteractor panelInteractor) {
		this.interactor = panelInteractor;
		data = DataHolder.getInstance();
		buildToolbar();
	}

	private void buildToolbar() {
		toolBar = new JToolBar();
		bgroup = new ButtonGroup();
		modeButtonNone = createButton("no action", "");
		modeButtonDrag = createButton("drag edges", "");
		modeButtonAdd = createButton("add vertice", "");
		modeButtonRemove = createButton("remove vertice", "");

		add(toolBar);
	}

	private JToggleButton createButton(String label, String imgUrl) {
		JToggleButton b = new JToggleButton(label);
		ImageIcon icon = ImageFiler
				.loadIcon("gui/icons/shapecreator/" + imgUrl);
		b.setIcon(icon);
		bgroup.add(b);
		toolBar.add(b);
		return b;
	}

}
