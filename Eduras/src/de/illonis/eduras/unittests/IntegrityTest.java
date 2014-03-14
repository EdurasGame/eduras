package de.illonis.eduras.unittests;

import static org.junit.Assert.assertFalse;

import java.util.ArrayList;

import org.junit.Test;

import de.illonis.eduras.ObjectFactory.ObjectType;

/**
 * Performs some important tests regarding integrity to prevent bugs at runtime.
 * 
 * @author illonis
 * 
 */
public class IntegrityTest {

	/**
	 * Tests if all {@link ObjectType}s' ids are distinct.
	 * 
	 * @author illonis
	 */
	@Test
	public void uniqueObjectIds() {
		ArrayList<Integer> presentIds = new ArrayList<Integer>();
		ObjectType[] types = ObjectType.values();
		for (ObjectType objectType : types) {
			int id = objectType.getNumber();
			assertFalse(presentIds.contains(id));
			presentIds.add(id);
		}
	}
}
