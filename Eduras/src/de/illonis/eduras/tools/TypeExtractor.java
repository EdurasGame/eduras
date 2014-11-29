package de.illonis.eduras.tools;

import de.illonis.eduras.ObjectFactory.ObjectType;
import de.illonis.eduras.events.GameEvent.GameEventNumber;

/**
 * A simple tool to export typenumbers for events and objects to
 * streaming-server.
 * 
 * @author illonis
 * 
 */
public class TypeExtractor {

	public static void main(String[] args) {
		printObjectTypes();
		System.out.println();
		printEventTypes();
	}

	private static void printEventTypes() {
		System.out.println("Event types:");
		for (GameEventNumber type : GameEventNumber.values()) {
			System.out.printf("const int %s = %d;\n", type.name(),
					type.getNumber());
		}
	}

	private static void printObjectTypes() {
		System.out.println("object types:");
		for (ObjectType type : ObjectType.values()) {
			System.out.printf("const int %s = %d;\n", type.name(),
					type.getNumber());
		}

	}
}
