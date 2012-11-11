package de.illonis.eduras.images;

public class ImageData {

	public int w, h;
	public int[] pixels;

	public ImageData(int w, int h) {
		this.w = w;
		this.h = h;
		pixels = new int[w * h];
	}

	public ImageData(int w, int h, int[] pixels) {
		this.w = w;
		this.h = h;
		this.pixels = pixels;
	}

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
