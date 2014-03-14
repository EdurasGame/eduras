package de.illonis.eduras.gameclient.datacache;

import javax.swing.SwingWorker;

/**
 * A loader that loads data asynchronously. This may be data that need a large
 * amount of time to load. {@link AsyncLoader}s are designed in a way that data
 * will be only loaded once. Assigned listeners will be notified automatically
 * when loading finished.
 * 
 * @author illonis
 * 
 * @param <T>
 *            data type.
 */
public abstract class AsyncLoader<T> extends SwingWorker<T, Void> {

	private final AsyncLoadCompletedListener listener;

	/**
	 * Creates a new asynchronous data loader.
	 * 
	 * @param listener
	 *            the listener.
	 */
	public AsyncLoader(AsyncLoadCompletedListener listener) {
		this.listener = listener;
		addPropertyChangeListener(listener);
	}

	@Override
	protected void done() {
		listener.onDataLoaded();
	}
}
