package de.illonis.eduras.events;

import java.util.logging.Logger;

import org.newdawn.slick.geom.Vector2f;

import de.illonis.edulog.EduLog;

public class BlinkEvent extends OwnerGameEvent {

	private final static Logger L = EduLog.getLoggerFor(BlinkEvent.class
			.getName());

	private final Vector2f blinkTarget;

	public BlinkEvent(int owner, Vector2f blinkTarget) {
		super(GameEventNumber.BLINK, owner);
		putArgument(blinkTarget.x);
		putArgument(blinkTarget.y);

		this.blinkTarget = blinkTarget;
	}

	public Vector2f getBlinkTarget() {
		return blinkTarget;
	}

}
