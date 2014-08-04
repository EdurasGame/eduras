package de.illonis.eduras.actions;

import java.util.logging.Logger;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.GameInformation;
import de.illonis.eduras.Player;
import de.illonis.eduras.Team;
import de.illonis.eduras.logic.EventTriggerer;

/**
 * This class represents an abstract action that can be performed by a player in
 * strategy mode. Performing an action always consumes resources. If the
 * player's team doesn't have a sufficient resource count, the action isn't
 * performed. If there are enough resources the action's costs are deducted from
 * the player's team's resource balance and the action is performed.
 * 
 * @author Florian 'Ren' Mai <florian.ren.mai@googlemail.com>
 * 
 */
public abstract class RTSAction {

	protected int costs;
	protected Player executingPlayer;

	private final static Logger L = EduLog.getLoggerFor(RTSAction.class
			.getName());

	/**
	 * Create a new action that is performed by the given player and consumes
	 * the given amount of resource costs.
	 * 
	 * @param executingPlayer
	 * @param costs
	 */
	public RTSAction(Player executingPlayer, int costs) {
		this.costs = costs;
		this.executingPlayer = executingPlayer;
	}

	/**
	 * Checks if the executing player's team has sufficient resources to perform
	 * the action. If so, the action is performed.
	 * 
	 * @param info
	 */
	public void execute(GameInformation info) {
		Team executingTeam = executingPlayer.getTeam();

		if (executingTeam.getResource() >= costs) {
			// TODO: If executeAction failed, refund costs.
			EventTriggerer eventTriggerer = info.getEventTriggerer();
			eventTriggerer.changeResourcesOfTeamByAmount(executingTeam, -costs);
			executeAction(info);
		} else {
			// should be caught on client already
			L.warning("Received an RTSAction although the executing team doesn't have sufficient resources.");
		}
	}

	protected abstract void executeAction(GameInformation info);

	/**
	 * Returns the costs that are deducted when performing the action.
	 * 
	 * @return costs
	 */
	public int getCosts() {
		return costs;
	}

	/**
	 * Returns the player who executes the action.
	 * 
	 * @return player
	 */
	public Player getExecutingPlayer() {
		return executingPlayer;
	}
}
