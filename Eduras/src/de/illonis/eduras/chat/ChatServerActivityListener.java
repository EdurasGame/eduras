package de.illonis.eduras.chat;

public interface ChatServerActivityListener {

	public void onUserConnected(ChatUser user);

	public void onUserDisconnected(ChatUser user);

}
