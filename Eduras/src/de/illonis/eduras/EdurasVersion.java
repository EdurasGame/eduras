package de.illonis.eduras;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
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

	/**
	 * Returns the Eduras? distribution's version.
	 * 
	 * @return version
	 */
	public static String getVersion() {
		String version = "unknown";

		File versionFile = new File(System.class.getResource("/version")
				.getPath());
		BufferedReader fileReader = null;
		try {
			fileReader = new BufferedReader(new FileReader(versionFile));
			version = fileReader.readLine();
		} catch (IOException e) {
			L.log(Level.SEVERE, "Cannot find or read version file.", e);
			version = "unknown";
		} finally {
			if (fileReader != null) {
				try {
					fileReader.close();
				} catch (IOException e) {
					L.log(Level.WARNING, "Cannot close file reader.", e);
				}
			}
		}

		return version;
	}
}
