package de.illonis.eduras.gameclient.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
	 * Handles connection timeout.
	 */
	private SwingWorker<Boolean, Void> worker = new SwingWorker<Boolean, Void>() {
		@Override
		public Boolean doInBackground() {
			// wait some time to prevent a bug (progress dialog not hiding on
			// immediate connection)
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				EduLog.passException(e);
			}
			t = new Thread(connector);
			t.start();
			int i = 10;
			while (true) {
				i--;
				try {
					t.join(1000);
				} catch (InterruptedException e) {
					EduLog.passException(e);
				}
				if (t.isAlive()) {
					EduLog.info("Waiting..." + i);
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
	};

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
			try {
				Thread.sleep(500);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
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
		frame.showLogin();
	}

	/**
	 * Resets message.
	 */
	public void reset() {
		text.setText("Verbindung wird hergestellt...");
		text.setIcon(icon);
	}
}
