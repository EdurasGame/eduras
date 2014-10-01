package de.illonis.eduras.mapeditor.validate;

import java.util.LinkedList;
import java.util.List;

import de.illonis.eduras.mapeditor.MapData;

/**
 * Validates a map for specific criteria.
 * 
 * @author illonis
 * 
 */
public abstract class ValidateTask {

	protected final MapData data;
	private final List<String> errorMessages;
	private final String description;
	private boolean success;

	protected ValidateTask(String description) {
		this.description = description;
		errorMessages = new LinkedList<String>();
		data = MapData.getInstance();
	}

	/**
	 * Validates the current map.
	 * 
	 * @return true if validation passed.
	 * @see #getErrorMessages()
	 */
	public final boolean validate() {
		errorMessages.clear();
		success = false;
		success = performValidation();
		return success;
	}

	protected abstract boolean performValidation();

	/**
	 * @return true if validation was successful.
	 */
	public final boolean isSuccess() {
		return success;
	}

	/**
	 * @return the description of this validator.
	 */
	public final String getDescription() {
		return description;
	}

	protected final void addErrorMessage(String errorMessage) {
		this.errorMessages.add(errorMessage);
		success = false;
	}

	/**
	 * Returns a list of error messages. Call this after {@link #validate()}
	 * returned false for detailed message.
	 * 
	 * @return the error message.
	 */
	public final List<String> getErrorMessages() {
		return errorMessages;
	}
}
