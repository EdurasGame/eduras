package de.illonis.eduras.gameclient.gui.animation;

import de.illonis.eduras.gameclient.gui.animation.AnimationFactory.AnimationNumber;

/**
 * Indicates that an animation that there is no animation handler for an
 * animation that was tried to start.
 * 
 * @author illonis
 * 
 */
public class AnimationNotFoundException extends Exception {

	private static final long serialVersionUID = 1L;

	AnimationNotFoundException(AnimationNumber animation) {
		super("There is no animation case for animation with number "
				+ animation + ".");
	}
}
