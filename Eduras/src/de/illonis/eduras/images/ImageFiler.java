package de.illonis.eduras.images;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.locale.Localization;

/**
 * Provides utility features to load or interact with images stored in game
 * package. All images are stored in image-package or subpackages.
 * 
 * @author illonis
 * 
 */
public class ImageFiler {

	private final static Logger L = EduLog.getLoggerFor(ImageFiler.class
			.getName());

	/**
	 * Loads an image from internal filesystem and returns its
	 * {@link BufferedImage}.
	 * 
	 * @param fileName
	 *            file name of image. Must be relative to images-package.
	 * @return image.
	 * @throws IOException
	 *             when image could not be loaded.
	 * @throws IllegalArgumentException
	 *             if there is no file with given filename.
	 */
	public static BufferedImage load(String fileName) throws IOException,
			IllegalArgumentException {
		return ImageIO.read(ImageFiler.class.getResource(fileName));
	}

	/**
	 * Loads an icon from internal filesystem.
	 * 
	 * @param path
	 *            file name of icon. Must be relative to images-package.
	 * @return an {@link ImageIcon} from given file.
	 */
	public static ImageIcon loadIcon(String path) {
		URL imgURL = ImageFiler.class.getResource(path);
		if (imgURL != null) {
			return new ImageIcon(imgURL);
		} else {
			L.severe(Localization.getStringF("Client.errors.io.imagenotfound",
					path));
			return null;
		}
	}

	/**
	 * Calculates a scaled instance of an image.
	 * 
	 * @param srcImg
	 *            the source.
	 * @param w
	 *            the target width.
	 * @param h
	 *            the target height.
	 * @return the scaled image.
	 */
	public static Image getScaledImage(Image srcImg, int w, int h) {
		BufferedImage resizedImg = new BufferedImage(w, h,
				BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = resizedImg.createGraphics();
		g2.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION,
				RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
		g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
				RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		g2.drawImage(srcImg, 0, 0, w, h, null);
		g2.dispose();
		return resizedImg;
	}
}
