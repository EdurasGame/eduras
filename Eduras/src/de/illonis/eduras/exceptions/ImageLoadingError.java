package de.illonis.eduras.exceptions;

/**
 * An error that occurs if an image could not be loaded from storage.
 * 
 * @author illonis
 * 
 */
public class ImageLoadingError extends Exception {

	private static final long serialVersionUID = 1L;
	private String fileName;

	public ImageLoadingError(String msg, String file) {
		super(msg);
		this.fileName = file;
	}

	public String getFileName() {
		return fileName;
	}
}
