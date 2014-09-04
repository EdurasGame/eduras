package de.illonis.eduras.mapeditor.validate;

import org.newdawn.slick.geom.Rectangle;

import de.illonis.eduras.mapeditor.EditorPlaceable;
import de.illonis.eduras.math.Geometry;

/**
 * Checks if any gameobjects are outside the map.
 * 
 * @author illonis
 * 
 */
public class OutsideValidator extends ValidateTask {
	private Rectangle mapBounds;

	protected OutsideValidator() {
		super("Object positions");
	}

	@Override
	protected boolean performValidation() {
		mapBounds = new Rectangle(0, 0, data.getWidth(), data.getHeight());
		boolean ok = true;
		for (EditorPlaceable element : data.getGameObjects()) {
			if (!checkElementInBounds(element)) {
				ok = false;
			}
		}
		for (EditorPlaceable element : data.getBases()) {
			if (!checkElementInBounds(element)) {
				ok = false;
			}
		}
		for (EditorPlaceable element : data.getSpawnPoints()) {
			if (!checkElementInBounds(element)) {
				ok = false;
			}
		}
		return ok;
	}

	private boolean checkElementInBounds(EditorPlaceable element) {
		Rectangle elementBounds = new Rectangle(element.getXPosition(),
				element.getYPosition(), element.getWidth(), element.getHeight());

		if (!Geometry.rectangleContains(mapBounds, elementBounds)) {
			addErrorMessage(element.getClass().getSimpleName() + " "
					+ element.getRefName() + " is outside the map.");
			return false;
		}
		return true;
	}
}
