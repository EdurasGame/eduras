package de.illonis.eduras.maps;

import java.io.IOException;
import java.util.Date;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.gamemodes.GameMode.GameModeNumber;
import de.illonis.eduras.maps.persistence.InvalidDataException;

/**
 * This map is going to be the first elaborate Edura! map.
 * 
 * @author Florian 'Ren' Mai <florian.ren.mai@googlemail.com>
 * 
 */
public class Eduramus extends EduraMap {

	private final static Logger L = EduLog.getLoggerFor(EduraTestMap.class
			.getName());

	/**
	 * Creates the map.
	 */
	public Eduramus() {
		super("eduramus", "Florian Mai", 200, 200, new Date(),
				new LinkedList<SpawnPosition>(),
				new LinkedList<InitialObjectData>(),
				new LinkedList<GameModeNumber>(), null);
	}

	@Override
	protected void buildMap() {
		try {
			loadFromFile("eduramus.erm");
		} catch (InvalidDataException e) {
			L.log(Level.WARNING, "TODO: message", e);
		} catch (IOException e) {
			L.log(Level.WARNING, "TODO: message", e);
		}
	}
}
