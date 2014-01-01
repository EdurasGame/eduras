package de.illonis.eduras.units;

import java.util.logging.Level;
import java.util.logging.Logger;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.GameInformation;
import de.illonis.eduras.ObjectFactory.ObjectType;
import de.illonis.eduras.Team;
import de.illonis.eduras.exceptions.ShapeVerticesNotApplicableException;
import de.illonis.eduras.gameobjects.GameObject;
import de.illonis.eduras.interfaces.MovementControlable;
import de.illonis.eduras.inventory.Inventory;
import de.illonis.eduras.math.Vector2D;
import de.illonis.eduras.settings.S;
import de.illonis.eduras.shapes.Triangle;

/**
 * This class represents a player.
 * 
 * @author Florian Mai <florian.ren.mai@googlemail.com>
 * 
 */
public class PlayerMainFigure extends Unit implements MovementControlable {

	/**
	 * Interaction modes where players can be in.
	 * 
	 * @author illonis
	 * 
	 */
	@SuppressWarnings("javadoc")
	public enum InteractMode {

		MODE_EGO(5000), MODE_STRATEGY(1000);

		private final long coolDown; // in ms.

		private InteractMode(long coolDown) {
			this.coolDown = coolDown;
		}

		public long getCoolDown() {
			return coolDown;
		}

		public InteractMode next() {
			if (this == MODE_EGO)
				return MODE_STRATEGY;
			return MODE_EGO;
		}
	};

	private final static Logger L = EduLog.getLoggerFor(PlayerMainFigure.class
			.getName());

	private String name;
	private Team team;
	private InteractMode currentMode;
	private long lastModeSwitch;
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
		super(game, S.player_maxhealth_default, id);
		setObjectType(ObjectType.PLAYER);
		this.name = name;
		setSpeed(S.player_speed_default);
		lastModeSwitch = 0;
		currentMode = InteractMode.MODE_EGO;
		setOwner(ownerId);

		// get position
		Vector2D firstEdge = new Vector2D(0, 25);
		Vector2D secondEdge = new Vector2D(10, -10);
		Vector2D thirdEdge = new Vector2D(-10, -10);

		try {
			setShape(new Triangle(firstEdge, secondEdge, thirdEdge));
		} catch (ShapeVerticesNotApplicableException e) {
			L.log(Level.SEVERE, "error setting player shape", e);
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

	/**
	 * Changes the interaction mode this player is in.
	 * 
	 * @param newMode
	 *            the new mode.
	 */
	public void setMode(InteractMode newMode) {
		currentMode = newMode;
		lastModeSwitch = System.currentTimeMillis();
	}

	/**
	 * Returns the interaction mode the player is currently in.
	 * 
	 * @return this player's interaction mode.
	 */
	public InteractMode getCurrentMode() {
		return currentMode;
	}

	/**
	 * Calculates the cooldown remaining until player can switch mode again.
	 * 
	 * @return the remaining cooldown in ms. This may be negative is there is no
	 *         cooldown anymore.
	 */
	public long getModeSwitchCooldown() {
		return (lastModeSwitch + currentMode.getCoolDown())
				- System.currentTimeMillis();
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
		L.fine("I JUST COLLIDED!");
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

}
