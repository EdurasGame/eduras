package de.illonis.eduras.shapecreator;

import java.awt.Color;

import de.illonis.eduras.shapecreator.gui.DrawPanel;
import de.illonis.eduras.shapecreator.gui.RecordTableModel;
import de.illonis.eduras.shapecreator.gui.ToolPanel;

/**
 * Contains all data of the shapecreator. This is a singleton.
 * 
 * @author illonis
 * 
 */
public class DataHolder {

	private static DataHolder instance;
	private RecordTableModel tableModel;
	private ToolPanel toolPanel;
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

	/**
	 * Loads given polygon into the editor.
	 * 
	 * @param newPolygon
	 *            the new edtor.
	 */
	public void loadPolygon(EditablePolygon newPolygon) {
		this.polygon = newPolygon;
		tableModel.fireTableDataChanged();
	}

	private DataHolder() {
		instance = this;
		settings = new Settings();
		polygon = new EditablePolygon();
	}

	/**
	 * Returns the currently edited polygon.
	 * 
	 * @return active polygon.
	 */
	public EditablePolygon getPolygon() {
		return polygon;
	}

	/**
	 * @return current zoom value.
	 */
	public float getZoom() {
		return zoom;
	}

	/**
	 * Sets the zoom to a new value.
	 * 
	 * @param zoom
	 *            new zoom value.
	 */
	public void setZoom(float zoom) {
		if (zoom < 0.1f)
			return;
		this.zoom = zoom;
	}

	/**
	 * Sets the table model.
	 * 
	 * @param tableModel
	 *            new table model.
	 */
	public void setVerticeTableModel(RecordTableModel tableModel) {
		this.tableModel = tableModel;
	}

	/**
	 * Sets the draw panel.
	 * 
	 * @param drawPanel
	 *            the new drawpanel.
	 */
	public void setDrawPanel(DrawPanel drawPanel) {
		this.drawPanel = drawPanel;
	}

	/**
	 * Sets the toolpanel.
	 * 
	 * @param toolPanel
	 *            tool panel.
	 */
	public void setToolPanel(ToolPanel toolPanel) {
		this.toolPanel = toolPanel;
	}

	/**
	 * Notifies that vertice data has changed and updates table values.
	 */
	public void notifyVerticesChanged() {
		if (tableModel != null) {
			tableModel.fireTableDataChanged();
		}
	}

	/**
	 * @return the current settings.
	 */
	public Settings getSettings() {
		return settings;
	}

	/**
	 * Holds settings.
	 * 
	 * @author illonis
	 * 
	 */
	public class Settings {
		private Color gridColor = new Color(255, 199, 218);
		private Color shapeLineColor = Color.BLACK;
		private Color shapeDotColor = Color.BLACK;
		private Color backgroundColor = Color.WHITE;
		private Color hoverShapeDotColor = Color.BLUE;
		private Color selectedShapeDotColor = Color.RED;

		private Settings() {
		}

		/**
		 * @return the color of hovered shape dots.
		 */
		public Color getHoverShapeDotColor() {
			return hoverShapeDotColor;
		}

		/**
		 * Sets the color of hovered shape dots.
		 * 
		 * @param hoverShapeDotColor
		 *            new color.
		 */
		public void setHoverShapeDotColor(Color hoverShapeDotColor) {
			this.hoverShapeDotColor = hoverShapeDotColor;
		}

		/**
		 * @return the background color of the drawpanel.
		 */
		public Color getBackgroundColor() {
			return backgroundColor;
		}

		/**
		 * Sets the color of the panel background.
		 * 
		 * @param backgroundColor
		 *            new color.
		 */
		public void setBackgroundColor(Color backgroundColor) {
			this.backgroundColor = backgroundColor;
		}

		/**
		 * @return the grid color.
		 */
		public Color getGridColor() {
			return gridColor;
		}

		/**
		 * @return the color of shapedots.
		 */
		public Color getShapeDotColor() {
			return shapeDotColor;
		}

		/**
		 * @return the color of shapelines.
		 */
		public Color getShapeLineColor() {
			return shapeLineColor;
		}

		/**
		 * Sets the color of the grid.
		 * 
		 * @param gridColor
		 *            the new color.
		 */
		public void setGridColor(Color gridColor) {
			this.gridColor = gridColor;
		}

		/**
		 * Sets the color of the shape dots.
		 * 
		 * @param shapeDotColor
		 *            the new color.
		 */
		public void setShapeDotColor(Color shapeDotColor) {
			this.shapeDotColor = shapeDotColor;
		}

		/**
		 * Sets the color of shape lines.
		 * 
		 * @param shapeLineColor
		 *            the new color.
		 */
		public void setShapeLineColor(Color shapeLineColor) {
			this.shapeLineColor = shapeLineColor;
		}

		/**
		 * @return the color of selected shapedots.
		 */
		public Color getSelectedShapeDotColor() {
			return selectedShapeDotColor;
		}

		/**
		 * Sets the color of selected shape dots.
		 * 
		 * @param selectedShapeDotColor
		 *            the new color.
		 */
		public void setSelectedShapeDotColor(Color selectedShapeDotColor) {
			this.selectedShapeDotColor = selectedShapeDotColor;
		}
	}

	/**
	 * Notify that a vertice was selected on gui.
	 * 
	 * @param selectedVertice
	 *            the selected vertice.
	 */
	public void verticeSelectedOnGui(Vertice selectedVertice) {
		tableModel.selectVertice(selectedVertice);
	}

	/**
	 * Notify that a vertice was selected in table.
	 * 
	 * @param selectedVertice
	 *            the selected vertice.
	 */
	public void verticeSelectedOnTable(Vertice selectedVertice) {
		drawPanel.onVerticeSelected(selectedVertice);
	}
}
