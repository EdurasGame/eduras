package de.illonis.eduras.utils;

import java.beans.PropertyEditor;
import java.beans.PropertyEditorManager;

import javax.lang.model.type.PrimitiveType;

/**
 * Contains useful methods when using reflections.
 * 
 * @author illonis
 * 
 */
public class ReflectionTools {

	/**
	 * Converts a string to object of given {@link PrimitiveType}.
	 * 
	 * @param targetType
	 *            the target type.
	 * @param value
	 *            the string value.
	 * @return an object of given type with given value.
	 */
	public static Object toPrimitive(Class<?> targetType, String value) {
		PropertyEditor editor = PropertyEditorManager.findEditor(targetType);
		editor.setAsText(value);
		return editor.getValue();
	}
}
