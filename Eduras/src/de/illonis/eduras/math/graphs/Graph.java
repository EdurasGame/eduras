package de.illonis.eduras.math.graphs;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import de.illonis.edulog.EduLog;

public class Graph {
	private final static Logger L = EduLog.getLoggerFor(Graph.class.getName());

	private Set<Vertex> vertices;

	public Graph(Set<Vertex> vertices) {
		this.vertices = vertices;
	}

	public Graph() {
		this(new HashSet<Vertex>());
	}

	public void addVertex(Vertex vertex) {
		vertices.add(vertex);
	}

	public void removeVertex(Vertex vertex) {
		vertices.remove(vertex);
	}
}
