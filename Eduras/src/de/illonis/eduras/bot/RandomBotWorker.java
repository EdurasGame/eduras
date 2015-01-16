package de.illonis.eduras.bot;

import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.newdawn.slick.geom.Vector2f;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.events.GameEvent;
import de.illonis.eduras.events.GameEvent.GameEventNumber;
import de.illonis.eduras.events.ItemEvent;
import de.illonis.eduras.events.MovementEvent;
import de.illonis.eduras.exceptions.MessageNotSupportedException;
import de.illonis.eduras.exceptions.ObjectNotFoundException;
import de.illonis.eduras.exceptions.WrongEventTypeException;
import de.illonis.eduras.inventory.Inventory;
import de.illonis.eduras.inventory.ItemSlotIsEmptyException;
import de.illonis.eduras.items.Item;
import de.illonis.eduras.logicabstraction.EventSender;
import de.illonis.eduras.logicabstraction.InformationProvider;

/**
 * The random bot worker generates random input.
 * 
 * @author Florian 'Ren' Mai <florian.ren.mai@googlemail.com>
 * 
 */
public class RandomBotWorker implements EdurasBotWorker {
	private final static Logger L = EduLog.getLoggerFor(RandomBotWorker.class
			.getName());
	private final Random random = new Random(System.currentTimeMillis());

	private static final long INPUT_INTERVAL = 250;

	private InformationProvider infoProvider;
	private EventSender eventSender;

	private GameEventNumber moveGameNumber;

	@Override
	public void run() {
		while (true) {
			try {
				if (!infoProvider.getPlayer().isDead()) {
					GameEvent input = createRandomInput();
					if (input != null) {
						try {
							eventSender.sendEvent(input);
							L.info("Event sent: " + input);
						} catch (WrongEventTypeException
								| MessageNotSupportedException e) {
							L.log(Level.SEVERE, "Couldn't send event!", e);
						}
					}
				}
			} catch (ObjectNotFoundException e1) {
				L.log(Level.SEVERE, "Could not find player", e1);
			}
			try {
				Thread.sleep(INPUT_INTERVAL);
			} catch (InterruptedException e) {
				L.log(Level.WARNING, "Bot got interrupted", e);
			}
		}
	}

	private GameEvent createRandomInput() {
		int inputChooser = random.nextInt(4);
		switch (inputChooser) {
		case 0: {
			return startMoving();
		}
		case 1: {
			return stopMoving();
		}
		case 2: {
			return useItem();
		}
		case 3: {
			return setRotation();
		}
		default:
			break;
		}

		// just don't do anything in default case
		return null;
	}

	private GameEvent setRotation() {
		try {
			float newRotation = random.nextFloat() * 360;
			infoProvider.getPlayer().getPlayerMainFigure()
					.setRotation(newRotation);
			L.info("Set rotation to " + newRotation);
		} catch (ObjectNotFoundException e) {
			L.log(Level.SEVERE, "Cannot find player!", e);
		}
		return null;
	}

	private GameEvent useItem() {
		int numberOfItems = 0;
		Inventory inventory;
		try {
			inventory = infoProvider.getPlayer().getInventory();
		} catch (ObjectNotFoundException e) {
			L.log(Level.SEVERE, "Cannot find the player!!", e);
			return null;
		}
		numberOfItems = inventory.getNumItems();

		if (numberOfItems == 0) {
			// don't use items because we don't have any
			return null;
		}
		int slotNumber;
		Item item;
		do {
			slotNumber = random.nextInt(Inventory.MAX_CAPACITY);
			try {
				item = inventory.getItemAt(slotNumber);
			} catch (ItemSlotIsEmptyException e) {
				item = null;
			}
		} while (item == null);

		ItemEvent itemEvent = new ItemEvent(GameEventNumber.ITEM_USE,
				infoProvider.getOwnerID(), item.getType());

		// also set the target randomly
		itemEvent.setTarget(new Vector2f(random.nextFloat()
				* infoProvider.getMapBounds().getWidth(), random.nextFloat()
				* infoProvider.getMapBounds().getHeight()));
		return itemEvent;
	}

	private GameEvent stopMoving() {
		if (moveGameNumber == null) {
			return null;
		}
		GameEventNumber stopMoveDirection;
		switch (moveGameNumber) {
		case MOVE_UP_PRESSED:
			stopMoveDirection = GameEventNumber.MOVE_UP_RELEASED;
			break;
		case MOVE_DOWN_PRESSED:
			stopMoveDirection = GameEventNumber.MOVE_DOWN_RELEASED;
			break;
		case MOVE_RIGHT_PRESSED:
			stopMoveDirection = GameEventNumber.MOVE_RIGHT_RELEASED;
			break;
		case MOVE_LEFT_PRESSED:
			stopMoveDirection = GameEventNumber.MOVE_LEFT_RELEASED;
			break;
		default:
			stopMoveDirection = GameEventNumber.MOVE_UP_RELEASED;
			break;
		}
		moveGameNumber = null;
		return new MovementEvent(stopMoveDirection, infoProvider.getOwnerID());
	}

	private GameEvent startMoving() {
		if (moveGameNumber == null) {
			int directionChooser = random.nextInt(4);
			switch (directionChooser) {
			case 0:
				moveGameNumber = GameEventNumber.MOVE_UP_PRESSED;
				break;
			case 1:
				moveGameNumber = GameEventNumber.MOVE_DOWN_PRESSED;
				break;
			case 2:
				moveGameNumber = GameEventNumber.MOVE_LEFT_PRESSED;
				break;
			case 3:
				moveGameNumber = GameEventNumber.MOVE_RIGHT_PRESSED;
				break;
			default:
				moveGameNumber = GameEventNumber.MOVE_UP_PRESSED;
				break;
			}
			return new MovementEvent(moveGameNumber, infoProvider.getOwnerID());
		}
		return null;
	}

	@Override
	public void setEventSender(EventSender eventSender) {
		this.eventSender = eventSender;
	}

	@Override
	public void setInformationProvider(InformationProvider infoProvider) {
		this.infoProvider = infoProvider;
	}

}
