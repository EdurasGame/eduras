package de.illonis.eduras.math.graphs;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import de.illonis.edulog.EduLog;

/**
 * Represents a graph. A graph contains of none, one or multiple {@link Vertex}.
 * 
 * @author Florian 'Ren' Mai <florian.ren.mai@googlemail.com>
 * 
 */
public class Graph {
	private final static Logger L = EduLog.getLoggerFor(Graph.class.getName());

	private Set<Vertex> vertices;

	/**
	 * Create a graph consisting of the given vertices.
	 * 
	 * @param vertices
	 */
	public Graph(Set<Vertex> vertices) {
		this.vertices = vertices;
	}

	/**
	 * Create an empty graph.
	 */
	public Graph() {
		this(new HashSet<Vertex>());
	}

	/**
	 * Add a {@link Vertex} to the graph.
	 * 
	 * @param vertex
	 */
	public void addVertex(Vertex vertex) {
		vertices.add(vertex);
	}

	/**
	 * Remove a {@link Vertex} from the graph.
	 * 
	 * @param vertex
	 */
	public void removeVertex(Vertex vertex) {
		vertices.remove(vertex);
	}
}
