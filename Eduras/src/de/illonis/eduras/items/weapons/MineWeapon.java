package de.illonis.eduras.items.weapons;

import java.util.logging.Level;
import java.util.logging.Logger;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.GameInformation;
import de.illonis.eduras.ObjectFactory.ObjectType;
import de.illonis.eduras.events.SetVisibilityEvent;
import de.illonis.eduras.exceptions.ObjectNotFoundException;
import de.illonis.eduras.gameobjects.TimingSource;
import de.illonis.eduras.items.ItemUseInformation;
import de.illonis.eduras.settings.S;
import de.illonis.eduras.shapes.ShapeFactory;
import de.illonis.eduras.shapes.ShapeFactory.ShapeType;

/**
 * Describes the behavior and properties of a mine weapon.
 * 
 * @author Florian 'Ren' Mai <florian.ren.mai@googlemail.com>
 * 
 */
public class MineWeapon extends Weapon {

	private final static Logger L = EduLog.getLoggerFor(MineWeapon.class
			.getName());

	/**
	 * Create a mine weapon within the given game info.
	 * 
	 * @param gameInformation
	 *            The game informatin.
	 * @param timingSource
	 *            the timing source.
	 * @param id
	 *            The id of the mine weapon.
	 */
	public MineWeapon(GameInformation gameInformation,
			TimingSource timingSource, int id) {
		super(ObjectType.MINELAUNCHER, gameInformation, timingSource, id);
		setName("Mine Launcher");

		setShape(ShapeFactory.createShape(ShapeType.STAR));
		defaultCooldown = S.Server.go_mineweapon_cooldown;
		setAmmunitionLimited(S.Server.go_mineweapon_fillamount,
				S.Server.go_mineweapon_maxammo);
	}

	@Override
	protected void doIfReady(ItemUseInformation info) {
		int missileId = shootMissile(ObjectType.MINE_MISSILE, info);

		if (missileId == -1) {
			return;
		}

		MineMissile mineMissile;
		try {
			mineMissile = (MineMissile) getGame().findObjectById(missileId);
		} catch (ObjectNotFoundException e) {
			L.log(Level.WARNING, "Cannot find just created missile.", e);
			return;
		}
		mineMissile.setVisible(Visibility.OWNER_TEAM);
		SetVisibilityEvent visEvent = new SetVisibilityEvent(missileId,
				Visibility.OWNER_TEAM);
		getGame().getEventTriggerer().notifyGameObjectVisibilityChanged(
				visEvent);
	}
}
