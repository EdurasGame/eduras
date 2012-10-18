package de.illonis.eduras.locale;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import de.illonis.eduras.exceptions.LocaleNotFoundException;

/**
 * A localization class that provides features to use internationalized strings.
 * Supported languages are defined in {@link #SUPPORTED_LOCALES}. Their values
 * are stored in their appropriate properties-files (e.g.
 * <code>lang_de.properties</code>).
 * 
 * @author illonis
 * 
 */
public final class Localization {
	private static final String BUNDLE_NAME = "de.illonis.eduras.locale.lang"; //$NON-NLS-1$

	/**
	 * Contains all supported languages. First language is default language.
	 */
	public static final String[] SUPPORTED_LOCALES = new String[] { "EN", "DE" };

	private final static ResourceBundle[] RESOURCES = new ResourceBundle[SUPPORTED_LOCALES.length];
	private static Locale currentLocale;
	private static int currentLocaleNumber;

	static {
		for (int i = 0; i < SUPPORTED_LOCALES.length; i++)
			RESOURCES[i] = ResourceBundle.getBundle(BUNDLE_NAME, new Locale(
					SUPPORTED_LOCALES[i]));
		setToSystemDefault();
	}

	/**
	 * Sets current locale to given locale. If locale is not found, language is
	 * not changed.
	 * 
	 * @param locale
	 *            locale to use.
	 * @throws LocaleNotFoundException
	 *             if locale is not supported.
	 */
	public static final void setLocale(Locale locale)
			throws LocaleNotFoundException {
		currentLocaleNumber = findIndexOf(locale);
		currentLocale = locale;
		System.out.println("[LOCALE] Set to " + currentLocale.toString() + ".");
	}

	/**
	 * Sets locale to system default locale. If system default locale is not
	 * supported, locale will be reset to default locale.
	 * 
	 * @return true if system default language is supported, false otherwise.
	 */
	public static final boolean setToSystemDefault() {

		try {
			setLocale(Locale.getDefault());
			return true;
		} catch (LocaleNotFoundException e) {
			resetLocale();
			return false;
		}
	}

	/**
	 * Returns system default locale.
	 * 
	 * @return system default locale.
	 */
	public static final Locale getSystemDefault() {
		return Locale.getDefault();
	}

	/**
	 * Resets locale to default locale.
	 */
	public static final void resetLocale() {
		currentLocaleNumber = 0;
		currentLocale = new Locale(SUPPORTED_LOCALES[0]);
	}

	/**
	 * Returns current locale
	 * 
	 * @return current locale
	 */
	public static Locale getCurrentLocale() {
		return currentLocale;
	}

	/**
	 * Searches for given string in given string array and returns its index.
	 * 
	 * @param needle
	 *            string to search for.
	 * @param haystack
	 *            string array to search in.
	 * @return index of given string, -1 if no string was found.
	 */
	private static final int getIndexOf(String needle, String[] haystack) {
		for (int i = 0; i < haystack.length; i++) {
			String value = haystack[i];
			if (needle.equalsIgnoreCase(value))
				return i;
		}
		return -1;
	}

	/**
	 * Returns index of given locale in supported locales.
	 * 
	 * @param locale
	 *            locale to search for.
	 * @return index of given locale.
	 * @throws LocaleNotFoundException
	 *             if locale is not supported.
	 */
	private static final int findIndexOf(Locale locale)
			throws LocaleNotFoundException {
		String toFind = locale.getLanguage();
		System.out.println(toFind);

		int pos = getIndexOf(toFind, SUPPORTED_LOCALES);
		if (pos == -1)
			throw new LocaleNotFoundException(locale);
		return pos;
	}

	/**
	 * Returns localized string of given key.
	 * 
	 * @param key
	 *            key of string.
	 * @return localized string.
	 */
	public static final String getString(String key) {
		try {
			return RESOURCES[currentLocaleNumber].getString(key);
		} catch (MissingResourceException e) {
			return '!' + key + '!';
		}
	}
}
