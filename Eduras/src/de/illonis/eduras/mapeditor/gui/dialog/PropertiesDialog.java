package de.illonis.eduras.mapeditor.gui.dialog;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SpringLayout;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.newdawn.slick.Color;

import de.illonis.eduras.ReferencedEntity;
import de.illonis.eduras.gameobjects.Base;
import de.illonis.eduras.gameobjects.Base.BaseType;
import de.illonis.eduras.gameobjects.DynamicPolygonObject;
import de.illonis.eduras.gameobjects.GameObject;
import de.illonis.eduras.gameobjects.GameObject.Visibility;
import de.illonis.eduras.gameobjects.Portal;
import de.illonis.eduras.mapeditor.MapData;
import de.illonis.eduras.mapeditor.gui.EditorWindow;
import de.illonis.eduras.maps.NodeData;
import de.illonis.eduras.maps.SpawnPosition;
import de.illonis.eduras.maps.SpawnPosition.SpawnType;
import de.illonis.eduras.maps.persistence.MapParser;

/**
 * Allows change of object or base properties.
 * 
 * @author illonis
 * 
 */
public class PropertiesDialog extends JDialog implements ItemListener,
		ActionListener, ChangeListener {

	private static final long serialVersionUID = 1L;

	private final static Dimension MIN_SIZE = new Dimension(600, 400);

	protected GameObject object;
	protected NodeData node;
	protected SpawnPosition spawn;
	private ReferencedEntity entity;

	private JRadioButton visibleNone;
	private JRadioButton visibleAlways;
	private JRadioButton visibleTeam;
	private JRadioButton visibleOwner;
	private final JTabbedPane tabbedPane;
	private JColorChooser chooser;
	private JSlider sliderX, sliderY, sliderZ;
	private JLabel currentX, currentY;
	private JRadioButton baseTeamA;
	private JRadioButton baseTeamB;
	private JRadioButton baseNone;
	private JRadioButton spawnTeamA, spawnTeamB, spawnSingle, spawnAny;
	private JSpinner baseMult;
	private JTextField refName;
	private JButton saveButton;
	private JComboBox<Portal> otherPortals;

	/**
	 * Creates a properties dialog for a node.
	 * 
	 * @param parent
	 *            the parent window.
	 * @param node
	 *            the node.
	 */
	public PropertiesDialog(EditorWindow parent, NodeData node) {
		this(parent);
		this.node = node;
		this.entity = node;
		addPositionTab();
		addNeutralBaseTab();
	}

	/**
	 * Create a properties dialog for a spawnarea.
	 * 
	 * @param window
	 *            parent window.
	 * @param spawn
	 *            the spawnarea.
	 */
	public PropertiesDialog(EditorWindow window, SpawnPosition spawn) {
		this(window);
		this.spawn = spawn;
		this.entity = spawn;
		addPositionTab();
		addSpawnTab();
	}

	/**
	 * Creates a properties dialog for a gameobject.
	 * 
	 * @param parent
	 *            parent window.
	 * @param object
	 *            the object.
	 */
	public PropertiesDialog(EditorWindow parent, GameObject object) {
		this(parent);
		this.object = object;
		this.entity = object;
		addPropertiesTab();
		addPositionTab();
		if (object instanceof DynamicPolygonObject) {
			addColorTab(((DynamicPolygonObject) object).getColor());
		} else if (object instanceof Portal) {
			addPortalTab();
		}
	}

	private PropertiesDialog(EditorWindow parent) {
		super(parent);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setModal(false);
		tabbedPane = new JTabbedPane();
		SpringLayout sl = new SpringLayout();
		getContentPane().setLayout(sl);
		sl.putConstraint(SpringLayout.WEST, tabbedPane, 2, SpringLayout.WEST,
				getContentPane());
		sl.putConstraint(SpringLayout.NORTH, tabbedPane, 2, SpringLayout.NORTH,
				getContentPane());
		sl.putConstraint(SpringLayout.EAST, tabbedPane, 2, SpringLayout.EAST,
				getContentPane());
		sl.putConstraint(SpringLayout.SOUTH, tabbedPane, 2, SpringLayout.SOUTH,
				getContentPane());
		getContentPane().add(tabbedPane);
		setMinimumSize(MIN_SIZE);
	}

	private void updateTitle() {
		if (object != null) {
			setTitle("Properties of " + object.getType() + " #"
					+ object.getId() + " [" + entity.getRefName() + "]");
		} else if (spawn != null) {
			setTitle("Properties of Spawnarea " + " [" + entity.getRefName()
					+ "]");
		} else if (node != null) {
			setTitle("Properties of Base #" + node.getId() + " ["
					+ entity.getRefName() + "]");
		} else {
			setTitle("Properties of " + entity.getRefName());
		}
	}

	private void addSpawnTab() {
		JPanel spawnPanel = new JPanel();
		spawnPanel.setLayout(new BoxLayout(spawnPanel, BoxLayout.PAGE_AXIS));
		Border border = BorderFactory.createEmptyBorder(8, 8, 8, 8);
		JPanel propertiesPanel = new JPanel();
		propertiesPanel.setBorder(border);
		spawnTeamA = new JRadioButton("Team A",
				spawn.getTeaming() == SpawnType.TEAM_A);
		spawnTeamB = new JRadioButton("Team B",
				spawn.getTeaming() == SpawnType.TEAM_B);
		spawnAny = new JRadioButton("Any", spawn.getTeaming() == SpawnType.ANY);
		spawnSingle = new JRadioButton("Single",
				spawn.getTeaming() == SpawnType.SINGLE);
		ButtonGroup group = new ButtonGroup();
		group.add(spawnTeamA);
		group.add(spawnTeamB);
		group.add(spawnAny);
		group.add(spawnSingle);
		spawnTeamA.addActionListener(this);
		spawnTeamB.addActionListener(this);
		spawnAny.addActionListener(this);
		spawnSingle.addActionListener(this);
		propertiesPanel.add(spawnTeamA);
		propertiesPanel.add(spawnTeamB);
		propertiesPanel.add(spawnAny);
		propertiesPanel.add(spawnSingle);
		spawnPanel.add(propertiesPanel);
		addTab("Spawnarea", spawnPanel);
	}

	private void addNeutralBaseTab() {
		JPanel basePanel = new JPanel();
		basePanel.setLayout(new BoxLayout(basePanel, BoxLayout.PAGE_AXIS));

		ButtonGroup group = new ButtonGroup();
		JPanel baseOwnerPanel = new JPanel();
		baseNone = new JRadioButton("None",
				(node.isMainNode() == Base.BaseType.NEUTRAL));
		baseTeamA = new JRadioButton("Team A",
				(node.isMainNode() == Base.BaseType.TEAM_A));
		baseTeamB = new JRadioButton("Team B",
				(node.isMainNode() == Base.BaseType.TEAM_B));
		group.add(baseNone);
		group.add(baseTeamA);
		group.add(baseTeamB);
		baseOwnerPanel.add(baseNone);
		baseOwnerPanel.add(baseTeamA);
		baseOwnerPanel.add(baseTeamB);
		basePanel.add(baseOwnerPanel);
		JPanel multPanel = new JPanel();
		multPanel.add(new JLabel("Resource multiplicator"), BorderLayout.NORTH);
		SpinnerModel model = new SpinnerNumberModel(
				node.getResourceMultiplicator(), -10, 30, .5);
		baseMult = new JSpinner(model);
		baseMult.addChangeListener(this);
		multPanel.add(baseMult, BorderLayout.CENTER);
		basePanel.add(multPanel);
		addTab("Base", basePanel);
	}

	private void addPortalTab() {
		JPanel portalPanel = new JPanel();
		Portal portal = (Portal) object;
		portalPanel.setLayout(new BoxLayout(portalPanel, BoxLayout.PAGE_AXIS));
		addTab("Portal", portalPanel);
		Portal partner = portal.getPartnerPortal();
		String partnerName = "<none>";
		if (partner != null) {
			partnerName = partner.getRefName();
		}

		List<Portal> portals = new LinkedList<Portal>();
		for (GameObject o : MapData.getInstance().getGameObjects()) {
			if (o instanceof Portal) {
				if (portal.equals(o))
					continue;
				portals.add((Portal) o);
			}
		}
		otherPortals = new JComboBox<Portal>(portals.toArray(new Portal[] {}));
		otherPortals.setRenderer(new PortalListRenderer());
		GameObject otherObject = MapData.getInstance().findObjectByRef(
				partnerName);
		if (otherObject != null) {
			otherPortals.setSelectedItem((Portal) otherObject);
		} else {
			otherPortals.setSelectedIndex(-1);
		}
		otherPortals.addActionListener(this);
		JPanel portPanel = new JPanel();
		JLabel otherRef = new JLabel("Target portal: ");
		portalPanel.add(otherRef);
		portPanel.add(otherPortals);
		portalPanel.add(portPanel);
	}

	private class PortalListRenderer extends JLabel implements
			ListCellRenderer<Portal> {
		private static final long serialVersionUID = 1L;

		public PortalListRenderer() {
			setOpaque(true);
			setHorizontalAlignment(CENTER);
			setVerticalAlignment(CENTER);
		}

		@Override
		public Component getListCellRendererComponent(
				JList<? extends Portal> list, Portal value, int index,
				boolean isSelected, boolean cellHasFocus) {

			if (isSelected) {
				setForeground(java.awt.Color.white);
				setBackground(java.awt.Color.blue);
			} else {
				setForeground(otherPortals.getForeground());
				setBackground(otherPortals.getBackground());
			}
			if (value == null)
				setText("");
			else
				setText(value.getRefName());
			return this;
		}
	}

	private int getItemX() {
		if (object != null)
			return (int) object.getXPosition();
		if (node != null)
			return (int) node.getX();
		if (spawn != null) {
			return (int) spawn.getArea().getX();
		}
		return 0;
	}

	private int getItemY() {
		if (object != null)
			return (int) object.getYPosition();
		if (node != null)
			return (int) node.getY();
		if (spawn != null) {
			return (int) spawn.getArea().getY();
		}
		return 0;
	}

	private void setItemX(float x) {
		if (object != null)
			object.setXPosition(x);
		if (node != null)
			node.setX(x);
		if (spawn != null) {
			spawn.getArea().setX(x);
		}
	}

	private void setItemY(float y) {
		if (object != null)
			object.setYPosition(y);
		if (node != null)
			node.setY(y);
		if (spawn != null) {
			spawn.getArea().setY(y);
		}
	}

	private void addPositionTab() {
		int x = getItemX();
		int y = getItemY();
		MapData data = MapData.getInstance();
		JPanel positionTab = new JPanel();
		positionTab.setLayout(new BoxLayout(positionTab, BoxLayout.PAGE_AXIS));
		JPanel xPanel = new JPanel(new BorderLayout());
		JPanel yPanel = new JPanel(new BorderLayout());
		sliderX = new JSlider(0, data.getWidth(), x);
		sliderX.addChangeListener(this);
		sliderY = new JSlider(0, data.getHeight(), y);
		sliderY.addChangeListener(this);
		Border border = BorderFactory.createEmptyBorder(15, 15, 15, 15);
		sliderX.setBorder(border);
		sliderY.setBorder(border);
		xPanel.setBorder(border);
		yPanel.setBorder(border);
		JLabel labelX = new JLabel("Horizontal position");
		labelX.setLabelFor(sliderX);
		JLabel labelY = new JLabel("Vertical position");
		labelY.setLabelFor(sliderY);
		currentX = new JLabel(x + "");
		currentY = new JLabel(y + "");

		Hashtable<Integer, JLabel> labelTable = new Hashtable<Integer, JLabel>();
		labelTable.put(new Integer(0), new JLabel("Left"));
		labelTable.put(new Integer(sliderX.getMaximum()), new JLabel("Right"));
		sliderX.setLabelTable(labelTable);
		sliderX.setPaintLabels(true);
		labelTable = new Hashtable<Integer, JLabel>();
		labelTable.put(new Integer(0), new JLabel("Up"));
		labelTable.put(new Integer(sliderY.getMaximum()), new JLabel("Down"));
		sliderY.setLabelTable(labelTable);
		sliderY.setPaintLabels(true);
		sliderX.addChangeListener(this);
		sliderY.addChangeListener(this);
		xPanel.add(labelX, BorderLayout.WEST);
		xPanel.add(sliderX, BorderLayout.CENTER);
		xPanel.add(currentX, BorderLayout.EAST);
		yPanel.add(labelY, BorderLayout.WEST);
		yPanel.add(sliderY, BorderLayout.CENTER);
		yPanel.add(currentY, BorderLayout.EAST);
		positionTab.add(xPanel);
		positionTab.add(yPanel);

		JPanel refSection = new JPanel(new BorderLayout());
		refSection.setBorder(border);
		refSection.add(new JLabel("ReferenceName"), BorderLayout.NORTH);
		JPanel refPanel = new JPanel();
		refSection.add(refPanel, BorderLayout.CENTER);
		refName = new JTextField(entity.getRefName());
		refName.addActionListener(this);
		refName.setColumns(20);

		saveButton = new JButton("Save");
		saveButton.setEnabled(false);
		saveButton.addActionListener(this);
		refName.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void removeUpdate(DocumentEvent arg0) {
				check();
			}

			@Override
			public void insertUpdate(DocumentEvent arg0) {
				check();
			}

			@Override
			public void changedUpdate(DocumentEvent arg0) {
				check();
			}

			private void check() {
				saveButton.setEnabled(!(refName.getText().isEmpty() || refName
						.getText().equals(entity.getRefName())));
			}
		});
		refPanel.add(refName);
		refPanel.add(saveButton);
		refSection.setBorder(border);
		positionTab.add(refSection);
		addTab("General", positionTab);
	}

	private void addPropertiesTab() {
		Border border = BorderFactory.createEmptyBorder(8, 8, 8, 8);
		JPanel propertiesPanel = new JPanel();
		propertiesPanel.setLayout(new BoxLayout(propertiesPanel,
				BoxLayout.PAGE_AXIS));
		visibleNone = new JRadioButton("Invisible",
				object.getVisibility() == Visibility.INVISIBLE);
		visibleOwner = new JRadioButton("Owner",
				object.getVisibility() == Visibility.OWNER);
		visibleTeam = new JRadioButton("Team",
				object.getVisibility() == Visibility.TEAM);
		visibleOwner.setEnabled(false);
		visibleTeam.setEnabled(false);
		visibleAlways = new JRadioButton("Everybody",
				object.getVisibility() == Visibility.ALL);
		ButtonGroup group = new ButtonGroup();
		group.add(visibleAlways);
		group.add(visibleNone);
		group.add(visibleTeam);
		group.add(visibleOwner);
		visibleNone.addActionListener(this);
		visibleOwner.addActionListener(this);
		visibleTeam.addActionListener(this);
		visibleAlways.addActionListener(this);
		JPanel visibilityPanel = new JPanel();
		visibilityPanel.add(visibleAlways);
		visibilityPanel.add(visibleTeam);
		visibilityPanel.add(visibleOwner);
		visibilityPanel.add(visibleNone);
		JPanel visSection = new JPanel(new BorderLayout());
		visSection.add(new JLabel("Visibility"), BorderLayout.NORTH);
		visSection.add(visibilityPanel, BorderLayout.CENTER);
		visSection.setBorder(border);
		JPanel zindexPanel = new JPanel(new BorderLayout());
		zindexPanel.add(new JLabel("z-index"), BorderLayout.NORTH);
		sliderZ = new JSlider(-10, 10, object.getzLayer());
		sliderZ.setPaintLabels(true);
		sliderZ.setPaintTicks(true);
		sliderZ.setMajorTickSpacing(5);
		sliderZ.setMinorTickSpacing(1);
		sliderZ.setSnapToTicks(true);
		sliderZ.addChangeListener(this);
		sliderZ.setEnabled(false);
		zindexPanel.add(sliderZ, BorderLayout.CENTER);
		zindexPanel.setBorder(border);
		propertiesPanel.add(visSection);
		propertiesPanel.add(zindexPanel);
		addTab("Properties", propertiesPanel);
	}

	void addColorTab(Color initialColor) {
		java.awt.Color color = new java.awt.Color(initialColor.r,
				initialColor.g, initialColor.b, initialColor.a);
		JPanel colorTab = new JPanel(new BorderLayout());
		chooser = new JColorChooser(color);
		chooser.getSelectionModel().addChangeListener(this);
		colorTab.add(chooser, BorderLayout.CENTER);
		addTab("Color", colorTab);
	}

	protected final void addTab(String title, JPanel content) {
		tabbedPane.addTab(title, content);
	}

	@Override
	public void setVisible(boolean b) {
		pack();
		setLocationRelativeTo(getParent());
		updateTitle();
		super.setVisible(b);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() instanceof JComboBox) {
			Portal portal = (Portal) otherPortals.getSelectedItem();
			((Portal) object).setPartnerPortal(portal);
			return;
		}
		if (e.getSource().equals(saveButton) || e.getSource().equals(refName)) {
			if (refName.getText().matches(MapParser.IDENTIFIER_REGEX)) {

				ReferencedEntity o = MapData.getInstance().findByRef(
						refName.getText());
				if (o != null) {
					if (o.equals(entity)) {
						// name did not change
						saveButton.setEnabled(false);
						return;
					} else {
						JOptionPane.showMessageDialog(this,
								"Object with name \"" + refName.getText()
										+ "\" already exists.",
								"Error setting reference name.",
								JOptionPane.ERROR_MESSAGE);
						return;
					}
				}
				entity.setRefName(refName.getText());
				updateTitle();
				saveButton.setEnabled(false);
			} else {
				JOptionPane.showMessageDialog(this, "Invalid reference name",
						"Error", JOptionPane.ERROR_MESSAGE);
			}
			return;
		}

		JRadioButton button = (JRadioButton) e.getSource();
		if (button == baseNone) {
			node.setIsMainNode(BaseType.NEUTRAL);
		} else if (button == baseTeamA) {
			node.setIsMainNode(BaseType.TEAM_A);
		} else if (button == baseTeamB) {
			node.setIsMainNode(BaseType.TEAM_B);
		} else if (button == visibleTeam) {
			object.setVisible(Visibility.TEAM);
		} else if (button == visibleOwner) {
			object.setVisible(Visibility.OWNER);
		} else if (button == visibleNone) {
			object.setVisible(Visibility.INVISIBLE);
		} else if (button == visibleAlways) {
			object.setVisible(Visibility.ALL);
		} else if (button == spawnAny) {
			spawn.setTeaming(SpawnType.ANY);
		} else if (button == spawnSingle) {
			spawn.setTeaming(SpawnType.SINGLE);
		} else if (button == spawnTeamA) {
			spawn.setTeaming(SpawnType.TEAM_A);
		} else if (button == spawnTeamB) {
			spawn.setTeaming(SpawnType.TEAM_B);
		}
	}

	@Override
	public void itemStateChanged(ItemEvent event) {
	}

	@Override
	public void stateChanged(ChangeEvent event) {
		if (event.getSource() instanceof JSlider) {
			JSlider source = (JSlider) event.getSource();

			if (source == sliderX) {
				setItemX(sliderX.getValue());
				currentX.setText(sliderX.getValue() + "");
			} else if (source == sliderY) {
				setItemY(sliderY.getValue());
				currentY.setText(sliderY.getValue() + "");
			} else if (source == sliderZ) {
				object.setzLayer(sliderZ.getValue());
			}
		} else if (event.getSource() instanceof JSpinner) {
			JSpinner spinner = (JSpinner) event.getSource();
			float val = (float) (double) spinner.getValue();
			node.setResourceMultiplicator(val);
		} else {
			java.awt.Color newColor = chooser.getColor();
			Color color = new Color(newColor.getRed(), newColor.getGreen(),
					newColor.getBlue(), newColor.getAlpha());
			DynamicPolygonObject o = (DynamicPolygonObject) object;
			o.setColor(color);
		}
	}
}