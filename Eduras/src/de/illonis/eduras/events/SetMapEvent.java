package de.illonis.eduras.events;

import java.util.logging.Logger;

import de.illonis.edulog.EduLog;

public class SetMapEvent extends GameEvent {

	private final static Logger L = EduLog.getLoggerFor(SetMapEvent.class
			.getName());

	private String nameOfNewMap;

	public SetMapEvent(String nameOfNewMap) {
		super(GameEventNumber.SET_MAP);
		this.nameOfNewMap = nameOfNewMap;
		putArgument(nameOfNewMap);
	}

	public String getNameOfNewMap() {
		return nameOfNewMap;
	}

}
