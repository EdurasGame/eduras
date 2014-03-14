package de.illonis.eduras.maps;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.maps.persistence.InvalidDataException;

/**
 * a deathmatch map
 * 
 * @author Jan Reese
 * 
 */
public class FunMap extends Map {

	private final static Logger L = EduLog.getLoggerFor(FunMap.class.getName());

	/**
	 * Creates the map.
	 */
	public FunMap() {
		super("funmap", "Jan Reese", 600, 600);
		setCreated("2013-06-11");
	}

	@Override
	protected void buildMap() {
		try {
			loadFromFile("funmap.erm");
		} catch (InvalidDataException | IOException e) {
			L.log(Level.SEVERE, "error loading map", e);
		}
	}
}