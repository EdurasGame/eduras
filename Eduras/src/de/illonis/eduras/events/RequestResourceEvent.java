package de.illonis.eduras.events;

import java.util.logging.Logger;

import de.illonis.edulog.EduLog;

public class RequestResourceEvent extends OwnerGameEvent {

	private final static Logger L = EduLog
			.getLoggerFor(RequestResourceEvent.class.getName());

	private final String resourceName;

	public RequestResourceEvent(int owner, GameEventNumber type, String resource) {
		super(type, owner);

		resourceName = resource;
		putArgument(resourceName);
	}

	public String getResourceName() {
		return resourceName;
	}
}
