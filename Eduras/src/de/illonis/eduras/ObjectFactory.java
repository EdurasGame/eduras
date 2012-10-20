package de.illonis.eduras;

import de.illonis.eduras.events.GameEvent;
import de.illonis.eduras.events.ObjectFactoryEvent;
import de.illonis.eduras.test.YellowCircle;

public class ObjectFactory implements GameLogicInterface {

	private Game game;

	public ObjectFactory(Game game) {
		this.game = game;
	}

	@Override
	public void onGameEventAppeared(GameEvent event) {
		// do not handle other events in case they are received.
		if (!(event instanceof ObjectFactoryEvent))
			return;
		ObjectFactoryEvent ofe = (ObjectFactoryEvent) event;
		GameObject go = null;
		switch (ofe.getType()) {
		case OBJECT_CREATE:
			switch (ofe.getObjectType()) {
			case PLAYER:
				go = new Player(game);

				break;
			case YELLOWCIRCLE:
				go = new YellowCircle(game);
				break;
			default:
				break;
			}
			break;
		case OBJECT_REMOVE:
			break;
		default:
			break;

		}

		// set id if id is set in event
		if (go != null && ofe.hasId()) {
			go.setId(ofe.getId());
		}

	}
}
