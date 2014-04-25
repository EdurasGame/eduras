package de.illonis.eduras.exceptions;

import java.util.logging.Logger;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.ObjectFactory.ObjectType;

public class WrongObjectTypeException extends Exception {
	private final static Logger L = EduLog
			.getLoggerFor(WrongObjectTypeException.class.getName());

	private final ObjectType wrongType;

	public WrongObjectTypeException(ObjectType wrongType) {
		super("Cannot use objecttype " + wrongType);
		this.wrongType = wrongType;
	}

	public ObjectType getWrongType() {
		return wrongType;
	}
}
