package de.illonis.eduras.maps.persistence;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.file.Path;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.newdawn.slick.Color;
import org.newdawn.slick.geom.Rectangle;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.ObjectFactory.ObjectType;
import de.illonis.eduras.gameclient.datacache.TextureInfo.TextureKey;
import de.illonis.eduras.gamemodes.GameMode.GameModeNumber;
import de.illonis.eduras.gameobjects.Base;
import de.illonis.eduras.gameobjects.Portal;
import de.illonis.eduras.maps.InitialObjectData;
import de.illonis.eduras.maps.LoadedMap;
import de.illonis.eduras.maps.Map;
import de.illonis.eduras.maps.NodeData;
import de.illonis.eduras.maps.SpawnPosition;
import de.illonis.eduras.maps.SpawnPosition.SpawnType;
import de.illonis.eduras.math.Vector2df;
import de.illonis.eduras.utils.ColorUtils;

/**
 * Provides functionality to parse a human readable map into map data. More
 * details on readable mapfiles can be found in docbook in the map section.
 * 
 * @author illonis
 * 
 */
public class MapParser {

	/**
	 * The default file extension for saved maps.<br>
	 * erm stands for "eduras? readable mapfile".
	 */
	public final static String FILE_EXTENSION = ".erm";
	/**
	 * Regex that validates a reference key.
	 */
	public static final String IDENTIFIER_REGEX = "^[a-zA-Z]+[A-Za-z0-9]*$";

	private final static Logger L = EduLog.getLoggerFor(MapParser.class
			.getName());

	/**
	 * Sections in mapfile.
	 * 
	 * @author illonis
	 * 
	 */
	@SuppressWarnings("javadoc")
	public enum MapFileSection {
		NONE, SPAWNPOINTS, OBJECTS, NODES, NODECONNECTIONS;
	}

