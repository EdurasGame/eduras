package de.illonis.eduras;

import java.util.Set;

import de.illonis.eduras.gameclient.userprefs.KeyBindings.KeyBinding;
import de.illonis.eduras.gamemodes.GameMode;
import de.illonis.eduras.gameobjects.GameObject;
import de.illonis.eduras.gameobjects.GameObject.Relation;
import de.illonis.eduras.gameobjects.NeutralBase;
import de.illonis.eduras.maps.SpawnPosition.SpawnType;
import de.illonis.eduras.units.Unit;

/**
 * This is a dummy class for the client which does nothing, because all the
 * action needed here shall be performed on the server.
 * 
 * @author Florian Mai <florian.ren.mai@googlemail.com>
 * 
 */
public class ClientGameMode implements GameMode {

	@Override
	public String getName() {
		return "";
	}

	@Override
	public void onDeath(Unit killedUnit, int killingUnit) {
	}

	@Override
	public void onTimeUp() {
	}

	@Override
	public void onConnect(int ownerId) {
	}

	@Override
	public void onGameStart() {
	}

	@Override
	public SpawnType getSpawnTypeForTeam(Team team) {
		return null;
	}

	@Override
	public GameModeNumber getNumber() {
		return GameModeNumber.NO_GAMEMODE;
	}

	@Override
	public Relation getRelation(GameObject a, GameObject b) {
		return Relation.UNKNOWN;
	}

	@Override
	public void onDisconnect(int ownerId) {
	}

	@Override
	public Team determineProgressingTeam(GameObject object,
			boolean objectEntered, Set<GameObject> presentObjects) {
		return null;
	}

	@Override
	public void onBaseOccupied(NeutralBase base, Team occupyingTeam) {
	}

	@Override
	public void onBaseLost(NeutralBase base, Team losingTeam) {
	}

	@Override
	public boolean supportsKeyBinding(KeyBinding binding) {
		return false;
	}

}
