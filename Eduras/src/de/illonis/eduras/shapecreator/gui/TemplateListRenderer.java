package de.illonis.eduras.shapecreator.gui;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;

import de.illonis.eduras.shapecreator.templates.ShapeTemplate;

public class TemplateListRenderer extends JPanel implements
		ListCellRenderer<ShapeTemplate> {

	private final static Color SELECTED_BG = new Color(194, 198, 255);
	private final JLabel label;

	public TemplateListRenderer() {
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