	/**
	 * Reads a textfile containing map data.
	 * 
	 * @param inputFile
	 *            the input file. Must have correct syntax as described in
	 *            docbook. It's file extension must be correct (
	 *            {@value #FILE_EXTENSION}).
	 * @return the map data.
	 * @throws InvalidDataException
	 *             if file contains invalid data or is not well formatted.
	 * @throws IOException
	 *             if an I/O error occurs while reading the file.
	 */
	public static Map readMap(URL inputFile) throws InvalidDataException,
			IOException {

		if (inputFile == null) {
			throw new IOException("URL is null");
		}

		String currentIdentifier = "";
		int currentNodeId = 1;
		HashMap<String, NodeData> nodeIds = new HashMap<String, NodeData>();
		HashMap<String, InitialObjectData> objectIds = new HashMap<String, InitialObjectData>();
		HashSet<String> existingIdentifiers = new HashSet<String>();

		// map name is file name without .erm ending
		String mapName = inputFile.getFile().substring(0,
				inputFile.getFile().length() - 4);
		mapName = mapName.substring(mapName.lastIndexOf("/") + 1,
				mapName.length());
		String author = "";
		int width = 0;
		int height = 0;
		TextureKey background = TextureKey.NONE;
		MapFileSection currentMode = MapFileSection.NONE;
		Date created = new Date();
		final LinkedList<GameModeNumber> gameModes = new LinkedList<GameModeNumber>();
		final LinkedList<SpawnPosition> spawnPositions = new LinkedList<SpawnPosition>();
		final LinkedList<InitialObjectData> gameObjects = new LinkedList<InitialObjectData>();
		final LinkedList<NodeData> nodes = new LinkedList<NodeData>();

		// Charset charset = Charset.forName("UTF-8");

		BufferedReader reader = new BufferedReader(new InputStreamReader(
				inputFile.openStream()));

		String line = null;
		int lineNumber = 0;
		while ((line = reader.readLine()) != null) {
			lineNumber++;
			if (line.trim().isEmpty())
				continue;
			if (line.startsWith("#"))
				continue;
			if (line.startsWith("&")) {
				String identifier = line.substring(1).trim();
				if (identifier.matches(IDENTIFIER_REGEX)) {
					if (existingIdentifiers.contains(identifier)) {
						throw new InvalidDataException("Reference "
								+ identifier + " already exists.", lineNumber);
					}
					currentIdentifier = identifier;
					existingIdentifiers.add(identifier);
				} else {
					throw new InvalidDataException(
							"Found invalid reference name: " + identifier,
							lineNumber);
				}
				continue;
			}
			if (line.startsWith("@")) {
				String[] data = line.substring(1).split("=");
				String key = data[0].trim();
				String value = data[1].trim();
				switch (key) {
				case "author":
					author = value;
					break;
				case "width":
					width = Integer.parseInt(value);
					break;
				case "height":
					height = Integer.parseInt(value);
					break;
				case "created":
					try {
						created = Map.DATE_FORMAT.parse(value);
					} catch (ParseException e) {
						throw new InvalidDataException(
								"Date format is invalid: " + e.getMessage(),
								lineNumber);
					}
					break;
				case "background":
					try {
						background = TextureKey.valueOf(value);
					} catch (IllegalArgumentException e) {
						throw new InvalidDataException(
								"Texture does not exist: " + value, lineNumber);
					}
					break;
				case "gamemodes":
					String[] modes = value.split(",");
					for (int i = 0; i < modes.length; i++) {
						try {
							gameModes.add(GameModeNumber.valueOf(modes[i]
									.trim().toUpperCase()));
						} catch (IllegalArgumentException e) {
							throw new InvalidDataException(
									"Gamemode does not exist: " + modes[i],
									lineNumber);
						}
					}
					break;
				default:
					L.log(Level.WARNING,
							"Unknown tag found while loading map: @" + key
									+ " in line " + lineNumber);
					break;
				}
			} else if (line.startsWith(":")) {
				String mode = line.substring(1).trim();
				switch (mode) {
				case "spawnpoints":
					currentMode = MapFileSection.SPAWNPOINTS;
					break;
				case "nodeconnections":
					currentMode = MapFileSection.NODECONNECTIONS;
					break;
				case "objects":
					currentMode = MapFileSection.OBJECTS;
					break;
				case "nodes":
					currentMode = MapFileSection.NODES;
					break;
				default:
					throw new InvalidDataException("Invalid control point: "
							+ mode, lineNumber);
				}
			} else {
				switch (currentMode) {
				case NONE:
					throw new InvalidDataException("Invalid line found: "
							+ line, lineNumber);
				case OBJECTS: {
					String[] objectData = line.split(",");
					float objX = 0, objY = 0;
					try {
						ObjectType objectType = ObjectType
								.valueOf(objectData[0].trim());
						objX = evaluateString(objectData[1], width, height);
						objY = evaluateString(objectData[2], width, height);

						InitialObjectData oData;

						if (objectType.equals(ObjectType.DYNAMIC_POLYGON_BLOCK)) {
							// handle dynamic blocks
							int numOfVertexVectors = objectData.length - 3;
							String last = objectData[objectData.length - 1]
									.trim();
							boolean lastColor = false;
							if (!last.startsWith("[")) {
								lastColor = true;
								numOfVertexVectors--;
								// last value is color.
							}
							if (numOfVertexVectors < 3) {
								throw new InvalidDataException(
										"Dynamic polygon must have at least three points.",
										lineNumber);
							} else {
								Vector2df[] vertices = readVertices(objectData,
										width, height, numOfVertexVectors,
										lineNumber);
								oData = new InitialObjectData(objectType, objX,
										objY, vertices, currentIdentifier);
								if (lastColor) {
									if (isColor(last)) {
										oData.setColor(readColor(last,
												lineNumber));
									} else {
										try {
											TextureKey key = TextureKey
													.valueOf(last);
											oData.setTexture(key);
										} catch (IllegalArgumentException e) {
											oData.setTexture(TextureKey.NONE);
											oData.setColor(Color.gray);
										}
									}
								}
							}
						} else {
							// handle normal objects
							oData = new InitialObjectData(objectType, objX,
									objY, currentIdentifier);
							if (objectType == ObjectType.PORTAL) {
								if (objectData.length > 3) {
									String refPortal = null;
									String wString = null, hString = null;
									if (objectData.length == 4) {
										refPortal = objectData[3].trim();
									} else if (objectData.length >= 5) {
										wString = objectData[3].trim();
										hString = objectData[4].trim();
										if (objectData.length == 6) {
											refPortal = objectData[5].trim();
										}
									}
									if (refPortal != null) {
										if (refPortal.startsWith("*")
												&& refPortal
														.substring(1)
														.matches(
																IDENTIFIER_REGEX)) {
											oData.addReference(
													Portal.OTHER_PORTAL_REFERENCE,
													refPortal.substring(1));
										} else {
											throw new InvalidDataException(
													refPortal
															+ " is no valid reference",
													lineNumber);
										}
									}
									if (wString != null && hString != null) {
										int w = 0, h = 0;
										try {
											w = Integer.parseInt(wString);
											h = Integer.parseInt(hString);
										} catch (NumberFormatException e) {
											throw new InvalidDataException(
													"Invalid width/height value: "
															+ e.getMessage(),
													lineNumber);
										}
										oData.setWidth(w);
										oData.setHeight(h);
									}
								}
							}
						}

						if (!currentIdentifier.isEmpty()) {
							objectIds.put(currentIdentifier, oData);
						}
						gameObjects.add(oData);
					} catch (ScriptException e) {
						throw new InvalidDataException(
								"Invalid math expression: " + e.getMessage(),
								lineNumber);
					} catch (IllegalArgumentException e) {
						throw new InvalidDataException(
								"Illegal game object type: "
										+ objectData[0].trim(), lineNumber);
					}

					break;
				}
				case SPAWNPOINTS: {
					String[] spawnData = line.split(",");
					float x = 0, y = 0;
					int w = 0, h = 0;
					try {
						x = evaluateString(spawnData[0].trim(), width, height);
						y = evaluateString(spawnData[1].trim(), width, height);
					} catch (ScriptException e) {
						throw new InvalidDataException(
								"Invalid math expression: " + e.getMessage(),
								lineNumber);
					}
					try {
						w = Integer.parseInt(spawnData[2].trim());
						h = Integer.parseInt(spawnData[3].trim());
					} catch (NumberFormatException e) {
						throw new InvalidDataException(
								"Invalid width/height value: " + e.getMessage(),
								lineNumber);
					}
					SpawnType type = SpawnType.valueOf(spawnData[4].trim());
					Rectangle area = new Rectangle(x, y, w, h);
					spawnPositions.add(new SpawnPosition(area, type));
					break;
				}
				case NODES:
					String[] nodeData = line.split(",");
					if (nodeData.length < 5) {
						throw new InvalidDataException(
								"Not enough arguments for node: " + line,
								lineNumber);
					}
					float nodeX = 0,
					nodeY = 0;
					float nodeWidth = 0,
					nodeHeight = 0;
					Base.BaseType baseType;

					try {
						nodeX = evaluateString(nodeData[0], width, height);
						nodeY = evaluateString(nodeData[1], width, height);
						nodeWidth = evaluateString(nodeData[2], width, height);
						nodeHeight = evaluateString(nodeData[3], width, height);
					} catch (ScriptException | NumberFormatException e) {
						throw new InvalidDataException(
								"Invalid math expression in line: " + line,
								lineNumber);
					}

					try {
						baseType = Base.BaseType.valueOf(nodeData[4].trim());
					} catch (IllegalArgumentException e) {
						throw new InvalidDataException("Invalid base teaming: "
								+ nodeData[4].trim(), lineNumber);
					}

					NodeData node = new NodeData(nodeX, nodeY, nodeWidth,
							nodeHeight, currentNodeId++,
							new LinkedList<NodeData>(), baseType,
							currentIdentifier);
					if (nodeData.length > 5) {
						try {
							float resourceMult = evaluateString(nodeData[5],
									width, height);
							node.setResourceMultiplicator(resourceMult);
						} catch (ScriptException e) {
							throw new InvalidDataException(
									"Invalid base resource multiplicator: "
											+ nodeData[5], lineNumber);
						}
					}
					if (!currentIdentifier.isEmpty())
						nodeIds.put(currentIdentifier, node);
					nodes.add(node);
					break;
				case NODECONNECTIONS:
					String[] connectedNodes = line.split(",");
					if (connectedNodes.length != 2) {
						throw new InvalidDataException(
								"Invalid node connection: " + line, lineNumber);
					}
					String firstIdentifier = connectedNodes[0].trim();
					String secondIdentifier = connectedNodes[1].trim();
					if (firstIdentifier.startsWith("*")
							&& secondIdentifier.startsWith("*")) {
						firstIdentifier = firstIdentifier.substring(1);
						secondIdentifier = secondIdentifier.substring(1);
						NodeData firstNode = nodeIds.get(firstIdentifier);
						if (firstNode == null) {
							throw new InvalidDataException(
									"Could not find reference \""
											+ firstIdentifier + "\"",
									lineNumber);
						}
						NodeData secondNode = nodeIds.get(secondIdentifier);
						if (secondNode == null) {
							throw new InvalidDataException(
									"Could not find reference \""
											+ secondIdentifier + "\"",
									lineNumber);
						}
						firstNode.addAdjacentNode(secondNode);
						secondNode.addAdjacentNode(firstNode);
					} else {
						throw new InvalidDataException(
								"Invalid node connection: " + line, lineNumber);
					}
					break;
				default:
					break;
				}
				currentIdentifier = "";
			}
		}
		reader.close();

		return new LoadedMap(mapName, author, width, height, created,
				spawnPositions, gameObjects, gameModes, nodes, background);
	}

