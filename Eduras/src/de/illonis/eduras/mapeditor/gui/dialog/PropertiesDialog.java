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
import javax.swing.JTabbedPane;
import javax.swing.SpringLayout;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.newdawn.slick.Color;

import de.illonis.eduras.gameobjects.DynamicPolygonObject;
import de.illonis.eduras.gameobjects.GameObject;
import de.illonis.eduras.mapeditor.MapData;
import de.illonis.eduras.mapeditor.gui.EditorWindow;

public class PropertiesDialog extends JDialog implements ItemListener,
		ActionListener, ChangeListener {

	private final static Dimension MIN_SIZE = new Dimension(600, 400);

	protected final GameObject object;

	private JRadioButton visibleNone;
	private JRadioButton visibleAlways;
	private JRadioButton visibleTeam;
	private JRadioButton visibleOwner;
	private final JTabbedPane tabbedPane;
	private JColorChooser chooser;
	private JSlider sliderX, sliderY;
	private JLabel currentX, currentY;

	public PropertiesDialog(EditorWindow parent, GameObject object) {
		super(parent);
		this.object = object;
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setModal(false);
		setTitle("Properties of " + object.getType() + " #" + object.getId());
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

		addPropertiesTab();
		addPositionTab();
		if (object instanceof DynamicPolygonObject) {
			addColorTab(((DynamicPolygonObject) object).getColor());
		}
		getContentPane().add(tabbedPane);
		setMinimumSize(MIN_SIZE);
	}

	private void addPositionTab() {
		MapData data = MapData.getInstance();
		JPanel positionTab = new JPanel();
		positionTab.setLayout(new BoxLayout(positionTab, BoxLayout.PAGE_AXIS));
		JPanel xPanel = new JPanel(new BorderLayout());
		JPanel yPanel = new JPanel(new BorderLayout());
		sliderX = new JSlider(0, data.getWidth(), (int) object.getXPosition());
		sliderX.addChangeListener(this);
		sliderY = new JSlider(0, data.getHeight(), (int) object.getYPosition());
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
		currentX = new JLabel((int) object.getXPosition() + "");
		currentY = new JLabel((int) object.getYPosition() + "");

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
		JPanel propertiesPanel = new JPanel();
		propertiesPanel.setLayout(new SpringLayout());
		visibleNone = new JRadioButton("Never");
		visibleOwner = new JRadioButton("Owner");
		visibleTeam = new JRadioButton("Team");
		visibleOwner.setEnabled(false);
		visibleTeam.setEnabled(false);
		visibleAlways = new JRadioButton("Always", true);
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
		visSection.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
		propertiesPanel.add(visSection);
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
		// TODO Auto-generated method stub

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
				object.setXPosition(sliderX.getValue());
				currentX.setText(sliderX.getValue() + "");
			} else if (source == sliderY) {
				object.setYPosition(sliderY.getValue());
				currentY.setText(sliderY.getValue() + "");
			}
		} else {
			java.awt.Color newColor = chooser.getColor();
			Color color = new Color(newColor.getRed(), newColor.getGreen(),
					newColor.getBlue(), newColor.getAlpha());
			DynamicPolygonObject o = (DynamicPolygonObject) object;
			o.setColor(color);
		}
	}

}