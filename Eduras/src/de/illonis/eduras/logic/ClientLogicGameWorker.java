package de.illonis.eduras.logic;

import de.illonis.eduras.GameInformation;
import de.illonis.eduras.gameobjects.GameObject;
import de.illonis.eduras.gameobjects.MoveableGameObject;
import de.illonis.eduras.interfaces.GameEventListener;
import de.illonis.eduras.items.Usable;

/**
 * The {@link LogicGameWorker} on client side.
 * 
 * @author illonis
 * 
 */
public class ClientLogicGameWorker extends LogicGameWorker {

	protected ClientLogicGameWorker(GameInformation gameInfo,
			ListenerHolder<? extends GameEventListener> listenerHolder) {
		super(gameInfo, listenerHolder);
	}

	@Override
	protected void gameUpdate(long delta) {

		for (GameObject o : gameInformation.getObjects().values()) {
			if (o instanceof Usable) {
				((Usable) o).reduceCooldown(delta);
			}
			if (o instanceof MoveableGameObject) {
				if (!((MoveableGameObject) o).getSpeedVector().isNull()) {
					((MoveableGameObject) o).onMove(delta);
					if (listenerHolder.hasListener())
						listenerHolder.getListener().onNewObjectPosition(o);
					gameInformation.getEventTriggerer()
							.notifyNewObjectPosition(o);
				}

				if (hasRotated(o)) {
					gameInformation.getEventTriggerer().setRotation(o);
				}
			}
		}
		updateVision();
	}

	private void updateVision() {
		
		// TODO Auto-generated method stub
		
	}

}
