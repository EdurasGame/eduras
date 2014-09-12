package de.illonis.eduras.events;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.logging.Logger;

import de.illonis.edulog.EduLog;

public class SendResourceEvent extends GameEvent {
	private final static Logger L = EduLog.getLoggerFor(SendResourceEvent.class
			.getName());

	private final String resourceName;
	private final File resource;

	public SendResourceEvent(GameEventNumber type, String resourceName,
			File resource) throws IOException {
		super(type);

		this.resourceName = resourceName;
		this.resource = resource;

		putArgument(resourceName);
		putArgument(Files.readAllBytes(resource.toPath()));
	}

	public File getResource() {
		return resource;
	}

	public String getResourceName() {
		return resourceName;
	}
}
