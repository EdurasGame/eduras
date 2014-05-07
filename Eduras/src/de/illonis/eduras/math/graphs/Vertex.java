package de.illonis.eduras.math.graphs;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import de.illonis.edulog.EduLog;

/**
 * Represents a vertex in a {@link Graph}. In this implementation a vertex can
 * have at most one edge to the same vertex. A vertex also has a color which is
 * represented as integer. The same number then represents the same color.
 * 
 * @author Florian 'Ren' Mai <florian.ren.mai@googlemail.com>
 * 
 */
public class Vertex {
	private final static Logger L = EduLog.getLoggerFor(Vertex.class.getName());

	private int color;
	Set<Vertex> adjacentVertices;

	/**
	 * Create a new vertex.
	 * 
	 * @param adjacents
	 *            Adjacent vertices.
	 * @param color
	 *            The vertex' color.
	 */
	public Vertex(Set<Vertex> adjacents, int color) {
		this.color = color;
		this.adjacentVertices = adjacents;
	}

	/**
	 * Creates a vertex that doesn't have a color.
	 * 
	 * @param adjacents
	 *            The vertex' adjacent vertices.
	 */
	public Vertex(Set<Vertex> adjacents) {
		this(adjacents, -1);
	}

	/**
	 * Create a vertex with no adjacent vertices and no color.
	 */
	public Vertex() {
		this(new HashSet<Vertex>());
	}

	/**
	 * Returns this vertex' adjacent vertices.
	 * 
	 * @return adjacent vertices
	 */
	public Set<Vertex> getAdjacentVertices() {
		return adjacentVertices;
	}

	/**
	 * Color of the vertex
	 * 
	 * @return color as integer
	 */
	public int getColor() {
		return color;
	}

	/**
	 * 
	 * @param color
	 */
	public void setColor(int color) {
		this.color = color;
	}

	/**
	 * Add another vertex as an adjacent vertex to this vertex. This vertex is
	 * added as an adjacent of the other vertex as well.
	 * 
	 * @param otherVertex
	 */
	public void addAdjacentVertex(Vertex otherVertex) {
		adjacentVertices.add(otherVertex);
		otherVertex.adjacentVertices.add(this);
	}

	/**
	 * Remove another vertex as an adjacent vertex from this vertex. This vertex
	 * is removed as an adjacent of the other vertex as well.
	 * 
	 * @param otherVertex
	 */
	public void removeAdjacentVertex(Vertex otherVertex) {
		adjacentVertices.remove(otherVertex);
		otherVertex.adjacentVertices.remove(this);
	}

	/**
	 * Checks if at least one of the adjacent nodes is of the given color.
	 * 
	 * @param colorToCheckFor
	 * @return true, if there is such an adjacent
	 */
	public boolean hasAdjacentNodeOfColor(int colorToCheckFor) {
		for (Vertex adjacent : adjacentVertices) {
			if (adjacent.getColor() == colorToCheckFor) {
				return true;
			}
		}
		return false;
	}
}
