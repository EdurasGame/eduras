package de.illonis.eduras.mapeditor.gui.dialog;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.Border;

import de.illonis.eduras.mapeditor.MapData;
import de.illonis.eduras.mapeditor.gui.EditorWindow;

/**
 * Allows editing map properties like size, name.
 * 
 * @author illonis
 * 
 */
public class MapPropertiesDialog extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1L;

	private final static int MAP_MAX_SIZE = 10000;
	private final static int MAP_MIN_SIZE = 200;
	private final static int MAP_SIZE_STEP = 100;

	private final MapData data;
	private JTextField mapNameField, authorField;
	private JSpinner widthField, heightField;
	private JButton okButton, abortButton;

	public MapPropertiesDialog(EditorWindow window) {
		super(window);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		data = MapData.getInstance();
		setTitle("Map properties");
		Dimension d = new Dimension(300, 250);
		setPreferredSize(d);
		setSize(d);
		setModal(true);
		buildGui();
	}

	private void buildGui() {
		Border border = BorderFactory.createEmptyBorder(5, 5, 5, 5);
		JPanel panel = (JPanel) getContentPane();
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
		JPanel namePanel = new JPanel(new BorderLayout());
		namePanel.setBorder(border);
		JLabel nameLabel = new JLabel("Map name");
		namePanel.add(nameLabel, BorderLayout.WEST);
		mapNameField = new JTextField(data.getMapName());
		nameLabel.setBorder(border);
		namePanel.add(mapNameField, BorderLayout.CENTER);
		panel.add(namePanel);
		JPanel authorPanel = new JPanel(new BorderLayout());
		JLabel authorLabel = new JLabel("Author");
		authorPanel.add(authorLabel, BorderLayout.WEST);
		authorPanel.setBorder(border);
		authorField = new JTextField(data.getAuthor());
		authorLabel.setBorder(border);
		authorPanel.add(authorField, BorderLayout.CENTER);
		panel.add(authorPanel);
		JPanel widthPanel = new JPanel(new BorderLayout());
		JLabel widthLabel = new JLabel("Width");
		widthPanel.add(widthLabel, BorderLayout.WEST);
		widthPanel.setBorder(border);
		SpinnerModel widthModel = new SpinnerNumberModel(data.getWidth(),
				MAP_MIN_SIZE, MAP_MAX_SIZE, MAP_SIZE_STEP);
		widthField = new JSpinner(widthModel);
		widthLabel.setBorder(border);
		widthPanel.add(widthField, BorderLayout.CENTER);
		panel.add(widthPanel);
		JPanel heightPanel = new JPanel(new BorderLayout());
		heightPanel.setBorder(border);
		JLabel heightLabel = new JLabel("Height");
		heightPanel.add(heightLabel, BorderLayout.WEST);
		SpinnerModel heightModel = new SpinnerNumberModel(data.getHeight(),
				MAP_MIN_SIZE, MAP_MAX_SIZE, MAP_SIZE_STEP);
		heightField = new JSpinner(heightModel);
		heightLabel.setBorder(border);
		heightPanel.add(heightField);
		panel.add(heightPanel);
		JPanel buttonPanel = new JPanel(new BorderLayout());
		buttonPanel.setBorder(border);
		okButton = new JButton("Save");
		okButton.addActionListener(this);
		buttonPanel.add(okButton, BorderLayout.WEST);
		abortButton = new JButton("Abort");
		abortButton.addActionListener(this);
		buttonPanel.add(abortButton, BorderLayout.EAST);
		panel.add(buttonPanel);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == abortButton) {
			dispose();
		} else {
			data.setAuthor(authorField.getText());
			data.setMapName(mapNameField.getText());
			data.setWidth((int) widthField.getValue());
			data.setHeight((int) heightField.getValue());
		}
	}

	@Override
	public void setVisible(boolean b) {
		setLocationRelativeTo(null);
		super.setVisible(b);
	}

}
