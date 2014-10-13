package de.illonis.eduras.items.weapons;

import org.newdawn.slick.geom.Circle;

import de.illonis.eduras.GameInformation;
import de.illonis.eduras.gameobjects.GameObject;
import de.illonis.eduras.gameobjects.MoveableGameObject;
import de.illonis.eduras.gameobjects.TimingSource;
import de.illonis.eduras.math.ShapeGeometry;
import de.illonis.eduras.math.Vector2df;
import de.illonis.eduras.settings.S;
import de.illonis.eduras.units.Unit;

/**
 * A missile is shot by a weapon.
 * 
 * @author illonis
 * 
 */
public abstract class Missile extends MoveableGameObject {

	private int damage;
	private float maxRange = 0;
	private float rangeMoved;

	/**
	 * Creates a new missile with the id given and in the context of specific
	 * gameinformation.
	 * 
	 * @param game
	 *            The gameinformation.
	 * @param timingSource
	 *            the timing source.
	 * @param id
	 *            The id.
	 */
	public Missile(GameInformation game, TimingSource timingSource, int id) {
		super(game, timingSource, id);
		rangeMoved = 0;
		setShape(new Circle(S.Server.go_missile_radius,
				S.Server.go_missile_radius, S.Server.go_missile_radius));
		setMaxRange(S.Server.go_missile_defaultrange);
	}

	/**
	 * Returns range of this missile.
	 * 
	 * @return missile range.
	 * 
	 * @author illonis
	 */
	public float getMaxRange() {
		return maxRange;
	}

	/**
	 * Sets the maximum range of this missile.
	 * 
	 * @param maxRange
	 *            new value
	 * 
	 * @author illonis
	 */
	protected void setMaxRange(float maxRange) {
		this.maxRange = maxRange;
	}

	/**
	 * Set the damage the missile causes when hitting someone.
	 * 
	 * @param damage
	 *            The new damage.
	 */
	public void setDamage(int damage) {
		this.damage = damage;
	}

	/**
	 * Returns the damage the missile causes when hitting someone.
	 * 
	 * @return The damage.
	 */
	public int getDamage() {
		return damage;
	}

	@Override
	public void onMove(long delta, ShapeGeometry geometry) {
		if (maxRange == 0)
			super.onMove(delta, geometry);
		else {
			Vector2df lastPos = getPositionVector();
			super.onMove(delta, geometry);
			Vector2df newPos = getPositionVector();
			rangeMoved += lastPos.distance(newPos);
			if (rangeMoved > maxRange)
				removeSelf();
		}
	}

	@Override
	public void onCollision(GameObject collidingObject, float angle) {
		Relation relation = getGame().getGameSettings().getGameMode()
				.getRelation(this, collidingObject);

		if ((relation == Relation.HOSTILE || (relation == Relation.ALLIED && S.Server.mp_teamattack))
				&& collidingObject.isUnit()) {
			((Unit) collidingObject).damagedBy(getDamage(), getOwner());
		}

		// TODO: use damage radius
		removeSelf();
	}

	@Override
	protected boolean isCollidableWith(GameObject otherObject) {
		if (otherObject instanceof Weapon || otherObject instanceof Missile) {
			return false;
		} else {
			return true;
		}
	}
}
