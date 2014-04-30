package de.illonis.eduras.units;

import java.util.logging.Logger;

import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Vector2f;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.GameInformation;
import de.illonis.eduras.ObjectFactory.ObjectType;
import de.illonis.eduras.gameobjects.GameObject;
import de.illonis.eduras.gameobjects.TimingSource;
import de.illonis.eduras.interfaces.MovementControlable;
import de.illonis.eduras.inventory.Inventory;
import de.illonis.eduras.settings.S;

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
	private InteractMode currentMode;
	private long lastModeSwitch;
	private final Inventory inventory = new Inventory();

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
	 * @param name
	 *            The name of the player.
	 * @param id
	 *            id of the player.
	 */
	public PlayerMainFigure(GameInformation game, TimingSource timingSource,
			int ownerId, String name, int id) {
		super(game, timingSource, S.player_maxhealth_default, id);
		setObjectType(ObjectType.PLAYER);
		this.name = name;
		setSpeed(S.player_speed_default);
		lastModeSwitch = 0;
		currentMode = InteractMode.MODE_EGO;
		setOwner(ownerId);

		// get position
		// Vector2df firstEdge = new Vector2df(25, 0);
		// Vector2df secondEdge = new Vector2df(-10, 10);
		// Vector2df thirdEdge = new Vector2df(-10, -10);

		setShape(new Circle(4.5f, 4.5f, 9f, 360));

		// try {
		// // setShape(new Triangle(firstEdge, secondEdge, thirdEdge));
		// } catch (ShapeVector2dfsNotApplicableException e) {
		// L.log(Level.SEVERE, "error setting player shape", e);
		// }
	}

	/**
	 * Create a new player that belongs to the given game and has the given
	 * owner.
	 * 
	 * @param game
	 *            The game the player belongs to.
	 * @param timingSource
	 *            the timing source.
	 * @param ownerId
	 *            The id of the owner.
	 * @param id
	 *            The id of the player.
	 */
	public PlayerMainFigure(GameInformation game, TimingSource timingSource,
			int ownerId, int id) {
		this(game, timingSource, ownerId, "unbekannt", id);
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
		Vector2f normalized = getSpeedVector().normalise().scale(getSpeed());
		currentSpeedX = normalized.x;
		currentSpeedY = normalized.y;
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
	protected void onDead(int killer) {
		//
	}

}
