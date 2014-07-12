package de.illonis.eduras.bot;

import de.illonis.eduras.logicabstraction.EventSender;
import de.illonis.eduras.logicabstraction.InformationProvider;

public interface EdurasBotWorker extends Runnable {

	public void setEventSender(EventSender eventSender);

	public void setInformationProvider(InformationProvider infoProvider);
}
