package de.illonis.eduras.logic;

import de.illonis.eduras.GameInformation;
import de.illonis.eduras.ai.movement.MotionAIControllable;
import de.illonis.eduras.ai.movement.MovingUnitAI;
import de.illonis.eduras.ai.movement.UnitMover;
import de.illonis.eduras.events.GameEvent.GameEventNumber;
import de.illonis.eduras.events.SetBooleanGameObjectAttributeEvent;
import de.illonis.eduras.events.SetVisibilityEvent;
import de.illonis.eduras.gameobjects.GameObject;
import de.illonis.eduras.gameobjects.GameObject.Visibility;
import de.illonis.eduras.gameobjects.MoveableGameObject;
import de.illonis.eduras.interfaces.GameEventListener;
import de.illonis.eduras.items.Respawnable;
import de.illonis.eduras.items.Usable;

/**
 * The {@link LogicGameWorker} on server side.
 * 
 * @author illonis
 * 
 */
public class ServerLogicGameWorker extends LogicGameWorker {

	protected ServerLogicGameWorker(GameInformation gameInfo,
			ListenerHolder<? extends GameEventListener> listenerHolder) {
		super(gameInfo, listenerHolder);
	}

	@Override
	public void gameUpdate(long delta) {

		if (gameInformation.getGameSettings().getRemainingTime() <= 0) {
			gameInformation.getGameSettings().getGameMode().onTimeUp();
			gameInformation.getGameSettings().resetRemainingTime();
		}

		for (DelayedLogicAction action : DelayedActionQueue.getActions()) {
			if (action.timePassed(delta))
				action.performAction(gameInformation);
		}

		for (GameObject o : gameInformation.getObjects().values()) {
			if (o instanceof Usable) {
				((Usable) o).reduceCooldown(delta);
			}
			if (o instanceof Respawnable) {
				boolean rs = ((Respawnable) o).reduceRespawnRemaining(delta);
				if (rs) {
					SetBooleanGameObjectAttributeEvent sc = new SetBooleanGameObjectAttributeEvent(
							GameEventNumber.SET_COLLIDABLE, o.getId(), true);
					SetVisibilityEvent sv = new SetVisibilityEvent(o.getId(),
							Visibility.ALL);
					if (listenerHolder.hasListener()) {
						listenerHolder.getListener().onVisibilityChanged(sv);
						listenerHolder.getListener().onObjectStateChanged(sc);
					}
					gameInformation.getEventTriggerer().setCollidability(
							o.getId(), true);
					gameInformation.getEventTriggerer().setVisibility(
							o.getId(), Visibility.ALL);
				}
			}
			if (o instanceof MoveableGameObject) {
				MoveableGameObject mgo = (MoveableGameObject) o;
				synchronized (o) {
					mgo.onMove(delta, geometry);
				}
				if (o instanceof MotionAIControllable) {
					MovingUnitAI ai = (MovingUnitAI) ((MotionAIControllable) o)
							.getAI();
					UnitMover mover = ai.getMover();
					if (mover.isActive())
						mover.check();
				}
				if (listenerHolder.hasListener())
					listenerHolder.getListener().onNewObjectPosition(o);
				gameInformation.getEventTriggerer().notifyNewObjectPosition(o);
			}

			if (hasRotated(o)) {
				gameInformation.getEventTriggerer().setRotation(o);
			}

		}
	}
}
