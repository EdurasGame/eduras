package de.illonis.eduras.units;

import org.newdawn.slick.geom.Rectangle;

import de.illonis.eduras.GameInformation;
import de.illonis.eduras.ObjectFactory.ObjectType;
import de.illonis.eduras.gameobjects.GameObject;
import de.illonis.eduras.gameobjects.TimingSource;
import de.illonis.eduras.settings.S;

/**
 * The unit used for visibility spell.
 * 
 * @author illonis
 * 
 */
public class ScoutSpell extends DisposableUnit {

	/**
	 * Creates a new scout spell.
	 * 
	 * @param game
	 *            game info.
	 * @param timingSource
	 *            timing source.
	 * @param id
	 *            the object id.
	 * @param owner
	 *            the owner.
	 */
	public ScoutSpell(GameInformation game, TimingSource timingSource, int id,
			int owner) {
		super(game, timingSource, 1, id);
		setOwner(owner);
		setShape(new Rectangle(0, 0, 5f, 5f));
		setObjectType(ObjectType.SPELL_SCOUT);
		setVisionAngle(S.Server.spell_scout_visionangle);
		setVisionRange(S.Server.spell_scout_visionrange);
		setCollidable(false);
		setVisible(Visibility.INVISIBLE);
	}

	@Override
	public void onCollision(GameObject collidingObject, float angle) {
	}

	@Override
	protected boolean isCollidableWith(GameObject otherObject) {
		return false;
	}
}
