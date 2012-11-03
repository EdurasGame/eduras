/**
 * 
 */
package de.illonis.eduras.networking;

import java.util.ArrayList;

import de.illonis.eduras.GameObject;
import de.illonis.eduras.events.GameEvent;
import de.illonis.eduras.events.GameEvent.GameEventNumber;
import de.illonis.eduras.events.MovementEvent;
import de.illonis.eduras.events.ObjectFactoryEvent;
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
	private final ServerSender serverSender;

	/**
	 * Creates a new ServerGameEventListener with the given outputBuffer.
	 * 
	 * @param outputBuffer
	 *            The outputBuffer to pass events to.
	 * @param serverSender
	 *            The sender that is used to send messages.
	 */
	public ServerGameEventListener(Buffer outputBuffer, ServerSender serverSender) {
		this.outputBuffer = outputBuffer;
		this.serverSender = serverSender;
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

		moveEvent = new MovementEvent(GameEventNumber.SET_POS, o.getId());
		moveEvent.setNewXPos(o.getXPosition());
		moveEvent.setNewYPos(o.getYPosition());
		try {
			String msg = NetworkMessageSerializer.serialize(moveEvent);
			outputBuffer.append(msg);
		} catch (MessageNotSupportedException e) {
			e.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.illonis.eduras.interfaces.GameEventListener#onInformationRequested
	 * (java.util.ArrayList)
	 */
	@Override
	public void onInformationRequested(ArrayList<GameEvent> infos, int owner) {

		for (GameEvent event : infos) {
			String msg;
			try {
				msg = NetworkMessageSerializer.serialize(event);
			} catch (MessageNotSupportedException e) {
				continue;
			}
			serverSender.sendMessageToClient(owner, msg);
		}
	}

	@Override
	public void onObjectCreation(ObjectFactoryEvent event) {
		String str;
		try {
			str = NetworkMessageSerializer.serialize(event);
		} catch (MessageNotSupportedException e) {
			e.printStackTrace();
			return;
		}
		outputBuffer.append(str);

	}
}