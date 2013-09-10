package de.illonis.eduras.shapecreator;

import java.util.ArrayList;
import java.util.Collection;

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
		vertices.add(new Vertice(-20, -40));
		vertices.add(new Vertice(0, -60));
		vertices.add(new Vertice(20, -40));
		vertices.add(new Vertice(20, 0));
		vertices.add(new Vertice(-20, 0));
	}

	protected void addVertice(Vertice v) {
		vertices.add(v);
		DataHolder.getInstance().notifyVerticesChanged();
	}

	public void removeVertice(Vertice v) {
		if (vertices.size() > 1) {
			vertices.remove(v);
			DataHolder.getInstance().notifyVerticesChanged();
		}
	}

	public void moveDownVertice(Vertice v) throws VerticeListException {
		int index = vertices.indexOf(v);
		if (index == vertices.size() - 1)
			throw new VerticeListException("Cannot move vertice down.");
		exchangeVertices(index, index + 1);
	}

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

	protected Vertice findNearestVertice(Vertice searchPoint)
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

	public Collection<Vertice> getVertices() {
		return vertices;
	}
}