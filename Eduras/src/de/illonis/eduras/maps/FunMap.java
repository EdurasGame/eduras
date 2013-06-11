package de.illonis.eduras.maps;

import java.util.LinkedList;

import de.illonis.eduras.exceptions.ShapeVerticesNotApplicableException;
import de.illonis.eduras.gameobjects.BigBlock;
import de.illonis.eduras.gameobjects.BiggerBlock;
import de.illonis.eduras.gameobjects.Building;
import de.illonis.eduras.gameobjects.GameObject;
import de.illonis.eduras.items.weapons.ExampleWeapon;
import de.illonis.eduras.items.weapons.SniperWeapon;
import de.illonis.eduras.logger.EduLog;

/**
 * a deathmatch map
 * 
 * @author Jan Reese
 * 
 */
public class FunMap extends Map {

	/**
	 * Creates the map
	 */
	public FunMap() {

		super("funmap", 600, 600);

		LinkedList<GameObject> initialObjects = new LinkedList<GameObject>();

		ExampleWeapon weap0 = new ExampleWeapon(null, -1);
		weap0.setPosition(25, 25);
		initialObjects.add(weap0);

		ExampleWeapon weap1 = new ExampleWeapon(null, -1);
		weap1.setPosition(25, getHeight() - 25);
		initialObjects.add(weap1);

		ExampleWeapon weap2 = new ExampleWeapon(null, -1);
		weap2.setPosition(getWidth() - 25, 25);
		initialObjects.add(weap2);

		ExampleWeapon weap3 = new ExampleWeapon(null, -1);
		weap3.setPosition(getWidth() - 25, getHeight() - 25);
		initialObjects.add(weap3);

		ExampleWeapon weap4 = new ExampleWeapon(null, -1);
		weap4.setPosition(getWidth() / 2, 25);
		initialObjects.add(weap4);

		ExampleWeapon weap5 = new ExampleWeapon(null, -1);
		weap5.setPosition(getWidth() / 2, getHeight() - 25);
		initialObjects.add(weap5);

		SniperWeapon snip0 = new SniperWeapon(null, -1);
		snip0.setPosition(getWidth() / 2, getHeight() / 2);
		initialObjects.add(snip0);

		Building b = new Building(null, -1);
		Building b1 = new Building(null, -1);
		Building b2 = new Building(null, -1);
		Building b3 = new Building(null, -1);
		b.setPosition(80, 80);
		b1.setPosition(getWidth() - 80, 80);
		b2.setPosition(80, getHeight() - 80);
		b3.setPosition(getWidth() - 80, getHeight() - 80);
		initialObjects.add(b);
		initialObjects.add(b1);
		initialObjects.add(b2);
		initialObjects.add(b3);

		try {
			BiggerBlock bigger0 = new BiggerBlock(null, -1);
			BiggerBlock bigger1 = new BiggerBlock(null, -1);
			BiggerBlock bigger2 = new BiggerBlock(null, -1);
			BiggerBlock bigger3 = new BiggerBlock(null, -1);
			BiggerBlock bigger4 = new BiggerBlock(null, -1);
			BiggerBlock bigger5 = new BiggerBlock(null, -1);
			BiggerBlock bigger6 = new BiggerBlock(null, -1);
			BiggerBlock bigger7 = new BiggerBlock(null, -1);
			BigBlock block0 = new BigBlock(null, -1);
			BigBlock block1 = new BigBlock(null, -1);
			BigBlock block2 = new BigBlock(null, -1);
			BigBlock block3 = new BigBlock(null, -1);
			BigBlock block4 = new BigBlock(null, -1);
			BigBlock block5 = new BigBlock(null, -1);
			BigBlock block6 = new BigBlock(null, -1);
			BigBlock block7 = new BigBlock(null, -1);
			BigBlock block8 = new BigBlock(null, -1);
			BigBlock block9 = new BigBlock(null, -1);
			BigBlock block10 = new BigBlock(null, -1);
			BigBlock block11 = new BigBlock(null, -1);
			BigBlock block12 = new BigBlock(null, -1);
			BigBlock block13 = new BigBlock(null, -1);
			BigBlock block14 = new BigBlock(null, -1);
			BigBlock block15 = new BigBlock(null, -1);
			BigBlock block16 = new BigBlock(null, -1);
			BigBlock block17 = new BigBlock(null, -1);
			BigBlock block18 = new BigBlock(null, -1);
			BigBlock block19 = new BigBlock(null, -1);
			BigBlock block20 = new BigBlock(null, -1);
			BigBlock block21 = new BigBlock(null, -1);
			BigBlock block22 = new BigBlock(null, -1);
			BigBlock block23 = new BigBlock(null, -1);
			BigBlock block24 = new BigBlock(null, -1);
			BigBlock block25 = new BigBlock(null, -1);
			BigBlock block26 = new BigBlock(null, -1);
			BigBlock block27 = new BigBlock(null, -1);
			BigBlock block28 = new BigBlock(null, -1);
			BigBlock block29 = new BigBlock(null, -1);
			BigBlock block30 = new BigBlock(null, -1);
			BigBlock block31 = new BigBlock(null, -1);
			BigBlock block32 = new BigBlock(null, -1);
			BigBlock block33 = new BigBlock(null, -1);
			BigBlock block34 = new BigBlock(null, -1);
			BigBlock block35 = new BigBlock(null, -1);
			BigBlock block36 = new BigBlock(null, -1);
			BigBlock block37 = new BigBlock(null, -1);

			bigger0.setPosition((getWidth() / 2) + 45, 25);
			bigger1.setPosition((getWidth() / 2) - 45, 25);
			bigger2.setPosition((getWidth() / 2) + 45, getHeight() - 25);
			bigger3.setPosition((getWidth() / 2) - 45, getHeight() - 25);
			bigger4.setPosition(getWidth() - 25, 180);
			bigger5.setPosition(getWidth() - 80, 360);
			bigger6.setPosition(25, 180);
			bigger7.setPosition(80, 360);
			block0.setPosition(120, 120);
			block1.setPosition(120, getHeight() - 125);
			block2.setPosition(getWidth() - 120, getHeight() - 120);
			block3.setPosition(getWidth() - 120, 120);
			block4.setPosition(120, 120 + 30);
			block5.setPosition(120, 120 + 60);
			block6.setPosition(120, 120 + 120);
			block7.setPosition(120, 120 + 150);
			block8.setPosition(120, 120 + 210);
			block9.setPosition(120, 120 + 240);
			block10.setPosition(120, 120 + 300);
			block11.setPosition(120, 120 + 330);
			block12.setPosition((getWidth() / 2) - 45, getHeight() / 2);
			block13.setPosition((getWidth() / 2) + 45, getHeight() / 2);
			block14.setPosition(getWidth() - 120, 120 + 30);
			block15.setPosition(getWidth() - 120, 120 + 60);
			block16.setPosition(getWidth() - 120, 120 + 120);
			block17.setPosition(getWidth() - 120, 120 + 150);
			block18.setPosition(getWidth() - 120, 120 + 210);
			block19.setPosition(getWidth() - 120, 120 + 240);
			block20.setPosition(getWidth() - 120, 120 + 300);
			block21.setPosition(getWidth() - 120, 120 + 330);
			block22.setPosition(120 + 30, 120);
			block23.setPosition(120 + 60, 150);
			block24.setPosition(120 + 90, 180);
			block25.setPosition(120 + 120, 180);
			block26.setPosition(120 + 240, 180);
			block27.setPosition(120 + 270, 180);
			block28.setPosition(120 + 300, 150);
			block29.setPosition(120 + 330, 120);
			block30.setPosition(120 + 30, getHeight() - 120);
			block31.setPosition(120 + 60, getHeight() - 150);
			block32.setPosition(120 + 90, getHeight() - 180);
			block33.setPosition(120 + 120, getHeight() - 180);
			block34.setPosition(120 + 240, getHeight() - 180);
			block35.setPosition(120 + 270, getHeight() - 180);
			block36.setPosition(120 + 300, getHeight() - 150);
			block37.setPosition(120 + 330, getHeight() - 120);

			initialObjects.add(bigger0);
			initialObjects.add(bigger1);
			initialObjects.add(bigger2);
			initialObjects.add(bigger3);
			initialObjects.add(bigger4);
			initialObjects.add(bigger5);
			initialObjects.add(bigger6);
			initialObjects.add(bigger7);
			initialObjects.add(block0);
			initialObjects.add(block1);
			initialObjects.add(block2);
			initialObjects.add(block3);
			initialObjects.add(block4);
			initialObjects.add(block5);
			initialObjects.add(block6);
			initialObjects.add(block7);
			initialObjects.add(block8);
			initialObjects.add(block9);
			initialObjects.add(block10);
			initialObjects.add(block11);
			initialObjects.add(block12);
			initialObjects.add(block13);
			initialObjects.add(block14);
			initialObjects.add(block15);
			initialObjects.add(block16);
			initialObjects.add(block17);
			initialObjects.add(block18);
			initialObjects.add(block19);
			initialObjects.add(block20);
			initialObjects.add(block21);
			initialObjects.add(block22);
			initialObjects.add(block23);
			initialObjects.add(block24);
			initialObjects.add(block25);
			initialObjects.add(block26);
			initialObjects.add(block27);
			initialObjects.add(block28);
			initialObjects.add(block29);
			initialObjects.add(block30);
			initialObjects.add(block31);
			initialObjects.add(block32);
			initialObjects.add(block33);
			initialObjects.add(block34);
			initialObjects.add(block35);
			initialObjects.add(block36);
			initialObjects.add(block37);
		} catch (ShapeVerticesNotApplicableException e) {
			// choosing the right values above, this should never happen
			EduLog.passException(e);
		}

		setInitialObjects(initialObjects);

		// TODO: Set spawnpoints

	}
}