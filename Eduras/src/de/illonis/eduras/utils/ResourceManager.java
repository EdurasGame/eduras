package de.illonis.eduras.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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

	private static final String RES_FOLDER = "data/";
	private static final String MAP_FOLDER = "maps/";

	private static final String PATH_TO_MAPS = RES_FOLDER + MAP_FOLDER;

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
		createFolderIfDoesntExist(RES_FOLDER);
		URI mapFolder = createFolderIfDoesntExist(PATH_TO_MAPS);

		for (int i = 0; i < Map.defaultMaps.length; i++) {
			InputStream mapInputStream = Map.class
					.getResourceAsStream(RES_FOLDER + Map.defaultMaps[i]);

			if (mapInputStream == null) {
				L.severe("Cannot find resource map " + Map.defaultMaps[i]);
				continue;
			}

			File existingMapFile = new File(mapFolder.getPath()
					+ Map.defaultMaps[i]);

			if (!existingMapFile.exists()
					|| !HashCalculator.computeHash(mapInputStream).equals(
							HashCalculator.computeHash(new FileInputStream(
									existingMapFile)))) {
				try {
					copyAndReplace(existingMapFile, mapInputStream);
				} catch (IOException e) {
					L.log(Level.WARNING, "Error when copying map file.", e);
					continue;
				}
			}

		}
	}

	private static void copyAndReplace(File fileToReplace,
			InputStream streamToReplaceWith) throws IOException {
		System.out.println(fileToReplace.getAbsolutePath());
		if (!fileToReplace.exists()) {
			fileToReplace.createNewFile();
		}
		FileOutputStream out = new FileOutputStream(fileToReplace);

		byte[] buffer = new byte[1024];
		int len = streamToReplaceWith.read(buffer);
		while (len != -1) {
			out.write(buffer, 0, len);
			len = streamToReplaceWith.read(buffer);
		}
		out.close();
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
		return PathFinder.findFile(PATH_TO_MAPS + mapFileName + ".erm").toURL();
	}

	public static String getHashOfMap(String mapFileName)
			throws MalformedURLException {
		URI pathToFile = PathFinder.findFile(PATH_TO_MAPS + mapFileName
				+ ".erm");
		if (pathToFile == null) {
			throw new MalformedURLException("Invalid map name " + mapFileName);
		}

		File file = new File(pathToFile);
		try {
			String hash = HashCalculator.computeHash(new FileInputStream(file));
			L.info("Hash of map " + mapFileName + " is " + hash);
			return HashCalculator.computeHash(new FileInputStream(file));
		} catch (FileNotFoundException e) {
			L.log(Level.INFO, "No such file to get hash of", e);
			return "";
		}
	}

	public static File writeMapFile(String name, byte[] data)
			throws IOException {

		File file = new File(PathFinder.findFile(PATH_TO_MAPS + name + ".erm"));
		if (file.exists() && file.isFile()) {
			file.delete();
		}
		file.createNewFile();
		System.out.println(file.getAbsolutePath());

		FileOutputStream outputStream;
		outputStream = new FileOutputStream(file);
		outputStream.write(data);
		outputStream.close();

		return file;

	}

	public static File createTemporaryFileFromResource(InputStream inputStream,
			String name) throws IOException {
		File file = File.createTempFile(name, "eduras");
		System.out.println(file.getPath());

		FileOutputStream outputStream = new FileOutputStream(file);

		int byteData = 0;
		do {
			byteData = inputStream.read();
			if (byteData != -1) {
				outputStream.write(byteData);
			}
		} while (byteData != -1);

		outputStream.close();
		return file;
	}
}
