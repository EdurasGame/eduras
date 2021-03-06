package de.illonis.eduras.shapecreator.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.LinkedList;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.newdawn.slick.geom.Vector2f;

import de.illonis.eduras.shapecreator.DataHolder;
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
	 * @throws IOException 
	 */
	public VerticeListPanel() throws IOException {
		data = DataHolder.getInstance();
		setLayout(new BorderLayout());
		buildGui();
	}

	private void buildGui() throws IOException {

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
				Vector2f v = ((RecordTableModel) table.getModel())
						.getVector2df(modelRow);
				try {
					data.getPolygon().moveUpVector2df(v);
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
				Vector2f v = ((RecordTableModel) table.getModel())
						.getVector2df(modelRow);
				try {

					data.getPolygon().moveDownVector2df(v);
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
				Vector2f v = ((RecordTableModel) table.getModel())
						.getVector2df(modelRow);

				m.remove(modelRow);
				data.getPolygon().removeVector2df(v);
			}
		};

		ButtonColumn buttonColumn = new ButtonColumn(verticeList, up, 2);
		verticeList.getColumnModel().getColumn(2).setCellRenderer(buttonColumn);
		verticeList.getColumnModel().getColumn(2).setCellEditor(buttonColumn);
		ButtonColumn buttonColumn2 = new ButtonColumn(verticeList, down, 3);
		verticeList.getColumnModel().getColumn(3)
				.setCellRenderer(buttonColumn2);
		verticeList.getColumnModel().getColumn(3).setCellEditor(buttonColumn2);
		ButtonColumn buttonColumn3 = new ButtonColumn(verticeList, delete, 4);
		verticeList.getColumnModel().getColumn(4)
				.setCellRenderer(buttonColumn3);
		verticeList.getColumnModel().getColumn(4).setCellEditor(buttonColumn3);
		verticeList.getColumnModel().getColumn(0).setHeaderValue("X");
		verticeList.getColumnModel().getColumn(1).setHeaderValue("Y");
		verticeList.getColumnModel().getColumn(2).setHeaderValue("up");
		verticeList.getColumnModel().getColumn(3).setHeaderValue("down");
		verticeList.getColumnModel().getColumn(4).setHeaderValue("del");

		data.setVector2dfTableModel(m);
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
			LinkedList<Vector2f> l = new LinkedList<Vector2f>(data.getPolygon()
					.getVector2dfs());
			int selection = verticeList.getSelectedRow();
			if (selection >= 0)
				data.verticeSelectedOnTable(l.get(selection));
		}
	}
}
