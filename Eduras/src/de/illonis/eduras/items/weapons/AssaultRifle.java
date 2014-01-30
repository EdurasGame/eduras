package de.illonis.eduras.items.weapons;

import de.illonis.eduras.GameInformation;
import de.illonis.eduras.ObjectFactory.ObjectType;
import de.illonis.eduras.items.ItemUseInformation;
import de.illonis.eduras.settings.S;
import de.illonis.eduras.shapes.Circle;

/**
 * An assault rifle is a very fast in cooldown and missile speed weapon, that
 * causes very low damage.
 * 
 * @author Florian 'Ren' Mai <florian.ren.mai@googlemail.com>
 * 
 */
public class AssaultRifle extends Weapon {

	/**
	 * Create a new {@link AssaultRifle}.
	 * 
	 * @param gi
	 * @param id
	 */
	public AssaultRifle(GameInformation gi, int id) {
		super(ObjectType.ASSAULTRIFLE, gi, id);
		setName("Assault Rifle");

		setShape(new Circle(S.go_assaultrifle_shape_size));
		defaultCooldown = S.go_assaultrifle_cooldown;
		setAmmunitionLimited(S.go_assaultrifle_fillamount,
				S.go_assaultrifle_maxammo);
	}

	@Override
	protected void doIfReady(ItemUseInformation info) {
		shootMissile(ObjectType.ASSAULT_MISSILE, info);

	}

}
