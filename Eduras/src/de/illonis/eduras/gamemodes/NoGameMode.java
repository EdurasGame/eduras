package de.illonis.eduras.gamemodes;

import java.util.Set;

import de.illonis.eduras.GameInformation;
import de.illonis.eduras.ObjectFactory.ObjectType;
import de.illonis.eduras.Player;
import de.illonis.eduras.Team;
import de.illonis.eduras.gameclient.userprefs.KeyBindings.KeyBinding;
import de.illonis.eduras.gameobjects.GameObject;
import de.illonis.eduras.gameobjects.GameObject.Relation;
import de.illonis.eduras.gameobjects.NeutralBase;
import de.illonis.eduras.logic.EventTriggerer;
import de.illonis.eduras.maps.SpawnPosition.SpawnType;
import de.illonis.eduras.units.InteractMode;
import de.illonis.eduras.units.PlayerMainFigure;
import de.illonis.eduras.units.Unit;

/**
 * This game mode is one that has got no winning conditions, does not care for
 * statistics and respawns dead players immediately.
 * 
 * @author Florian Mai <florian.ren.mai@googlemail.com>
 * 
 */
public class NoGameMode extends BasicGameMode {

	/**
	 * Creates a new instance of this gamemode.
	 * 
	 * @param gameInfo
	 */
	public NoGameMode(GameInformation gameInfo) {
		super(gameInfo);
	}

	@Override
	public String getName() {
		return "NONE";
	}

	@Override
	public void onDeath(Unit killedUnit, int killingUnit) {
		// The player's health is set to max and he is respawned somewhere.
		// Other units are simply removed.

		EventTriggerer triggerer = gameInfo.getEventTriggerer();

		if (killedUnit instanceof PlayerMainFigure) {
			PlayerMainFigure killedPlayer = (PlayerMainFigure) killedUnit;

			triggerer.respawnPlayerAtRandomSpawnpoint(killedPlayer.getPlayer());
		} else {
			triggerer.removeObject(killedUnit.getId());
		}
	}

	@Override
	public void onTimeUp() {

		// do nothing

	}

	@Override
	public void onConnect(int ownerId) {

		super.onConnect(ownerId);

		// simply create the player
		gameInfo.getEventTriggerer().createObject(ObjectType.PLAYER, ownerId);

	}

	@Override
	public void onGameStart() {

	}

	@Override
	public SpawnType getSpawnTypeForTeam(Team team) {
		return SpawnType.ANY;
	}

	@Override
	public GameModeNumber getNumber() {
		return GameModeNumber.NO_GAMEMODE;
	}

	@Override
	public Relation getRelation(GameObject a, GameObject b) {
		return Relation.HOSTILE;
	}

	@Override
	public void onDisconnect(int ownerId) {
		// simply remove the player
		gameInfo.getEventTriggerer().removePlayer(ownerId);
	}

	@Override
	public Team determineProgressingTeam(NeutralBase base, GameObject object,
			boolean objectEntered, Set<GameObject> presentObjects) {
		// don't care about neutral bases
		return null;
	}

	@Override
	public void onBaseOccupied(NeutralBase base, Team occupyingTeam) {
		// don't care about neutral bases
	}

	@Override
	public void onBaseLost(NeutralBase base, Team losingTeam) {
		// don't care about neutral bases
	}

	@Override
	public boolean supportsKeyBinding(KeyBinding binding) {
		return true;
	}

	@Override
	public boolean canSwitchMode(Player player, InteractMode mode) {
		return false;
	}

	@Override
	public void onGameEnd() {
	}

	@Override
	public boolean doItemsRespawn() {
		return true;
	}

	@Override
	public void onPlayerSpawn(Player player) {
	}
}
