package de.illonis.eduras.images;

import java.awt.image.BufferedImage;
import java.io.IOException;

import de.illonis.eduras.exceptions.ImageLoadingError;

/**
 * Provides functionality to extract multiple images or tiles from a single
 * image. For more details, see Documentation or google "Sprite".
 * 
 * @author illonis
 * 
 */
public class SpriteSheet {

	private ImageData[] data;
	private int tileCount;

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
	 * @throws ImageLoadingError
	 *             when image file could not be loaded.
	 */
	public SpriteSheet(String path, int tileSize) throws ImageLoadingError {
		this(path, tileSize, tileSize);
	}

	/**
	 * Creates a new {@linkplain SpriteSheet} using given image with tiles.
	 * 
	 * @param path
	 *            path to image file that contains sprite-sheet. Path is
	 *            relative to "de.illonis.eduras.image" package.
	 * @param tileWidth
	 *            width of each tile.
	 * @param tileHeight
	 *            height of each tile.
	 * @throws ImageLoadingError
	 *             when image file could not be loaded.
	 */
	public SpriteSheet(String path, int tileWidth, int tileHeight)
			throws ImageLoadingError {

		try {
			data = cut(path, tileWidth, tileHeight);
		} catch (IOException e) {
			throw new ImageLoadingError(e.getMessage(), path);
		}

	}

	/**
	 * Return the ImageData for a given piece of art, cut out from a sheet. Also
	 * stores the number of found tiles to {@link #tileCount}.
	 * 
	 * @param string
	 *            Art piece name
	 * @param w
	 *            Width of a single sprite
	 * @param h
	 *            Height of a single sprite
	 * 
	 * @return ImageData array of sprites.
	 * @throws IOException
	 *             when image load error occurs.
	 */
	private ImageData[] cut(String string, int w, int h) throws IOException {

		BufferedImage bi = ImageLoader.load(string);

		int xTiles = bi.getWidth() / w;
		int yTiles = bi.getHeight() / h;
		System.out.println("xtiles" + xTiles + " ytiles: " + yTiles
				+ " width: " + bi.getWidth());

		ImageData[] result = new ImageData[xTiles * yTiles];
		int i = 0;
		for (int y = 0; y < yTiles; y++) {
			for (int x = 0; x < xTiles; x++) {
				result[i] = new ImageData(w, h);
				System.out.println(x * w + " " + y * h);
				bi.getRGB(x * w, y * h, w, h, result[i].pixels, 0, w);
				i++;
			}
		}
		tileCount = i;
		return result;
	}

	/**
	 * Returns number of tiles this {@linkplain SpriteSheet} contains.
	 * 
	 * @return number of tiles.
	 */
	public int getTileCount() {
		return tileCount;
	}

	/**
	 * Returns imagedata of selected tile.
	 * 
	 * @param tile
	 *            tile index.
	 * @return tile with given index.
	 */
	public ImageData getTile(int tile) {
		return data[tile];
	}

}
