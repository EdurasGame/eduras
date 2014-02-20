package de.illonis.eduras.shapecreator;

import java.util.ArrayList;
import java.util.Collection;

import de.illonis.eduras.shapecreator.templates.TemplateManager;
import de.illonis.eduras.shapecreator.templates.TemplateNotFoundException;

/**
 * An editable polygon that can be changed dynamically.
 * 
 * @author illonis
 * 
 */
public class EditablePolygon {

	private final ArrayList<Vertice> vertices;

	protected EditablePolygon() {
		vertices = new ArrayList<Vertice>();
	}

	/**
	 * Added a vertice to this polygon.
	 * 
	 * @param v
	 *            the new vertice.
	 */
	public void addVertice(Vertice v) {
		vertices.add(v);
		DataHolder.getInstance().notifyVerticesChanged();
	}

	/**
	 * Removes given vertice.
	 * 
	 * @param v
	 *            the vertice to be removed.
	 */
	public void removeVertice(Vertice v) {
		if (vertices.size() > 1) {
			vertices.remove(v);
			DataHolder.getInstance().notifyVerticesChanged();
		}
	}

	/**
	 * Moves down given vertice one position in the list.
	 * 
	 * @param v
	 *            the vertice to move down.
	 * @throws VerticeListException
	 *             if vertice is at bottom list position.
	 */
	public void moveDownVertice(Vertice v) throws VerticeListException {
		int index = vertices.indexOf(v);
		if (index == vertices.size() - 1)
			throw new VerticeListException("Cannot move vertice down.");
		exchangeVertices(index, index + 1);
	}

	/**
	 * Moves up given vertice one position in the list.
	 * 
	 * @param v
	 *            the vertice to move up.
	 * @throws VerticeListException
	 *             if vertice is at top list position.
	 */
	public void moveUpVertice(Vertice v) throws VerticeListException {
		int index = vertices.indexOf(v);
		if (index == 0)
			throw new VerticeListException("Cannot move vertice up.");
		exchangeVertices(index, index - 1);
	}

	private void exchangeVertices(int firstIndex, int secondIndex) {
		Vertice first = vertices.get(firstIndex);
		Vertice second = vertices.get(secondIndex);
		vertices.set(firstIndex, second);
		vertices.set(secondIndex, first);
		DataHolder.getInstance().notifyVerticesChanged();
	}

	/**
	 * Searches for nearby vertices and returns the nearest vertice that this
	 * polygon contains.
	 * 
	 * @param searchPoint
	 *            the location to search at.
	 * @return the nearest vertice in this polygon.
	 * @throws NoVerticeFoundException
	 *             if no vertice is near the point (this is when vertice list is
	 *             empty).
	 */
	public Vertice findNearestVertice(Vertice searchPoint)
			throws NoVerticeFoundException {
		if (vertices.size() == 0)
			throw new NoVerticeFoundException();
		double distance = Double.MAX_VALUE;
		Vertice result = null;
		for (Vertice thisVert : vertices) {
			double thisDistance = thisVert.calculateDistance(searchPoint);
			if (thisDistance < distance) {
				result = thisVert;
				distance = thisDistance;
			}
		}
		return result;
	}

	/**
	 * Imports vertices from given template.
	 * 
	 * @param templateName
	 *            the name of the template.
	 * @throws TemplateNotFoundException
	 *             if template was not found.
	 */
	public void importTemplate(String templateName)
			throws TemplateNotFoundException {
		vertices.clear();
		TemplateManager manager = TemplateManager.getInstance();
		vertices.addAll(manager.getVertsOfTemplate(templateName));
	}

	/**
	 * Returns a list of vertices in this polygon.
	 * 
	 * @return the vertices of this polygon.
	 */
	public Collection<Vertice> getVertices() {
		return vertices;
	}
}