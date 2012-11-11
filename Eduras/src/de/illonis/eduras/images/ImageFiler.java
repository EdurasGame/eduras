package de.illonis.eduras.images;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

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
	 * @throws IOExccetion
	 *             when image could not be loaded.
	 */
	public static BufferedImage load(String fileName) throws IOException {
		return ImageIO.read(ImageFiler.class.getResource(fileName));
	}
}
