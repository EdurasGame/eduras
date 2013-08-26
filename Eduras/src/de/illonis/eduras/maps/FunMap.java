package de.illonis.eduras.maps;

import java.io.IOException;

import de.illonis.eduras.logger.EduLog;
import de.illonis.eduras.maps.persistence.InvalidDataException;

/**
 * a deathmatch map
 * 
 * @author Jan Reese
 * 
 */
public class FunMap extends Map {

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
		} catch (InvalidDataException e) {
			EduLog.passException(e);
		} catch (IOException e) {
			EduLog.passException(e);
		}
	}
}