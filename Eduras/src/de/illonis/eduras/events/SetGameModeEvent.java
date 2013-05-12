package de.illonis.eduras.events;

public class SetGameModeEvent extends GameEvent {
	private String newMode;

	public SetGameModeEvent(String newMode) {
		super(GameEventNumber.SET_GAMEMODE);
		this.newMode = newMode;
	}

	public String getNewMode() {
		return newMode;
	}
}
