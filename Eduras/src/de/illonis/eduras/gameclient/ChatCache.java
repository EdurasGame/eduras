package de.illonis.eduras.gameclient;

import java.util.HashMap;
import java.util.LinkedList;

import de.illonis.eduras.chat.ChatMessage;
import de.illonis.eduras.chat.ChatRoom;
import de.illonis.eduras.chat.ChatUser;

public class ChatCache {

	private final LinkedList<ChatMessage> messages;
	private int self;
	private final HashMap<Integer, ChatUser> users;
	private ChatRoom currentRoom;
	private StringBuilder input;
	private boolean writing;

	private ChatUser getSelfUser() {
		return users.get(self);
	}

	public void write(char letter) {
		input.append(letter);
		startWriting();
	}

	public void deleteChar() {
		if (writing && input.length() > 0)
			input.deleteCharAt(input.length() - 1);
	}

	public String sendInput() {
		String text = input.toString();
		writing = false;
		stopWriting();
		return text;
	}

	public String getInput() {
		return input.toString();
	}

	public void stopWriting() {
		input = new StringBuilder();
		writing = false;
	}

	public ChatCache() {
		messages = new LinkedList<ChatMessage>();
		this.self = -1;
		input = new StringBuilder();
		users = new HashMap<Integer, ChatUser>();
		writing = false;
		if (!getSelfUser().getOccupiedRooms().isEmpty())
			currentRoom = getSelfUser().getOccupiedRooms().getFirst();
	}

	public void setSelf(ChatUser user) {
		self = user.getId();
		pushUser(user);
	}

	public boolean isWriting() {
		return writing;
	}

	public void pushMessage(ChatMessage message) {
		messages.add(message);
	}

	public String getRoomName() {
		return currentRoom.getName();
	}

	public void pushSystemMessage(String string) {
		pushSystemMessage(string, null);
	}

	public void pushSystemMessage(String string, ChatRoom chatRoom) {
		// TODO Auto-generated method stub

	}

	public void setCurrentRoom(ChatRoom chatRoom) {
		pushSystemMessage("You joined " + chatRoom.getName());
		currentRoom = chatRoom;
	}

	public void pushUser(ChatUser user) {
		users.put(user.getId(), user);
	}

	public void pushRoom(ChatRoom chatRoom) {
		// TODO Auto-generated method stub

	}

	public String popMessage() {
		return messages.size() + " messages";
	}

	public void startWriting() {
		writing = true;
	}

	public ChatRoom getCurrentRoom() {
		return currentRoom;
	}

}
