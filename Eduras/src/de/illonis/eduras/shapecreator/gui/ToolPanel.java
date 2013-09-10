package de.illonis.eduras.shapecreator.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;

import de.illonis.eduras.images.ImageFiler;
import de.illonis.eduras.shapecreator.DataHolder;
import de.illonis.eduras.shapecreator.PanelInteractor;
import de.illonis.eduras.shapecreator.PanelInteractor.InteractMode;

public class ToolPanel extends JPanel implements ActionListener {

	private final DataHolder data;
	private final PanelInteractor interactor;
	private JToolBar toolBar;
	private JToggleButton modeButtonDrag, modeButtonAdd, modeButtonRemove;
	private ButtonGroup bgroup;

	public ToolPanel(PanelInteractor panelInteractor) {
		this.interactor = panelInteractor;
		data = DataHolder.getInstance();
		buildToolbar();
	}

	private void buildToolbar() {
		toolBar = new JToolBar();
		bgroup = new ButtonGroup();
		modeButtonDrag = createButton("select edges", "button_drag.png");
		modeButtonAdd = createButton("add vertices", "button_add.png");
		modeButtonRemove = createButton("remove vertices", "button_remove.png");
		modeButtonDrag.setSelected(true);
		add(toolBar);
	}

	private JToggleButton createButton(String label, String imgUrl) {
		ImageIcon icon = ImageFiler.loadIcon("shapecreator/icons/" + imgUrl);
		JToggleButton b = new JToggleButton(icon);
		b.setText(label);
		b.setHorizontalTextPosition(SwingConstants.RIGHT);

		b.setToolTipText(label);
		bgroup.add(b);
		toolBar.add(b);
		b.addActionListener(this);
		return b;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JToggleButton source = (JToggleButton) e.getSource();
		toolBar.requestFocus();
		if (source == modeButtonAdd)
			interactor.setMode(InteractMode.ADD_VERT);
		else if (source == modeButtonDrag)
			interactor.setMode(InteractMode.NONE);
		else if (source == modeButtonRemove)
			interactor.setMode(InteractMode.REM_VERT);
	}
}
