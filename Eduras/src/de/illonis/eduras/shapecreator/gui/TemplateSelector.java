package de.illonis.eduras.shapecreator.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;

import de.illonis.eduras.shapecreator.templates.ShapeTemplate;
import de.illonis.eduras.shapecreator.templates.TemplateManager;

public class TemplateSelector extends JDialog implements ActionListener {

	private JButton okButton, abortButton;
	private boolean answer = false;
	private String selectedTemplate = "";
	private JList<ShapeTemplate> templateList;
	private DefaultListModel<ShapeTemplate> model;

	public TemplateSelector(JFrame parent) {
		super(parent);
		setTitle("Template list");
		setModal(true);
		buildGui();
	}

	public boolean getAnswer() {
		return answer;
	}

	public String getSelectedTemplate() {
		return selectedTemplate;
	}

	private void buildGui() {
		JPanel panel = (JPanel) getContentPane();
		panel.setLayout(new BorderLayout());

		okButton = new JButton("open");
		abortButton = new JButton("abort");

		JLabel infoLabel = new JLabel(
				"<html>Please select a template from the list below:</html>");
		infoLabel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
		panel.add(infoLabel, BorderLayout.NORTH);
		JPanel buttonPanel = new JPanel();
		buttonPanel.add(okButton);
		buttonPanel.add(abortButton);
		okButton.addActionListener(this);
		abortButton.addActionListener(this);
		model = new DefaultListModel<ShapeTemplate>();
		for (ShapeTemplate template : TemplateManager.getInstance()
				.getTemplates())
			model.addElement(template);
		templateList = new JList<ShapeTemplate>(model);
		templateList.setCellRenderer(new TemplateListRenderer());
		templateList.setVisibleRowCount(10);
		// templateList.setBorder(BorderFactory.createLineBorder(Color.BLACK,
		// 1));

		panel.add(infoLabel, BorderLayout.NORTH);
		panel.add(templateList, BorderLayout.CENTER);
		panel.add(buttonPanel, BorderLayout.SOUTH);
	}

	public void showFrame() {
		setSize(200, 300);
		setLocationRelativeTo(null);
		templateList.removeSelectionInterval(0, model.size());
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JButton source = (JButton) e.getSource();

		if (okButton == source) {
			ShapeTemplate template = templateList.getSelectedValue();
			if (template != null) {
				answer = true;
				selectedTemplate = template.getName();
			} else {
				answer = false;
			}
		} else {
			answer = false;
		}
		setVisible(false);
	}

}