package de.illonis.eduras.units;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.newdawn.slick.geom.Circle;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.GameInformation;
import de.illonis.eduras.ObjectFactory.ObjectType;
import de.illonis.eduras.Player;
import de.illonis.eduras.Team;
import de.illonis.eduras.exceptions.PlayerHasNoTeamException;
import de.illonis.eduras.gameobjects.GameObject;
import de.illonis.eduras.gameobjects.TimingSource;
import de.illonis.eduras.interfaces.MovementControlable;
import de.illonis.eduras.settings.S;

/**
 * This class represents a player.
 * 
 * @author Florian Mai <florian.ren.mai@googlemail.com>
 * 
 */
public class PlayerMainFigure extends Unit implements MovementControlable {

	private final static Logger L = EduLog.getLoggerFor(PlayerMainFigure.class
			.getName());

	private Player player;

	/**
	 * Create a new player that belongs to the given game and has the given
	 * owner and has a name.
	 * 
	 * @param game
	 *            The game the player belongs to.
	 * @param timingSource
	 *            the timing source.
	 * @param ownerId
	 *            The id of the owner.
	 * @param id
	 *            id of the player.
	 * @param player
	 *            the respective player this main figure belongs to.
	 */
	public PlayerMainFigure(GameInformation game, TimingSource timingSource,
			int ownerId, int id, Player player) {
		super(game, timingSource, S.Server.player_maxhealth_default, id);
		setObjectType(ObjectType.PLAYER);
		setSpeed(S.Server.player_speed_default);
		setOwner(ownerId);
		this.player = player;

		// get position
		// Vector2df firstEdge = new Vector2df(25, 0);
		// Vector2df secondEdge = new Vector2df(-10, 10);
		// Vector2df thirdEdge = new Vector2df(-10, -10);

		setShape(new Circle(4.5f, 4.5f, 9f, 360));
		// shapeOffsetX = -getShape().getWidth() / 2;
		// shapeOffsetY = -getShape().getHeight() / 2;
		// try {
		// // setShape(new Triangle(firstEdge, secondEdge, thirdEdge));
		// } catch (ShapeVector2dfsNotApplicableException e) {
		// L.log(Level.SEVERE, "error setting player shape", e);
		// }
	}

	@Override
	public void startMoving(Direction direction) {
		switch (direction) {
		case TOP:
			currentSpeedY = -getSpeed();
			break;
		case BOTTOM:
			currentSpeedY = getSpeed();
			break;
		case LEFT:
			currentSpeedX = -getSpeed();
			break;
		case RIGHT:
			currentSpeedX = getSpeed();
			break;
		default:
			break;
		}
	}

	@Override
	public void stopMoving(Direction direction) {
		if (isHorizontal(direction)) {
			currentSpeedX = 0;
		} else {
			currentSpeedY = 0;
		}
	}

	@Override
	public void stopMoving() {
		currentSpeedX = 0;
		currentSpeedY = 0;
	}

	@Override
	public void onCollision(GameObject collidingObject) {
		// do nothing
		L.fine("I JUST COLLIDED!");
	}

	@Override
	protected void onDead(int killer) {
		//
	}

	/**
	 * Set the player this main figure belongs to.
	 * 
	 * @param player
	 */
	public void setPlayer(Player player) {
		this.player = player;
	}

	/**
	 * Returns the player this main figure belongs to.
	 * 
	 * @return player
	 */
	public Player getPlayer() {
		return player;
	}

	@Override
	public Team getTeam() {
		try {
			return player.getTeam();
		} catch (PlayerHasNoTeamException e) {
			L.log(Level.SEVERE,
					"The player doesn't have a team at this point!!", e);
			return null;
		}
	}

	@Override
	protected boolean isCollidableWith(GameObject otherObject) {
		return true;
	}
}
