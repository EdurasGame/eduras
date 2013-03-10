package de.illonis.eduras.gameclient.gui;

import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

/**
 * A filterable text field only allows to specify a filter function on text
 * input. Keep in mind to call super filter method when overwriting filter
 * method to ensure filter capability.
 * 
 * @author illonis
 * 
 */
public class FilterableTextField extends JTextField {

	private static final long serialVersionUID = 1L;

	/**
	 * Creates a filterable text field.
	 */
	public FilterableTextField() {
		setDocument(new FilterDocument());
	}

	/**
	 * Filters given input string. This function controlles whether a given
	 * string may be added to current text.<br>
	 * Return value decides if the string is added completely or nothing will be
	 * added.
	 * 
	 * @param str
	 *            typed string.
	 * @return true if text may be added, false otherwise.
	 */
	protected boolean filter(String str) {
		return true;
	}

	private class FilterDocument extends PlainDocument {
		private static final long serialVersionUID = 1L;

		@Override
		public void insertString(int offs, String str, AttributeSet a)
				throws BadLocationException {
			if (str.length() == 0)
				return;

			if (filter(str))
				super.insertString(offs, str, a);
		}
	}
}