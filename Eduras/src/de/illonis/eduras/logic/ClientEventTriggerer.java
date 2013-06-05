package de.illonis.eduras.logic;

import de.illonis.eduras.ObjectFactory.ObjectType;
import de.illonis.eduras.gamemodes.GameMode;
import de.illonis.eduras.items.Item;
import de.illonis.eduras.maps.Map;
import de.illonis.eduras.math.Vector2D;
import de.illonis.eduras.units.Player;
import de.illonis.eduras.units.Unit;

/**
 * The client eventtriggerer does not do anything as client does not trigger
 * game-configuration-relevant events.
 * 
 * @author Florian Mai <florian.ren.mai@googlemail.com>
 * 
 */
public class ClientEventTriggerer implements EventTriggerer {

	@Override
	public void createMissile(ObjectType missileType, int owner,
			Vector2D position, Vector2D speedVector) {
	}

	@Override
	public void removeObject(int objectId) {
	}

	@Override
	public int createObjectAt(ObjectType object, Vector2D position, int owner) {
		return -1;
	}

	@Override
	public void lootItem(int objectId, int playerId) {
	}

	@Override
	public void setPositionOfObject(int objectId, Vector2D newPosition) {
	}

	@Override
	public void init() {
	}

	@Override
	public int createObject(ObjectType object, int owner) {
		return -1;
	}

	@Override
	public void setHealth(int id, int newHealth) {
	}

	@Override
	public void respawnPlayer(Player player) {
	}

	@Override
	public void renamePlayer(int ownerId, String newName) {
	}

	@Override
	public void onMatchEnd() {
	}

	@Override
	public void restartRound() {
	}

	@Override
	public void setRemainingTime(long remainingTime) {
	}

	@Override
	public void changeGameMode(GameMode newMode) {

	}

	@Override
	public void changeItemSlot(int slot, int player, Item newItem) {
	}

	@Override
	public void changeMap(Map map) {
	}

	@Override
	public void remaxHealth(Unit unit) {
	}

	@Override
	public void onDeath(Unit unit, int killer) {
	}
}
