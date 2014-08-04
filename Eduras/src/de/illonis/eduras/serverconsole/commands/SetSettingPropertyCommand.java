package de.illonis.eduras.serverconsole.commands;

import java.util.logging.Logger;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.logic.ConsoleEventTriggerer;
import de.illonis.eduras.serverconsole.ConsolePrinter;

public class SetSettingPropertyCommand extends ConsoleCommand {

	private final static Logger L = EduLog
			.getLoggerFor(SetSettingPropertyCommand.class.getName());

	public SetSettingPropertyCommand() {
		super("set", "Sets a property to a ");
		setExactNumArgs(2);
	}

	@Override
	public void onCommand(String[] args, ConsolePrinter console,
			ConsoleEventTriggerer triggerer) {

		try {
			console.println("Setting " + args[1] + "=" + args[2]);
			triggerer.setSetting(args[1], args[2]);
			console.println("Success!");
		} catch (NoSuchFieldException | SecurityException
				| IllegalArgumentException | IllegalAccessException e) {
			console.println("Error: " + e);
		}
	}
}
