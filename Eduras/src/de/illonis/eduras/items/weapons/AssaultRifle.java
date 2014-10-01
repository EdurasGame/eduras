package de.illonis.eduras.items.weapons;

import org.newdawn.slick.geom.Circle;

import de.illonis.eduras.GameInformation;
import de.illonis.eduras.ObjectFactory.ObjectType;
import de.illonis.eduras.gameobjects.TimingSource;
import de.illonis.eduras.items.ItemUseInformation;
import de.illonis.eduras.settings.S;

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
	 *            game information.
	 * @param timingSource
	 *            the timing source.
	 * @param id
	 *            the object id.
	 */
	public AssaultRifle(GameInformation gi, TimingSource timingSource, int id) {
		super(ObjectType.ASSAULTRIFLE, gi, timingSource, id);
		setName("Assault Rifle");

		setShape(new Circle(S.Server.go_assaultrifle_shape_size,
				S.Server.go_assaultrifle_shape_size,
				S.Server.go_assaultrifle_shape_size));
		defaultCooldown = S.Server.go_assaultrifle_cooldown;
		setAmmunitionLimited(S.Server.go_assaultrifle_fillamount,
				S.Server.go_assaultrifle_maxammo);
	}

	@Override
	protected void doIfReady(ItemUseInformation info) {
		shootMissile(ObjectType.ASSAULT_MISSILE, info);

	}

}
