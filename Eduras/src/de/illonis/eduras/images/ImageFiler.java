package de.illonis.eduras.images;

import java.io.IOException;
import java.io.InputStream;
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
import de.illonis.eduras.settings.S;
import de.illonis.eduras.utils.Pair;
import de.illonis.eduras.utils.ResourceManager;
import de.illonis.eduras.utils.ResourceManager.ResourceType;

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
		WINDOWED(".low", 1066, 600), LAPTOP(".medium", 1366, 768), FULLHD(
				"",
				1920,
				1080);

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

		public int getWidth() {
			return width;
		}

		public int getHeight() {
			return height;
		}
	}

	private static float getScaleFactor(ImageResolution res) {
		if (Display.getWidth() == res.getWidth()
				&& Display.getHeight() == res.getHeight())
			return 1f;
		return GameRenderer.getRenderScale() / GameRenderer.getRenderScale(res);
	}

	private static Pair<ImageResolution, Float> calculateResolution() {
		int width = Display.getWidth();
		int height = Display.getHeight();
		if (width > ImageResolution.LAPTOP.getWidth()
				&& height > ImageResolution.LAPTOP.getHeight()) {
			return new Pair<ImageResolution, Float>(ImageResolution.FULLHD,
					getScaleFactor(ImageResolution.FULLHD));
		} else if (width > ImageResolution.WINDOWED.getWidth()
				&& height > ImageResolution.WINDOWED.getHeight()) {
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
	 * Loads an image scaled for current resolution from internal filesystem.
	 * 
	 * @param fileName
	 *            file name of image. Must be relative to images-package.
	 * @return image.
	 * @throws SlickException
	 *             if image could not be loaded.
	 * @throws IOException
	 *             if opening file failed.
	 */
	public static Image loadScaled(String fileName) throws SlickException,
			IOException {
		Pair<ImageResolution, Float> resolution = calculateResolution();

		float factor = resolution.getSecond();
		Image image;
		if (S.Client.localres) {
			image = new Image(CacheInfo.BASE_URL + fileName
					+ resolution.getFirst().getSuffix());
		} else {
			try {
				try (InputStream input = ResourceManager.openResource(
						ResourceType.IMAGE, fileName
								+ resolution.getFirst().getSuffix())) {
					image = new Image(input, fileName, false);
				}
			} catch (RuntimeException | SlickException | IOException e) {
				factor = getScaleFactor(ImageResolution.FULLHD);
				L.log(Level.WARNING, "Could not load image " + fileName
						+ " for " + resolution.getFirst().name()
						+ ", falling back to fullHD at " + factor, e);
				try (InputStream input = ResourceManager.openResource(
						ResourceType.IMAGE,
						fileName + ImageResolution.FULLHD.getSuffix())) {
					image = new Image(input, fileName, false);
				}
			}
		}

		if (factor == 1f) {
			return image;
		} else {
			return image.getScaledCopy(factor);
		}
	}

	/**
	 * Loads an image from internal filesystem.<br>
	 * To retrieve a resolution-dependent image, use {@link #loadScaled(String)}
	 * .
	 * 
	 * @param fileName
	 *            file name of image. Must be relative to images-package.
	 * @return the image.
	 * @throws SlickException
	 *             when image could not be loaded.
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
	public static Image load(String fileName) throws SlickException,
			IOException {
		Image image;
		if (S.Client.localres) {
			image = new Image(CacheInfo.BASE_URL + fileName);
		} else {
			try (InputStream input = ResourceManager.openResource(
					ResourceType.IMAGE, fileName)) {
				image = new Image(input, fileName, false);
			}
		}
		return image;
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
}
