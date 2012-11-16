package de.illonis.eduras.gui;

import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

/**
 * A single-line textfield that has a maximum input length. If more chars are
 * typed or pasted, they will be ignored.
 * 
 * @author illonis
 * 
 */
public class MaxLengthTextField extends JTextField {

	private static final long serialVersionUID = 1L;

	/**
	 * Creates a new limited textfield with given maximum input length.
	 * 
	 * @param maxLength
	 *            maximum input length.
	 */
	public MaxLengthTextField(int maxLength) {
		super();
		setDocument(new MaxSizeDocument(maxLength));
	}

	private class MaxSizeDocument extends PlainDocument {
		private static final long serialVersionUID = 1L;
		private int maxLength;

		public MaxSizeDocument(int maxLength) {
			this.maxLength = maxLength + 1;
		}

		@Override
		public void insertString(int offs, String str, AttributeSet a)
				throws BadLocationException {
			if (str.length() == 0)
				return;
			if (getLength() + str.length() < maxLength) {
				if (filter(str))
					super.insertString(offs, str, a);
			}
		}
	}

	/**
	 * Filteres given input string. This function controlles whether a given
	 * string may be added to current text when maximum length is not exceeded.<br>
	 * Return value decides if the string is added completely or nothing will be
	 * added.
	 * 
	 * @param str
	 *            String to check.
	 * @return true if text may be added, false otherwise.
	 */
	protected boolean filter(String str) {
		return true;
	}

}
