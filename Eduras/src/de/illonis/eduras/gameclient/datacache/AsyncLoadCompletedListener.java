package de.illonis.eduras.gameclient.datacache;

import java.beans.PropertyChangeListener;

/**
 * Listens to a {@link AsyncLoader}.
 * 
 * @author illonis
 * 
 */
public interface AsyncLoadCompletedListener extends PropertyChangeListener {

	/**
	 * Indicates that data have been loaded.
	 */
	void onDataLoaded();
}
