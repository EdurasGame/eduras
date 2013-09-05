package de.illonis.eduras.maps.persistence;

import java.awt.geom.Rectangle2D;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.text.ParseException;
import java.util.Date;
import java.util.LinkedList;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import de.illonis.eduras.ObjectFactory.ObjectType;
import de.illonis.eduras.gamemodes.GameMode.GameModeNumber;
import de.illonis.eduras.maps.InitialObjectData;
import de.illonis.eduras.maps.LoadedMap;
import de.illonis.eduras.maps.Map;
import de.illonis.eduras.maps.SpawnPosition;
import de.illonis.eduras.maps.SpawnPosition.SpawnType;

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

	private enum ReadMode {
		NONE, SPAWNPOINTS, OBJECTS;
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
	public static Map readMap(File inputFile) throws InvalidDataException,
			IOException {

		String mapName = "";
		String author = "";
		int width = 0;
		int height = 0;
		ReadMode currentMode = ReadMode.NONE;
		Date created = new Date();
		final LinkedList<GameModeNumber> gameModes = new LinkedList<GameModeNumber>();
		final LinkedList<SpawnPosition> spawnPositions = new LinkedList<SpawnPosition>();
		final LinkedList<InitialObjectData> gameObjects = new LinkedList<InitialObjectData>();

		// Charset charset = Charset.forName("UTF-8");

		BufferedReader reader = new BufferedReader(new FileReader(inputFile));

		String line = null;
		while ((line = reader.readLine()) != null) {
			if (line.trim().isEmpty())
				continue;
			if (line.startsWith("#"))
				continue;

			if (line.startsWith("@")) {
				String[] data = line.substring(1).split("=");
				String key = data[0].trim();
				String value = data[1].trim();
				switch (key) {
				case "mapname":
					mapName = value;
					break;
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
								"Date format is invalid: " + e.getMessage());
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
									"Gamemode does not exist: " + modes[i]);
						}
					}
					break;
				default:
					throw new InvalidDataException("Unknown tag found: @" + key);
				}
			} else if (line.startsWith(":")) {
				String mode = line.substring(1).trim();
				switch (mode) {
				case "spawnpoints":
					currentMode = ReadMode.SPAWNPOINTS;
					break;
				case "objects":
					currentMode = ReadMode.OBJECTS;
					break;
				default:
					throw new InvalidDataException("Invalid control point: "
							+ mode);
				}
			} else {
				switch (currentMode) {
				case NONE:
					throw new InvalidDataException("Invalid line found: "
							+ line);
				case OBJECTS:
					String[] objectData = line.split(",");
					double objX = 0,
					objY = 0;
					try {
						ObjectType objectType = ObjectType
								.valueOf(objectData[0].trim());
						objX = evaluateString(objectData[1], width, height);
						objY = evaluateString(objectData[2], width, height);
						InitialObjectData oData = new InitialObjectData(
								objectType, objX, objY);
						gameObjects.add(oData);
					} catch (ScriptException e) {
						throw new InvalidDataException(
								"Invalid math expression: " + e.getMessage());
					} catch (IllegalArgumentException e) {
						throw new InvalidDataException(
								"Illegal game object type: "
										+ objectData[0].trim());
					}

					break;
				case SPAWNPOINTS:
					String[] spawnData = line.split(",");
					double x = 0,
					y = 0;
					int w = 0,
					h = 0;
					try {
						x = evaluateString(spawnData[0].trim(), width, height);
						y = evaluateString(spawnData[1].trim(), width, height);
					} catch (ScriptException e) {
						throw new InvalidDataException(
								"Invalid math expression: " + e.getMessage());
					}
					try {
						w = Integer.parseInt(spawnData[2].trim());
						h = Integer.parseInt(spawnData[3].trim());
					} catch (NumberFormatException e) {
						throw new InvalidDataException(
								"Invalid width/height value: " + e.getMessage());
					}
					SpawnType type = SpawnType.valueOf(spawnData[4].trim());
					Rectangle2D.Double area = new Rectangle2D.Double(x, y, w, h);
					spawnPositions.add(new SpawnPosition(area, type));
					break;
				default:
					break;
				}
			}
		}
		reader.close();
		return new LoadedMap(mapName, author, width, height, created,
				spawnPositions, gameObjects, gameModes);
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
	private static Double evaluateString(String expression, int w, int h)
			throws ScriptException {

		expression = expression.replaceAll("H", h + "").replaceAll("W", w + "");

		ScriptEngineManager mgr = new ScriptEngineManager();
		ScriptEngine engine = mgr.getEngineByName("JavaScript");
		Object result = engine.eval(expression);
		return (Double) result;
	}

}
