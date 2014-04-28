package de.illonis.eduras.gameclient.datacache;

/**
 * Listens for {@link GraphicsPreLoader} to be finished.
 * 
 * @author illonis
 * 
 */
public interface CacheReadyListener {

	/**
	 * Called when all graphics have been cached.
	 */
	void cacheReady();
}