	private static boolean isColor(String string) {
		return string.startsWith("0x");
	}

	private static Color readColor(String last, final int lineNumber)
			throws InvalidDataException {
		try {
			return ColorUtils.fromString(last);
		} catch (NumberFormatException e) {
			throw new InvalidDataException("Invalid color: " + last, lineNumber);
		}
	}

	private static Vector2df[] readVertices(String[] objectData, int width,
			int height, int numberOfVertices, final int lineNumber)
			throws ScriptException, InvalidDataException {
		Vector2df[] vertices = new Vector2df[numberOfVertices];
		for (int i = 0; i < numberOfVertices; i++) {
			String coord = objectData[3 + i].trim();
			if (!coord.startsWith("[") || !coord.endsWith("]")) {
				throw new InvalidDataException("Invalid coordinate: " + coord,
						lineNumber);
			}
			coord = coord.substring(1, coord.length() - 1);
			String[] parts = coord.split(" ");
			if (parts.length != 2) {
				throw new InvalidDataException("Invalid coordinate: " + coord,
						lineNumber);
			}

			float vertexXCoordinate = evaluateString(parts[0], width, height);
			float vertexYCoordinate = evaluateString(parts[1], width, height);
			vertices[i] = new Vector2df(vertexXCoordinate, vertexYCoordinate);
		}

		return vertices;
	}

	/**
	 * Writes a map to a readable file.
	 * 
	 * @param map
	 *            the map to save.
	 * @param targetFile
	 *            the target file. It's file extension must be correct (
	 *            {@value #FILE_EXTENSION}).
	 * @throws IOException
	 *             if an I/O errors occurs while writing to the file.
	 */
	public static void writeMap(Map map, Path targetFile) throws IOException {

	}

	/**
	 * Evaluates the given math expression.
	 * 
	 * @param expression
	 *            the mathematic expression as string.
	 * @param w
	 *            the map's width.
	 * @param h
	 *            the map's height.
	 * @return the result of the calculation.
	 * @throws ScriptException
	 *             if there is a syntax error in expression.
	 */
	private static float evaluateString(String expression, int w, int h)
			throws ScriptException, NumberFormatException {
		expression = expression.trim();
		expression = expression.replaceAll("H", h + "").replaceAll("W", w + "");

		ScriptEngineManager mgr = new ScriptEngineManager();
		ScriptEngine engine = mgr.getEngineByName("JavaScript");
		Object result = engine.eval(expression);
		return Float.parseFloat(result.toString());
	}

}
