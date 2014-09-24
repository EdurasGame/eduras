package de.illonis.eduras.items.weapons;

import de.illonis.eduras.GameInformation;
import de.illonis.eduras.ObjectFactory.ObjectType;
import de.illonis.eduras.gameobjects.GameObject;
import de.illonis.eduras.gameobjects.TimingSource;
import de.illonis.eduras.math.Vector2df;
import de.illonis.eduras.settings.S;
import de.illonis.eduras.units.Unit;

/**
 * @author Florian Mai <florian.ren.mai@googlemail.com>
 * 
 */
public class SimpleMissile extends Missile {

	private int bouncesLeft;

	/**
	 * Creates a new simplemissile
	 * 
	 * @param game
	 *            The game context
	 * @param timingSource
	 *            the timing source.
	 * @param id
	 *            The id of the new missile
	 */
	public SimpleMissile(GameInformation game, TimingSource timingSource, int id) {
		super(game, timingSource, id);
		setDamage(S.Server.go_simplemissile_damage);
		setObjectType(ObjectType.SIMPLEMISSILE);
		setSpeed(S.Server.go_simplemissile_speed);
		setMaxRange(S.Server.go_simplemissile_maxrange);

		bouncesLeft = S.Server.go_simplemissile_bounces;
	}

	@Override
	public void onCollision(GameObject collidingObject, float angle) {
		Relation relation = getGame().getGameSettings().getGameMode()
				.getRelation(this, collidingObject);
		if ((relation == Relation.HOSTILE || (relation == Relation.ALLIED && S.Server.mp_teamattack))
				&& collidingObject.isUnit()) {
			((Unit) collidingObject).damagedBy(getDamage(), getOwner());
			removeSelf();
		} else {
			if (bouncesLeft > 0) {
				bouncesLeft--;

				float arrivalAngle = angle <= 90 ? angle : 180 - angle;
				Vector2df currentSpeedVector = new Vector2df(getSpeedVector());

				float rotateAngle;
				if (angle < 90) {
					rotateAngle = -2 * arrivalAngle;
				} else {
					rotateAngle = +2 * arrivalAngle;
				}
				currentSpeedVector.rotate(rotateAngle);
				setSpeedVector(currentSpeedVector);
			} else {
				removeSelf();
			}
		}

	}
}
