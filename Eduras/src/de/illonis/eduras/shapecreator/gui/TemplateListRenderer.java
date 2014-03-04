package de.illonis.eduras.shapecreator.gui;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;

import de.illonis.eduras.shapecreator.templates.ShapeTemplate;

/**
 * Renders the contents of the template list.
 * 
 * @author illonis
 * 
 */
public class TemplateListRenderer extends JPanel implements
		ListCellRenderer<ShapeTemplate> {

	private static final long serialVersionUID = 1L;
	private final static Color SELECTED_BG = new Color(194, 198, 255);
	private final JLabel label;

	TemplateListRenderer() {
		setOpaque(true);
		label = new JLabel();
		add(label);
	}

	@Override
	public Component getListCellRendererComponent(
			JList<? extends ShapeTemplate> list, ShapeTemplate value,
			int index, boolean isSelected, boolean cellHasFocus) {
		label.setText(value.getName());
		if (isSelected)
			setBackground(SELECTED_BG);
		else
			setBackground(Color.WHITE);
		return this;
	}
}
