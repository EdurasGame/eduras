package de.illonis.eduras;

import de.illonis.eduras.exceptions.ShapeVerticesNotApplicableException;
import de.illonis.eduras.interfaces.Controllable;
import de.illonis.eduras.math.Vector2D;
import de.illonis.eduras.shapes.Triangle;

/**
 * This class represents a player.
 * 
 * @author Florian Mai <florian.ren.mai@googlemail.com>
 * 
 */
public class Player extends MoveableGameObject implements Controllable {
	private String name;

	/**
	 * Create a new player that belongs to the given game and has the given
	 * owner and has a name.
	 * 
	 * @param game
	 *            The game the player belongs to.
	 * @param ownerId
	 *            The id of the owner.
	 * @param name
	 *            The name of the player.
	 */
	public Player(GameInformation game, int ownerId, String name) {
		super(game);
		this.name = name;
		setSpeed(50);
		setOwner(ownerId);

		// get position
		Vector2D firstEdge = new Vector2D(0f, 10f);
		Vector2D secondEdge = new Vector2D(10f, -10f);
		Vector2D thirdEdge = new Vector2D(-10f, -10f);

		try {
			setShape(new Triangle(firstEdge, secondEdge, thirdEdge));
		} catch (ShapeVerticesNotApplicableException e) {
			e.printStackTrace();
		}

		// TODO: Replace setting the position to a non random value.
		double randX = (game.getMap().getWidth()) * Math.random();
		double randY = (game.getMap().getHeight()) * Math.random();
		setPosition(randX, randY);
	}

	/**
	 * Create a new player that belongs to the given game and has the given
	 * owner.
	 * 
	 * @param game
	 *            The game the player belongs to.
	 * @param ownerId
	 *            The id of the owner.
	 */
	public Player(GameInformation game, int ownerId) {
		this(game, ownerId, "unbekannt");
	}

	@Override
	public void startMoving(Direction direction) {
		switch (direction) {
		case TOP:
			getSpeedVector().setY(-getSpeed());
			break;
		case BOTTOM:
			getSpeedVector().setY(getSpeed());
			break;
		case LEFT:
			getSpeedVector().setX(-getSpeed());
			break;
		case RIGHT:
			getSpeedVector().setX(getSpeed());
			break;
		default:
			break;
		}
		getSpeedVector().setLength(getSpeed());
	}

	@Override
	public void stopMoving(Direction direction) {
		if (isHorizontal(direction)) {
			getSpeedVector().setX(0);
		} else {
			getSpeedVector().setY(0);
		}
	}

	@Override
	public void stopMoving() {
		setSpeedVector(new Vector2D());
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
