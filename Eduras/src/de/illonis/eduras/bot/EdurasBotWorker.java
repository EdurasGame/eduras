package de.illonis.eduras.bot;

import de.illonis.eduras.logicabstraction.EventSender;
import de.illonis.eduras.logicabstraction.InformationProvider;

/**
 * This interface guarantees that an Eduras? bot must always be able to send
 * events and get the information it might need.
 * 
 * @author Florian 'Ren' Mai <florian.ren.mai@googlemail.com>
 * 
 */
public interface EdurasBotWorker extends Runnable {

	/**
	 * Sets the {@link EventSender} on the bot worker.
	 * 
	 * @param eventSender
	 */
	public void setEventSender(EventSender eventSender);

	/**
	 * Sets the {@link InformationProvider} on the bot worker.
	 * 
	 * @param infoProvider
	 */
	public void setInformationProvider(InformationProvider infoProvider);
}
