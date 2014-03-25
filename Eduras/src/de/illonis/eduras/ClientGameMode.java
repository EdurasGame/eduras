package de.illonis.eduras;

import de.illonis.eduras.gamemodes.GameMode;
import de.illonis.eduras.gameobjects.GameObject;
import de.illonis.eduras.gameobjects.GameObject.Relation;
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

		// do nothing

	}

	@Override
	public void onTimeUp() {
		// do nothing

	}

	@Override
	public void onConnect(int ownerId) {
		// do nothing

	}

	@Override
	public void onGameStart() {
		// nothing

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
		// do nothing
	}

}
