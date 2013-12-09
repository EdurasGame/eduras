package de.illonis.eduras.gameclient;

import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.LinkedList;

import de.illonis.eduras.chat.ChatMessage;
import de.illonis.eduras.chat.ChatRoom;
import de.illonis.eduras.chat.ChatUser;

/**
 * A local chat cache for client that is accessed by network and gui.
 * 
 * @author illonis
 * 
 */
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

	public void write(KeyEvent letter) {
		if (letter.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
			input.deleteCharAt(input.length() - 1);
		} else {
			input.append(letter.getKeyChar());
			startWriting();
		}
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
	}

	public void setSelf(ChatUser user) {
		self = user.getId();
		pushUser(user);
		if (!getSelfUser().getOccupiedRooms().isEmpty())
			currentRoom = getSelfUser().getOccupiedRooms().getFirst();
	}

	public boolean isWriting() {
		return writing;
	}

	public void pushMessage(ChatMessage message) {
		messages.add(message);
	}

	public String getRoomName() {
		if (currentRoom == null)
			return "";
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
		System.out.println("start");
		writing = true;
	}

	public ChatRoom getCurrentRoom() {
		return currentRoom;
	}

}
