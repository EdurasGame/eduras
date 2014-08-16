package de.illonis.eduras.maps;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;

import org.newdawn.slick.geom.Rectangle;

import de.illonis.eduras.gameobjects.Base;
import de.illonis.eduras.gameobjects.Base.BaseType;
import de.illonis.eduras.gameobjects.NeutralArea;

/**
 * Represents Edura! nodes on a map as they are read from a file.
 * 
 * @author Florian 'Ren' Mai <florian.ren.mai@googlemail.com>
 * 
 */
public class NodeData {

	private final Rectangle area;
	private final int id;
	private final LinkedList<Integer> adjacentNodes;
	private final Base.BaseType isMainNode;

	/**
	 * Create a new node data wrapper instance with default node size.
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
		this(x, y, NeutralArea.DEFAULT_SIZE, NeutralArea.DEFAULT_SIZE, id,
				adjacentNodes, baseType);
	}

	/**
	 * @param x
	 *            the x coordinate of the nodes position
	 * @param y
	 *            the y coordinate of the nodes position
	 * @param width
	 *            the width of the node.
	 * @param height
	 *            the height of the node.
	 * @param id
	 *            the id of the node that is used to distinguish it from other
	 *            nodes
	 * @param adjacentNodes
	 *            adjacent nodes.
	 * @param baseType
	 *            tells whether this node may be used as a main node of one of
	 *            the teams.
	 */
	public NodeData(float x, float y, float width, float height, int id,
			LinkedList<Integer> adjacentNodes, BaseType baseType) {
		area = new Rectangle(x, y, width, height);
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
		return area.getX();
	}

	/**
	 * Returns the y coordinate of the nodes position.
	 * 
	 * @return y
	 */
	public float getY() {
		return area.getY();
	}

	/**
	 * @return width of the node.
	 */
	public float getWidth() {
		return area.getWidth();
	}

	/**
	 * @return height of the node.
	 */
	public float getHeight() {
		return area.getHeight();
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
