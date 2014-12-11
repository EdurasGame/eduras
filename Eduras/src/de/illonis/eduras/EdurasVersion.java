package de.illonis.eduras;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.illonis.edulog.EduLog;
import de.illonis.edulog.PathFinder;
import de.illonis.eduras.settings.S;

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

	private final static String VERSION_FILENAME = "gitversion";

	/**
	 * Returns the Eduras? distribution's version.
	 * 
	 * @return version
	 */
	public static String getVersion() {
		String version = "unknown";
		URL res;
		if (S.fromEclipse) {
			try {
				res = new URL(PathFinder.getBaseDir(), "ant/gitversion");
				L.log(Level.INFO, "Using local version file due to debugging.");
			} catch (MalformedURLException e) {
				L.log(Level.SEVERE,
						"Error building url to debug version file.", e);
				return version;
			}
		} else {
			res = EdurasVersion.class.getResource("/" + VERSION_FILENAME);
			if (res == null) {
				L.log(Level.SEVERE,
						"Could not locate version file in resources.");
				return version;
			}
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
