package de.illonis.eduras.mapeditor.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import de.illonis.eduras.ObjectFactory.ObjectType;
import de.illonis.eduras.mapeditor.MapEditor;
import de.illonis.eduras.mapeditor.MapInteractor;
import de.illonis.eduras.mapeditor.MapInteractor.InteractType;

/**
 * Displays list of objecttypes that can be placed in editor.
 * 
 * @author illonis
 * 
 */
public class ObjectPlacingSelectionPanel extends JPanel implements
		ListSelectionListener {

	private static final long serialVersionUID = 1L;
	private final DefaultListModel<ObjectType> typeListModel;
	private final JList<ObjectType> typesList;
	private final MapInteractor interactor;
	private final ToolMenuBar quickMenu;

	private final class TypeComparator implements Comparator<ObjectType> {

		@Override
		public int compare(ObjectType a, ObjectType b) {
			return a.name().compareTo(b.name());
		}

	}

	ObjectPlacingSelectionPanel(MapInteractor interactor, ToolMenuBar quickMenu) {
		super(new BorderLayout());
		this.quickMenu = quickMenu;
		this.interactor = interactor;
		Dimension dimension = new Dimension(200, 100);
		setPreferredSize(dimension);
		setSize(200, 100);
		typeListModel = new DefaultListModel<ObjectType>();
		List<ObjectType> types = new LinkedList<ObjectType>();
		for (ObjectType type : ObjectType.values()) {
			if (MapEditor.isSupported(type))
				types.add(type);
		}
		Collections.sort(types, new TypeComparator());
		for (ObjectType type : types)
			typeListModel.addElement(type);
		typesList = new JList<ObjectType>(typeListModel);
		JScrollPane scroller = new JScrollPane(typesList);
		scroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		add(scroller, BorderLayout.CENTER);
		typesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		typesList.setLayoutOrientation(JList.VERTICAL);
		typesList.setVisibleRowCount(-1);
		typesList.addListSelectionListener(this);
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		if (!e.getValueIsAdjusting()) {
			if (typesList.getSelectedIndex() == -1) {
				interactor.setSpawnType(ObjectType.NO_OBJECT);
			} else {
				quickMenu.deselectAll();
				interactor.setInteractType(InteractType.PLACE_OBJECT);
				interactor.setSpawnType(typesList.getSelectedValue());
			}
		}
	}

	void deselect() {
		typesList.clearSelection();
	}

}
