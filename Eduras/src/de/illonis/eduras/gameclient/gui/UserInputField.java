package de.illonis.eduras.gameclient.gui;

import java.util.regex.Pattern;

/**
 * This text input field allows only alphanumeric characters including "-".
 * 
 * @author illonis
 * 
 */
public class UserInputField extends FilterableTextField {
	private static final long serialVersionUID = 1L;
	private final static String pattern = "[a-zA-Z0-9-]*";

	@Override
	protected boolean filter(String str) {
		if (!Pattern.matches(pattern, str))
			return false;
		return super.filter(str);
	}

	@Override
	public void setText(String t) {
		System.out.println("settext");
		if (filter(t)) {
			System.out.println("filter ok");

			super.setText(t);
		}
	}

}
