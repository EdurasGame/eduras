package de.illonis.eduras.shapecreator.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.util.LinkedList;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import de.illonis.eduras.shapecreator.DataHolder;
import de.illonis.eduras.shapecreator.Vertice;
import de.illonis.eduras.shapecreator.VerticeListException;

/**
 * Lists all contained vertices of the current polygon.
 * 
 * @author illonis
 * 
 */
public class VerticeListPanel extends ScrollablePanel implements
		ListSelectionListener {

	private static final long serialVersionUID = 1L;
	private final DataHolder data;
	private JTable verticeList;

	/**
	 * Creates a new vertice list panel.
	 */
	public VerticeListPanel() {
		data = DataHolder.getInstance();
		setLayout(new BorderLayout());
		buildGui();
	}

	private void buildGui() {

		verticeList = new JTable();

		final RecordTableModel m = new RecordTableModel(verticeList);
		verticeList.setModel(m);
		verticeList.getSelectionModel().addListSelectionListener(this);

		Action up = new AbstractAction() {

			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				JTable table = (JTable) e.getSource();
				int modelRow = Integer.valueOf(e.getActionCommand());
				Vertice v = ((RecordTableModel) table.getModel())
						.getVertice(modelRow);
				try {
					data.getPolygon().moveUpVertice(v);
					m.exchange(modelRow, modelRow - 1);
				} catch (VerticeListException e1) {
				}
			}
		};
		Action down = new AbstractAction() {

			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				JTable table = (JTable) e.getSource();
				int modelRow = Integer.valueOf(e.getActionCommand());
				Vertice v = ((RecordTableModel) table.getModel())
						.getVertice(modelRow);
				try {

					data.getPolygon().moveDownVertice(v);
					m.exchange(modelRow, modelRow + 1);
				} catch (VerticeListException e1) {
				}
			}
		};
		Action delete = new AbstractAction() {

			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				JTable table = (JTable) e.getSource();
				int modelRow = Integer.valueOf(e.getActionCommand());
				Vertice v = ((RecordTableModel) table.getModel())
						.getVertice(modelRow);

				m.remove(modelRow);
				data.getPolygon().removeVertice(v);
			}
		};

		ButtonColumn buttonColumn = new ButtonColumn(verticeList, up, 2);
		verticeList.getColumn(verticeList.getColumnName(2)).setCellRenderer(
				buttonColumn);
		verticeList.getColumn(verticeList.getColumnName(2)).setCellEditor(
				buttonColumn);
		ButtonColumn buttonColumn2 = new ButtonColumn(verticeList, down, 3);
		verticeList.getColumn(verticeList.getColumnName(3)).setCellRenderer(
				buttonColumn2);
		verticeList.getColumn(verticeList.getColumnName(3)).setCellEditor(
				buttonColumn2);
		ButtonColumn buttonColumn3 = new ButtonColumn(verticeList, delete, 4);
		verticeList.getColumn(verticeList.getColumnName(4)).setCellRenderer(
				buttonColumn3);
		verticeList.getColumn(verticeList.getColumnName(4)).setCellEditor(
				buttonColumn3);

		data.setVerticeTableModel(m);
		verticeList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		JScrollPane scrollPanel = new JScrollPane(verticeList);
		setPreferredSize(new Dimension(300, 0));
		scrollPanel
				.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		add(scrollPanel, BorderLayout.CENTER);
		verticeList.getColumnModel().getColumn(2).setPreferredWidth(30);
		verticeList.getColumnModel().getColumn(3).setPreferredWidth(30);
		verticeList.getColumnModel().getColumn(4).setPreferredWidth(30);
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		if (!e.getValueIsAdjusting()) {
			LinkedList<Vertice> l = new LinkedList<Vertice>(data.getPolygon()
					.getVertices());
			int selection = verticeList.getSelectedRow();
			if (selection >= 0)
				data.verticeSelectedOnTable(l.get(selection));
		}
	}
}
