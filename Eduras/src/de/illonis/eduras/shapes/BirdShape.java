package de.illonis.eduras.shapes;

import java.io.IOException;

import de.illonis.eduras.shapecreator.FileCorruptException;

/**
 * A shape of a flying bird.
 * 
 * @author illonis
 * 
 */
public class BirdShape extends Polygon {

	/**
	 * Creates a new bird shape.
	 */
	public BirdShape() {
		super();
		try {
			loadFromFile("bird.esh");
		} catch (FileCorruptException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
