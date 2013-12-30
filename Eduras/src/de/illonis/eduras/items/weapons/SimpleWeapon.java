/**
 * 
 */
package de.illonis.eduras.items.weapons;

import de.illonis.eduras.GameInformation;
import de.illonis.eduras.ObjectFactory.ObjectType;
import de.illonis.eduras.gameobjects.GameObject;
import de.illonis.eduras.items.ItemUseInformation;
import de.illonis.eduras.math.Vector2D;
import de.illonis.eduras.settings.S;
import de.illonis.eduras.shapes.Circle;

/**
 * @author Florian Mai <florian.ren.mai@googlemail.com>
 * 
 */
public class SimpleWeapon extends Weapon {

	/**
	 * Creates a new instance of the example weapon.
	 * 
	 * @param gi
	 *            game information
	 * @param id
	 *            id of weapon
	 */
	public SimpleWeapon(GameInformation gi, int id) {

		super(ObjectType.ITEM_WEAPON_SIMPLE, gi, id);
		// TODO: Need to get away the missile prototypes.
		setMissile(null);
		setName("SimpleWeapon");
		setShape(new Circle(S.go_simpleweapon_shape_radius));
		defaultCooldown = S.go_simpleweapon_cooldown;
	}

	@Override
	protected void doIfReady(ItemUseInformation info) {
		// (jme) Spawn position will be calculated in a simplified way. We use
		// diagonal's length of shooting player to move missile away from him.
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

		getGame().getEventTriggerer().createMissile(ObjectType.SIMPLEMISSILE,
				getOwner(), position, speedVector);

	}
}
