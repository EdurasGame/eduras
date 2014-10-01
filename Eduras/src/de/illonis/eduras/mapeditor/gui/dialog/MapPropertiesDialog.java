package de.illonis.eduras.mapeditor.gui.dialog;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.Border;

import de.illonis.eduras.gamemodes.GameMode;
import de.illonis.eduras.gamemodes.GameMode.GameModeNumber;
import de.illonis.eduras.mapeditor.MapData;
import de.illonis.eduras.mapeditor.gui.EditorWindow;

/**
 * Allows editing map properties like size, name.
 * 
 * @author illonis
 * 
 */
public class MapPropertiesDialog extends ESCDialog implements ActionListener {

	private static final long serialVersionUID = 1L;

	private final static int MAP_MAX_SIZE = 10000;
	private final static int MAP_MIN_SIZE = 200;
	private final static int MAP_SIZE_STEP = 100;

	private final MapData data;
	private JTextField authorField;
	private JSpinner widthField, heightField;
	private JButton okButton, abortButton;
	private JList<GameModeNumber> gameModes;
	private final EditorWindow window;

	/**
	 * @param parent
	 *            the parent frame.
	 */
	public MapPropertiesDialog(EditorWindow parent) {
		super(parent);
		this.window = parent;
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		data = MapData.getInstance();
		setTitle("Map properties");
		Dimension d = new Dimension(500, 250);
		setPreferredSize(d);
		setSize(d);
		setModal(true);
		buildGui();
	}

	private void buildGui() {
		Border border = BorderFactory.createEmptyBorder(5, 5, 5, 5);
		JPanel content = (JPanel) getContentPane();
		content.setLayout(new BorderLayout());
		JPanel gameModesPanel = new JPanel(new BorderLayout());
		gameModesPanel.add(new JLabel("supported gamemodes"),
				BorderLayout.NORTH);
		gameModesPanel.setBorder(border);
		gameModes = new JList<GameMode.GameModeNumber>(GameModeNumber.values());
		gameModes
				.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

		List<Integer> selected = new LinkedList<Integer>();
		for (GameModeNumber mode : data.getSupportedGameModes()) {
			for (int j = 0; j < gameModes.getModel().getSize(); j++) {
				if (gameModes.getModel().getElementAt(j) == mode) {
					selected.add(j);
				}
			}
		}
		int[] indices = new int[selected.size()];
		for (int i = 0; i < indices.length; i++) {
			indices[i] = selected.get(i);
		}
		gameModes.setSelectedIndices(indices);
		JScrollPane scroller = new JScrollPane(gameModes);
		gameModesPanel.add(scroller, BorderLayout.CENTER);
		JPanel attributesPanel = new JPanel();
		content.add(attributesPanel, BorderLayout.CENTER);
		content.add(gameModesPanel, BorderLayout.EAST);
		attributesPanel.setLayout(new BoxLayout(attributesPanel,
				BoxLayout.PAGE_AXIS));
		JPanel namePanel = new JPanel(new BorderLayout());
		namePanel.setBorder(border);
		attributesPanel.add(namePanel);
		JPanel authorPanel = new JPanel(new BorderLayout());
		JLabel authorLabel = new JLabel("Author");
		authorPanel.add(authorLabel, BorderLayout.WEST);
		authorPanel.setBorder(border);
		authorField = new JTextField(data.getAuthor());
		authorLabel.setBorder(border);
		authorPanel.add(authorField, BorderLayout.CENTER);
		attributesPanel.add(authorPanel);
		JPanel widthPanel = new JPanel(new BorderLayout());
		JLabel widthLabel = new JLabel("Width");
		widthPanel.add(widthLabel, BorderLayout.WEST);
		widthPanel.setBorder(border);
		SpinnerModel widthModel = new SpinnerNumberModel(data.getWidth(),
				MAP_MIN_SIZE, MAP_MAX_SIZE, MAP_SIZE_STEP);
		widthField = new JSpinner(widthModel);
		widthLabel.setBorder(border);
		widthPanel.add(widthField, BorderLayout.CENTER);
		attributesPanel.add(widthPanel);
		JPanel heightPanel = new JPanel(new BorderLayout());
		heightPanel.setBorder(border);
		JLabel heightLabel = new JLabel("Height");
		heightPanel.add(heightLabel, BorderLayout.WEST);
		SpinnerModel heightModel = new SpinnerNumberModel(data.getHeight(),
				MAP_MIN_SIZE, MAP_MAX_SIZE, MAP_SIZE_STEP);
		heightField = new JSpinner(heightModel);
		heightLabel.setBorder(border);
		heightPanel.add(heightField);
		attributesPanel.add(heightPanel);
		JPanel buttonPanel = new JPanel(new BorderLayout());
		buttonPanel.setBorder(border);
		okButton = new JButton("Save");
		okButton.addActionListener(this);
		buttonPanel.add(okButton, BorderLayout.WEST);
		abortButton = new JButton("Abort");
		abortButton.addActionListener(this);
		buttonPanel.add(abortButton, BorderLayout.EAST);
		content.add(buttonPanel, BorderLayout.SOUTH);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == abortButton) {
			dispose();
		} else {
			data.setAuthor(authorField.getText());
			data.setWidth((int) widthField.getValue());
			data.setHeight((int) heightField.getValue());
			List<GameModeNumber> modes = gameModes.getSelectedValuesList();
			data.setSupportedGameModes(modes);
			window.refreshTitle();
		}
	}

	@Override
	public void setVisible(boolean b) {
		setLocationRelativeTo(null);
		super.setVisible(b);
	}
}
