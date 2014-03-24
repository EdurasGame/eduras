package de.illonis.eduras.logic;

import de.illonis.eduras.GameInformation;
import de.illonis.eduras.events.GameEvent.GameEventNumber;
import de.illonis.eduras.events.SetBooleanGameObjectAttributeEvent;
import de.illonis.eduras.gameobjects.GameObject;
import de.illonis.eduras.gameobjects.MoveableGameObject;
import de.illonis.eduras.interfaces.GameEventListener;
import de.illonis.eduras.items.Lootable;
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
		for (GameObject o : gameInformation.getObjects().values()) {
			if (o instanceof Usable) {
				((Usable) o).reduceCooldown(delta);
			}
			if (o instanceof Lootable) {
				boolean rs = ((Lootable) o).reduceRespawnRemaining(delta);
				if (rs) {
					SetBooleanGameObjectAttributeEvent sc = new SetBooleanGameObjectAttributeEvent(
							GameEventNumber.SET_COLLIDABLE, o.getId(), true);
					SetBooleanGameObjectAttributeEvent sv = new SetBooleanGameObjectAttributeEvent(
							GameEventNumber.SET_VISIBLE, o.getId(), true);
					o.setCollidable(true);
					o.setVisible(true);
					if (listenerHolder.hasListener()) {
						listenerHolder.getListener().onObjectStateChanged(sv);
						listenerHolder.getListener().onObjectStateChanged(sc);

					}
				}
			}
			if (o instanceof MoveableGameObject) {
				MoveableGameObject mgo = (MoveableGameObject) o;
				if (mgo.getSpeedVector().x > 0 || mgo.getSpeedVector().y > 0)
					mgo.onMove(delta);
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
