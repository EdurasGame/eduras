package de.illonis.eduras;

import java.util.logging.Logger;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.exceptions.PlayerHasNoTeamException;
import de.illonis.eduras.inventory.Inventory;
import de.illonis.eduras.units.InteractMode;
import de.illonis.eduras.units.PlayerMainFigure;

/**
 * Represents an Eduras? player.
 * 
 * @author Florian 'Ren' Mai <florian.ren.mai@googlemail.com>
 * 
 */
public class Player {
	private final static Logger L = EduLog.getLoggerFor(Player.class.getName());

	private String name;
	private int playerId;
	private InteractMode currentMode;
	private long lastModeSwitch;
	private final Inventory inventory = new Inventory();
	private PlayerMainFigure playerMainFigure;
	private Team team;
	private int blinksAvailable;

	/**
	 * 
	 * @param playerId
	 * @param name
	 */
	public Player(int playerId, String name) {
		this.name = name;
		this.playerId = playerId;
		lastModeSwitch = 0;
		currentMode = InteractMode.MODE_EGO;

		blinksAvailable = 100;
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

	/**
	 * Returns the player's inventory.
	 * 
	 * @return Player's inventory.
	 */
	public Inventory getInventory() {
		return inventory;
	}

	/**
	 * Sets the player's respective {@link PlayerMainFigure}.
	 * 
	 * @param go
	 *            the main figure
	 */
	public void setPlayerMainFigure(PlayerMainFigure go) {
		this.playerMainFigure = go;
	}

	/**
	 * Returns the player's id. It's the same as the main figure's owner id.
	 * 
	 * @return player's id / owner id
	 */
	public int getPlayerId() {
		return playerId;
	}

	/**
	 * Returns the player's respective {@link PlayerMainFigure}.
	 * 
	 * @return player's main figure
	 */
	public PlayerMainFigure getPlayerMainFigure() {
		return playerMainFigure;
	}

	/**
	 * Set the team the player is in.
	 * 
	 * @param team
	 */
	public void setTeam(Team team) {
		this.team = team;
	}

	/**
	 * Returns the team the player is in.
	 * 
	 * @return player's team
	 * @throws PlayerHasNoTeamException
	 *             thrown if the player doesn't have a team
	 */
	public Team getTeam() throws PlayerHasNoTeamException {
		if (team == null) {
			throw new PlayerHasNoTeamException(this);
		}
		return team;
	}

	/**
	 * Returns whether the player is currently dead in the game's sense. That is
	 * , if either he doesn't have a {@link PlayerMainFigure} or the
	 * {@link PlayerMainFigure #isDead()}.
	 * 
	 * @return if the player is dead
	 */
	public boolean isDead() {
		return (playerMainFigure == null || playerMainFigure.isDead());
	}

	public int getBlinksAvailable() {
		synchronized (this) {
			return blinksAvailable;
		}
	}

	public void setBlinksAvailable(int blinksAvailable) {
		synchronized (this) {
			this.blinksAvailable = blinksAvailable;
		}
	}
}
