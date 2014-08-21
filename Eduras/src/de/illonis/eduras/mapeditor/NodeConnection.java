package de.illonis.eduras.mapeditor;

import de.illonis.eduras.maps.NodeData;

/**
 * Represents a temporal bidirectional node connection.
 * 
 * @author illonis
 * 
 */
public final class NodeConnection {
	final NodeData a;
	final NodeData b;

	/**
	 * @param a
	 *            first node.
	 * @param b
	 *            second node.
	 */
	public NodeConnection(NodeData a, NodeData b) {
		this.a = a;
		this.b = b;
	}

	@Override
	public String toString() {
		return a.getRefName() + " <--> " + b.getRefName();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof NodeConnection) {
			NodeConnection conn = (NodeConnection) obj;
			return (conn.a.equals(a) && conn.b.equals(b))
					|| (conn.a.equals(b) && conn.b.equals(a));
		}
		return super.equals(obj);
	}
}