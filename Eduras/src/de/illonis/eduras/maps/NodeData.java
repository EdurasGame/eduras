package de.illonis.eduras.maps;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.logging.Logger;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.gameobjects.Base;
import de.illonis.eduras.gameobjects.Base.BaseType;

/**
 * Represents Edura! nodes on a map as they are read from a file.
 * 
 * @author Florian 'Ren' Mai <florian.ren.mai@googlemail.com>
 * 
 */
public class NodeData {
	private final static Logger L = EduLog.getLoggerFor(NodeData.class
			.getName());

	private final float x;
	private final float y;
	private final int id;
	private final LinkedList<Integer> adjacentNodes;
	private final Base.BaseType isMainNode;

	/**
	 * Create a new node data wrapper instance.
	 * 
	 * @param x
	 *            the x coordinate of the nodes position
	 * @param y
	 *            the y coordinate of the nodes position
	 * @param id
	 *            the id of the node that is used to distinguish it from other
	 *            nodes
	 * @param adjacentNodes
	 *            adjacent nodes
	 * @param baseType
	 *            tells whether this node may be used as a main node of one of
	 *            the teams
	 */
	public NodeData(float x, float y, int id,
			LinkedList<Integer> adjacentNodes, BaseType baseType) {
		this.x = x;
		this.y = y;
		this.id = id;
		this.adjacentNodes = adjacentNodes;
		this.isMainNode = baseType;
	}

	/**
	 * Returns the x coordinate of the nodes position.
	 * 
	 * @return x
	 */
	public float getX() {
		return x;
	}

	/**
	 * Returns the y coordinate of the nodes position.
	 * 
	 * @return y
	 */
	public float getY() {
		return y;
	}

	/**
	 * Returns the id of the node that distinguishes it from other nodes.
	 * 
	 * @return id
	 */
	public int getId() {
		return id;
	}

	/**
	 * Returns adjacent nodes of this node.
	 * 
	 * @return adjacent nodes
	 */
	public LinkedList<Integer> getAdjacentNodes() {
		return adjacentNodes;
	}

	/**
	 * Determines what {@link BaseType} this node is of.
	 * 
	 * @return base type.
	 */
	public BaseType isMainNode() {
		return isMainNode;
	}

	/**
	 * Given a collection of NodeData this function returns a HashMap that maps
	 * a nodes id onto the node.
	 * 
	 * @param nodeData
	 *            the collection of node data
	 * @return the HashMap
	 */
	public static HashMap<Integer, NodeData> nodeDataToVertices(
			Collection<NodeData> nodeData) {
		HashMap<Integer, NodeData> nodeIdToVertex = new HashMap<Integer, NodeData>();

		// initialize vertices
		for (NodeData node : nodeData) {
			nodeIdToVertex.put(node.getId(), node);
		}
		return nodeIdToVertex;
	}
}
