package de.illonis.eduras.chat;

/**
 * Encapsulates the information of an invitation of a user to a room.
 * 
 * @author Florian Mai <florian.ren.mai@googlemail.com>
 * 
 */
public class Invitation {

	private final ChatRoom room;
	private final ChatUser invitingUser;
	private final ChatUser invitedUser;

	Invitation(ChatRoom room, ChatUser invitingUser, ChatUser invitedUser) {
		this.room = room;
		this.invitedUser = invitedUser;
		this.invitingUser = invitingUser;
	}

	/**
	 * Returns the chat room that this invitation is about.
	 * 
	 * @return The room
	 */
	public ChatRoom getRoom() {
		return room;
	}

	/**
	 * Returns the user who sent the invitation.
	 * 
	 * @return the inviting user
	 */
	public ChatUser getInvitingUser() {
		return invitingUser;
	}

	/**
	 * Returns the user who was invited.
	 * 
	 * @return The invited user
	 */
	public ChatUser getInvitedUser() {
		return invitedUser;
	}

	@Override
	public boolean equals(Object other) {
		if (other instanceof Invitation) {
			Invitation otherInvitation = (Invitation) other;
			if (otherInvitation.getInvitedUser().equals(invitedUser)
					&& otherInvitation.getInvitingUser().equals(invitingUser)
					&& otherInvitation.getRoom().equals(room)) {
				return true;
			}
		}
		return false;
	}
}
