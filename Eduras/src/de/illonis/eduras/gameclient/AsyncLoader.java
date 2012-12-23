package de.illonis.eduras.gameclient;

import java.util.LinkedList;

/**
 * A loader that loads data asynchronously. This may be data that need a large
 * amount of time to load. {@link AsyncLoader}s are designed in a way that data
 * will be only loaded once.
 * 
 * @author illonis
 * 
 * @param <T>
 *            data type.
 */
public abstract class AsyncLoader<T> implements Runnable {

	private boolean finished;
	private int progress;
	private final LinkedList<AsyncLoadCompletedListener<T>> listeners;
	private T data;

	/**
	 * Creates a new asynchronous data loader.
	 */
	public AsyncLoader() {
		finished = false;
		listeners = new LinkedList<AsyncLoadCompletedListener<T>>();
	}

	/**
	 * Starts loading data. This will be done asynchronously and must not be
	 * explicitly threaded.
	 */
	public final void startLoading() {
		Thread t = new Thread(this);
		t.start();
	}

	/**
	 * Called when loading has finished.
	 */
	protected abstract void onLoadingFinished();

	/**
	 * Return whether data have been loaded completely.
	 * 
	 * @return true if data have been loaded, false otherwise.
	 */
	public final boolean hasFinished() {
		return finished;
	}

	/**
	 * Returns loading progress. This is a value between 0 and 100 (inclusive)
	 * that represents percentage of progess..
	 * 
	 * @return loading progress (percentage).
	 */
	public int getProgress() {
		return progress;
	}

	/**
	 * Sets progress of this loader to given value. If progress reaches 100,
	 * listeners are notified that loading is completed.
	 * 
	 * @param progress
	 *            new progress value. Must be between 0 and 100 (inclusive).
	 *            Will be adjusted otherwise.
	 */
	protected final void setProgress(int progress) {
		if (progress > 100)
			progress = 100;
		if (progress < 0)
			progress = 0;

		if (progress == 100 && this.progress < 100) {
			finished = true;
			notifyListeners();
		}
		this.progress = progress;
	}

	/**
	 * This method will be executed asynchronously when {@link #startLoading()}
	 * is called. Put your loading stuff here. You should call
	 * {@link #setProgress(int)} to announce progress. Call
	 * {@link #setData(Object)} to store loaded data.
	 */
	protected abstract void loadAsync();

	/**
	 * Sets data to given value.
	 * 
	 * @param data
	 *            data.
	 */
	protected void setData(T data) {
		this.data = data;
	}

	/**
	 * Aborts loading data.
	 */
	public abstract void abortLoading();

	/**
	 * Returns data that were loaded by this loader.
	 * 
	 * @return loaded data.
	 * @throws DataNotAvailableException
	 *             when data not loaded yet.
	 */
	public final T getData() throws DataNotAvailableException {
		if (hasFinished())
			return data;
		else {
			throw new DataNotAvailableException();
		}
	}

	/**
	 * Adds a listener that will be notified when loading is finished.
	 * 
	 * @param l
	 *            listener to add.
	 */
	public final void addCompletedListener(AsyncLoadCompletedListener<T> l) {
		listeners.add(l);
	}

	/**
	 * Removes given listener.
	 * 
	 * @param l
	 *            listener to remove.
	 */
	public final void removeCompletedListener(AsyncLoadCompletedListener<T> l) {
		listeners.remove(l);
	}

	/**
	 * Notifies all listeners that loading is completed.
	 */
	protected final void notifyListeners() {
		for (AsyncLoadCompletedListener<T> listener : listeners) {
			listener.onDataLoaded(data);
		}
	}

	@Override
	public final void run() {
		setProgress(0);
		loadAsync();
		setProgress(100);
	}
}
