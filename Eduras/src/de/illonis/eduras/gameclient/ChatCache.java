package de.illonis.eduras.gameclient;

import java.util.HashMap;
import java.util.LinkedList;

import org.newdawn.slick.Input;

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
	private boolean enabled;

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
	 * @param key
	 *            the key number
	 * @param c
	 *            the character.
	 */
	public void write(int key, char c) {
		if (key == Input.KEY_BACK) {
			deleteChar();
		} else if (c != Input.KEY_UNLABELED) {
			input.append(c);
		}
	}

	private void deleteChar() {
		if (writing && input.length() > 0)
			input.deleteCharAt(input.length() - 1);
	}

	/**
	 * Tells whether the chat is currently enabled.
	 * 
	 * @return true if so
	 */
	public boolean isEnabled() {
		return enabled;
	}

	/**
	 * Set if the chat is enabled.
	 * 
	 * @param enabled
	 */
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
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

	/**
	 * @return true if user is active in chat window.
	 */
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
		pushMessage(new SystemMessage(string, chatRoom,
				System.currentTimeMillis()));
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

	/**
	 * Clears all information from cache and results in a freshly created state.
	 */
	public void reset() {
		writing = false;
		messages.clear();
		input = new StringBuilder();
		self = -1;
		users.clear();
		currentRoom = null;
	}
}
