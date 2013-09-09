package de.illonis.eduras.shapecreator.gui;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

import de.illonis.eduras.shapecreator.DataHolder;
import de.illonis.eduras.shapecreator.Vertice;
import de.illonis.eduras.shapecreator.VerticeListException;

/**
 * Lists all contained vertices of the current polygon.
 * 
 * @author illonis
 * 
 */
public class VerticeListPanel extends JPanel {
	private final DataHolder data;
	private JTable verticeList;

	public VerticeListPanel() {
		data = DataHolder.getInstance();

		buildGui();
	}

	private void buildGui() {

		verticeList = new JTable();
		final RecordTableModel m = new RecordTableModel(verticeList);
		verticeList.setModel(m);
		for (Vertice v : data.getPolygon().getVertices()) {
			m.add(new TableRecord(v));
		}
		Action up = new AbstractAction() {
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
		buttonColumn.setMnemonic(KeyEvent.VK_U);
		verticeList.getColumn(verticeList.getColumnName(2)).setCellRenderer(
				buttonColumn);
		verticeList.getColumn(verticeList.getColumnName(2)).setCellEditor(
				buttonColumn);
		ButtonColumn buttonColumn2 = new ButtonColumn(verticeList, down, 3);
		buttonColumn.setMnemonic(KeyEvent.VK_D);
		verticeList.getColumn(verticeList.getColumnName(3)).setCellRenderer(
				buttonColumn2);
		verticeList.getColumn(verticeList.getColumnName(3)).setCellEditor(
				buttonColumn2);
		ButtonColumn buttonColumn3 = new ButtonColumn(verticeList, delete, 4);
		buttonColumn.setMnemonic(KeyEvent.VK_R);
		verticeList.getColumn(verticeList.getColumnName(4)).setCellRenderer(
				buttonColumn3);
		verticeList.getColumn(verticeList.getColumnName(4)).setCellEditor(
				buttonColumn3);

		data.setVerticeTableModel(m);
		verticeList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		JScrollPane scrollPanel = new JScrollPane(verticeList);
		scrollPanel
				.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		add(scrollPanel);
	}

}
