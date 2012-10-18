package de.illonis.eduras.exceptions;

import java.util.Locale;

/**
 * An Exception that is thrown if you try to load a language that is not
 * supported by Eduras? yet.
 * 
 * @author illonis
 * 
 */
public class LocaleNotFoundException extends Exception {

	private static final long serialVersionUID = 1L;
	private Locale locale;

	/**
	 * Creates a new <code>LocaleNotFoundException</code>.
	 * 
	 * @param locale
	 *            locale that failed to load.
	 */
	public LocaleNotFoundException(Locale locale) {
		this.locale = locale;
	}

	/**
	 * Returns locale that was tried to load.
	 * 
	 * @return locale that was tried to load.
	 */
	public Locale getLocale() {
		return locale;
	}
}
