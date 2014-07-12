package de.illonis.eduras.bot;

import java.util.logging.Level;
import java.util.logging.Logger;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.logicabstraction.EventSender;
import de.illonis.eduras.logicabstraction.InformationProvider;

public class RandomBotWorker implements EdurasBotWorker {
	private final static Logger L = EduLog.getLoggerFor(RandomBotWorker.class
			.getName());

	private InformationProvider infoProvider;
	private EventSender eventSender;

	@Override
	public void run() {
		while (true) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				L.log(Level.WARNING, "TODO: message", e);
			}
		}
	}

	@Override
	public void setEventSender(EventSender eventSender) {
		this.eventSender = eventSender;
	}

	@Override
	public void setInformationProvider(InformationProvider infoProvider) {
		this.infoProvider = infoProvider;
	}

}
