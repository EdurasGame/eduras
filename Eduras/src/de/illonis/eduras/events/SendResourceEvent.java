package de.illonis.eduras.events;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Logger;

import de.illonis.edulog.EduLog;

public class SendResourceEvent extends GameEvent {
	private final static Logger L = EduLog.getLoggerFor(SendResourceEvent.class
			.getName());

	private final String resourceName;
	private final Path resource;

	public SendResourceEvent(GameEventNumber type, String mapName,
			Path resource) throws IOException {
		super(type);

		this.resourceName = mapName;
		this.resource = resource;

		putArgument(mapName);
		putArgument(Files.readAllBytes(resource));
	}

	public Path getResource() {
		return resource;
	}

	public String getResourceName() {
		return resourceName;
	}
}
