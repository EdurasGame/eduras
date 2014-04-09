package de.illonis.eduras.maps;

import java.util.LinkedList;
import java.util.logging.Logger;

import de.illonis.edulog.EduLog;

public class NodeData {
	private final static Logger L = EduLog.getLoggerFor(NodeData.class
			.getName());

	private final int x;
	private final int y;
	private final int id;
	private final LinkedList<Integer> adjacentNodes;
	private final boolean isMainNode;

	public NodeData(int x, int y, int id, LinkedList<Integer> adjacentNodes,
			boolean isMainNode) {
		this.x = x;
		this.y = y;
		this.id = id;
		this.adjacentNodes = adjacentNodes;
		this.isMainNode = isMainNode;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getId() {
		return id;
	}

	public LinkedList<Integer> getAdjacentNodes() {
		return adjacentNodes;
	}

	public boolean isMainNode() {
		return isMainNode;
	}

}
