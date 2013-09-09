package de.illonis.eduras.shapecreator.gui;

import de.illonis.eduras.gameclient.gui.MaxLengthTextField;

public class DoubleInputField extends MaxLengthTextField {

	public DoubleInputField() {
		super(50);
		setColumns(5);
	}

	/**
	 * @return the value as a double.
	 * 
	 * @author illonis
	 */
	public double getValue() {
		try {
			return Double.parseDouble(getText());
		} catch (NumberFormatException e) {
			return 0;
		}
	}

	@Override
	protected boolean filter(String str) {
		if (!isDouble(str))
			return false;
		return super.filter(str);
	}

	private boolean isDouble(String str) {
		System.out.println(str);
		return true;
	}
}
