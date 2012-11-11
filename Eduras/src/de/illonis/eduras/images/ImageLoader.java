package de.illonis.eduras.images;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageLoader {

	public static BufferedImage load(String fileName) throws IOException {
		return ImageIO.read(ImageLoader.class.getResource(fileName));
	}
}
