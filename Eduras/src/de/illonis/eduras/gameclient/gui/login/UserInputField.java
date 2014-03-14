package de.illonis.eduras.gameclient.gui.login;

import java.util.regex.Pattern;

/**
 * This text input field allows only alphanumeric characters including "-".
 * 
 * @author illonis
 * 
 */
public class UserInputField extends FilterableTextField {
	private final static long serialVersionUID = 1L;
	private final static String pattern = "[a-zA-Z0-9-]*";

	@Override
	protected boolean filter(String str) {
		if (!Pattern.matches(pattern, str))
			return false;
		return super.filter(str);
	}

	@Override
	public void setText(String t) {
		if (filter(t))
			super.setText(t);
	}

}
