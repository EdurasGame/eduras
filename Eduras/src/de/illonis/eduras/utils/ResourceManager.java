package de.illonis.eduras.utils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.gameclient.EdurasClient;
import de.illonis.eduras.maps.Map;

public class ResourceManager {
	private final static Logger L = EduLog.getLoggerFor(ResourceManager.class
			.getName());

	private static final Path RES_FOLDER = Paths.get("data/");

	@SuppressWarnings("javadoc")
	public enum ResourceType {
		MAP("maps/"), SOUND("sounds/"), IMAGE("images/"), MUSIC("music/"),
		LIBRARY("lib"), NATIVE_LIBRARY("lib/native/");

		private String folder;

		private ResourceType(String folder) {
			this.folder = folder;
		}

		String getFolder() {
			return folder;
		}
	}

	private final static String[] nativeFiles = new String[] {
			"jinput-dx8_64.dll", "jinput-dx8.dll", "jinput-raw_64.dll",
			"jinput-raw.dll", "libjinput-linux.so", "libjinput-linux64.so",
			"libjinput-osx.jnilib", "liblwjgl.jnilib", "liblwjgl.so",
			"liblwjgl64.so", "libopenal.so", "libopenal64.so", "lwjgl.dll",
			"lwjgl64.dll", "openal.dylib", "OpenAL32.dll", "OpenAL64.dll" };

	public static void extractNatives() throws IOException {
		Files.createDirectories(RES_FOLDER.resolve(ResourceType.MAP.getFolder()));

		L.info("Extracting native libraries...");
		for (String file : nativeFiles) {
			try (InputStream internalFile = EdurasClient.class
					.getResourceAsStream("/native/" + file)) {
				ResourceManager.saveResource(ResourceType.NATIVE_LIBRARY, file,
						internalFile);
			}
		}

		L.info("Done extracting native libraries.");
	}

	public static void extractMaps() throws IOException {
		Files.createDirectories(resourceToPath(ResourceType.MAP, ""));

		for (int i = 0; i < Map.defaultMaps.length; i++) {
			String packedHash = "";
			String mapPath = "data/" + Map.defaultMaps[i];
			try (InputStream mapInputStreamForHash = Map.class
					.getResourceAsStream(mapPath)) {
				packedHash = HashCalculator.computeHash(mapInputStreamForHash);
			} catch (IOException e) {
				L.log(Level.SEVERE, "Could not extract map "
						+ Map.defaultMaps[i], e);
				continue;
			}
			Path existingMap = resourceToPath(ResourceType.MAP,
					Map.defaultMaps[i]);

			if (!Files.exists(existingMap)
					|| !packedHash.equals(getHashOfResource(ResourceType.MAP,
							Map.defaultMaps[i]))) {
				try (InputStream mapInputStreamForCopy = Map.class
						.getResourceAsStream(mapPath)) {
					ResourceManager.saveResource(ResourceType.MAP,
							Map.defaultMaps[i], mapInputStreamForCopy);
				} catch (IOException e) {
					L.log(Level.WARNING, "Error when copying map file.", e);
					continue;
				}
			}
		}
	}

	/**
	 * Returns te SHA-256 hash of given resource.
	 * 
	 * @param type
	 *            type of resource.
	 * @param fileName
	 *            resource file name.
	 * @return the hash.
	 * @throws IOException
	 *             if an error occurs while calculating hash.
	 */
	public static String getHashOfResource(ResourceType type, String fileName)
			throws IOException {
		String hash = "";
		try (InputStream is = openResource(type, fileName)) {
			hash = HashCalculator.computeHash(is);
			L.info("Hash of " + type.name() + " " + fileName + " is " + hash);
		}
		return hash;
	}

	/**
	 * Opens a game resource and returns a open stream for reading that has to
	 * be closed.
	 * 
	 * @param type
	 *            the type of resource.
	 * 
	 * @param fileName
	 *            the filename.
	 * 
	 * @return an input stream of given resource.
	 * 
	 * @throws IOException
	 *             if an error occured opening file.
	 */
	public static InputStream openResource(ResourceType type, String fileName)
			throws IOException {
		Path path = resourceToPath(type, fileName);
		return Files.newInputStream(path, StandardOpenOption.READ);
	}

	/**
	 * Saves data into a resource file. This method replaces an existing file.
	 * 
	 * @param type
	 *            the resource type.
	 * @param fileName
	 *            the new filename.
	 * @param data
	 *            the data to save.
	 * @return the path to the file.
	 * @throws IOException
	 *             if an error occurs while writing to file.
	 */
	public static Path saveResource(ResourceType type, String fileName,
			byte[] data) throws IOException {
		Path path = resourceToPath(type, fileName);
		return Files.write(path, data, StandardOpenOption.TRUNCATE_EXISTING,
				StandardOpenOption.CREATE);
	}

	/**
	 * Saves data into a resource file. This method replaces an existing file.
	 * 
	 * @param type
	 *            the resource type.
	 * @param fileName
	 *            the new filename.
	 * @param data
	 *            the input stream to read data from.
	 * @return the path to the file.
	 * @throws IOException
	 *             if an error occurs while writing to file.
	 */
	public static Path saveResource(ResourceType type, String fileName,
			InputStream data) throws IOException {
		Path path = resourceToPath(type, fileName);
		Files.copy(data, path, StandardCopyOption.REPLACE_EXISTING);
		return path;
	}

	/**
	 * Retrieves a path for a game resource.
	 * 
	 * @param type
	 *            the type of resource.
	 * @param fileName
	 *            the filename.
	 * @return the path.
	 */
	public static Path resourceToPath(ResourceType type, String fileName) {
		Path p = Paths.get(PathFinder.getBaseDir());

		return p.resolve(RES_FOLDER).resolve(type.getFolder())
				.resolve(fileName);
	}

	public static Path createTemporaryFileFromResource(InputStream inputStream,
			String name) throws IOException {
		Path file = Files.createTempFile(name, "eduras");
		Files.copy(inputStream, file, StandardCopyOption.REPLACE_EXISTING);
		return file;
	}
}
