package de.illonis.eduras.shapecreator;

import java.io.File;

import de.illonis.eduras.shapecreator.PanelInteractor.InteractMode;
import de.illonis.eduras.shapecreator.templates.TemplateNotFoundException;

/**
 * Provides methods to react on menu input.
 * 
 * @author illonis
 * 
 */
public interface MenuActionReactor {

	/**
	 * The mirror axis.
	 * 
	 * @author illonis
	 * 
	 */
	@SuppressWarnings("javadoc")
	public enum Axis {
		VERTICAL, HORIZONTAL;
	}

	/**
	 * Exits the shape creator.
	 */
	void exit();

	/**
	 * Undoes the last editing step.
	 */
	void undo();

	/**
	 * Redoes the last undone step.
	 */
	void redo();

	/**
	 * Sets the zoom to the given new zoom-factor.
	 * 
	 * @param factor
	 *            new zom-factor.
	 */
	void setZoom(float factor);

	/**
	 * Modifies zoom by given value.
	 * 
	 * @param modifier
	 *            modifier.
	 */
	void modZoom(float modifier);

	/**
	 * Resets the panel to default view.
	 */
	void resetPanel();

	/**
	 * Loads a new empty shape into the editor.
	 */
	void newShape();

	/**
	 * Loads a new shape into the editor that inherits from the given template.
	 * 
	 * @param template
	 *            the name of the used template.
	 * @throws TemplateNotFoundException
	 *             if that template was not found.
	 */
	void newShape(String template) throws TemplateNotFoundException;

	/**
	 * Loads a shape from the given file.
	 * 
	 * @param file
	 *            source file.
	 */
	void openShape(File file);

	/**
	 * Saves a shape to the given file.
	 * 
	 * @param file
	 *            target file.
	 */
	void saveShape(File file);

	/**
	 * Rotates the shape by given angle.
	 * 
	 * @param angle
	 *            the rotation angle (in degree).
	 */
	void rotateShape(float angle);

	/**
	 * Mirrors the shape at given axis.
	 * 
	 * @param axis
	 *            the mirror axis.
	 */
	void mirrorShape(Axis axis);

	/**
	 * Sets the editing mode.
	 * 
	 * @param mode
	 *            new mode.
	 */
	void setMode(InteractMode mode);

	/**
	 * Loads given file as background image.
	 * 
	 * @param file
	 *            the file.
	 */
	void loadBackgroundImage(File file);

}
