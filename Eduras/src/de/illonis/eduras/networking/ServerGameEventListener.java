package de.illonis.eduras.networking;

import java.util.ArrayList;

import de.illonis.eduras.events.ClientRenameEvent;
import de.illonis.eduras.events.GameEvent;
import de.illonis.eduras.events.GameEvent.GameEventNumber;
import de.illonis.eduras.events.MatchEndEvent;
import de.illonis.eduras.events.MovementEvent;
import de.illonis.eduras.events.ObjectFactoryEvent;
import de.illonis.eduras.events.SetGameObjectAttributeEvent;
import de.illonis.eduras.events.SetIntegerGameObjectAttributeEvent;
import de.illonis.eduras.events.SetItemSlotEvent;
import de.illonis.eduras.events.SetOwnerEvent;
import de.illonis.eduras.exceptions.MessageNotSupportedException;
import de.illonis.eduras.gamemodes.GameMode;
import de.illonis.eduras.gameobjects.GameObject;
import de.illonis.eduras.interfaces.GameEventListener;
import de.illonis.eduras.logger.EduLog;

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
	private final Server server;

	/**
	 * Creates a new ServerGameEventListener with the given outputBuffer.
	 * 
	 * @param outputBuffer
	 *            The outputBuffer to pass events to.
	 * @param serverSender
	 *            The sender that is used to send messages.
	 * @param server
	 *            The associated server.
	 */
	public ServerGameEventListener(Buffer outputBuffer,
			ServerSender serverSender, Server server) {
		this.outputBuffer = outputBuffer;
		this.serverSender = serverSender;
		this.server = server;
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
			EduLog.passException(e);
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

		try {
			String str = NetworkMessageSerializer.serialize(event);
			outputBuffer.append(str);
		} catch (MessageNotSupportedException e) {
			EduLog.passException(e);
			return;
		}
	}

	@Override
	public void onClientRename(ClientRenameEvent event) {
		String string;
		try {
			string = NetworkMessageSerializer.serialize(event);
		} catch (MessageNotSupportedException e) {
			EduLog.passException(e);
			return;
		}
		outputBuffer.append(string);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.illonis.eduras.interfaces.GameEventListener#onObjectStateChanged(de
	 * .illonis.eduras.events.SetGameObjectAttributeEvent)
	 */
	@Override
	public void onObjectStateChanged(SetGameObjectAttributeEvent<?> event) {
		try {
			String string = NetworkMessageSerializer.serialize(event);
			outputBuffer.append(string);
		} catch (MessageNotSupportedException e) {
			EduLog.passException(e);
			return;
		}
	}

	@Override
	public void onHealthChanged(SetIntegerGameObjectAttributeEvent event) {
		try {
			String string = NetworkMessageSerializer.serialize(event);
			outputBuffer.append(string);
		} catch (MessageNotSupportedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onMaxHealthChanged(SetIntegerGameObjectAttributeEvent event) {
		try {
			String string = NetworkMessageSerializer.serialize(event);
			outputBuffer.append(string);
		} catch (MessageNotSupportedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onOwnerChanged(SetOwnerEvent event) {
		try {
			String string = NetworkMessageSerializer.serialize(event);
			outputBuffer.append(string);
		} catch (MessageNotSupportedException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void onItemSlotChanged(SetItemSlotEvent event) {
		try {
			String string = NetworkMessageSerializer.serialize(event);
			outputBuffer.append(string);
		} catch (MessageNotSupportedException e) {
			EduLog.passException(e);
			return;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.illonis.eduras.interfaces.GameEventListener#onObjectRemove(de.illonis
	 * .eduras.events.ObjectFactoryEvent)
	 */
	@Override
	public void onObjectRemove(ObjectFactoryEvent event) {

		try {
			String string = NetworkMessageSerializer.serialize(event);
			outputBuffer.append(string);
		} catch (MessageNotSupportedException e) {
			EduLog.passException(e);
			return;
		}
	}

	@Override
	public void onMatchEnd(MatchEndEvent event) {
		try {
			outputBuffer.append(NetworkMessageSerializer.serialize(event));
		} catch (MessageNotSupportedException e) {
			EduLog.passException(e);
		}
		server.stopServer();
	}

	@Override
	public void onGameModeChanged(GameMode newGameMode) {
	}

}