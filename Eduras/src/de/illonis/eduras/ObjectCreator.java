package de.illonis.eduras;

import de.illonis.eduras.ObjectFactory.ObjectType;
import de.illonis.eduras.exceptions.ShapeVerticesNotApplicableException;
import de.illonis.eduras.gameobjects.Base;
import de.illonis.eduras.gameobjects.BigBlock;
import de.illonis.eduras.gameobjects.BiggerBlock;
import de.illonis.eduras.gameobjects.Bird;
import de.illonis.eduras.gameobjects.Building;
import de.illonis.eduras.gameobjects.DynamicPolygonObject;
import de.illonis.eduras.gameobjects.GameObject;
import de.illonis.eduras.gameobjects.Portal;
import de.illonis.eduras.gameobjects.TimingSource;
import de.illonis.eduras.items.powerups.HealingPotion;
import de.illonis.eduras.items.powerups.InvisibilityPowerUp;
import de.illonis.eduras.items.powerups.SpeedPowerUp;
import de.illonis.eduras.items.weapons.AssaultMissile;
import de.illonis.eduras.items.weapons.AssaultRifle;
import de.illonis.eduras.items.weapons.MineMissile;
import de.illonis.eduras.items.weapons.MineWeapon;
import de.illonis.eduras.items.weapons.RocketLauncher;
import de.illonis.eduras.items.weapons.RocketMissile;
import de.illonis.eduras.items.weapons.SimpleMissile;
import de.illonis.eduras.items.weapons.SimpleWeapon;
import de.illonis.eduras.items.weapons.SniperMissile;
import de.illonis.eduras.items.weapons.SniperWeapon;
import de.illonis.eduras.items.weapons.SplashMissile;
import de.illonis.eduras.items.weapons.SplashWeapon;
import de.illonis.eduras.items.weapons.SplashedMissile;
import de.illonis.eduras.items.weapons.SwordMissile;
import de.illonis.eduras.items.weapons.SwordWeapon;
import de.illonis.eduras.units.Observer;
import de.illonis.eduras.units.ScoutSpell;

/**
 * Creates objects from objecttypes.
 * 
 * @author illonis
 * 
 */
public final class ObjectCreator {

	/**
	 * Creates an object of given object type.
	 * 
	 * @param type
	 *            the type of the object.
	 * @param logic
	 *            the logic.
	 * @param timingSource
	 *            the timing source.
	 * @return the object.
	 * @throws ShapeVerticesNotApplicableException
	 *             if trying to create a polygon with invalid vertices.
	 * @throws FactoryException
	 *             if given type is not supported in any way.
	 */
	public static GameObject createObject(ObjectType type,
			GameInformation logic, TimingSource timingSource)
			throws FactoryException, ShapeVerticesNotApplicableException {
		return createObject(type, logic, timingSource, -1);
	}

	/**
	 * Creates an object of given object type with given id.
	 * 
	 * @param type
	 *            type of object.
	 * @param logic
	 *            the logic.
	 * @param timingSource
	 *            the timing source.
	 * @param id
	 *            the object id for the newly created object.
	 * @return the new object.
	 * @throws ShapeVerticesNotApplicableException
	 *             if trying to create a polygon with invalid vertices.
	 * @throws FactoryException
	 *             if given type is not supported in any way.
	 */
	public static GameObject createObject(ObjectType type,
			GameInformation logic, TimingSource timingSource, int id)
			throws FactoryException, ShapeVerticesNotApplicableException {
		GameObject go = null;
		switch (type) {
		case SIMPLEMISSILE:
			go = new SimpleMissile(logic, timingSource, id);
			break;
		case BUILDING:
			go = new Building(logic, timingSource, id);
			break;
		case BIRD:
			go = new Bird(logic, timingSource, id);
			break;
		case BIGGERBLOCK:
			go = new BiggerBlock(logic, timingSource, id);
			break;
		case ITEM_WEAPON_SIMPLE:
			go = new SimpleWeapon(logic, timingSource, id);
			break;
		case BIGBLOCK:
			go = new BigBlock(logic, timingSource, id);
			break;
		case SNIPERMISSILE:
			go = new SniperMissile(logic, timingSource, id);
			break;
		case MISSILE_SPLASH:
			go = new SplashMissile(logic, timingSource, id);
			break;
		case MISSILE_SPLASHED:
			go = new SplashedMissile(logic, timingSource, id);
			break;
		case SWORDMISSILE:
			go = new SwordMissile(logic, timingSource, id);
			break;
		case ITEM_WEAPON_SPLASH:
			go = new SplashWeapon(logic, timingSource, id);
			break;
		case ITEM_WEAPON_SNIPER:
			go = new SniperWeapon(logic, timingSource, id);
			break;
		case ITEM_WEAPON_SWORD:
			go = new SwordWeapon(logic, timingSource, id);
			break;
		case ROCKETLAUNCHER:
			go = new RocketLauncher(logic, timingSource, id);
			break;
		case ROCKET_MISSILE:
			go = new RocketMissile(logic, timingSource, id);
			break;
		case MINELAUNCHER:
			go = new MineWeapon(logic, timingSource, id);
			break;
		case MINE_MISSILE:
			go = new MineMissile(logic, timingSource, id);
			break;
		case ASSAULTRIFLE:
			go = new AssaultRifle(logic, timingSource, id);
			break;
		case ASSAULT_MISSILE:
			go = new AssaultMissile(logic, timingSource, id);
			break;
		case NEUTRAL_BASE:
			go = new Base(logic, timingSource, id, 1f);
			break;
		case DYNAMIC_POLYGON_BLOCK:
			go = new DynamicPolygonObject(ObjectType.DYNAMIC_POLYGON_BLOCK,
					logic, timingSource, id);
			break;
		case MAPBOUNDS:
			go = new DynamicPolygonObject(ObjectType.MAPBOUNDS, logic,
					timingSource, id);
			break;
		case OBSERVER:
			go = new Observer(logic, timingSource, id, -1);
			break;
		case HEALING_POTION:
			go = new HealingPotion(timingSource, logic, id);
			break;
		case SPEED_POWERUP:
			go = new SpeedPowerUp(timingSource, logic, id);
			break;
		case INVISIBILITY_POWERUP:
			go = new InvisibilityPowerUp(timingSource, logic, id);
			break;
		case SPELL_SCOUT:
			go = new ScoutSpell(logic, timingSource, id, -1);
			break;
		case PORTAL:
			go = new Portal(logic, timingSource, id);
			break;
		default:
			break;
		}
		if (go == null)
			throw new FactoryException(type);
		return go;
	}

}
