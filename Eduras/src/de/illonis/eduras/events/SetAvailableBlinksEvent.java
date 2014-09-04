package de.illonis.eduras.events;

import java.util.logging.Logger;

import de.illonis.edulog.EduLog;

public class SetAvailableBlinksEvent extends OwnerGameEvent {

	private final static Logger L = EduLog
			.getLoggerFor(SetAvailableBlinksEvent.class.getName());

	private int charges;

	public SetAvailableBlinksEvent(int owner, int charges) {
		super(GameEventNumber.SET_AVAILABLE_BLINKS, owner);

		putArgument(charges);
		this.charges = charges;
	}

	public int getCharges() {
		return charges;
	}
}
