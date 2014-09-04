package de.illonis.eduras.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.gameclient.EdurasClient;
import de.illonis.eduras.maps.Map;

public class ResourceManager {
	private final static Logger L = EduLog.getLoggerFor(ResourceManager.class
			.getName());

	private final static String[] nativeFiles = new String[] {
			"jinput-dx8_64.dll", "jinput-dx8.dll", "jinput-raw_64.dll",
			"jinput-raw.dll", "libjinput-linux.so", "libjinput-linux64.so",
			"libjinput-osx.jnilib", "liblwjgl.jnilib", "liblwjgl.so",
			"liblwjgl64.so", "libopenal.so", "libopenal64.so", "lwjgl.dll",
			"lwjgl64.dll", "openal.dylib", "OpenAL32.dll", "OpenAL64.dll" };

	public static void extractNatives() throws UnsatisfiedLinkError,
			IOException {
		URI nativeDir = createFolderIfDoesntExist("native/");

		L.info("Extracting native libraries...");
		for (String file : nativeFiles) {
			InputStream internalFile = EdurasClient.class
					.getResourceAsStream("/native/" + file);
			if (internalFile == null)
				throw new UnsatisfiedLinkError("Could not load " + file);

			Path target = Paths.get(nativeDir.resolve(file));
			Files.copy(internalFile, target,
					StandardCopyOption.REPLACE_EXISTING);
		}

		L.info("Done extracting native libraries.");
	}

	public static void extractResources() throws IOException {
		URI resourceFolder = createFolderIfDoesntExist("data/");
		createFolderIfDoesntExist("data/maps/");

		for (int i = 0; i < Map.defaultMaps.length; i++) {
			URL urlToMap = Map.class.getResource("data/" + Map.defaultMaps[i]);

			if (urlToMap == null) {
				L.severe("Cannot find resource map " + Map.defaultMaps[i]);
				continue;
			}

			File mapFileToExtract = new File(urlToMap.getPath());
			File existingMapFile = new File(resourceFolder.getPath() + "maps/"
					+ Map.defaultMaps[i]);

			if (!existingMapFile.exists()
					|| !HashCalculator.computeHash(mapFileToExtract.toPath())
							.equals(HashCalculator.computeHash(existingMapFile
									.toPath()))) {
				try {
					Files.copy(mapFileToExtract.toPath(),
							existingMapFile.toPath(),
							StandardCopyOption.REPLACE_EXISTING);
				} catch (IOException e) {
					L.log(Level.WARNING, "Error when copying map file.", e);
					continue;
				}
			}

		}
	}

	private static URI createFolderIfDoesntExist(String folderName)
			throws IOException {
		URI nativeDir = PathFinder.findFile(folderName);
		Path nativePath = Paths.get(nativeDir);
		if (Files.exists(nativePath, LinkOption.NOFOLLOW_LINKS)) {
			L.info("Found " + folderName + " folder. Nothing to do.");
			return nativeDir;
		} else {
			L.fine("Creating native directory at " + nativePath);
			Files.createDirectory(nativePath);
			return nativeDir;
		}
	}

	public static URL getMapFileUrl(String mapFileName)
			throws MalformedURLException {
		return PathFinder.findFile("data/maps/" + mapFileName).toURL();
	}
}
