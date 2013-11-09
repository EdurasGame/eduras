package de.illonis.eduras.gameclient.gui;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.SwingWorker;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.gameclient.ConnectionEstablisher;
import de.illonis.eduras.gameclient.LoginStepLogic;
import de.illonis.eduras.networking.Client;

/**
 * Displays login progress. Also shows errors occuring while connecting.
 * 
 * @author illonis
 * 
 */
public class ProgressPanel extends LoginStepLogic implements ActionListener,
		PropertyChangeListener {

	private final static Logger L = EduLog.getLoggerFor(ProgressPanel.class
			.getName());

	private final ProgressGui gui;
	private final ProgressPanelReactor reactor;
	private ConnectionEstablisher establisher;
	private ConnectionWaiter worker;

	/**
	 * Creates the progress panel
	 * 
	 * @param reactor
	 *            the reactor on panel's actions.
	 * 
	 */
	public ProgressPanel(ProgressPanelReactor reactor) {
		this.reactor = reactor;
		this.gui = new ProgressGui(this);
	}

	/**
	 * Shows error message.
	 * 
	 * @param s
	 *            error message.
	 */
	void setError(String s) {
		gui.setText("<html>" + s + "</html>");
	}

	/**
	 * Checks whether connection could be established or not.
	 * 
	 * @return false if an error occurred.
	 */
	public boolean isOK() {
		try {
			return worker.get();
		} catch (InterruptedException | ExecutionException e) {
			L.log(Level.SEVERE, "could not get connect result", e);
		}
		return false;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// abort pressend
		reactor.abort();
		worker.cancel(true);
	}

	private void terminateWorker() {
		if (worker != null && !worker.isDone()) {
			worker.removePropertyChangeListener(this);
			worker.cancel(true);
		}
	}

	@Override
	public void onShown() {
		terminateWorker();
		worker = new ConnectionWaiter();
		worker.addPropertyChangeListener(this);
		worker.execute();
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getPropertyName().equals("timeWaited")) {
			int nv = (int) evt.getNewValue();
			if (nv >= 0)
				gui.updateTimeMessage(nv);
		}
	}

	@Override
	public void onHidden() {
		terminateWorker();
	}

	@Override
	protected Component getGui() {
		return gui;
	}

	/**
	 * Handles waiting for established connection.
	 * 
	 * @author illonis
	 * 
	 */
	private class ConnectionWaiter extends SwingWorker<Boolean, Void> {

		@Override
		public Boolean doInBackground() {
			Thread.currentThread().setName("ConnectionWaiter");
			if (null == establisher)
				return false;

			int i = Client.CONNECT_TIMEOUT / 1000;
			while (true) {
				i--;
				try {
					establisher.join(1000);
				} catch (InterruptedException e) {
					L.log(Level.WARNING, "connection waiter interrupted", e);
					break;
				}

				if (establisher.isAlive()) {
					firePropertyChange("timeWaited", i + 1, i);
				} else
					break;
			}
			String msg = establisher.getErrorMessage();
			return msg.isEmpty();
		}

		@Override
		public void done() {
			try {
				if (!get())
					showError(establisher.getErrorMessage(), "Error");
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
		}
	}

	void setEstablishThread(ConnectionEstablisher establisher) {
		this.establisher = establisher;
	}
}
