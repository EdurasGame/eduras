package de.illonis.eduras.events;

public class SetGameModeEvent extends GameEvent {
	private String newMode;

	public SetGameModeEvent(GameEventNumber type, String newMode) {
		super(type);
		this.newMode = newMode;
	}
public String getNewMode() {
	return newMode;
}
}
