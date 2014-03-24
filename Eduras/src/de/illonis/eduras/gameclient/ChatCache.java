package de.illonis.eduras.gameclient;

import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.LinkedList;

import de.illonis.eduras.chat.ChatMessage;
import de.illonis.eduras.chat.ChatRoom;
import de.illonis.eduras.chat.ChatUser;
import de.illonis.eduras.chat.SystemMessage;

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
	private int n = 1;
	private boolean writing;

	ChatCache() {
		messages = new LinkedList<ChatMessage>();
		this.self = -1;
		input = new StringBuilder();
		users = new HashMap<Integer, ChatUser>();
		writing = false;
	}

	private ChatUser getSelfUser() {
		return users.get(self);
	}

	/**
	 * Indicates that a letter has been typed into chat window.
	 * 
	 * @param letter
	 *            the event of writing the letter.
	 */
	public void write(KeyEvent letter) {
		if (letter.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
			deleteChar();
		} else if (letter.getKeyChar() != KeyEvent.CHAR_UNDEFINED) {
			input.append(letter.getKeyChar());
			startWriting();
		}
	}

	private void deleteChar() {
		if (writing && input.length() > 0)
			input.deleteCharAt(input.length() - 1);
	}

	/**
	 * Sends typed message.
	 * 
	 * @return the sent text.
	 */
	public String sendInput() {
		String text = input.toString();
		// prevent send errors due to messages separated by #
		text = text.replace('#', ' ');
		writing = false;
		stopWriting();
		return text;
	}

	/**
	 * @return current input text.
	 */
	public String getInput() {
		return input.toString();
	}

	/**
	 * Indicates user stopped writing.
	 */
	public void stopWriting() {
		input = new StringBuilder();
		writing = false;
	}

	/**
	 * Sets the user.
	 * 
	 * @param user
	 *            the user using this chatdisplay.
	 */
	public void setSelf(ChatUser user) {
		self = user.getId();
		pushUser(user);
		if (!getSelfUser().getOccupiedRooms().isEmpty())
			currentRoom = getSelfUser().getOccupiedRooms().getFirst();
	}

	public boolean isWriting() {
		return writing;
	}

	/**
	 * Adds an incoming message to cache.
	 * 
	 * @param message
	 *            the new message.
	 */
	public void pushMessage(ChatMessage message) {
		messages.add(message);
	}

	/**
	 * @return the currently active room.
	 */
	public String getRoomName() {
		if (currentRoom == null)
			return "";
		return currentRoom.getName();
	}

	/**
	 * @param string
	 *            the message.
	 */
	public void pushSystemMessage(String string) {
		pushSystemMessage(string, null);
	}

	/**
	 * @param string
	 *            the message.
	 * @param chatRoom
	 *            the room.
	 */
	public void pushSystemMessage(String string, ChatRoom chatRoom) {
		pushMessage(new SystemMessage(string, chatRoom));
	}

	/**
	 * @param chatRoom
	 *            the new chatroom.
	 */
	public void setCurrentRoom(ChatRoom chatRoom) {
		pushSystemMessage("Set current room to " + chatRoom.getName());
		currentRoom = chatRoom;
	}

	public void pushUser(ChatUser user) {
		users.put(user.getId(), user);
	}

	public void pushRoom(ChatRoom chatRoom) {
		// TODO Auto-generated method stub
	}

	/**
	 * Returns most recent non popped chat messages.<br>
	 * When called in a loop, this method will return all received chat messages
	 * in inversed order call-by-call. When no message is remaining, <b>null</b>
	 * is returned.<br>
	 * This method does not touch the original message list.<br>
	 * To reset pop-counter (to read messages again), call {@link #resetPop()}.
	 * 
	 * @return the recent chat message not yet popped.
	 */
	public ChatMessage popMessage() {
		int i = messages.size() - n++;
		if (i < 0)
			return null;
		return messages.get(i);
	}

	/**
	 * Resets pop counter used for {@link #popMessage()}.
	 */
	public void resetPop() {
		n = 1;
	}

	/**
	 * Indicates that user is currently writing a chat message.
	 */
	public void startWriting() {
		writing = true;
	}

	/**
	 * @return the room the user is in.
	 */
	public ChatRoom getCurrentRoom() {
		return currentRoom;
	}
}
