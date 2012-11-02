package de.illonis.eduras.gui;

import java.text.NumberFormat;
import java.text.ParsePosition;

/**
 * A textfield that accepts numeric input [0-9] only. It has a maximum value
 * lenth and does not accept float values (containing ".").
 * 
 * @author illonis
 * 
 */
public class NumericTextField extends MaxLengthTextField {

	private static final long serialVersionUID = 1L;

	/**
	 * Creates a new numeric textfield with given maximum length
	 * 
	 * @param maxLength
	 *            maximum length.
	 */
	public NumericTextField(int maxLength) {
		super(maxLength);
	}

	@Override
	protected boolean filter(String str) {
		return isNumeric(str);
	}

	/**
	 * Returns integer value of content. If textfield is empty, 0 is returned.
	 * 
	 * @return value.
	 */
	public int getValue() {
		if (getText().isEmpty())
			return 0;
		return Integer.parseInt(getText());
	}

	/**
	 * Checks if given string is numeric (contains only [0-9]) or not.
	 * 
	 * @param str
	 *            string to test.
	 * @return true if string is numeric, false otherwise.
	 */
	public static boolean isNumeric(String str) {
		NumberFormat formatter = NumberFormat.getInstance();
		ParsePosition pos = new ParsePosition(0);
		formatter.parse(str, pos);
		return str.length() == pos.getIndex();
	}
}
