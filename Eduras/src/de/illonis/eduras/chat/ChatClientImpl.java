package de.illonis.eduras.chat;

import java.util.LinkedList;
import java.util.logging.Logger;

import de.eduras.eventingserver.Client;
import de.eduras.eventingserver.ClientInterface;
import de.eduras.eventingserver.ClientNetworkEventHandler;
import de.illonis.edulog.EduLog;
import de.illonis.eduras.EdurasServer;

class ChatClientImpl implements ChatClient {

	private final static Logger L = EduLog.getLoggerFor(EdurasServer.class
			.getName());

	private ClientInterface client;
	private String nickName;
	private boolean connected;

	public ChatClientImpl(String name) {
		nickName = name;
		client = new Client();
		client.setEventHandler(new ChatEventHandlerClient(this));
		client.setNetworkPolicy(new ChatPolicy());

		client.setNetworkEventHandler(new ClientNetworkEventHandler() {

			@Override
			public void onClientDisconnected(int clientId) {

			}

			@Override
			public void onClientConnected(int clientId) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onServerIsFull() {
				// TODO Auto-generated method stub

			}

			@Override
			public void onDisconnected() {
				// TODO Auto-generated method stub

			}

			@Override
			public void onConnectionLost() {
				// TODO Auto-generated method stub

			}

			@Override
			public void onClientKicked(int clientId, String reason) {
				// TODO Auto-generated method stub

			}
		});
	}

	@Override
	public void postChatMessage(String message, ChatRoom chatRoom) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setName(String newName) {
		// TODO Auto-generated method stub

	}

	@Override
	public void inviteUserToRoom(ChatUser user, ChatRoom chatRoom) {
		// TODO Auto-generated method stub

	}

	@Override
	public LinkedList<ChatRoom> getRooms() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void acceptInvite(Invite invite) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setChatActivityListener(ChatActivityListener listener) {
		// TODO Auto-generated method stub

	}

	@Override
	public void connect(String hostname, int port) {
		// TODO Auto-generated method stub

	}
}
