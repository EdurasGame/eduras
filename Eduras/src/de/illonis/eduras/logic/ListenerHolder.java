package de.illonis.eduras.logic;

/**
 * A listener holder that supports passing a nonexistent listener that will be
 * placed later on.
 * 
 * @param <T>
 *            type of listener that can be hold.
 * 
 * @author illonis
 */
public final class ListenerHolder<T> {

	private T listener;

	/**
	 * Creates an empty listener holder.
	 */
	public ListenerHolder() {
		listener = null;
	}

	/**
	 * Creates a listener holder holding given listener.
	 * 
	 * @param listener
	 *            initial listener.
	 */
	public ListenerHolder(T listener) {
		this.listener = listener;
	}

	/**
	 * @return true if a listener is attached.
	 */
	public boolean hasListener() {
		return listener != null;
	}

	/**
	 * Sets listener to given value. This replaces an attached listener.
	 * 
	 * @param listener
	 *            new listener.
	 */
	public void setListener(T listener) {
		this.listener = listener;
	}

	/**
	 * Removes current listener.
	 */
	public void removeListener() {
		listener = null;
	}

	/**
	 * @return the attached listener.
	 * @throws IllegalStateException
	 *             if no listener attached.
	 */
	public T getListener() throws IllegalStateException {
		if (listener == null)
			throw new IllegalStateException("No listener attached.");
		return listener;
	}

}
