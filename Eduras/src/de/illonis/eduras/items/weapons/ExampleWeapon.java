/**
 * 
 */
package de.illonis.eduras.items.weapons;

import de.illonis.eduras.GameInformation;
import de.illonis.eduras.GameObject;
import de.illonis.eduras.ObjectFactory.ObjectType;
import de.illonis.eduras.Player;
import de.illonis.eduras.items.ItemUseInformation;
import de.illonis.eduras.math.Vector2D;
import de.illonis.eduras.shapes.Circle;

/**
 * @author Florian Mai <florian.ren.mai@googlemail.com>
 * 
 */
public class ExampleWeapon extends Weapon {

	/**
	 * Creates a new instance of the example weapon.
	 * 
	 * @param gi
	 */
	public ExampleWeapon(GameInformation gi) {

		super(ObjectType.ITEM_WEAPON_1, gi);
		setMissile(new SimpleMissile(gi));
		setName("WeaponExample");
		setShape(new Circle(10));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.illonis.eduras.items.Lootable#loot()
	 */
	@Override
	public void loot() {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.illonis.eduras.items.Usable#use(de.illonis.eduras.GameObject)
	 */
	@Override
	public void use(ItemUseInformation info) {

		Vector2D target = info.getTarget();
		GameObject triggeringObject = info.getTriggeringObject();

		Vector2D speedVector = new Vector2D(target);
		speedVector.subtract(triggeringObject.getPositionVector());

		// TODO: This needs to be solved in another way because this will make
		// the missile crash into the triggering object. => eventtrigger

		Vector2D position = triggeringObject.getPositionVector();
		Vector2D diffVector = new Vector2D(triggeringObject.getBoundingBox()
				.getMaxX() - position.getX(), position.getY()
				- triggeringObject.getBoundingBox().getMinY());

		Vector2D diag = new Vector2D(triggeringObject.getBoundingBox()
				.getWidth(), triggeringObject.getBoundingBox().getHeight());
		Vector2D copy = speedVector.copy();
		copy.setLength(diag.getLength());
		position.add(copy);

		getGame().getEventTriggerer().createMissile(ObjectType.SIMPLEMISSILE,
				getOwner(), position, speedVector);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.illonis.eduras.GameObject#onCollision(de.illonis.eduras.GameObject)
	 */
	@Override
	public void onCollision(GameObject collidingObject) {

		if (!(collidingObject.getType() == ObjectType.PLAYER)) {
			return;
		}

		Player player = (Player) collidingObject;
		getGame().getEventTriggerer().lootItem(getId(), player.getId());

	}
}
