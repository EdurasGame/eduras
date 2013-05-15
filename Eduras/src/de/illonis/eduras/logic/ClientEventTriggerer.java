package de.illonis.eduras.logic;

import de.illonis.eduras.ObjectFactory.ObjectType;
import de.illonis.eduras.gamemodes.GameMode;
import de.illonis.eduras.items.Item;
import de.illonis.eduras.math.Vector2D;
import de.illonis.eduras.units.Player;

/**
 * @author Florian Mai <florian.ren.mai@googlemail.com>
 * 
 */
public class ClientEventTriggerer implements EventTriggerer {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.illonis.eduras.logic.EventTriggerer#createMissile(de.illonis.eduras
	 * .ObjectFactory.ObjectType, int, de.illonis.eduras.math.Vector2D,
	 * de.illonis.eduras.math.Vector2D)
	 */
	@Override
	public void createMissile(ObjectType missileType, int owner,
			Vector2D position, Vector2D speedVector) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.illonis.eduras.logic.EventTriggerer#removeObject(int)
	 */
	@Override
	public void removeObject(int objectId) {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.illonis.eduras.logic.EventTriggerer#createObjectAt(de.illonis.eduras
	 * .ObjectFactory.ObjectType, de.illonis.eduras.math.Vector2D, int)
	 */
	@Override
	public void createObjectAt(ObjectType object, Vector2D position, int owner) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.illonis.eduras.logic.EventTriggerer#lootItem(int, int)
	 */
	@Override
	public void lootItem(int objectId, int playerId) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.illonis.eduras.logic.EventTriggerer#setPositionOfObject(int,
	 * de.illonis.eduras.math.Vector2D)
	 */
	@Override
	public void setPositionOfObject(int objectId, Vector2D newPosition) {

	}

	@Override
	public void init() {
	}

	@Override
	public void createObject(ObjectType object, int owner) {
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
}
