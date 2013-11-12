package de.illonis.eduras.gameclient;

import de.illonis.eduras.gameclient.gui.progress.LoadingPanel;

/**
 * Handles reactions that are triggered by {@link LoadingPanel}.
 * 
 * @author illonis
 * 
 */
public interface LoadingPanelReactor {

	/**
	 * Indicates that preloading of graphics has finished.
	 */
	void onLoadingFinished();
}
