package de.illonis.eduras.actions;

import java.util.logging.Logger;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.GameInformation;
import de.illonis.eduras.Player;
import de.illonis.eduras.gameobjects.NeutralBase;
import de.illonis.eduras.settings.S;

public class RespawnPlayerAction extends RTSAction {

	private Player playerToRespawn;
	private NeutralBase base;

	public RespawnPlayerAction(Player executingPlayer, Player playerToRespawn,
			NeutralBase base) {
		super(executingPlayer, S.gm_edura_action_respawnplayer_cost);

		this.playerToRespawn = playerToRespawn;
		this.base = base;
	}

	private final static Logger L = EduLog
			.getLoggerFor(RespawnPlayerAction.class.getName());

	@Override
	public void executeAction(GameInformation info) {

		if (!playerToRespawn.getTeam().equals(base.getCurrentOwnerTeam())) {
			// should already be caught on client
			L.warning("Trying to respawn player at a base that isn't owned by the players team!");
			return;
		}
		info.getEventTriggerer().respawnPlayerAtPosition(playerToRespawn,
				base.getPositionVector());
	}
}
