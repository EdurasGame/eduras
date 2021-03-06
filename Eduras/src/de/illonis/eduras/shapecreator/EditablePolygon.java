package de.illonis.eduras.shapecreator;

import java.util.ArrayList;
import java.util.Collection;

import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Vector2f;

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

	/**
	 * Mirror at x-axis.
	 */
	public final static int X_AXIS = 1;
	/**
	 * Mirror at y-axis.
	 */
	public final static int Y_AXIS = 0;

	private final ArrayList<Vector2f> vertices;

	/**
	 * Creates a new empty polygon.
	 */
	public EditablePolygon() {
		vertices = new ArrayList<Vector2f>();
	}

	/**
	 * Added a vertice to this polygon.
	 * 
	 * @param v
	 *            the new vertice.
	 */
	public void addVector2df(Vector2f v) {
		vertices.add(v);
		DataHolder.getInstance().notifyVector2dfsChanged();
	}

	/**
	 * Removes given vertice.
	 * 
	 * @param v
	 *            the vertice to be removed.
	 */
	public void removeVector2df(Vector2f v) {
		vertices.remove(v);
		DataHolder.getInstance().notifyVector2dfsChanged();
	}

	/**
	 * Moves down given vertice one position in the list.
	 * 
	 * @param v
	 *            the vertice to move down.
	 * @throws VerticeListException
	 *             if vertice is at bottom list position.
	 */
	public void moveDownVector2df(Vector2f v) throws VerticeListException {
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
	public void moveUpVector2df(Vector2f v) throws VerticeListException {
		int index = vertices.indexOf(v);
		if (index == 0)
			throw new VerticeListException("Cannot move vertice up.");
		exchangeVector2dfs(index, index - 1);
	}

	private void exchangeVector2dfs(int firstIndex, int secondIndex) {
		Vector2f first = vertices.get(firstIndex);
		Vector2f second = vertices.get(secondIndex);
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
	public Vector2f findNearestVector2df(Vector2f searchPoint)
			throws NoVerticeFoundException {
		if (vertices.size() == 0)
			throw new NoVerticeFoundException();
		float distance = Float.MAX_VALUE;
		Vector2f result = null;
		for (Vector2f thisVert : vertices) {
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
	public Vector2f findBefore(Vector2f vector) {
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
	public Vector2f findAfter(Vector2f vector) {
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
	public Collection<Vector2f> getVector2dfs() {
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
	public void addVerticeAfter(Vector2f vert, Vector2f nearest) {
		int index = vertices.indexOf(nearest);
		if (index == vertices.size() - 1) {
			vertices.add(vert);
		} else
			vertices.add(vertices.indexOf(nearest) + 1, vert);
		DataHolder.getInstance().notifyVector2dfsChanged();

	}

	/**
	 * Rotates this polygon around its center by given angle.
	 * 
	 * @param angle
	 *            angle in degree.
	 */
	public void rotate(float angle) {
		Vector2f center = getCenter();
		Vector2df centerPos = new Vector2df(center);
		for (Vector2f v : vertices) {
			Vector2df vec = new Vector2df(v);
			vec.rotate(angle, centerPos);
			v.set(vec);
		}
	}

	/**
	 * Mirrors the polygon at either x- or y-axis.
	 * 
	 * @param axis
	 *            either {@link #X_AXIS} or {@link #Y_AXIS}.
	 */
	public void mirror(int axis) {
		Vector2f center = getCenter();
		for (Vector2f v : vertices) {
			if (axis == Y_AXIS) {
				float diff = v.x - center.x;
				v.x -= 2 * diff;
			}
			if (axis == X_AXIS) {
				float diff = v.y - center.y;
				v.y -= 2 * diff;
			}
		}
	}

	private Vector2f getCenter() {
		float x = 0;
		float y = 0;
		for (Vector2f v : vertices) {
			x += v.x;
			y += v.y;
		}
		x = x / vertices.size();
		y = y / vertices.size();
		return new Vector2f(x, y);
	}

	/**
	 * Adds a vertice before another one.
	 * 
	 * @param vert
	 *            the vertice to add.
	 * @param nearest
	 *            the vertice before that given vertice should be added.
	 */
	public void addVerticeBefore(Vector2f vert, Vector2f nearest) {
		int index = vertices.indexOf(nearest);
		if (index < 0) {
			index = 0;
		}
		vertices.add(index, vert);
		DataHolder.getInstance().notifyVector2dfsChanged();

	}

	/**
	 * Creates an editable polygon from given shape.
	 * 
	 * @param shape
	 *            the shape.
	 * @return a polygon containing the same vertices as given shape.
	 */
	public static EditablePolygon fromShape(Shape shape) {
		EditablePolygon poly = new EditablePolygon();
		for (int i = 0; i < shape.getPointCount(); i++) {
			Vector2f point = new Vector2f(shape.getPoint(i));
			poly.addVector2df(point);
		}
		return poly;
	}
}