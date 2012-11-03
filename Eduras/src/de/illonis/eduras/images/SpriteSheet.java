package de.illonis.eduras.images;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * Provides functionality to extract multiple images or tiles from a single
 * image. For more details, see Documentation or google "Sprite".
 * 
 * @author illonis
 * 
 */
public class SpriteSheet {

	private int width;
	private int height;
	private int[] pixels;
	private int tileCount;
	private int columns;
	private int rows;

	/**
	 * Creates a new {@linkplain SpriteSheet} using given image with square
	 * tiles.
	 * 
	 * @see #SpriteSheet(String, int, int)
	 * 
	 * @param path
	 *            path to image file that contains sprite-sheet.
	 * @param tileSize
	 *            tile size of each tile.
	 */
	public SpriteSheet(String path, int tileSize) {
		this(path, tileSize, tileSize);
	}

	/**
	 * Creates a new {@linkplain SpriteSheet} using given image with tiles.
	 * 
	 * @param path
	 *            path to image file that contains sprite-sheet.
	 * @param tileWidth
	 *            width of each tile.
	 * @param tileHeight
	 *            height of each tile.
	 */
	public SpriteSheet(String path, int tileWidth, int tileHeight) {
		BufferedImage image = null;
		try {
			image = ImageIO.read(SpriteSheet.class.getResourceAsStream(path));
		} catch (IOException e) {
		}

		if (image == null)
			return;

		width = image.getWidth();
		height = image.getHeight();
		columns = width / tileWidth;
		rows = height / tileHeight;
		tileCount = columns * rows;

		pixels = image.getRGB(0, 0, width, height, null, 0, width);

	}

	/**
	 * Returns number of tiles this {@linkplain SpriteSheet} contains.
	 * 
	 * @return number of tiles.
	 */
	public int getTileCount() {
		return tileCount;
	}
	
	public int[] getPixels() {
		return pixels;
	}
}
