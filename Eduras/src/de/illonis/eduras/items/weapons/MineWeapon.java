package de.illonis.eduras.items.weapons;

import de.illonis.eduras.GameInformation;
import de.illonis.eduras.ObjectFactory.ObjectType;
import de.illonis.eduras.items.ItemUseInformation;
import de.illonis.eduras.settings.S;
import de.illonis.eduras.shapes.Circle;

/**
 * Describes the behavior and properties of a mine weapon.
 * 
 * @author Florian 'Ren' Mai <florian.ren.mai@googlemail.com>
 * 
 */
public class MineWeapon extends Weapon {

	/**
	 * Create a mine weapon within the given game info.
	 * 
	 * @param gameInformation
	 *            The game informatin.
	 * @param id
	 *            The id of the mine weapon.
	 */
	public MineWeapon(GameInformation gameInformation, int id) {
		super(ObjectType.MINELAUNCHER, gameInformation, id);
		setName("Mine Launcher");

		setShape(new Circle(S.go_mineweapon_size));
		defaultCooldown = S.go_mineweapon_cooldown;
	}

	@Override
	protected void doIfReady(ItemUseInformation info) {
		shootMissile(ObjectType.MINE_MISSILE, info);
	}

}
