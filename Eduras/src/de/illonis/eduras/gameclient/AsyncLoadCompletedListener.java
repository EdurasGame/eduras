package de.illonis.eduras.gameclient;

/**
 * Listens to a {@link AsyncLoader}.
 * 
 * @author illonis
 * 
 */
public interface AsyncLoadCompletedListener<T> {

	/**
	 * Indicates that data have been loaded.
	 * 
	 * @param data
	 *            data that have been loaded.
	 */
	void onDataLoaded(T data);
}
