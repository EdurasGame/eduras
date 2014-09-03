package de.illonis.eduras.mapeditor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;

import javax.swing.JOptionPane;

import org.newdawn.slick.Color;
import org.newdawn.slick.geom.Vector2f;

import de.illonis.eduras.ReferencedEntity;
import de.illonis.eduras.gamemodes.GameMode.GameModeNumber;
import de.illonis.eduras.gameobjects.DynamicPolygonObject;
import de.illonis.eduras.gameobjects.GameObject;
import de.illonis.eduras.gameobjects.Portal;
import de.illonis.eduras.gameobjects.TriggerArea;
import de.illonis.eduras.maps.NodeData;
import de.illonis.eduras.maps.SpawnPosition;
import de.illonis.eduras.maps.persistence.MapParser;
import de.illonis.eduras.maps.persistence.MapParser.MapFileSection;

/**
 * Provides methods to save currently edited map to file.
 * 
 * @author illonis
 * 
 */
public final class MapSaver {

	private final static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(
			"Y-m-d");
	private final PrintWriter writer;
	private final MapData data;

	/**
	 * Creates a worker for saving currently edited map.
	 * 
	 * @param file
	 *            target file. Will be overwritten without asking.
	 * @throws FileNotFoundException
	 *             if file could not be written.
	 */
	public MapSaver(File file) throws FileNotFoundException {
		data = MapData.getInstance();
		if (!file.getAbsolutePath().endsWith(MapParser.FILE_EXTENSION))
			file = new File(file.getAbsolutePath() + MapParser.FILE_EXTENSION);
		writer = new PrintWriter(file);
	}

	/**
	 * Performs actual writing.
	 * 
	 * @throws IOException
	 *             if writing fails.
	 * 
	 */
	public void save() throws IOException {
		addComment("Created by Eduras? MapEditor");
		writer.println();
		writeMetaData();
		writeSpawnPoints();
		try {
			writeObjects();
		} catch (EditorException e) {
			throw new IOException(e.getMessage());
		}
		writeNodes();
		writeNodeConnections();
		writer.close();
	}

	private void writeNodeConnections() {
		beginSection(MapFileSection.NODECONNECTIONS);
		List<NodeConnection> existing = new LinkedList<NodeConnection>();
		for (NodeData node : data.getBases()) {
			for (NodeData adjacent : node.getAdjacentNodes()) {
				NodeConnection conn = new NodeConnection(node, adjacent);
				if (existing.contains(conn))
					continue;
				writer.println("*" + node.getRefName() + ", *"
						+ adjacent.getRefName());
				existing.add(conn);
			}
		}
	}

	private void writeNodes() {
		beginSection(MapFileSection.NODES);
		for (NodeData node : data.getBases()) {
			maybeAddReference(node);
			writer.println(commaValues(node.getXPosition(),
					node.getYPosition(), node.getWidth(), node.getHeight(),
					node.isMainNode(), node.getResourceMultiplicator()));
		}
	}

	private void writeObjects() throws EditorException {
		beginSection(MapFileSection.OBJECTS);
		for (GameObject object : data.getGameObjects()) {
			maybeAddReference(object);
			String line = commaValues(object.getType(), object.getXPosition(),
					object.getYPosition());
			if (object instanceof TriggerArea) {
				line += ", " + (int) object.getWidth() + ", "
						+ (int) object.getHeight();
			}
			switch (object.getType()) {
			case DYNAMIC_POLYGON_BLOCK:
				line += ", ";
				DynamicPolygonObject dyno = (DynamicPolygonObject) object;
				StringBuilder builder = new StringBuilder();
				for (Vector2f vertice : dyno.getPolygonVertices()) {
					builder.append(buildVert(vertice.getX(), vertice.getY()));
					builder.append(", ");
				}
				builder.append(toColorString(dyno.getColor()));
				line += builder.toString();
				break;
			case PORTAL:
				Portal portal = (Portal) object;
				if (portal.getPartnerPortal() == null) {
					throw new EditorException("Portal has no partner.");
				}

				line += ", *" + portal.getPartnerPortal().getRefName();
				break;
			default:
				break;
			}
			writer.println(line);
		}

	}

	private String buildVert(float x, float y) {
		return String.format("[%d %d]", (int) x, (int) y);
	}

	private String toColorString(Color color) {
		return String.format("0x%02x%02x%02x%02x", color.getAlpha(),
				color.getRed(), color.getGreen(), color.getBlue());
	}

	private void maybeAddReference(ReferencedEntity entity) {
		if (!entity.getRefName().isEmpty()) {
			addReference(entity.getRefName());
		}
	}

	private void writeSpawnPoints() {
		beginSection(MapFileSection.SPAWNPOINTS);
		for (SpawnPosition spawn : data.getSpawnPoints()) {
			maybeAddReference(spawn);
			writer.println(commaValues(spawn.getArea().getX(), spawn.getArea()
					.getY(), spawn.getArea().getWidth(), spawn.getArea()
					.getHeight(), spawn.getTeaming()));
		}

	}

	private String commaValues(Object... values) {
		StringBuilder text = new StringBuilder();
		for (Object object : values) {
			text.append(", ");
			if (object instanceof Float) {
				text.append("" + (int) (float) object);
			} else
				text.append(object.toString());
		}
		return text.substring(2);
	}

	private void addReference(String ref) {
		writer.println("&" + ref);
	}

	private void addComment(String comment) {
		writer.println("# " + comment);
	}

	private void beginSection(MapFileSection section) {
		writer.println();
		writer.println(":" + section.name().toLowerCase());
	}

	private void writeMetaData() {
		writeMetaDataElement("mapname", data.getMapName());
		writeMetaDataElement("author", data.getAuthor());
		writeMetaDataElement("created", DATE_FORMAT.format(new Date()));
		StringBuffer gameModeList = new StringBuffer();
		for (GameModeNumber mode : data.getSupportedGameModes()) {
			gameModeList.append(mode);
			gameModeList.append(", ");
		}

		writeMetaDataElement("gamemodes",
				gameModeList.substring(0, gameModeList.length() - 2));
		writeMetaDataElement("width", data.getWidth() + "");
		writeMetaDataElement("height", data.getHeight() + "");
	}

	private void writeMetaDataElement(String key, String value) {
		writer.println("@" + key + " = " + value);
	}
}
