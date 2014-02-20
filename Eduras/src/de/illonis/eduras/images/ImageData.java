package de.illonis.eduras.images;

/**
 * Represents image data. Data are hold as a pixel array that is public
 * accessible. This is a relevant class for spritesheets.
 * 
 * @author illonis
 * 
 */
public class ImageData {

	/**
	 * width
	 */
	public int w;

	/**
	 * height
	 */
	public int h;
	/**
	 * The pixels.
	 */
	public int[] pixels;

	/**
	 * Creates a new {@link ImageData} object with empty pixels.
	 * 
	 * @param w
	 *            width
	 * @param h
	 *            height
	 */
	public ImageData(int w, int h) {
		this.w = w;
		this.h = h;
		pixels = new int[w * h];
	}

	/**
	 * Creates a new {@link ImageData} object with given pixel data. Note that
	 * this method does not check if data length matches given size.
	 * 
	 * @param w
	 *            width
	 * @param h
	 *            height
	 * @param pixels
	 *            pixel data
	 */
	public ImageData(int w, int h, int[] pixels) {
		this.w = w;
		this.h = h;
		this.pixels = pixels;
	}

	/**
	 * Creates a new {@link ImageData} object from given two-dimensional pixel
	 * array. Size is automatically calculated.
	 * 
	 * @param pixels2D
	 *            pixel data
	 */
	public ImageData(int[][] pixels2D) {
		w = pixels2D.length;
		if (w > 0) {
			h = pixels2D[0].length;
			pixels = new int[w * h];
			for (int y = 0; y < h; y++) {
				for (int x = 0; x < w; x++) {
					pixels[y * w + x] = pixels2D[x][y];
				}
			}
		} else {
			h = 0;
			pixels = new int[0];
		}
	}
}
