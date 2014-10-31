package de.illonis.eduras.shapecreator.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;

import de.illonis.eduras.images.ImageFiler;
import de.illonis.eduras.shapecreator.PanelInteractor;
import de.illonis.eduras.shapecreator.PanelInteractor.InteractMode;

/**
 * A panel that allows user to select the current editing mode.
 * 
 * @author illonis
 * 
 */
public class ToolPanel extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1L;
	private final PanelInteractor interactor;
	private JToolBar toolBar;
	private JToggleButton modeButtonDrag, modeButtonDragShape,
			modeButtonScaleShape, modeButtonAdd, modeButtonRemove;
	private ButtonGroup bgroup;

	/**
	 * Creates a new toolpanel.
	 * 
	 * @param panelInteractor
	 *            the interactor that is called to trigger mode changes.
	 */
	public ToolPanel(PanelInteractor panelInteractor) {
		this.interactor = panelInteractor;
		buildToolbar();
	}

	private void buildToolbar() {
		toolBar = new JToolBar();
		bgroup = new ButtonGroup();
		modeButtonDrag = createButton("Select and drag", "button_drag.png");
		modeButtonDragShape = createButton("Drag shape", "button_dragshape.png");
		modeButtonScaleShape = createButton("Scale shape", "button_scale.png");
		modeButtonAdd = createButton("Add vertices", "button_add.png");
		modeButtonRemove = createButton("Delete vertices", "button_remove.png");
		modeButtonDrag.setSelected(true);
		add(toolBar);
	}

	private JToggleButton createButton(String label, String imgUrl) {
		ImageIcon icon;
		JToggleButton b;
		try {
			icon = ImageFiler.loadIcon("shapecreator/icons/" + imgUrl);
			b = new JToggleButton(icon);
			b.setText(label);
		} catch (IOException e) {
			b = new JToggleButton(label);
		}
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
		else if (source == modeButtonDragShape)
			interactor.setMode(InteractMode.DRAG_SHAPE);
		else if (source == modeButtonScaleShape)
			interactor.setMode(InteractMode.SCALE_SHAPE);
	}

	/**
	 * Indicates a mode change.
	 * 
	 * @param mode
	 *            new mode.
	 */
	public void onModeSet(InteractMode mode) {
		switch (mode) {
		case ADD_VERT:
			modeButtonAdd.doClick();
			break;
		case NONE:
			modeButtonDrag.doClick();
			break;
		case REM_VERT:
			modeButtonRemove.doClick();
			break;
		case DRAG_SHAPE:
			modeButtonDragShape.doClick();
			break;
		case SCALE_SHAPE:
			modeButtonScaleShape.doClick();
			break;
		default:
			break;
		}
	}
}
