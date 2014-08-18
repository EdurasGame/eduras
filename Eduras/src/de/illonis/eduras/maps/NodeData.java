package de.illonis.eduras.maps;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;

import org.newdawn.slick.geom.Rectangle;

import de.illonis.eduras.ReferencedEntity;
import de.illonis.eduras.gameobjects.Base;
import de.illonis.eduras.gameobjects.Base.BaseType;
import de.illonis.eduras.gameobjects.NeutralArea;

/**
 * Represents Edura! nodes on a map as they are read from a file.
 * 
 * @author Florian 'Ren' Mai <florian.ren.mai@googlemail.com>
 * 
 */
public class NodeData extends ReferencedEntity {

	private final Rectangle area;
	private final int id;
	private final LinkedList<NodeData> adjacentNodes;
	private Base.BaseType isMainNode;
	private float resourceMultiplicator;

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
	 * @param refName
	 *            reference name.
	 */
	public NodeData(float x, float y, int id,
			LinkedList<NodeData> adjacentNodes, BaseType baseType,
			String refName) {
		this(x, y, NeutralArea.DEFAULT_SIZE, NeutralArea.DEFAULT_SIZE, id,
				adjacentNodes, baseType, refName);
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
	 * @param refName
	 *            reference name.
	 */
	public NodeData(float x, float y, float width, float height, int id,
			LinkedList<NodeData> adjacentNodes, BaseType baseType,
			String refName) {
		area = new Rectangle(x, y, width, height);
		this.id = id;
		this.adjacentNodes = adjacentNodes;
		this.isMainNode = baseType;
		resourceMultiplicator = 1f;
		super.setRefName(refName);
	}

	/**
	 * Sets the resource multiplicator for this node.
	 * 
	 * @param resourceMultiplicator
	 *            new value.
	 */
	public void setResourceMultiplicator(float resourceMultiplicator) {
		this.resourceMultiplicator = resourceMultiplicator;
	}

	/**
	 * @return the resource multiplicator for this node.
	 */
	public float getResourceMultiplicator() {
		return resourceMultiplicator;
	}

	/**
	 * Returns the x coordinate of the nodes position.
	 * 
	 * @return x
	 */
	public float getX() {
		return area.getX();
	}

	public void setX(float x) {
		area.setX(x);
	}

	public void setY(float y) {
		area.setY(y);
	}

	public void setWidth(float width) {
		area.setWidth(width);
	}

	public void setHeight(float height) {
		area.setHeight(height);
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

	public void setIsMainNode(Base.BaseType isMainNode) {
		this.isMainNode = isMainNode;
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
	public LinkedList<NodeData> getAdjacentNodes() {
		return adjacentNodes;
	}

	public void addAdjacentNode(NodeData node) {
		adjacentNodes.add(node);
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
