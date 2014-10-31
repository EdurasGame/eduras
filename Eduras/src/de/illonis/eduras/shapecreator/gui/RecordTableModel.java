package de.illonis.eduras.shapecreator.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Icon;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import org.newdawn.slick.geom.Vector2f;

import de.illonis.eduras.images.ImageFiler;
import de.illonis.eduras.shapecreator.DataHolder;

/**
 * Handles the table data.
 * 
 * @author illonis
 * 
 */
public class RecordTableModel extends AbstractTableModel {

	private static final long serialVersionUID = 1L;
	private List<TableRecord> lstRecords;
	private final Icon upIcon, downIcon, deleteIcon;
	private final JTable table;

	RecordTableModel(JTable table) throws IOException {
		this.table = table;
		lstRecords = new ArrayList<TableRecord>();
		upIcon = ImageFiler.loadIcon("shapecreator/icons/button_up.png");
		downIcon = ImageFiler.loadIcon("shapecreator/icons/button_down.png");
		deleteIcon = ImageFiler
				.loadIcon("shapecreator/icons/button_delete.png");
	}

	@Override
	public void fireTableDataChanged() {
		lstRecords.clear();
		DataHolder data = DataHolder.getInstance();
		for (Vector2f v : data.getPolygon().getVector2dfs()) {
			lstRecords.add(new TableRecord(v));
		}
		super.fireTableDataChanged();
	}

	/**
	 * Adds a table record to the table's data.
	 * 
	 * @param record
	 *            the new record.
	 */
	public void add(TableRecord record) {
		lstRecords.add(record);
		fireTableRowsInserted(lstRecords.size() - 1, lstRecords.size() - 1);
	}

	/**
	 * Removes a record from table data.
	 * 
	 * @param record
	 *            the record to remove.
	 */
	public void remove(TableRecord record) {
		if (lstRecords.contains(record)) {
			int index = lstRecords.indexOf(record);
			remove(index);
		}
	}

	/**
	 * Exchanges two given rows.
	 * 
	 * @param first
	 *            first row index.
	 * @param second
	 *            second row index.
	 */
	public void exchange(int first, int second) {
		TableRecord firstRecord = lstRecords.get(first);
		TableRecord secondRecord = lstRecords.get(second);

		lstRecords.set(first, secondRecord);
		lstRecords.set(second, firstRecord);

	}

	/**
	 * Removes given row.
	 * 
	 * @param index
	 *            the index of the row that should be removed.
	 */
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

	/**
	 * Retrieves a vertice by row index.
	 * 
	 * @param row
	 *            the row index.
	 * @return the vertice that is represented by given row.
	 */
	public Vector2f getVector2df(int row) {
		return lstRecords.get(row).getVector2df();
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
			record.setX((aValue == null ? 0 : Float.parseFloat(aValue
					.toString())));
			fireTableCellUpdated(rowIndex, columnIndex);
			break;
		case 1:
			if (aValue != null) {
				try {
					float value = Float.parseFloat(aValue.toString());
					record.setY(value);
					fireTableCellUpdated(rowIndex, columnIndex);
				} catch (NumberFormatException e) {

				}
			}
			break;
		}
	}

	/**
	 * Mark the row that contains given vertice as selected.
	 * 
	 * @param selectedVector2df
	 *            the vertice of row that should selected.
	 */
	public void selectVector2df(Vector2f selectedVector2df) {
		for (int i = 0; i < lstRecords.size(); i++) {
			TableRecord record = lstRecords.get(i);
			if (record.getVector2df().equals(selectedVector2df))
				table.setRowSelectionInterval(i, i);
		}

	}
}