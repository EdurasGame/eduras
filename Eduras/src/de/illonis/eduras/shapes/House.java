package de.illonis.eduras.shapes;

import java.io.IOException;

import de.illonis.eduras.shapecreator.FileCorruptException;

/**
 * A basic house.
 * 
 * @author illonis
 * 
 */
public class House extends Polygon {

	/**
	 * Creates a basic house shape.
	 */
	public House() {
		super();
		try {
			loadFromFile("pacman.esh");
		} catch (FileCorruptException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		/*
		 * Vector2D vertices[] = { new Vector2D(-20, -40), new Vector2D(0, -60),
		 * new Vector2D(20, -40), new Vector2D(20, 0), new Vector2D(-20, 0) };
		 * setVertices(vertices);
		 */
	}
}
