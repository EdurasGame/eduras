package de.illonis.eduras.networking;

/**
 * This indicates a client's role. Spectators will get any information and will
 * have full overview and special features. They will not be an active part of
 * the game though. Players will have an active role in the game.
 * 
 * @author Florian Mai <florian.ren.mai@googlemail.com>
 * 
 */
@SuppressWarnings("javadoc")
public enum ClientRole {
	PLAYER, SPECTATOR;
}
