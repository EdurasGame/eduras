package de.illonis.eduras.actions;

import java.util.logging.Level;
import java.util.logging.Logger;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.GameInformation;
import de.illonis.eduras.Player;
import de.illonis.eduras.Team;
import de.illonis.eduras.exceptions.PlayerHasNoTeamException;
import de.illonis.eduras.gameobjects.Base;
import de.illonis.eduras.logic.EventTriggerer;
import de.illonis.eduras.settings.S;
import de.illonis.eduras.units.InteractMode;

/**
 * A RespawnPlayerAction respawns a dead player at a neutral base of the
 * player's team.
 * 
 * @author Florian 'Ren' Mai <florian.ren.mai@googlemail.com>
 * 
 */
public class RespawnPlayerAction extends RTSAction {

	private Player playerToRespawn;
	private Base base;

	/**
	 * Create a new RespawnPlayerAction.
	 * 
	 * @param executingPlayer
	 *            the player to execute the respawning of a player
	 * @param playerToRespawn
	 *            the player to be respawned
	 * @param base
	 *            the base to respawn the player at
	 */
	public RespawnPlayerAction(Player executingPlayer, Player playerToRespawn,
			Base base) {
		super(executingPlayer, S.Server.gm_edura_action_respawnplayer_cost);

		this.playerToRespawn = playerToRespawn;
		this.base = base;
	}

	private final static Logger L = EduLog
			.getLoggerFor(RespawnPlayerAction.class.getName());

	@Override
	public void executeAction(GameInformation info) {

		Team teamOfPlayer;
		try {
			teamOfPlayer = playerToRespawn.getTeam();
		} catch (PlayerHasNoTeamException e) {
			L.log(Level.SEVERE, "Player MUST have a team at this point", e);
			return;
		}

		if (!teamOfPlayer.equals(base.getCurrentOwnerTeam())) {
			// should already be caught on client
			L.warning("Trying to respawn player at a base that isn't owned by the players team!");
			return;
		}

		EventTriggerer eventTriggerer = info.getEventTriggerer();

		eventTriggerer.respawnPlayerAtPosition(playerToRespawn,
				base.getPositionVector());
		eventTriggerer.changeInteractMode(playerToRespawn.getPlayerId(),
				InteractMode.MODE_EGO);
	}
}
