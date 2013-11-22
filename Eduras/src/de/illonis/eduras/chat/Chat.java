package de.illonis.eduras.chat;

@SuppressWarnings("javadoc")
public abstract class Chat {

	// server events
	public static final int NEW_MESSAGE = 1;
	public static final int NAME_CHANGED = 2;
	public static final int INVITE_TO_ROOM = 3;
	public static final int CONFIRM_ROOM_CREATE = 4;
	public static final int CONFIRM_ROOM_JOIN = 5;
	public static final int GIVE_USER_INFORMATION = 6;
	public static final int GIVE_ROOM_INFORMATION = 7;
	public static final int SHOW_ROOMS = 8;
	public static final int SHOW_USERS = 9;
	public static final int USER_JOINED_ROOM = 10;
	public static final int USER_LEFT_ROOM = 11;

	// client events
	public static final int SEND_MESSAGE = 20;
	public static final int SET_NAME = 21;
	public static final int CREATE_ROOM = 22;
	public static final int JOIN_ROOM = 23;
	public static final int INVITE_USER = 24;
	public static final int VIEW_ROOMS = 25;
	public static final int VIEWS_USERS_IN_ROOM = 26;
	public static final int ACCEPT_INVITE = 27;
	public static final int VIEW_USER_INFORMATION = 28;
	public static final int VIEW_ROOM_INFORMATION = 29;

}
