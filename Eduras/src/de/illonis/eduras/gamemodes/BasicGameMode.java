package de.illonis.eduras.gamemodes;

import de.illonis.eduras.GameInformation;
import de.illonis.eduras.Team;
import de.illonis.eduras.exceptions.NoSuchGameModeException;
import de.illonis.eduras.gameobjects.GameObject;
import de.illonis.eduras.gameobjects.GameObject.Relation;
import de.illonis.eduras.maps.SpawnPosition.SpawnType;
import de.illonis.eduras.units.Unit;

/**
 * The BasicGameMode defines behavior that applies to all game modes.
 * 
 * @author Florian Mai <florian.ren.mai@googlemail.com>
 * 
 */
public abstract class BasicGameMode implements GameMode {

	protected GameInformation gameInfo;

	/**
	 * Creates a GameMode with the given game information.
	 * 
	 * @param gameInfo
	 */
	public BasicGameMode(GameInformation gameInfo) {
		this.gameInfo = gameInfo;
	}

	@Override
	public abstract GameModeNumber getNumber();

	@Override
	public abstract String getName();

	@Override
	public abstract Relation getRelation(GameObject a, GameObject b);

	@Override
	public abstract void onDeath(Unit killedUnit, int killingPlayer);

	@Override
	public abstract void onTimeUp();

	@Override
	public void onConnect(int ownerId) {
		if (gameInfo.getPlayers().size() >= gameInfo.getGameSettings()
				.getMaxPlayers()) {
			gameInfo.getEventTriggerer().kickPlayer(ownerId);
		}
	}

	@Override
	public abstract void onGameStart();

	@Override
	public abstract SpawnType getSpawnTypeForTeam(Team team);

	/**
	 * 
	 * @param gameModeName
	 * @param gameInfo
	 * @return The gamemode, if existing.
	 * @throws NoSuchGameModeException
	 *             Thrown if there is no such GameMode of the given name.
	 */
	public static GameMode getGameModeByName(String gameModeName,
			GameInformation gameInfo) throws NoSuchGameModeException {
		switch (gameModeName.toLowerCase()) {
		case "deathmatch":
			return new Deathmatch(gameInfo);
		case "teamdeathmatch":
			return new TeamDeathmatch(gameInfo);
		case "kingofthehill":
			return new KingOfTheHill(gameInfo);
		default:
			throw new NoSuchGameModeException(gameModeName);
		}
	}

}
