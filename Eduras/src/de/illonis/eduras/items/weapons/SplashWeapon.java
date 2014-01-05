package de.illonis.eduras.items.weapons;

import de.illonis.eduras.GameInformation;
import de.illonis.eduras.ObjectFactory.ObjectType;
import de.illonis.eduras.gameobjects.GameObject;
import de.illonis.eduras.items.ItemUseInformation;
import de.illonis.eduras.math.Vector2D;
import de.illonis.eduras.settings.S;
import de.illonis.eduras.shapes.Circle;

/**
 * A splash weapon shoots a slow missile that explodes on collision and spreads
 * into multiple other missiles.
 * 
 * @author illonis
 * 
 */
public class SplashWeapon extends Weapon {

	/**
	 * Creates a new splashweapon.
	 * 
	 * @param infos
	 *            game info.
	 * @param id
	 *            object id.
	 */
	public SplashWeapon(GameInformation infos, int id) {
		super(ObjectType.ITEM_WEAPON_SPLASH, infos, id);
		setName("SplashWeapon");
		setShape(new Circle(S.go_splashweapon_shape_radius));
		defaultCooldown = S.go_splashweapon_cooldown;
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

		getGame().getEventTriggerer().createMissile(ObjectType.MISSILE_SPLASH,
				getOwner(), position, speedVector);

	}

}
