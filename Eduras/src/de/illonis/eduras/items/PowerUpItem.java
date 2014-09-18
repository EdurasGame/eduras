package de.illonis.eduras.items;

import java.util.logging.Logger;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.GameInformation;
import de.illonis.eduras.ObjectFactory.ObjectType;
import de.illonis.eduras.gameobjects.GameObject;
import de.illonis.eduras.gameobjects.TimingSource;
import de.illonis.eduras.units.PlayerMainFigure;

/**
 * A PowerUp is an item that only collides with players. On collision, this item
 * first calls its {@link #onActivation(PlayerMainFigure)} method and is removed
 * from the game afterwards.
 * 
 * @author Florian 'Ren' Mai
 * 
 */
public abstract class PowerUpItem extends Item {

	private final static Logger L = EduLog.getLoggerFor(PowerUpItem.class
			.getName());

	/**
	 * Create a new InstantItem.
	 * 
	 * @param type
	 * @param timingSource
	 * @param gi
	 * @param id
	 */
	public PowerUpItem(ObjectType type, TimingSource timingSource,
			GameInformation gi, int id) {
		super(type, timingSource, gi, id);
	}

	@Override
	public void onCollision(GameObject collidingObject, float angle) {
		if (collidingObject instanceof PlayerMainFigure) {
			onActivation((PlayerMainFigure) collidingObject);
			getGame().getEventTriggerer().removeObject(getId());
		} else {
			L.severe("PowerUp should only collide with PlayerMainFigures!!");
			return;
		}
	}

	/**
	 * This method is called right before this InstantItem is removed from the
	 * game.
	 * 
	 * @param touchingPlayer
	 */
	public abstract void onActivation(PlayerMainFigure touchingPlayer);

	@Override
	protected boolean isCollidableWith(GameObject otherObject) {
		return otherObject instanceof PlayerMainFigure;
	}
}
