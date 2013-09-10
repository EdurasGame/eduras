package de.illonis.eduras.shapecreator;

import de.illonis.eduras.shapecreator.PanelInteractor.InteractMode;

public interface PanelModifier {

	void setZoom(float zoom);

	void resetPanel();

	void selectVertice(Vertice vert);

	void setMode(InteractMode mode);

	boolean undo();

	boolean redo();

	void setShape(EditablePolygon polygon);

}
