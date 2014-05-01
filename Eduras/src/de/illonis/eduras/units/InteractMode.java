package de.illonis.eduras.units;

/**
 * Interaction modes where players can be in.
 * 
 * @author illonis
 * 
 */
@SuppressWarnings("javadoc")
public enum InteractMode {

	MODE_EGO(5000), MODE_STRATEGY(1000), MODE_DEAD(0);

	private final long coolDown; // in ms.

	private InteractMode(long coolDown) {
		this.coolDown = coolDown;
	}

	public long getCoolDown() {
		return coolDown;
	}

	public InteractMode next() {
		if (this == MODE_EGO)
			return MODE_STRATEGY;
		return MODE_EGO;
	}
}