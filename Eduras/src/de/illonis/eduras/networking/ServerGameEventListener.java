/**
 * 
 */
package de.illonis.eduras.networking;

import java.util.ArrayList;

import de.illonis.eduras.GameObject;
import de.illonis.eduras.events.GameEvent;
import de.illonis.eduras.events.GameEvent.GameEventNumber;
import de.illonis.eduras.events.MovementEvent;
import de.illonis.eduras.exceptions.MessageNotSupportedException;
import de.illonis.eduras.interfaces.GameEventListener;

/**
 * This class implements {@link GameEventListener}. Basically it generates
 * events to be send to the clients.
 * 
 * @author Florian Mai <florian.ren.mai@googlemail.com>
 * 
 */
public class ServerGameEventListener implements GameEventListener {

	private final Buffer outputBuffer;

	/**
	 * Creates a new ServerGameEventListener with the given outputBuffer.
	 * 
	 * @param outputBuffer
	 *            The outputBuffer to pass events to.
	 */
	public ServerGameEventListener(Buffer outputBuffer) {
		this.outputBuffer = outputBuffer;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.illonis.eduras.interfaces.GameEventListener#onNewObjectPosition(de
	 * .illonis.eduras.GameObject)
	 */
	@Override
	public void onNewObjectPosition(GameObject o) {

		MovementEvent moveEvent;
		String msg = null;
		try {
			moveEvent = new MovementEvent(GameEventNumber.SET_POS, o.getId());
			NetworkMessageSerializer.serialize(moveEvent);
		} catch (Exception e) {
			e.printStackTrace();
		}

		outputBuffer.append(msg);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.illonis.eduras.interfaces.GameEventListener#onInformationRequested
	 * (java.util.ArrayList)
	 */
	@Override
	public void onInformationRequested(ArrayList<GameEvent> infos) {

		for (GameEvent event : infos) {
			String msg = null;
			try {
				msg = NetworkMessageSerializer.serialize(event);
			} catch (MessageNotSupportedException e) {
				continue;
			}
			outputBuffer.append(msg);
		}
	}

}
