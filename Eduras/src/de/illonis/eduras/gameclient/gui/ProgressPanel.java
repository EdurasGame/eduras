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

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;

import de.illonis.eduras.images.ImageFiler;
import de.illonis.eduras.logger.EduLog;
import de.illonis.eduras.logicabstraction.NetworkManager;
import de.illonis.eduras.networking.Client;

/**
 * Displays login progress. Also shows errors occuring while connecting.
 * 
 * @author illonis
 * 
 */
public class ProgressPanel extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1L;
	private JLabel text;
	private ImageIcon icon;
	private NetworkManager nwm;
	private ClientFrame frame;
	private InetAddress addr;
	private int port;
	private JButton backButton;
	private Thread t;
	private String errorMessage;
	private ConnectionWaiter worker;

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
	 * @param addr
	 *            server address.
	 * @param port
	 *            server port.
	 */
	void start(InetAddress addr, int port) {
		this.addr = addr;
		this.port = port;
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
		} catch (InterruptedException e) {
			EduLog.passException(e);
		} catch (ExecutionException e) {
			EduLog.passException(e);
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
				EduLog.passException(e);
			} finally {
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					EduLog.passException(e);
				}
			}
		}
	};

	@Override
	public void actionPerformed(ActionEvent e) {
		worker.cancel(true);
		t.interrupt();
		try {
			t.join();
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

			t = new Thread(connector);
			t.start();
			int i = Client.CONNECT_TIMEOUT / 1000;
			while (true) {
				i--;
				try {
					t.join(1000);
				} catch (InterruptedException e) {
					EduLog.passException(e);
				}

				if (t.isAlive()) {
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
