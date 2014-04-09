package de.illonis.eduras.math.graphs;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import de.illonis.edulog.EduLog;

public class Vertex {
	private final static Logger L = EduLog.getLoggerFor(Vertex.class.getName());

	private int color;
	Set<Vertex> adjacentVertices;

	public Vertex(Set<Vertex> adjacents, int color) {
		this.color = color;
		this.adjacentVertices = adjacents;
	}

	public Vertex(Set<Vertex> adjacents) {
		this(adjacents, -1);
	}

	public Vertex() {
		this(new HashSet<Vertex>());
	}

	public Set<Vertex> getAdjacentVertices() {
		return adjacentVertices;
	}

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}

	public void addAdjacentVertex(Vertex vertex) {
		adjacentVertices.add(vertex);
		vertex.adjacentVertices.add(this);
	}

	public void removeAdjacentVertex(Vertex vertex) {
		adjacentVertices.remove(vertex);
		vertex.adjacentVertices.remove(this);
	}

	public boolean hasAdjacentNodeOfColor(int teamId) {
		for (Vertex adjacent : adjacentVertices) {
			if (adjacent.getColor() == teamId) {
				return true;
			}
		}
		return false;
	}
}
