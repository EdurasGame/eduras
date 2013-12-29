package de.illonis.eduras.chat;

@SuppressWarnings("javadoc")
public abstract class Chat {

	// server events
	public static final int NEW_MESSAGE = 1;
	public static final int NAME_CHANGED = 2;
	public static final int INVITE_TO_ROOM = 3;
	public static final int ROOM_CREATED = 4;
	public static final int CONFIRM_ROOM_JOIN = 5;
	public static final int USER_CREATED = 6;
	public static final int USER_REMOVED = 7;
	public static final int ROOM_REMOVED = 8;
	public static final int USER_JOINED_ROOM = 10;
	public static final int USER_LEFT_ROOM = 11;
	public static final int YOU_CONNECTED = 12;

	// client events
	public static final int SEND_MESSAGE = 20;
	public static final int SET_NAME = 21;
	public static final int CREATE_ROOM = 22;
	public static final int JOIN_ROOM = 23;
	public static final int INVITE_USER = 24;
	public static final int ACCEPT_INVITE = 27;

}
