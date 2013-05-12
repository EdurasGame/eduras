/**
 * 
 */
package de.illonis.eduras.items.weapons;

import de.illonis.eduras.GameInformation;
import de.illonis.eduras.ObjectFactory.ObjectType;
import de.illonis.eduras.gameobjects.GameObject;
import de.illonis.eduras.items.ItemUseInformation;
import de.illonis.eduras.math.Vector2D;
import de.illonis.eduras.shapes.Circle;
import de.illonis.eduras.units.Player;

/**
 * @author Florian Mai <florian.ren.mai@googlemail.com>
 * 
 */
public class ExampleWeapon extends Weapon {

	/**
	 * Creates a new instance of the example weapon.
	 * 
	 * @param gi
	 *            game information
	 * @param id
	 *            id of weapon
	 */
	public ExampleWeapon(GameInformation gi, int id) {

		super(ObjectType.ITEM_WEAPON_1, gi, id);
		// TODO: Need to get away the missile prototypes.
		setMissile(null);
		setName("WeaponExample");
		setShape(new Circle(10));
		defaultCooldown = 300;
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
		// (jme) Spawn position will be calculated in a simplified way. We use
		// diagonal's length of shooting player to move missile away from him.

		if (!hasCooldown()) {
			super.use(info);
			Vector2D target = info.getTarget();
			GameObject triggeringObject = info.getTriggeringObject();

			Vector2D position = triggeringObject.getPositionVector();

			Vector2D speedVector = new Vector2D(target);
			speedVector.subtract(position);

			Vector2D diag = new Vector2D(triggeringObject.getBoundingBox()
					.getWidth(), triggeringObject.getBoundingBox().getHeight());
			Vector2D copy = speedVector.copy();
			copy.setLength(diag.getLength());
			position.add(copy);

			getGame().getEventTriggerer()
					.createMissile(ObjectType.SIMPLEMISSILE, getOwner(),
							position, speedVector);
		}
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
