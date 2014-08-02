package de.illonis.eduras.shapecreator;

import java.util.ArrayList;
import java.util.Collection;

import de.illonis.eduras.math.Vector2df;
import de.illonis.eduras.shapecreator.templates.TemplateManager;
import de.illonis.eduras.shapecreator.templates.TemplateNotFoundException;

/**
 * An editable polygon that can be changed dynamically.
 * 
 * @author illonis
 * 
 */
public class EditablePolygon {

	private final ArrayList<Vector2df> vertices;

	protected EditablePolygon() {
		vertices = new ArrayList<Vector2df>();
	}

	/**
	 * Added a vertice to this polygon.
	 * 
	 * @param v
	 *            the new vertice.
	 */
	public void addVector2df(Vector2df v) {
		vertices.add(v);
		DataHolder.getInstance().notifyVector2dfsChanged();
	}

	/**
	 * Removes given vertice.
	 * 
	 * @param v
	 *            the vertice to be removed.
	 */
	public void removeVector2df(Vector2df v) {
		if (vertices.size() > 1) {
			vertices.remove(v);
			DataHolder.getInstance().notifyVector2dfsChanged();
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
	public void moveDownVector2df(Vector2df v) throws VerticeListException {
		int index = vertices.indexOf(v);
		if (index == vertices.size() - 1)
			throw new VerticeListException("Cannot move vertice down.");
		exchangeVector2dfs(index, index + 1);
	}

	/**
	 * Moves up given vertice one position in the list.
	 * 
	 * @param v
	 *            the vertice to move up.
	 * @throws VerticeListException
	 *             if vertice is at top list position.
	 */
	public void moveUpVector2df(Vector2df v) throws VerticeListException {
		int index = vertices.indexOf(v);
		if (index == 0)
			throw new VerticeListException("Cannot move vertice up.");
		exchangeVector2dfs(index, index - 1);
	}

	private void exchangeVector2dfs(int firstIndex, int secondIndex) {
		Vector2df first = vertices.get(firstIndex);
		Vector2df second = vertices.get(secondIndex);
		vertices.set(firstIndex, second);
		vertices.set(secondIndex, first);
		DataHolder.getInstance().notifyVector2dfsChanged();
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
	public Vector2df findNearestVector2df(Vector2df searchPoint)
			throws NoVerticeFoundException {
		if (vertices.size() == 0)
			throw new NoVerticeFoundException();
		float distance = Float.MAX_VALUE;
		Vector2df result = null;
		for (Vector2df thisVert : vertices) {
			float thisDistance = thisVert.distance(searchPoint);
			if (thisDistance < distance) {
				result = thisVert;
				distance = thisDistance;
			}
		}
		return result;
	}

	/**
	 * Returns the vertice that is before this one.
	 * 
	 * @param vector
	 *            the vector.
	 * @return the vector before given vector.
	 */
	public Vector2df findBefore(Vector2df vector) {
		int i = vertices.indexOf(vector);
		if (i == 0) {
			return vertices.get(vertices.size() - 1);
		} else
			return vertices.get(i - 1);
	}

	/**
	 * Returns the vertice that is after this one.
	 * 
	 * @param vector
	 *            the vector.
	 * @return the vector after given vector.
	 */
	public Vector2df findAfter(Vector2df vector) {
		int i = vertices.indexOf(vector);
		if (i == vertices.size() - 1) {
			return vertices.get(0);
		} else
			return vertices.get(i + 1);
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
	public Collection<Vector2df> getVector2dfs() {
		return vertices;
	}

	/**
	 * Adds a vertice after another one.
	 * 
	 * @param vert
	 *            the vertice to add.
	 * @param nearest
	 *            the vertice after that given vertice should be added.
	 */
	public void addVerticeAfter(Vector2df vert, Vector2df nearest) {
		int index = vertices.indexOf(nearest);
		if (index == vertices.size() - 1) {
			vertices.add(vert);
		} else
			vertices.add(vertices.indexOf(nearest) + 1, vert);
		DataHolder.getInstance().notifyVector2dfsChanged();

	}

	/**
	 * Adds a vertice before another one.
	 * 
	 * @param vert
	 *            the vertice to add.
	 * @param nearest
	 *            the vertice before that given vertice should be added.
	 */
	public void addVerticeBefore(Vector2df vert, Vector2df nearest) {
		vertices.add(vertices.indexOf(nearest), vert);
		DataHolder.getInstance().notifyVector2dfsChanged();

	}
}