package de.illonis.eduras.shapecreator.gui;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Icon;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import de.illonis.eduras.images.ImageFiler;
import de.illonis.eduras.shapecreator.Vertice;

public class RecordTableModel extends AbstractTableModel {

	private List<TableRecord> lstRecords;
	private final static Color DEFAULT_BACKGROUND = Color.WHITE;
	private final static Color SELECTED_BACKGROUND = Color.GRAY;
	private final Icon upIcon, downIcon, deleteIcon;
	private final JTable table;

	public RecordTableModel(JTable table) {
		this.table = table;
		lstRecords = new ArrayList<TableRecord>();
		upIcon = ImageFiler.loadIcon("shapecreator/icons/button_up.png");
		downIcon = ImageFiler.loadIcon("shapecreator/icons/button_down.png");
		deleteIcon = ImageFiler
				.loadIcon("shapecreator/icons/button_delete.png");
	}

	public void add(TableRecord record) {
		lstRecords.add(record);
		fireTableRowsInserted(lstRecords.size() - 1, lstRecords.size() - 1);
	}

	public void remove(TableRecord record) {
		if (lstRecords.contains(record)) {
			int index = lstRecords.indexOf(record);
			remove(index);
		}
	}

	public void exchange(int first, int second) {
		TableRecord firstRecord = lstRecords.get(first);
		TableRecord secondRecord = lstRecords.get(second);

		lstRecords.set(first, secondRecord);
		lstRecords.set(second, firstRecord);

	}

	public void remove(int index) {
		lstRecords.remove(index);
		fireTableRowsDeleted(index, index);
	}

	@Override
	public int getRowCount() {
		return lstRecords.size();
	}

	@Override
	public int getColumnCount() {
		return 5;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		TableRecord record = lstRecords.get(rowIndex);
		Object value = null;
		switch (columnIndex) {
		case 0:
			value = record.getX();
			break;
		case 1:
			value = record.getY();
			break;
		case 2:
			value = upIcon;
			break;
		case 3:
			value = downIcon;
			break;
		case 4:
			value = deleteIcon;
			break;
		}
		return value;
	}

	public Vertice getVertice(int row) {
		return lstRecords.get(row).getVertice();
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return true;
	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		TableRecord record = lstRecords.get(rowIndex);
		switch (columnIndex) {
		case 0:
			record.setX((aValue == null ? 0 : Double.parseDouble(aValue
					.toString())));
			fireTableCellUpdated(rowIndex, columnIndex);
			break;
		case 1:
			if (aValue != null) {
				try {
					double value = Double.parseDouble(aValue.toString());
					record.setY(value);
					fireTableCellUpdated(rowIndex, columnIndex);
				} catch (NumberFormatException e) {

				}
			}
			break;
		}
	}

	public void selectVertice(Vertice selectedVertice) {
		for (int i = 0; i < lstRecords.size(); i++) {
			TableRecord record = lstRecords.get(i);
			if (record.getVertice().equals(selectedVertice))
				table.setRowSelectionInterval(i, i);
		}

	}
}