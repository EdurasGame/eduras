package de.illonis.eduras.utils;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.security.CodeSource;

/**
 * Simplifies resource locating.
 * 
 * @author illonis
 * 
 */
public class PathFinder {

	/**
	 * Retrieves the path where the programm jar is located.
	 * 
	 * @return the jar's location.
	 */
	public static URI getBaseDir() {
		try {
			// try another way
			CodeSource source = PathFinder.class.getProtectionDomain()
					.getCodeSource();
			if (source != null) {
				URI url2 = source.getLocation().toURI();
				if (url2.toString().endsWith(".jar")) {
					return url2.resolve(".");
				}
				return url2;
			} else {
				URI url = ClassLoader.getSystemClassLoader().getResource(".")
						.toURI();
				if (url != null) {
					URI parent = url.resolve("../");
					return parent;
				} else {
					throw new RuntimeException(
							"Base directory could not be resolved.");
				}
			}
		} catch (URISyntaxException e) {
			return null;
		}
	}

	/**
	 * Returns an url that points to a file relative to program folder.<br>
	 * The path is built by combining {@link #getBaseDir()} with the fileName.
	 * 
	 * @param fileName
	 *            the file name.
	 * @return an uri locating the file.
	 */
	public static URI findFile(String fileName) {
		return PathFinder.getBaseDir().resolve(fileName);
	}

	/**
	 * Retrieves the name of the currently running jar file.
	 * 
	 * @return the name of the jar file.
	 * @throws NoJarFileException
	 *             if the program is running from eclipse or not from a jar.
	 */
	public static String getJarName() throws NoJarFileException {
		String path;
		String s = PathFinder.class.getName().replace('.', '/') + ".class";
		URL url = PathFinder.class.getClassLoader().getResource(s);

		try {
			path = URLDecoder.decode(url.getPath(), "UTF-8");
		} catch (UnsupportedEncodingException ex) {
			throw new NoJarFileException();
		}
		if (path.startsWith("file:")) {
			int end = path.lastIndexOf(".jar!");
			int begin = path.lastIndexOf("/", end) + 1;
			return path.substring(begin, end + 4);
		} else {
			throw new NoJarFileException();
		}
	}
}
