package de.illonis.eduras.gui;

/**
 * A single-line textfield that has a maximum input length. If more chars are
 * typed or pasted, they will be ignored.
 * 
 * @author illonis
 * 
 */
public class MaxLengthTextField extends FilterableTextField {

	private static final long serialVersionUID = 1L;
	private int maxLength;

	/**
	 * Creates a new limited textfield with given maximum input length.
	 * 
	 * @param maxLength
	 *            maximum input length.
	 */
	public MaxLengthTextField(int maxLength) {
		super();
		this.maxLength = maxLength + 1;
	}

	@Override
	protected boolean filter(String str) {
		if (getDocument().getLength() + str.length() < maxLength)
			return true;
		return super.filter(str);
	}
}
