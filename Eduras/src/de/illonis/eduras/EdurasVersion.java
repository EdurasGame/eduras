package de.illonis.eduras;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.illonis.edulog.EduLog;

/**
 * Retrieves and compares the version of the Eduras? distribution.
 * 
 * @author Florian 'Ren' Mai <florian.ren.mai@googlemail.com>
 * 
 */
public class EdurasVersion {
	private final static Logger L = EduLog.getLoggerFor(EdurasVersion.class
			.getName());
	private final static Charset ENCODING = StandardCharsets.UTF_8;

	/**
	 * Returns the Eduras? distribution's version.
	 * 
	 * @return version
	 */
	public static String getVersion() {
		String version = "unknown";
		URL res = EdurasVersion.class.getResource("/version");
		if (res == null) {
			L.log(Level.SEVERE, "Could not locate version file in resources.");
			return version;
		}
		// using a try-with-resource automatically closes this resource and
		// limits code chaos.
		// see
		// http://docs.oracle.com/javase/tutorial/essential/exceptions/tryResourceClose.html
		try (Scanner scanner = new Scanner(res.openStream(), ENCODING.name())) {
			if (scanner.hasNextLine())
				version = scanner.nextLine();
			else
				L.log(Level.SEVERE, "Version file is empty.");
		} catch (IOException e) {
			L.log(Level.SEVERE, "Cannot find or read version file.", e);
		}

		L.log(Level.INFO, "Resolved current version to " + version);

		return version;
	}
}
