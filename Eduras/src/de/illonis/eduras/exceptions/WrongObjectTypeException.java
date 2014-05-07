package de.illonis.eduras.exceptions;

import java.util.logging.Logger;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.ObjectFactory.ObjectType;

/**
 * This exception denotes that an object type was given that cannot be applied
 * to the specific case.
 * 
 * @author Florian 'Ren' Mai <florian.ren.mai@googlemail.com>
 * 
 */
public class WrongObjectTypeException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final static Logger L = EduLog
			.getLoggerFor(WrongObjectTypeException.class.getName());

	private final ObjectType wrongType;

	/**
	 * Create a new {@link WrongObjectTypeException}.
	 * 
	 * @param wrongType
	 *            the type that is not applicable for a specific situation
	 */
	public WrongObjectTypeException(ObjectType wrongType) {
		super("Cannot use objecttype " + wrongType);
		this.wrongType = wrongType;
	}

	/**
	 * The object type that is not applicable.
	 * 
	 * @return type
	 */
	public ObjectType getWrongType() {
		return wrongType;
	}
}
