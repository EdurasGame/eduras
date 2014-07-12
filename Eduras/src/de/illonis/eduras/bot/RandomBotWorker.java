package de.illonis.eduras.bot;

import java.util.logging.Level;
import java.util.logging.Logger;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.events.GameEvent;
import de.illonis.eduras.events.GameEvent.GameEventNumber;
import de.illonis.eduras.events.MovementEvent;
import de.illonis.eduras.exceptions.MessageNotSupportedException;
import de.illonis.eduras.exceptions.WrongEventTypeException;
import de.illonis.eduras.logicabstraction.EventSender;
import de.illonis.eduras.logicabstraction.InformationProvider;

public class RandomBotWorker implements EdurasBotWorker {
	private final static Logger L = EduLog.getLoggerFor(RandomBotWorker.class
			.getName());

	private InformationProvider infoProvider;
	private EventSender eventSender;

	private GameEventNumber moveGameNumber;

	@Override
	public void run() {
		while (true) {
			GameEvent input = createRandomInput();
			if (input != null) {
				try {
					eventSender.sendEvent(input);
					L.info("Event sent: " + input);
				} catch (WrongEventTypeException | MessageNotSupportedException e) {
					L.log(Level.SEVERE, "Couldn't send event!", e);
				}
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				L.log(Level.WARNING, "TODO: message", e);
			}
		}
	}

	private GameEvent createRandomInput() {
		int inputChooser = (int) (Math.random() * 2);
		switch (inputChooser) {
		case 0: {
			// start moving
			if (moveGameNumber == null) {
				int directionChooser = (int) (Math.random() * 4);
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
				return new MovementEvent(moveGameNumber,
						infoProvider.getOwnerID());
			}
		}
		case 1: {
			// stop moving
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
			return new MovementEvent(stopMoveDirection,
					infoProvider.getOwnerID());
		}
		default:
			break;
		}

		// just don't do anything in default case
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
