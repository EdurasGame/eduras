package de.illonis.eduras.images;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/**
 * Provides utility features to load or interact with images stored in game
 * package. All images are stored in image-package or subpackages.
 * 
 * @author illonis
 * 
 */
public class ImageFiler {

	/**
	 * Loads an image from internal filesystem and returns its
	 * {@link BufferedImage}.
	 * 
	 * @param fileName
	 *            file name of image. Must be relative to images-package.
	 * @return image.
	 * @throws IOException
	 *             when image could not be loaded.
	 */
	public static BufferedImage load(String fileName) throws IOException {
		return ImageIO.read(ImageFiler.class.getResource(fileName));
	}

	public static ImageIcon loadIcon(String path) {
		URL imgURL = ImageFiler.class.getResource(path);
		if (imgURL != null) {
			return new ImageIcon(imgURL);
		} else {
			System.err.println("Couldn't find image file: " + path);
			return null;
		}
	}
}
