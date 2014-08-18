package de.illonis.eduras.maps;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.maps.persistence.InvalidDataException;

/**
 * This map is going to be the first elaborate Edura! map.
 * 
 * @author Florian 'Ren' Mai <florian.ren.mai@googlemail.com>
 * 
 */
public class Eduramus extends Map {

	private final static Logger L = EduLog.getLoggerFor(EduraTestMap.class
			.getName());

	/**
	 * Creates the map.
	 */
	public Eduramus() {
		super("eduramus", "Florian Mai", 200, 200);
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
