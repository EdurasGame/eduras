package de.illonis.eduras.gameclient.gui.progress;

import java.awt.Component;
import java.beans.PropertyChangeEvent;

import de.illonis.eduras.gameclient.LoadingPanelReactor;
import de.illonis.eduras.gameclient.datacache.AsyncLoadCompletedListener;
import de.illonis.eduras.gameclient.datacache.GraphicsPreLoader;
import de.illonis.eduras.gameclient.gui.ClientGuiStepLogic;

/**
 * Displays login progress. Also shows errors occuring while connecting.
 * 
 * @author illonis
 * 
 */
public class LoadingPanelLogic extends ClientGuiStepLogic implements
		AsyncLoadCompletedListener {

	private final LoadingPanel gui;
	private final LoadingPanelReactor reactor;
	private GraphicsPreLoader preloader;

	/**
	 * Creates the progress panel
	 * 
	 * @param reactor
	 *            the reactor on panel's actions.
	 * 
	 */
	public LoadingPanelLogic(LoadingPanelReactor reactor) {
		this.reactor = reactor;
		this.gui = new LoadingPanel();
	}

	private void terminateLoader() {
		if (preloader != null && !preloader.isDone()) {
			preloader.removePropertyChangeListener(this);
			preloader.cancel(true);
		}
	}

	@Override
	public void onShown() {
		terminateLoader();
		startLoading();
	}

	private void startLoading() {
		preloader = new GraphicsPreLoader(this);
		preloader.execute();
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getPropertyName().equals("progress")) {
			int nv = (int) evt.getNewValue();
			if (nv >= 0) {
				gui.setProgress(nv);
			}
		}
	}

	@Override
	public void onHidden() {
		terminateLoader();
	}

	@Override
	public Component getGui() {
		return gui;
	}

	@Override
	public void onDataLoaded() {
		reactor.onLoadingFinished();
	}
}
