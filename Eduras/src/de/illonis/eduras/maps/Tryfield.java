package de.illonis.eduras.maps;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.maps.persistence.InvalidDataException;

/**
 * New Map for Edura mode
 * 
 * @author Jan Reese
 * 
 */
public class Tryfield extends Map {

	private final static Logger L = EduLog.getLoggerFor(EduraTestMap.class
			.getName());

	/**
	 * Creates the map.
	 */
	public Tryfield() {
		super("Tryfield", "jmr", 2700, 2700);
	}

	@Override
	protected void buildMap() throws InvalidDataException {
		try {
			loadFromFile("Tryfield.erm");
		} catch (IOException e) {
			L.log(Level.WARNING, "Could not open Tryfield mapfile.", e);
		}
	}
}
