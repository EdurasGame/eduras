package de.illonis.eduras.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.util.concurrent.ExecutionException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JTextPane;
import javax.swing.SwingWorker;

import de.illonis.eduras.logicabstraction.NetworkManager;

/**
 * A dialog that connects to server and shows connection progress to user and
 * displays occuring errors.
 * 
 * @author illonis
 * 
 */
public class ConnectProgressDialog extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1L;
	private NetworkManager nwm;
	private InetAddress addr;
	private int port;
	private JTextPane label;
	private String errorMessage;
	private JButton backButton;
	private Thread t;

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

			}
			t = new Thread(connector);
			t.start();
			int i = 10;
			while (true) {
				i--;
				try {
					t.join(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if (t.isAlive()) {
					System.out.println("Waiting..." + i);
				} else
					break;
			}
			return errorMessage.isEmpty();
		}

		@Override
		public void done() {
			if (errorMessage.isEmpty())
				setVisible(false);
			else
				label.setText("<html><b>Fehler:</b><br>" + errorMessage
						+ "</html>");
		}
	};

	ConnectProgressDialog(JFrame gui, NetworkManager nwm) {
		super(gui, "Connecting...");
		errorMessage = "";
		this.nwm = nwm;
		buildGui();
	}

	private void buildGui() {
		setModal(true);
		setSize(300, 300);
		label = new JTextPane();
		label.setContentType("text/html");
		label.setText("<html>Connecting...<br><br>Please wait!</html>");
		label.setEditable(false);
		backButton = new JButton("Back");
		backButton.addActionListener(this);
		label.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(label, BorderLayout.CENTER);
		getContentPane().add(backButton, BorderLayout.SOUTH);
		setLocationRelativeTo(getParent());
	}

	void start(InetAddress addr, int port) {
		this.addr = addr;
		this.port = port;
		worker.execute();
		setVisible(true);
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
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
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
				e.printStackTrace();
			} finally {
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	};

	@Override
	public void actionPerformed(ActionEvent e) {
		t.interrupt();
		setVisible(false);
	}
}
