package de.illonis.eduras.maps;

import java.io.IOException;
import java.util.Date;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.gamemodes.GameMode.GameModeNumber;
import de.illonis.eduras.maps.persistence.InvalidDataException;

public class EduraTestMap extends EduraMap {

	private final static Logger L = EduLog.getLoggerFor(EduraTestMap.class
			.getName());

	public EduraTestMap() {
		super("eduratestmap", "ren mai", 200, 200, new Date(),
				new LinkedList<SpawnPosition>(),
				new LinkedList<InitialObjectData>(),
				new LinkedList<GameModeNumber>(), null);
	}

	@Override
	protected void buildMap() {
		try {
			loadFromFile("eduratestmap.erm");
		} catch (InvalidDataException e) {
			L.log(Level.WARNING, "TODO: message", e);
		} catch (IOException e) {
			L.log(Level.WARNING, "TODO: message", e);
		}
	}
}
