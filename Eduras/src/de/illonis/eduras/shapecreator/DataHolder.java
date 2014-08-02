package de.illonis.eduras.shapecreator;

import java.awt.Color;
import java.awt.Image;

import de.illonis.eduras.math.Vector2df;
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
	private Image backgroundImage;

	private final Settings settings;

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
	
	public Image getBackgroundImage() {
		return backgroundImage;
	}
	
	public void setBackgroundImage(Image backgroundImage) {
		this.backgroundImage = backgroundImage;
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
	 * Sets the table model.
	 * 
	 * @param tableModel
	 *            new table model.
	 */
	public void setVector2dfTableModel(RecordTableModel tableModel) {
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
	 * Notifies that vertice data has changed and updates table values.
	 */
	public void notifyVector2dfsChanged() {
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
		private Color shapeLastLineColor = new Color(150, 150, 150);

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

		/**
		 * @return the color for line that connects first and last vertice.
		 */
		public Color getShapeLastLineColor() {
			return shapeLastLineColor;
		}

		/**
		 * Sets the color of the shape line connecting last and first vertice.
		 * 
		 * @param shapeLastLineColor
		 *            the new color.
		 */
		public void setShapeLastLineColor(Color shapeLastLineColor) {
			this.shapeLastLineColor = shapeLastLineColor;
		}
	}

	/**
	 * Notify that a vertice was selected on gui.
	 * 
	 * @param selectedVector2df
	 *            the selected vertice.
	 */
	public void verticeSelectedOnGui(Vector2df selectedVector2df) {
		tableModel.selectVector2df(selectedVector2df);
	}

	/**
	 * Notify that a vertice was selected in table.
	 * 
	 * @param selectedVector2df
	 *            the selected vertice.
	 */
	public void verticeSelectedOnTable(Vector2df selectedVector2df) {
		drawPanel.onVector2dfSelected(selectedVector2df);
	}
}
