package de.illonis.eduras.units;

import de.illonis.eduras.settings.S;

/**
 * Interaction modes where players can be in.
 * 
 * @author illonis
 * 
 */
@SuppressWarnings("javadoc")
public enum InteractMode {

	MODE_EGO, MODE_STRATEGY, MODE_DEAD;

	public InteractMode next() {
		if (this == MODE_EGO)
			return MODE_STRATEGY;
		return MODE_EGO;
	}

	public long getCoolDown() {
		switch (this) {
		case MODE_DEAD:
			return S.Server.sv_modeswitch_dead_cooldown;
		case MODE_EGO:
			return S.Server.sv_modeswitch_ego_cooldown;
		case MODE_STRATEGY:
			return S.Server.sv_modeswitch_rts_cooldown;
		default:
			return 0;
		}
	}
}