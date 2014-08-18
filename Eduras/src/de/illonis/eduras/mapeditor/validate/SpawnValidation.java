package de.illonis.eduras.mapeditor.validate;

import de.illonis.eduras.maps.SpawnPosition;

/**
 * Validates spawn areas.
 * 
 * @author illonis
 * 
 */
public class SpawnValidation extends ValidateTask {

	protected SpawnValidation() {
		super("Spawn points");
	}

	@Override
	protected boolean performValidation() {
		boolean ok = true;
		if (data.getSpawnPoints().isEmpty()) {
			addErrorMessage("Map must at least have one spawnpoint.");
			ok = false;
		} else {
			boolean spawnsOk = false;
			boolean teamA = false;
			boolean teamB = false;
			spawnLoop: for (SpawnPosition spawn : data.getSpawnPoints()) {
				switch (spawn.getTeaming()) {
				case ANY:
				case SINGLE:
					spawnsOk = true;
					break spawnLoop;
				case TEAM_A:
					teamA = true;
					break;
				case TEAM_B:
					teamB = true;
					break;
				default:
					break spawnLoop;
				}
			}
			if (!(spawnsOk || (teamA && teamB))) {
				ok = false;
				addErrorMessage("One common spawnpoint or a point for each team is required.");
			}
		}

		return ok;
	}
}
