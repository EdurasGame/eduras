package de.illonis.eduras.events;

/**
 * Indicates a loot of an item.
 * 
 * @author Florian Mai <florian.ren.mai@googlemail.com>
 * 
 */
public class LootItemEvent extends ObjectEvent {

	private final int playerId;

	/**
	 * Creates a new loot item events which tells that the player with
	 * 'playerId' wants to loot the item of id 'objectId'.
	 * 
	 * @param objectId
	 * @param playerId
	 */
	public LootItemEvent(int objectId, int playerId) {
		super(GameEventNumber.LOOT_ITEM_EVENT, objectId);
		this.playerId = playerId;
		putArgument(this.playerId);
	}

	/**
	 * @return the playerId
	 */
	public int getPlayerId() {
		return playerId;
	}
}
