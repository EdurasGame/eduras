package de.illonis.eduras.mapeditor.gui.dialog;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Hashtable;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SpringLayout;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.newdawn.slick.Color;

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
		setTitle("Properties of Base #" + node.getId());
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
		setTitle("Properties of Spawnarea");
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
		setTitle("Properties of " + object.getType() + " #" + object.getId());

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
		portalPanel.setLayout(new BoxLayout(portalPanel, BoxLayout.PAGE_AXIS));
		addTab("Portal", portalPanel);
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
		addTab("Position", positionTab);
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
		super.setVisible(b);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
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
		// TODO Auto-generated method stub
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