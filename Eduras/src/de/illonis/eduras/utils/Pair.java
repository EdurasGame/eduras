package de.illonis.eduras.utils;

/**
 * A pair holding two objects of given types.
 * 
 * @author illonis
 * 
 * @param <F>
 *            type of first object.
 * @param <S>
 *            type of second object.
 */
public class Pair<F, S> {

	private final F first;
	private final S second;

	/**
	 * Creates a new pair with given values.
	 * 
	 * @param first
	 *            first object.
	 * @param second
	 *            second object.
	 */
	public Pair(F first, S second) {
		this.first = first;
		this.second = second;
	}

	/**
	 * Returns first object.
	 * 
	 * @return the first object.
	 */
	public F getFirst() {
		return first;
	}

	/**
	 * Returns second object.
	 * 
	 * @return the second object.
	 */
	public S getSecond() {
		return second;
	}
}
