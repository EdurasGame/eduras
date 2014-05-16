package de.illonis.eduras.exceptions;

import org.newdawn.slick.geom.Shape;

/**
 * This is thrown if a shape isn't supported performing some calculation.
 * 
 * @author Florian 'Ren' Mai <florian.ren.mai@googlemail.com>
 * 
 */
public class ShapeNotSupportedException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Shape shape;

	/**
	 * Create the exception.
	 * 
	 * @param shape
	 *            the shape that isn't supported.
	 */
	public ShapeNotSupportedException(Shape shape) {
		this.shape = shape;
	}

	/**
	 * The shape that's not supported
	 * 
	 * @return the shape
	 */
	public Shape getShape() {
		return shape;
	}

}
