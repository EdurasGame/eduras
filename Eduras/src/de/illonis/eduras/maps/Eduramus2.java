package de.illonis.eduras.maps;

import java.io.IOException;

import de.illonis.eduras.maps.persistence.InvalidDataException;

public class Eduramus2 extends Map {
	public Eduramus2() {
		super("eduramus2", "flori", 2000, 2038);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void buildMap() throws InvalidDataException {
		try {
			loadFromFile("eduramus2.erm");
		} catch (IOException e) {
		}

	}
}
