package de.illonis.eduras.shapecreator;

import java.awt.Image;

import de.illonis.eduras.math.Vector2df;
import de.illonis.eduras.shapecreator.PanelInteractor.InteractMode;

/**
 * Allows modifications on the drawpanel.
 * 
 * @author illonis
 * 
 */
public interface PanelModifier {

	/**
	 * Sets the zoom of the panel.
	 * 
	 * @param zoom
	 *            new zoom value.
	 */
	void setZoom(float zoom);

	/**
	 * Modifies the zoom of the panel.
	 * 
	 * @param modifier
	 *            zoom increment.
	 */
	void modZoom(float modifier);

	/**
	 * Resets panel: Resets zoom to default value and puts origin in window
	 * center.
	 */
	void resetPanel();

	/**
	 * Marks given vertice as selected.
	 * 
	 * @param vert
	 *            the selected vertice.
	 */
	void selectVector2df(Vector2df vert);

	/**
	 * Sets the editing mode.
	 * 
	 * @param mode
	 *            the new mode.
	 */
	void setMode(InteractMode mode);

	/**
	 * Undoes the last action.
	 * 
	 * @return true if undoing was successful.
	 */
	boolean undo();

	/**
	 * Redoes the last undo action.
	 * 
	 * @return true if redoing was successful.
	 */
	boolean redo();

	/**
	 * Sets the shape that is currently edited.
	 * 
	 * @param polygon
	 *            new polygon.
	 */
	void setShape(EditablePolygon polygon);

	/**
	 * @return current displayed shape.
	 */
	EditablePolygon getShape();
	
	void setBackgroundImage(Image image);

}
