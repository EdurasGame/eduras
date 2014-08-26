package de.illonis.eduras.images;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.ImageIcon;

import org.lwjgl.opengl.Display;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.gameclient.datacache.CacheInfo;
import de.illonis.eduras.gameclient.gui.game.GameRenderer;
import de.illonis.eduras.locale.Localization;
import de.illonis.eduras.utils.Pair;

/**
 * Provides utility features to load or interact with images stored in game
 * package. All images are stored in image-package or subpackages.
 * 
 * @author illonis
 * 
 */
public class ImageFiler {

	/**
	 * Eduras provides different files for different resolutions and scales on
	 * resolutions between them.<br>
	 * Always use the next higher value if you are on a resolution not listed.
	 */
	@SuppressWarnings("javadoc")
	public enum ImageResolution {
		WINDOWED(".low", 800, 600), LAPTOP(".medium", 1366, 768), FULLHD("",
				1920, 1080);

		private ImageResolution(String suffix, int width, int height) {
			this.suffix = suffix;
			this.width = width;
			this.height = height;
		}

		private String suffix;
		private int width, height;

		public String getSuffix() {
			return suffix;
		}

		public float getWidth() {
			return width;
		}

		public float getHeight() {
			return height;
		}
	}

	private static float getScaleFactor(ImageResolution res) {
		if (Display.getWidth() == res.getWidth()
				&& Display.getHeight() == res.getHeight())
			return 1f;
		return GameRenderer.getRenderScale()/ GameRenderer.getRenderScale(res);
	}

	private static Pair<ImageResolution, Float> calculateResolution() {
		int width = Display.getWidth();
		int height = Display.getHeight();
		if (width > 1366 && height > 768) {
			return new Pair<ImageResolution, Float>(ImageResolution.FULLHD,
					getScaleFactor(ImageResolution.FULLHD));
		} else if (width > 800 && height > 600) {
			return new Pair<ImageResolution, Float>(ImageResolution.LAPTOP,
					getScaleFactor(ImageResolution.LAPTOP));
		} else {
			return new Pair<ImageResolution, Float>(ImageResolution.WINDOWED,
					getScaleFactor(ImageResolution.WINDOWED));
		}
	}

	private final static Logger L = EduLog.getLoggerFor(ImageFiler.class
			.getName());

	/**
	 * Loads an image scaled for current resolution from internal filesystem and
	 * returns its {@link BufferedImage}.
	 * 
	 * @param fileName
	 *            file name of image. Must be relative to images-package.
	 * @return image.
	 * @throws SlickException
	 *             when image could not be loaded.
	 */
	public static Image load(String fileName) throws SlickException {
		Pair<ImageResolution, Float> resolution = calculateResolution();

		float factor = resolution.getSecond();
		Image image;
		try {
			image = new Image(CacheInfo.BASE_URL + fileName
					+ resolution.getFirst().getSuffix());
		} catch (RuntimeException | SlickException e) {
			factor = getScaleFactor(ImageResolution.FULLHD);
			image = new Image(CacheInfo.BASE_URL + fileName
					+ ImageResolution.FULLHD.getSuffix());
			L.log(Level.WARNING, "Could not load image " + fileName + " for "
					+ resolution.getFirst().name()
					+ ", falling back to fullHD at " + factor, e);
		}

		if (factor == 1f) {
			return image;
		} else {
			return image.getScaledCopy(factor);
		}
	}

	/**
	 * Loads an icon for swing GUI from internal filesystem.
	 * 
	 * @param fileName
	 *            file name of icon. Must be relative to images-package.
	 * @return an {@link ImageIcon} from given file.
	 */
	public static ImageIcon loadIcon(String fileName) {
		URL imgURL = ImageFiler.class.getResource(fileName);
		ImageIcon icon;
		if (imgURL != null) {
			icon = new ImageIcon(imgURL);
		} else {
			L.severe(Localization.getStringF("Client.errors.io.imagenotfound",
					fileName));
			return null;
		}
		return icon;
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
		return srcImg.getScaledCopy(w, h);
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
	public static java.awt.Image getScaledImage(java.awt.Image srcImg, int w,
			int h) {
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
