package de.illonis.eduras.actions;

import org.newdawn.slick.geom.Vector2f;

import de.illonis.eduras.GameInformation;
import de.illonis.eduras.ObjectFactory.ObjectType;
import de.illonis.eduras.Player;
import de.illonis.eduras.logic.EventTriggerer;
import de.illonis.eduras.logic.delayedactions.RemoveObjectLaterAction;
import de.illonis.eduras.settings.S;

/**
 * A scout spell action gives the executing player's team vision at the
 * specified location.
 * 
 * @author Florian 'Ren' Mai <florian.ren.mai@googlemail.com>
 * 
 */
public class ScoutSpellAction extends RTSAction {

	private final Vector2f target;

	/**
	 * Create the action.
	 * 
	 * @param executingPlayer
	 *            The player to execute the action
	 * @param target
	 *            the location to give vision at
	 */
	public ScoutSpellAction(Player executingPlayer, Vector2f target) {
		super(executingPlayer, S.spell_scout_costs);
		this.target = target;
	}

	@Override
	protected void executeAction(GameInformation info) {
		EventTriggerer triggerer = info.getEventTriggerer();
		int objectId = triggerer.createObjectAt(ObjectType.SPELL_SCOUT, target,
				executingPlayer.getPlayerId());

		RemoveObjectLaterAction action = new RemoveObjectLaterAction(
				info.findObjectById(objectId), S.spell_scout_duration);
		action.schedule();
	}
}
