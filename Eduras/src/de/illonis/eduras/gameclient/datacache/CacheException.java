package de.illonis.eduras.gameclient.datacache;

/**
 * Indicates that an error occured accessing the cache.
 * 
 * @author illonis
 * 
 */
public class CacheException extends Exception {

	private static final long serialVersionUID = 1L;

	/**
	 * @param msg
	 *            the message.
	 */
	public CacheException(String msg) {
		super(msg);
	}

}
