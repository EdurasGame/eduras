package de.illonis.eduras.maps;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.maps.persistence.InvalidDataException;

/**
 * a map for testing purpose, so add all your shit here whenever you wanna test
 * something
 * 
 * @author Florian 'Ren' Mai <florian.ren.mai@googlemail.com>
 * 
 */
public class TestMap extends Map {

	private final static Logger L = EduLog.getLoggerFor(FunMap.class.getName());

	/**
	 * Creates the map.
	 */
	public TestMap() {
		super("testmap", "Florian Mai", 1000, 1000);
		setCreated("2014-03-25");
	}

	@Override
	protected void buildMap() {
		try {
			loadFromFile("testmap.erm");
		} catch (InvalidDataException | IOException e) {
			L.log(Level.SEVERE, "error loading testmap", e);
		}
	}
}
