package de.illonis.eduras.shapecreator;

import java.awt.Color;

import de.illonis.eduras.shapecreator.gui.DrawPanel;
import de.illonis.eduras.shapecreator.gui.RecordTableModel;

/**
 * Contains all data of the shapecreator. This is a singleton.
 * 
 * @author illonis
 * 
 */
public class DataHolder {

	private static DataHolder instance;
	private RecordTableModel tableModel;
	private DrawPanel drawPanel;
	private EditablePolygon polygon;

	private final Settings settings;

	private float zoom = 1.0f;

	/**
	 * @return the data holder instance.
	 * 
	 * @author illonis
	 */
	public static DataHolder getInstance() {
		if (instance == null)
			instance = new DataHolder();
		return instance;
	}

	public void loadPolygon(EditablePolygon polygon) {
		this.polygon = polygon;
		tableModel.fireTableDataChanged();
	}

	private DataHolder() {
		instance = this;
		settings = new Settings();
		polygon = new EditablePolygon();
	}

	public EditablePolygon getPolygon() {
		return polygon;
	}

	public float getZoom() {
		return zoom;
	}

	public void setZoom(float zoom) {
		if (zoom < 0.1f)
			return;
		this.zoom = zoom;
	}

	public void setVerticeTableModel(RecordTableModel tableModel) {
		this.tableModel = tableModel;
	}

	public void setDrawPanel(DrawPanel drawPanel) {
		this.drawPanel = drawPanel;
	}

	public void notifyVerticesChanged() {
		if (tableModel != null) {
			tableModel.fireTableDataChanged();
		}
	}

	public Settings getSettings() {
		return settings;
	}

	public class Settings {
		private Color gridColor = new Color(255, 199, 218);
		private Color shapeLineColor = Color.BLACK;
		private Color shapeDotColor = Color.BLACK;
		private Color backgroundColor = Color.WHITE;
		private Color hoverShapeDotColor = Color.BLUE;
		private Color selectedShapeDotColor = Color.RED;

		private Settings() {

		}

		public Color getHoverShapeDotColor() {
			return hoverShapeDotColor;
		}

		public void setHoverShapeDotColor(Color hoverShapeDotColor) {
			this.hoverShapeDotColor = hoverShapeDotColor;
		}

		public Color getBackgroundColor() {
			return backgroundColor;
		}

		public void setBackgroundColor(Color backgroundColor) {
			this.backgroundColor = backgroundColor;
		}

		public Color getGridColor() {
			return gridColor;
		}

		public Color getShapeDotColor() {
			return shapeDotColor;
		}

		public Color getShapeLineColor() {
			return shapeLineColor;
		}

		public void setGridColor(Color gridColor) {
			this.gridColor = gridColor;
		}

		public void setShapeDotColor(Color shapeDotColor) {
			this.shapeDotColor = shapeDotColor;
		}

		public void setShapeLineColor(Color shapeLineColor) {
			this.shapeLineColor = shapeLineColor;
		}

		public Color getSelectedShapeDotColor() {
			return selectedShapeDotColor;
		}

		public void setSelectedShapeDotColor(Color selectedShapeDotColor) {
			this.selectedShapeDotColor = selectedShapeDotColor;
		}
	}

	public void verticeSelectedOnGui(Vertice selectedVertice) {
		tableModel.selectVertice(selectedVertice);
	}

	public void verticeSelectedOnTable(Vertice selectedVertice) {
		drawPanel.onVerticeSelected(selectedVertice);
	}
}
