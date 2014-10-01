package de.illonis.eduras.actions;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.newdawn.slick.geom.Vector2f;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.GameInformation;
import de.illonis.eduras.ObjectFactory.ObjectType;
import de.illonis.eduras.Player;
import de.illonis.eduras.exceptions.ObjectNotFoundException;
import de.illonis.eduras.exceptions.PlayerHasNoTeamException;
import de.illonis.eduras.logic.EventTriggerer;
import de.illonis.eduras.logic.delayedactions.RemoveObjectLaterAction;
import de.illonis.eduras.settings.S;
import de.illonis.eduras.units.Unit;

/**
 * A scout spell action gives the executing player's team vision at the
 * specified location.
 * 
 * @author Florian 'Ren' Mai <florian.ren.mai@googlemail.com>
 * 
 */
public class ScoutSpellAction extends RTSAction {

	private static final Logger L = EduLog.getLoggerFor(ScoutSpellAction.class
			.getName());

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
		super(executingPlayer, S.Server.spell_scout_costs);
		this.target = target;
	}

	@Override
	protected void executeAction(GameInformation info) {
		EventTriggerer triggerer = info.getEventTriggerer();
		int objectId = triggerer.createObjectAt(ObjectType.SPELL_SCOUT, target,
				executingPlayer.getPlayerId());
		try {
			triggerer.setTeamOfUnit((Unit) info.findObjectById(objectId),
					executingPlayer.getTeam());
		} catch (ObjectNotFoundException | PlayerHasNoTeamException e1) {
			L.log(Level.WARNING,
					"Exception when trying to set team of spell_scout", e1);
			return;
		}

		RemoveObjectLaterAction action;
		try {
			action = new RemoveObjectLaterAction(info.findObjectById(objectId),
					S.Server.spell_scout_duration);
		} catch (ObjectNotFoundException e) {
			L.log(Level.WARNING,
					"Cannot find scout object that was just created!", e);
			return;
		}
		action.schedule();
	}
}
