/**
 * 
 */
package de.illonis.eduras.items.weapons;

import de.illonis.eduras.GameInformation;
import de.illonis.eduras.GameObject;
import de.illonis.eduras.ObjectFactory.ObjectType;
import de.illonis.eduras.Player;
import de.illonis.eduras.events.GameEvent.GameEventNumber;
import de.illonis.eduras.events.MovementEvent;
import de.illonis.eduras.events.ObjectFactoryEvent;
import de.illonis.eduras.events.SetGameObjectAttributeEvent;
import de.illonis.eduras.inventory.InventoryIsFullException;
import de.illonis.eduras.items.ItemUseInformation;
import de.illonis.eduras.logger.EduLog;
import de.illonis.eduras.math.Vector2D;
import de.illonis.eduras.shapes.Circle;

/**
 * @author Florian Mai <florian.ren.mai@googlemail.com>
 * 
 */
public class ExampleWeapon extends Weapon {

	/**
	 * Creates a new instance of the example weapon.
	 * 
	 * @param gi
	 */
	public ExampleWeapon(GameInformation gi) {

		super(ObjectType.ITEM_WEAPON_1, gi);
		setMissile(new SimpleMissile(gi));
		setName("WeaponExample");
		setShape(new Circle(10));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.illonis.eduras.items.Lootable#loot()
	 */
	@Override
	public void loot() {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.illonis.eduras.items.Usable#use(de.illonis.eduras.GameObject)
	 */
	@Override
	public void use(ItemUseInformation info) {

		Vector2D target = info.getTarget();
		GameObject triggeringObject = info.getTriggeringObject();

		Vector2D speedVector = new Vector2D(target);
		speedVector.subtract(triggeringObject.getPositionVector());

		// TODO: This needs to be solved in another way because this will make
		// the missile crash into the triggering object.

		ObjectFactoryEvent ofEvent = new ObjectFactoryEvent(
				GameEventNumber.OBJECT_CREATE, ObjectType.SIMPLEMISSILE);

		getGame().getOf().onGameEventAppeared(ofEvent);

		MovementEvent setPos = new MovementEvent(GameEventNumber.SET_POS,
				getId());

		setPos.setNewXPos(triggeringObject.getXPosition());
		setPos.setNewYPos(triggeringObject.getYPosition());

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.illonis.eduras.GameObject#onCollision(de.illonis.eduras.GameObject)
	 */
	@Override
	public void onCollision(GameObject collidingObject) {

		if (!(collidingObject.getType() == ObjectType.PLAYER)) {
			return;
		}

		Player player = (Player) collidingObject;
		try {
			player.getInventory().loot(this);
			this.setVisible(false);
			this.setCollidable(false);
			SetGameObjectAttributeEvent visibleEvent = new SetGameObjectAttributeEvent(
					GameEventNumber.SET_VISIBLE, this.getId(), false);
			SetGameObjectAttributeEvent collidableEvent = new SetGameObjectAttributeEvent(
					GameEventNumber.SET_COLLIDABLE, this.getId(), false);
			getGame().getOf().onObjectAttributeChanged(collidableEvent);
			getGame().getOf().onObjectAttributeChanged(visibleEvent);
		} catch (InventoryIsFullException e) {
			EduLog.passException(e);
			return;
		}

	}
}
