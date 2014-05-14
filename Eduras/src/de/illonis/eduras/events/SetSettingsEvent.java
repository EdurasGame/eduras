package de.illonis.eduras.events;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class SetSettingsEvent extends GameEvent {

	private File settingsFile;

	public SetSettingsEvent(File settingsFile) throws IOException {
		super(GameEventNumber.SET_SETTINGS);

		this.settingsFile = settingsFile;
		putArgument(Files.readAllBytes(settingsFile.toPath()));
	}

	public File getSettingsFile() {
		return settingsFile;
	}
}
