package de.illonis.eduras.items.weapons;

import de.illonis.eduras.GameInformation;
import de.illonis.eduras.ObjectFactory.ObjectType;
import de.illonis.eduras.events.GameEvent.GameEventNumber;
import de.illonis.eduras.events.SetBooleanGameObjectAttributeEvent;
import de.illonis.eduras.settings.S;
import de.illonis.eduras.shapes.Circle;

/**
 * Wraps properties of {@link MineWeapon}s missiles.
 * 
 * @author Florian 'Ren' Mai <florian.ren.mai@googlemail.com>
 * 
 */
public class MineMissile extends Missile {

	/**
	 * Create a new MineMissile.
	 * 
	 * @param game
	 * @param id
	 */
	public MineMissile(GameInformation game, int id) {
		super(game, id);
		setDamage(S.go_minemissile_damage);
		setDamageRadius(S.go_minemissile_damageradius);
		setObjectType(ObjectType.MINE_MISSILE);
		setSpeed(S.go_minemissile_speed);
		setMaxRange(S.go_minemissile_maxrange);
		setShape(new Circle(S.go_minemissile_shape_size));

		setVisible(false);
		SetBooleanGameObjectAttributeEvent invisEvent = new SetBooleanGameObjectAttributeEvent(
				GameEventNumber.SET_VISIBLE, id, false);
		game.getEventTriggerer().notifyGameObjectStateChanged(invisEvent);
	}
}
