package de.illonis.eduras.gamemodes;

import java.util.Set;

import de.illonis.eduras.Player;
import de.illonis.eduras.Team;
import de.illonis.eduras.gameclient.userprefs.KeyBindings.KeyBinding;
import de.illonis.eduras.gameobjects.GameObject;
import de.illonis.eduras.gameobjects.GameObject.Relation;
import de.illonis.eduras.gameobjects.NeutralBase;
import de.illonis.eduras.maps.SpawnPosition.SpawnType;
import de.illonis.eduras.units.InteractMode;
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
		NO_GAMEMODE, DEATHMATCH, TEAM_DEATHMATCH, CAPTURE_THE_FLAG, NINJA_VS_SAMURAI, KING_OF_THE_HILL, EDURA;
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
	 * Called when a player disconnects.
	 * 
	 * @param ownerId
	 *            id of disconnected player.
	 */
	public void onDisconnect(int ownerId);

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

	/**
	 * When an object is entering a {@link NeutralBase}, this method is called
	 * to determine which team becomes the base overtaking team.
	 * 
	 * @param base
	 *            The base that is being taken over.
	 * @param object
	 *            The object that entered or left the game.
	 * @param objectEntered
	 *            Tells whether it entered or left.
	 * @param presentObjects
	 *            All objects that are currently in the area.
	 * @return The team that is determined to be overtaking the base.
	 */
	public Team determineProgressingTeam(NeutralBase base, GameObject object,
			boolean objectEntered, Set<GameObject> presentObjects);

	/**
	 * Called when a team has occupied a {@link NeutralBase}.
	 * 
	 * @param base
	 *            the base that was occupied
	 * @param occupyingTeam
	 *            The team that has occupied the {@link NeutralBase}.
	 */
	public void onBaseOccupied(NeutralBase base, Team occupyingTeam);

	/**
	 * Called when a team loses a {@link NeutralBase}.
	 * 
	 * @param base
	 *            the base that was lost
	 * @param losingTeam
	 *            The team that has lost the {@link NeutralBase}.
	 */
	public void onBaseLost(NeutralBase base, Team losingTeam);

	/**
	 * Checks whether given binding is supported by this gamemode. If a key is
	 * reported as unsupported, keyhandlers will not be notified for that key
	 * while playing in this gamemode.
	 * 
	 * @param binding
	 *            the binding.
	 * @return true if the binding is supported.
	 */
	public boolean supportsKeyBinding(KeyBinding binding);

	/**
	 * Determines whether the given player is allowed/is able to switch to the
	 * given mode.
	 * 
	 * @param player
	 *            The player for which to determine whether he can switch modes.
	 * @param mode
	 * @return true if player is allowed/able to, false otherwise.
	 */
	public boolean canSwitchMode(Player player, InteractMode mode);

}
