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

	/**
	 * Creates a new {@link ImageLoadingError}.
	 * 
	 * @param msg
	 *            errormessage.
	 * @param file
	 *            filename of (not) loaded image.
	 */
	public ImageLoadingError(String msg, String file) {
		super(msg);
		this.fileName = file;
	}

	/**
	 * Returns filename of image that was tried to load.
	 * 
	 * @return filename of image.
	 */
	public String getFileName() {
		return fileName;
	}
}
