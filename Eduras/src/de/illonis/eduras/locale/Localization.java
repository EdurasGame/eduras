package de.illonis.eduras.locale;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import de.illonis.eduras.exceptions.LocaleNotFoundException;
import de.illonis.eduras.logger.EduLog;

/**
 * A localization class that provides features to use internationalized strings.
 * Supported languages are defined in {@link #SUPPORTED_LOCALES}. Their values
 * are stored in their appropriate properties-files (e.g.
 * <code>lang_de.properties</code>).<br>
 * Localization also supports <a href="../util/Formatter.html#syntax">format
 * strings</a>.
 * 
 * @author illonis
 * 
 */
public final class Localization {
	private static final String BUNDLE_NAME = "de.illonis.eduras.locale.lang";

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
		EduLog.info("[LOCALE] Set to " + currentLocale.toString() + ".");
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
		int pos = getIndexOf(toFind, SUPPORTED_LOCALES);
		if (pos == -1)
			throw new LocaleNotFoundException(locale);
		return pos;
	}

	/**
	 * Returns localized string identified by given key.<br>
	 * If string does not exist, its key is returned.<br>
	 * <br>
	 * <b>Note:</b> If you want to use localized formatted strings, use
	 * {@link #getStringF(String, Object...)}.
	 * 
	 * @see #getStringF(String, Object...)
	 * 
	 * @param key
	 *            key of string.<br>
	 *            String must exist in localization database.
	 * @return localized string.
	 */
	public static final String getString(String key) {
		try {
			return RESOURCES[currentLocaleNumber].getString(key);
		} catch (MissingResourceException e) {
			try {
				return RESOURCES[0].getString(key);
			} catch (MissingResourceException ex) {
				return '!' + key + '!';
			}
		}
	}

	/**
	 * Returns a localized string identified by given key formatted by
	 * {@link String#format(String, Object...)}.
	 * 
	 * @see #getString(String)
	 * 
	 * @param key
	 *            key of string. Dereferenced string must be a <a
	 *            href="../util/Formatter.html#syntax">format string</a>.
	 * @param args
	 *            Arguments referenced by the format specifiers in the format
	 *            string. If there are more arguments than format specifiers,
	 *            the extra arguments are ignored. The number of arguments is
	 *            variable and may be zero. The maximum number of arguments is
	 *            limited by the maximum dimension of a Java array as defined by
	 *            <cite>The Java&trade; Virtual Machine Specification</cite>.
	 *            The behaviour on a <tt>null</tt> argument depends on the <a
	 *            href="../util/Formatter.html#syntax">conversion</a>.
	 * @return localized and formatted string.
	 */
	public static final String getStringF(String key, Object... args) {
		if (RESOURCES[currentLocaleNumber].containsKey(key)) {
			String s = RESOURCES[currentLocaleNumber].getString(key);
			return String.format(s, args);
		} else if (RESOURCES[0].containsKey(key)) {
			String s = RESOURCES[0].getString(key);

			// NOTE: need to hardcode this string to prevent recursive errors
			// when this is not translated, too!
			String warning = "No translation to %s found for %s!";
			EduLog.warning(String.format(warning,
					RESOURCES[currentLocaleNumber].getLocale().toString(), key));

			return String.format(s, args);
		} else {

			// NOTE: need to hardcode this string to prevent recursive errors
			// when this is not translated, too!
			String error = "No translation to default language found for %s!";
			EduLog.error(String.format(error, RESOURCES[currentLocaleNumber]
					.getLocale().toString(), key));
			return '!' + key + '!';
		}
	}
}
