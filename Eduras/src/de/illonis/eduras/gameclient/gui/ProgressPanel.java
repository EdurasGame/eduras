package de.illonis.eduras.gameclient.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;

import de.illonis.edulog.EduLog;
import de.illonis.eduras.images.ImageFiler;
import de.illonis.eduras.logicabstraction.NetworkManager;
import de.illonis.eduras.networking.Client;

/**
 * Displays login progress. Also shows errors occuring while connecting.
 * 
 * @author illonis
 * 
 */
public class ProgressPanel extends JPanel implements ActionListener {

	private final static Logger L = EduLog.getLoggerFor(ProgressPanel.class
			.getName());

	private static final long serialVersionUID = 1L;
	private JLabel text;
	private ImageIcon icon;
	private NetworkManager nwm;
	private ClientFrame frame;
	private InetAddress addr;
	private int port;
	private JButton backButton;
	private Thread thread;
	private String errorMessage;
	private ConnectionWaiter worker;

	/**
	 * Creates the progress panel
	 * 
	 * @param frame
	 *            parent frame
	 * @param nwm
	 *            network manager.
	 */
	public ProgressPanel(ClientFrame frame, NetworkManager nwm) {
		super();
		this.frame = frame;
		this.nwm = nwm;
		setLayout(new BorderLayout());
		buildGui();
	}

	private void buildGui() {
		text = new JLabel();
		reset();
		text.setHorizontalTextPosition(SwingConstants.CENTER);
		text.setVerticalTextPosition(SwingConstants.BOTTOM);
		text.setAlignmentX(CENTER_ALIGNMENT);
		text.setHorizontalAlignment(JLabel.CENTER);
		text.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		icon = ImageFiler.loadIcon("gui/login/ajax-loader.gif");
		text.setIcon(icon);

		add(text, BorderLayout.CENTER);

		backButton = new JButton("Abbrechen");
		backButton.addActionListener(this);
		add(backButton, BorderLayout.SOUTH);
	}

	/**
	 * Shows error message.
	 * 
	 * @param s
	 *            error message.
	 */
	void setError(String s) {
		text.setText("<html>" + s + "</html>");
		text.setIcon(null);
		repaint();
	}

	/**
	 * Starts connecting to server asynchronously using given parameters.
	 * 
	 * @param address
	 *            server address.
	 * @param serverPort
	 *            server port.
	 */
	void start(InetAddress address, int serverPort) {
		this.addr = address;
		this.port = serverPort;
		worker = new ConnectionWaiter();
		worker.addPropertyChangeListener(new PropertyChangeListener() {

			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (evt.getPropertyName().equals("timeWaited")) {
					int nv = (int) evt.getNewValue();
					if (nv >= 0)
						updateTimeMessage(nv);
				}
			}
		});
		worker.execute();
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

	private Runnable connector = new Runnable() {
		@Override
		public void run() {
			errorMessage = "";
			try {
				nwm.connect(addr, port);
			} catch (SocketTimeoutException e) {
				errorMessage = "Connection timeout";
			} catch (IOException e) {
				errorMessage = e.getMessage();
				L.log(Level.SEVERE, "could not connect", e);
			} finally {
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					L.log(Level.WARNING, "interrupted", e);
				}
			}
		}
	};

	@Override
	public void actionPerformed(ActionEvent e) {
		worker.cancel(true);
		thread.interrupt();
		try {
			thread.join();
		} catch (InterruptedException e1) {
		}
		frame.showLogin();
	}

	/**
	 * Resets message.
	 */
	void reset() {
		updateTimeMessage(Client.CONNECT_TIMEOUT / 1000);
		text.setIcon(icon);
	}

	private void updateTimeMessage(int t) {
		text.setText("<html><center>Verbindung wird hergestellt...<br>Warte "
				+ t + "s</center></html>");
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

			thread = new Thread(connector);
			thread.setName("Connector");
			thread.start();
			int i = Client.CONNECT_TIMEOUT / 1000;
			while (true) {
				i--;
				try {
					thread.join(1000);
				} catch (InterruptedException e) {
					L.log(Level.WARNING, "interrupted", e);
				}

				if (thread.isAlive()) {
					firePropertyChange("timeWaited", i + 1, i);
				} else
					break;
			}
			return errorMessage.isEmpty();
		}

		@Override
		public void done() {
			if (!errorMessage.isEmpty())
				setError("<b>Fehler:</b> " + errorMessage);
		}
	}
}
