package de.illonis.eduras.gamemodes;

import de.illonis.eduras.Team;
import de.illonis.eduras.gameobjects.GameObject;
import de.illonis.eduras.gameobjects.GameObject.Relation;
import de.illonis.eduras.maps.SpawnPosition.SpawnType;
import de.illonis.eduras.units.Unit;

/**
 * This interface serves a number of methods which are called when a certain
 * event appears and an implementation shall reflect a game mode's behavior on
 * such an event.
 * 
 * @author Florian Mai <florian.ren.mai@googlemail.com>
 * 
 */
public interface GameMode {

	/**
	 * A list of all available gamemodes.
	 * 
	 * @author illonis
	 * 
	 */
	@SuppressWarnings("javadoc")
	public enum GameModeNumber {
		NO_GAMEMODE, DEATHMATCH, TEAM_DEATHMATCH, CAPTURE_THE_FLAG, NINJA_VS_SAMURAI;
	}

	/**
	 * Returns the associated {@link GameModeNumber}.
	 * 
	 * @return this mode's number.
	 */
	public GameModeNumber getNumber();

	/**
	 * Returns the game mode's name.
	 * 
	 * @return The name of the game mode.
	 */
	public String getName();

	/**
	 * Retrieves the relation status of given two gameobjects.
	 * 
	 * @param a
	 *            the first gameobject.
	 * @param b
	 *            the second gameobject.
	 * @return the relation between both objects.
	 * @author illonis
	 */
	public Relation getRelation(GameObject a, GameObject b);

	/**
	 * Called when someon died.
	 * 
	 * @param killedUnit
	 *            The player who died.
	 * @param killingPlayer
	 *            The player who killed the other player. Null if there is none.
	 */
	public void onDeath(Unit killedUnit, int killingPlayer);

	/**
	 * Called when the time is up.
	 */
	public void onTimeUp();

	/**
	 * Called when a player connects.
	 * 
	 * @param ownerId
	 *            id of connected player.
	 */
	public void onConnect(int ownerId);

	/**
	 * Called when match begins to let gamemode initalize some things.
	 * 
	 * @author illonis
	 */
	public void onGameStart();

	/**
	 * Assignes a team to a spawntype. Used to map teams to the
	 * pseudo-spawngroups in a map.<br>
	 * For example if you have team-deathmatch, return {@link SpawnType#TEAM_A}
	 * on first call and {@link SpawnType#TEAM_B} on even second call.
	 * 
	 * @param team
	 *            the team to find the type for.
	 * @return the team's spawntype.
	 */
	public SpawnType getSpawnTypeForTeam(Team team);

}
