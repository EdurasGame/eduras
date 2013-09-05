package de.illonis.eduras.units;

import de.illonis.eduras.GameInformation;
import de.illonis.eduras.ObjectFactory.ObjectType;
import de.illonis.eduras.Team;
import de.illonis.eduras.exceptions.ShapeVerticesNotApplicableException;
import de.illonis.eduras.gameobjects.ArtificialIntelligence;
import de.illonis.eduras.gameobjects.GameObject;
import de.illonis.eduras.interfaces.MovementControlable;
import de.illonis.eduras.inventory.Inventory;
import de.illonis.eduras.logger.EduLog;
import de.illonis.eduras.math.Vector2D;
import de.illonis.eduras.shapes.Triangle;

/**
 * This class represents a player.
 * 
 * @author Florian Mai <florian.ren.mai@googlemail.com>
 * 
 */
public class PlayerMainFigure extends Unit implements MovementControlable {
	private String name;
	private Team team;
	private final Inventory inventory = new Inventory();

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
	 * @param id
	 *            id of the player.
	 */
	public PlayerMainFigure(GameInformation game, int ownerId, String name,
			int id) {
		super(game, 30, id);
		setObjectType(ObjectType.PLAYER);
		this.name = name;
		setSpeed(50);
		setOwner(ownerId);

		// get position
		Vector2D firstEdge = new Vector2D(0, 10);
		Vector2D secondEdge = new Vector2D(10, -10);
		Vector2D thirdEdge = new Vector2D(-10, -10);

		try {
			setShape(new Triangle(firstEdge, secondEdge, thirdEdge));
		} catch (ShapeVerticesNotApplicableException e) {

			EduLog.passException(e);
		}
	}

	/**
	 * Create a new player that belongs to the given game and has the given
	 * owner.
	 * 
	 * @param game
	 *            The game the player belongs to.
	 * @param ownerId
	 *            The id of the owner.
	 * @param id
	 *            The id of the player.
	 */
	public PlayerMainFigure(GameInformation game, int ownerId, int id) {
		this(game, ownerId, "unbekannt", id);
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

	/**
	 * @return player's team.
	 * 
	 * @author illonis
	 */
	public Team getTeam() {
		return team;
	}

	/**
	 * Assigns player to given team.
	 * 
	 * @param team
	 *            new team.
	 * 
	 * @author illonis
	 */
	public void setTeam(Team team) {
		this.team = team;
	}

	/**
	 * Returns name of player.
	 * 
	 * @return player's name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets name of player.
	 * 
	 * @param name
	 *            new player name.
	 */
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public void onCollision(GameObject collidingObject) {
		// do nothing
		EduLog.info("I JUST COLLIDED!");
	}

	/**
	 * Returns the player's inventory.
	 * 
	 * @return Player's inventory.
	 */
	public Inventory getInventory() {
		return inventory;
	}

	@Override
	public void onMapBoundsReached() {
	}

	@Override
	public ArtificialIntelligence getAI() {
		// TODO Auto-generated method stub
		return null;
	}
}
